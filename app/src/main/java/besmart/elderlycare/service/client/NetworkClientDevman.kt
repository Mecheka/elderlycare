package besmart.elderlycare.service.client

import android.content.Context
import besmart.elderlycare.service.intercepter.ConnectWithAuthIntercepter
import besmart.elderlycare.util.Constance
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class NetworkClientDevman(private val mContext: Context) {

    fun create(): Retrofit {
        val gson = GsonBuilder().create()
        val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB
        val httpCacheDirectory = File(mContext.cacheDir, "http-cache")
        val cache = Cache(httpCacheDirectory, cacheSize)
        val connectIntercepter =
            ConnectWithAuthIntercepter(mContext)
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
        httpClient.cache(cache)
        httpClient.addInterceptor(connectIntercepter)
        httpClient.addInterceptor(logging)
        httpClient.connectTimeout(30, TimeUnit.SECONDS)
        httpClient.readTimeout(30, TimeUnit.SECONDS)

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(Constance.BASE_URL)
            .client(httpClient.build())
            .build()
    }


}