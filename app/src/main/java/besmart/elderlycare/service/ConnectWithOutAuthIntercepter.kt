package besmart.elderlycare.service

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import besmart.elderlycare.util.NoInternetException
import okhttp3.Interceptor
import okhttp3.Response

class ConnectWithOutAuthIntercepter(private val context: Context): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isOnline()) throw NoInternetException()
        return chain.proceed(chain.request())
    }

    @SuppressLint("ServiceCast")
    private fun isOnline(): Boolean{
        val connectInternet = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val neteworkInfo = connectInternet.activeNetworkInfo
        return neteworkInfo != null && neteworkInfo.isConnected
    }
}