package eu.mcomputing.mobv.mobvzadanie.data.api.auth

import android.content.Context
import eu.mcomputing.mobv.mobvzadanie.data.PreferenceData
import eu.mcomputing.mobv.mobvzadanie.config.AppConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")


        val token = PreferenceData.getInstance().getUser(context)?.access
        request.header("Authorization","Bearer $token")

        // add api key to each request
        request.addHeader("x-apikey", AppConfig.API_KEY)

        return chain.proceed(request.build())
    }
}