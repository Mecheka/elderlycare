package besmart.elderlycare.service

import besmart.elderlycare.model.profile.ProfileResponce
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ElderlyService {

    @GET("/api/v1.0/users/myElderlies/")
    fun getMyElderly(): Single<Response<List<ProfileResponce>>>

    @FormUrlEncoded
    @POST("/api/v1.0/myElderlies")
    fun createMyElderly(@Field("peopleCardID") peopleCardID: String): Single<Response<ResponseBody>>
}