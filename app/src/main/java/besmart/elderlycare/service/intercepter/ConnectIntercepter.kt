package besmart.elderlycare.service.intercepter

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import besmart.elderlycare.model.login.LoginResponce
import besmart.elderlycare.util.Constance
import besmart.elderlycare.util.NoInternetException
import com.orhanobut.hawk.Hawk
import okhttp3.Interceptor
import okhttp3.Response

class ConnectIntercepter(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isOnline()) throw NoInternetException()
        return chain.proceed(chain.request())
    }

    @SuppressLint("ServiceCast")
    private fun isOnline(): Boolean {
        val connectInternet =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val neteworkInfo = connectInternet.activeNetworkInfo
        return neteworkInfo != null && neteworkInfo.isConnected
    }
}