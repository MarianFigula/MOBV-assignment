package eu.mcomputing.mobv.mobvzadanie

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("x-apikey: c95332ee022df8c953ce470261efc695ecf3e784")
    @POST("user/create.php")
    suspend fun registerUser(@Body userInfo: UserRegistration) : Response<RegistrationResponse>

    @Headers("x-apikey: c95332ee022df8c953ce470261efc695ecf3e784")
    @POST("user/login.php")
    suspend fun loginUser(@Body userInfo: UserLogin): Response<LoginResponse>

    @Headers("x-apikey: c95332ee022df8c953ce470261efc695ecf3e784")
    @POST("user/reset.php")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<ResetPasswordResponse>

    companion object {
        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://zadanie.mpage.sk/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}

data class UserRegistration(
    val name: String,
    val email: String,
    val password: String
)

data class RegistrationResponse(
    val uid: String,
    val access: String,
    val refresh: String
)


data class UserLogin(
    val name: String,
    val password: String
)

data class LoginResponse(
    val uid: String,
    val access: String,
    val refresh: String
)

data class ResetPasswordRequest(val email: String)
data class ResetPasswordResponse(val status: String, val message: String? = null)