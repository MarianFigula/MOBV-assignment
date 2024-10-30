package eu.mcomputing.mobv.mobvzadanie.data.api

import android.content.Context
import eu.mcomputing.mobv.mobvzadanie.AuthInterceptor
import eu.mcomputing.mobv.mobvzadanie.TokenAuthenticator
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @Headers("x-apikey: c95332ee022df8c953ce470261efc695ecf3e784")
    @POST("user/create.php")
    suspend fun registerUser(@Body userInfo: UserRegistration) : Response<RegistrationResponse>

    @POST("user/login.php")
    suspend fun loginUser(@Body userInfo: UserLoginRequest): Response<LoginResponse>

    @GET("user/get.php")
    suspend fun getUser(
        @Query("id") id: String
    ): Response<UserResponse>

    @POST("user/refresh.php")
    suspend fun refreshToken(
        @Body refreshInfo: RefreshTokenRequest
    ): Response<RefreshTokenResponse>

    @POST("user/refresh.php")
    fun refreshTokenBlocking(
        @Body refreshInfo: RefreshTokenRequest
    ): Call<RefreshTokenResponse>

    @Headers("x-apikey: c95332ee022df8c953ce470261efc695ecf3e784")
    @POST("user/reset.php")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<ResetPasswordResponse>

    @Headers("x-apikey: c95332ee022df8c953ce470261efc695ecf3e784")
    @POST("user/password.php")
    suspend fun changePassword(
        @Header("Authorization") authToken: String,
        @Body request: ChangePasswordRequest
    ): Response<ChangePasswordResponse>


    @GET("geofence/list.php")
    suspend fun listGeofence(): Response<GeofenceResponse>

    companion object {
        fun create(context: Context): ApiService {

            val client = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(context))
                .authenticator(TokenAuthenticator(context))
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://zadanie.mpage.sk/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}

// create.php
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

// login.php
data class UserLoginRequest(
    val name: String,
    val password: String
)

data class LoginResponse(
    val uid: String,
    val access: String,
    val refresh: String
)

// get.php
// TODO: mozno uid miesto id
data class UserResponse(
    val id: String,
    val name: String,
    val photo: String
)

// refresh.php
data class RefreshTokenRequest(val refresh: String)

data class RefreshTokenResponse(
    val uid: String,
    val access: String,
    val refresh: String
)

// reset.php
data class ResetPasswordRequest(val email: String)
data class ResetPasswordResponse(val status: String, val message: String? = null)


// password.php
data class ChangePasswordRequest(val oldPassword: String, val newPassword: String)
data class ChangePasswordResponse(val status: String)

// geofence/list.php
data class GeofenceResponse(
    val me: GeofenceMeResponse,
    val list: List<GeofenceUserResponse>
)

data class GeofenceUserResponse(
    val uid: String,
    val radius: Double,
    val updated: String,
    val name: String,
    val photo: String
)


data class GeofenceMeResponse(
    val uid: String,
    val lat: Double,
    val lon: Double,
    val radius: Double
)