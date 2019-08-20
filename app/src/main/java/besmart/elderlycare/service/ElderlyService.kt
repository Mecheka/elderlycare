package besmart.elderlycare.service

import besmart.elderlycare.model.profile.ProfileResponce
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ElderlyService {

    @GET("/api/v1.0/users/myElderlies/")
    fun getMyElderly(): Single<Response<List<ProfileResponce>>>

    @GET("/api/v1.0/users/staffID/{staffId}/myElderlies")
    fun getMyElderlyByStaffId(@Path("staffId") staffId: String): Single<Response<List<ProfileResponce>>>

    @FormUrlEncoded
    @POST("/api/v1.0/myElderlies")
    fun createMyElderly(@Field("peopleCardID") peopleCardID: String): Single<Response<ResponseBody>>

    @DELETE("/api/v1.0/myElderlies/remove/{cardID}")
    fun removeElderly(@Path("cardID") cardId:String): Single<Response<ResponseBody>>
}