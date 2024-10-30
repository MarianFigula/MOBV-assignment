package eu.mcomputing.mobv.mobvzadanie

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import eu.mcomputing.mobv.mobvzadanie.data.api.ApiService
import eu.mcomputing.mobv.mobvzadanie.data.api.ChangePasswordRequest
import eu.mcomputing.mobv.mobvzadanie.data.api.ResetPasswordRequest
import eu.mcomputing.mobv.mobvzadanie.data.api.UserLoginRequest
import eu.mcomputing.mobv.mobvzadanie.data.api.UserRegistration
import eu.mcomputing.mobv.mobvzadanie.data.db.AppRoomDatabase
import eu.mcomputing.mobv.mobvzadanie.data.db.LocalCache
import eu.mcomputing.mobv.mobvzadanie.data.db.entities.UserEntity
import eu.mcomputing.mobv.mobvzadanie.data.model.User
import okio.IOException

class DataRepository private constructor(
    private val service: ApiService,
    private val cache: LocalCache
) {
    companion object {
        const val TAG = "DataRepository"

        @Volatile
        private var INSTANCE: DataRepository? = null
        private val lock = Any()

        fun getInstance(context: Context): DataRepository =
            INSTANCE ?: synchronized(lock) {
                INSTANCE
                    ?: DataRepository(
                        ApiService.create(context),
                        LocalCache(AppRoomDatabase.getInstance(context).appDao())
                    ).also { INSTANCE = it }
            }
    }

    suspend fun apiRegisterUser(
        username: String,
        email: String,
        password: String,
        repeatPassword: String
    ): Pair<String, User?> {
        if (username.isEmpty()) {
            return Pair("Username cannot be empty", null)
        }
        if (email.isEmpty()) {
            return Pair("Email cannot be empty", null)
        }
        if (password.isEmpty()) {
            return Pair("Password cannot be empty", null)
        }
        if (password != repeatPassword){
            return Pair("Passwords should match", null)
        }
        // Create the UserRegistration object
        val userRegistration = UserRegistration(username, email, password)

        // Log the data being sent
        Log.d(TAG, "Registering user with data: ${Gson().toJson(userRegistration)}")
        Log.d(TAG, "usrname: $username, email: $email, password: $password, repeat: $repeatPassword")

        try {
            val response = service.registerUser(UserRegistration(username, email, password))
            if (response.isSuccessful) {
                response.body()?.let { jsonResponse ->
                    return when (jsonResponse.uid) {
                        (-1).toString() -> {
                            Log.d(TAG, "Registration failed: Username already exists")
                            Pair("Username already exists", null)
                        }
                        (-2).toString() -> {
                            Log.d(TAG, "Registration failed: Email already exists")
                            Pair("Email already exists", null)
                        }
                        else -> {
                            Log.d(TAG, "Registration successful: ${Gson().toJson(jsonResponse)}")
                            Pair(
                                "Successful - redirecting", User(
                                    username, email, jsonResponse.uid,
                                    jsonResponse.access, jsonResponse.refresh
                                )
                            )
                        }
                    }
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.d(TAG, "Error body: $errorBody")
                return Pair("Error: ${response.code()} - ${response.message()}", null)
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            return Pair("Check internet connection. Failed to create user.", null)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return Pair("Fatal error. Failed to create user.", null)
    }

    suspend fun apiLoginUser(
        name: String,
        password: String
    ): Pair<String, User?> {
        Log.d(TAG, name)
        if (name.isEmpty()) {
            return Pair("Email cannot be empty", null)
        }
        if (password.isEmpty()) {
            return Pair("Password cannot be empty", null)
        }

        // Create the UserRegistration object
        val userLoginRequest = UserLoginRequest(name, password)

        // Log the data being sent
        Log.d(TAG, "Login user with data: ${Gson().toJson(userLoginRequest)}")
        Log.d(TAG, "email: $name, password: $password")

        try {
            val response = service.loginUser(UserLoginRequest(name, password))
            if (response.isSuccessful) {
                response.body()?.let { jsonResponse ->
                    return if (jsonResponse.uid.toInt() >= 0) {
                        Log.d(
                            TAG,
                            Pair(
                                "", User(
                                    "", name, jsonResponse.uid,
                                    jsonResponse.access, jsonResponse.refresh
                                )
                            ).toString()
                        )
                        Pair(
                            "", User(
                                "", name, jsonResponse.uid,
                                jsonResponse.access, jsonResponse.refresh
                            )
                        )
                    } else {
                        // UID is -1, indicating an incorrect login
                        Log.d(TAG, "Login failed: Incorrect email or password")
                        Pair("Incorrect email or password", null)
                    }
                }

            } else {
                val errorBody = response.errorBody()?.string()
                Log.d(TAG, "Error body: $errorBody")
                return Pair("Incorrect email or password", null)
            }

        } catch (ex: IOException) {
            ex.printStackTrace()
            return Pair("Check internet connection. Failed to login user.", null)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return Pair("Fatal error. Failed to login user.", null)
    }

    suspend fun apiResetPassword(email: String): Pair<String, String?> {
        if (email.isEmpty()) {
            return Pair("Email cannot be empty", null)
        }

        // Create the UserRegistration object
        val userLogin = ResetPasswordRequest(email)

        // Log the data being sent
        Log.d(TAG, "Login user with data: ${Gson().toJson(userLogin)}")
        Log.d(TAG, "email: $email")

        try {
            val response = service.resetPassword(ResetPasswordRequest(email))
            if (response.isSuccessful) {
                response.body()?.let { jsonResponse ->
                    Log.d(TAG, "Password reset response: ${Gson().toJson(jsonResponse)}")
                    return if (jsonResponse.status == "success") {
                        Pair("Password reset email sent successfully.", null)
                    } else {
                        Pair("Password reset failed: ${jsonResponse.message}", null)
                    }
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.d(TAG, "Error body: $errorBody")
                return Pair("Error: ${response.code()} - ${response.message()}", null)
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            return Pair("Check internet connection. Failed to reset password.", null)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return Pair("Fatal error. Failed to reset password.", null)

    }

    suspend fun apiChangePassword(
        authToken: String,
        oldPassword: String,
        newPassword: String
    ) : Pair<String, String?> {
        if (oldPassword.isEmpty() || newPassword.isEmpty()){
            return Pair("Password cannot be empty", null)
        }

        val changePasswordRequest = ChangePasswordRequest(oldPassword, newPassword)
        Log.d(TAG, "Changing password with data: ${Gson().toJson(changePasswordRequest)}")

        try {
            val response = service.changePassword(
                "Barer: $authToken",
                ChangePasswordRequest(oldPassword, newPassword)
            )

            if (response.isSuccessful){
                response.body()?.let { jsonResponse ->
                    return if (jsonResponse.status == "success") {
                        Pair("Password changed successfully", null)
                    } else {
                        Pair("Password change failed", null)
                    }
                }
            }
            else {
                return Pair("Error: ${response.code()} - ${response.message()}", null)
            }
        }catch (ex: IOException) {
            ex.printStackTrace()
            return Pair("Check internet connection. Failed to change password.", null)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return Pair("Fatal error. Failed to change password.", null)
    }


    suspend fun apiGetUser(
        uid: String
    ): Pair<String, User?> {
        try {
            val response = service.getUser(uid)

            if (response.isSuccessful) {
                response.body()?.let {
                    return Pair("", User(it.name, "", it.id, "", "", it.photo))
                }
            }

            return Pair("Failed to load user", null)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return Pair("Check internet connection. Failed to load user.", null)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return Pair("Fatal error. Failed to load user.", null)
    }

    suspend fun apiListGeofence(): String {
        try {
            val response = service.listGeofence()

            if (response.isSuccessful) {
                response.body()?.let { resp ->
                    val users = resp.list.map {
                        UserEntity(
                            it.uid, it.name, it.updated,
                            resp.me.lat, resp.me.lon, it.radius,
                            it.photo
                        )
                    }
                    cache.insertUserItems(users)
                    return ""
                }
            }
            return "Failed to load user"
        } catch (ex: IOException) {
            ex.printStackTrace()
            return "Check internet connection. Failed to load user."
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return "Fatal error. Failed to load user."
    }

    fun getUsers() = cache.getUsers()
}