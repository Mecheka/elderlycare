package besmart.elderlycare.service

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import besmart.elderlycare.model.login.LoginResponce
import besmart.elderlycare.util.Constance
import besmart.elderlycare.util.NoInternetException
import com.orhanobut.hawk.Hawk
import okhttp3.Interceptor
import okhttp3.Response

class ConnectWithAuthIntercepter(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isOnline()) throw NoInternetException()
        val token = Hawk.get(Constance.TOKEN, LoginResponce())
        val original = chain.request()
        val request = original.newBuilder()
            .addHeader("Authorization", "Bearer " + token.string)
            .method(original.method(), original.body())
            .build()
        return chain.proceed(request)
    }

    @SuppressLint("ServiceCast")
    private fun isOnline(): Boolean {
        val connectInternet =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val neteworkInfo = connectInternet.activeNetworkInfo
        return neteworkInfo != null && neteworkInfo.isConnected
    }
}