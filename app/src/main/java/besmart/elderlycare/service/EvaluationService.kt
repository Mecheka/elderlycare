package besmart.elderlycare.service

import besmart.elderlycare.model.blood.BloodPressuresRequest
import besmart.elderlycare.model.blood.BloodPressuresResponse
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EvaluationService {

    @GET("/api/v1.0/bloodPressures/cardID/{cardID}")
    fun getBloodPressuresLastIndex(@Path("cardID") cardID: String): Single<Response<List<BloodPressuresResponse>>>

    @GET("/api/v1.0/bloodPressures/cardID/{cardID}/history/{year}/{month}")
    fun getBloodPressuresHistory(
        @Path("cardID") cardID: String,
        @Path("year") year: String,
        @Path("month") month: String
    ): Single<Response<List<BloodPressuresResponse>>>

    @POST("/api/v1.0/bloodPressures")
    fun addBloodPressures(@Body body: BloodPressuresRequest): Single<Response<ResponseBody>>

    @GET("/api/v1.0/bloodPressures/{data}")
    fun getHistoryByDataID(@Path("data") dataID:String):Single<Response<List<BloodPressuresResponse>>>
}