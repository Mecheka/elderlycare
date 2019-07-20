package besmart.elderlycare.service

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface ElderlyService {

    @GET("/api/v1.0/users/myElderlies/")
    fun getMyElderly(): Single<Response<ResponseBody>>
}