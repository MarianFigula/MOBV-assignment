package eu.mcomputing.mobv.mobvzadanie.data.api

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @Headers("x-apikey: c95332ee022df8c953ce470261efc695ecf3e784")
    @POST("user/create.php")
    suspend fun registerUser(@Body userInfo: UserRegistration) : Response<RegistrationResponse>

    @Headers("x-apikey: c95332ee022df8c953ce470261efc695ecf3e784")
    @POST("user/login.php")
    suspend fun loginUser(@Body userInfo: UserLogin): Response<LoginResponse>

    @GET("user/get.php")
    suspend fun getUser(
        @HeaderMap header: Map<String, String>,
        @Query("id") id: String
    ): Response<UserResponse>

    @POST("user/refresh.php")
    suspend fun refreshToken(
        @HeaderMap header: Map<String, String>,
        @Body refreshInfo: RefreshTokenRequest
    ): Response<RefreshTokenResponse>

    @Headers("x-apikey: c95332ee022df8c953ce470261efc695ecf3e784")
    @POST("user/reset.php")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<ResetPasswordResponse>

    @Headers("x-apikey: c95332ee022df8c953ce470261efc695ecf3e784")
    @POST("user/password.php")
    suspend fun changePassword(
        @Header("Authorization") authToken: String,
        @Body request: ChangePasswordRequest
    ): Response<ChangePasswordResponse>

    companion object {
        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://zadanie.mpage.sk/")
                // TODO tu nieco pridat ako pozmenit header, asi ..
                //  ak by sme stratili access token tak pomocou refreshu ho obnovit -
                //  interceptor
                //
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
data class UserLogin(
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