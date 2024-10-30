package eu.mcomputing.mobv.mobvzadanie.data.api.auth

import android.content.Context
import eu.mcomputing.mobv.mobvzadanie.data.PreferenceData
import eu.mcomputing.mobv.mobvzadanie.data.api.ApiService
import eu.mcomputing.mobv.mobvzadanie.data.api.RefreshTokenRequest
import eu.mcomputing.mobv.mobvzadanie.data.model.User
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Route

class TokenAuthenticator(val context: Context) : Authenticator {
    override fun authenticate(route: Route?, response: okhttp3.Response): Request? {


        if (response.code == 401) {
            val userItem = PreferenceData.getInstance().getUser(context)
            userItem?.let { user ->
                val tokenResponse = ApiService.create(context).refreshTokenBlocking(
                    RefreshTokenRequest(user.refresh)
                ).execute()

                if (tokenResponse.isSuccessful) {
                    tokenResponse.body()?.let {
                        val newUser = User(
                            user.username,
                            user.email,
                            user.id,
                            it.access,
                            it.refresh,
                            user.photo
                        )
                        PreferenceData.getInstance().putUser(context, newUser)
                        return response.request.newBuilder()
                            .header("Authorization", "Bearer ${newUser.access}")
                            .build()
                    }
                }
            }
            //if there was no success of refresh token we logout user and clean any data
            PreferenceData.getInstance().clearData(context)
            return null
        }
        return null
    }
}
