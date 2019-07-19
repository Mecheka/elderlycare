package besmart.elderlycare.util

import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class HandingNetworkError {
    companion object {
        fun handingError(e: Throwable): String {
            return when (e) {
                is HttpException -> {
                    val responseBody = e.response()?.errorBody()
                    getErrorMessage(responseBody!!)
                }
                is SocketTimeoutException,
                is IOException,
                is NoInternetException->e.message.toString()
                else -> e.message.toString()
            }
        }

        private fun getErrorMessage(responseBody: ResponseBody): String {
            return try {
                val jsonObject = JSONObject(responseBody.string())
                jsonObject.getString("reason")
            } catch (e: Exception) {
                e.message.toString()
            }

        }
    }
}