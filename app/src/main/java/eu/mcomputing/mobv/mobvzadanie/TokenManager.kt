package eu.mcomputing.mobv.mobvzadanie
import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveTokens(accessToken: String, refreshToken: String) {
        with(sharedPreferences.edit()) {
            putString("access", accessToken)
            putString("refresh", refreshToken)
            apply()
        }
    }

    fun getAccessToken(): String? = sharedPreferences.getString("access", null)

    fun getRefreshToken(): String? = sharedPreferences.getString("refresh", null)

    fun clearTokens() {
        with(sharedPreferences.edit()) {
            remove("access")
            remove("refresh")
            apply()
        }
    }
}
