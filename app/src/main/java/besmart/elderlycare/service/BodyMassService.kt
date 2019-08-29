package besmart.elderlycare.service

import besmart.elderlycare.model.bodymass.BodyMassRequest
import besmart.elderlycare.model.bodymass.BodyMassResponce
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface BodyMassService {

    @GET("/api/v1.0/users/cardID/{cardID}/bodyMassIndexs")
    fun getBodyMassLastIndex(@Path("cardID") cardID: String): Single<Response<List<BodyMassResponce>>>

    @GET("/api/v1.0/bodyMassIndexs/cardID/{cardID}/history/{year}/{month}")
    fun getBodyMassHistory(
        @Path("cardID") cardID: String,
        @Path("year") year: String,
        @Path("month") month: String
    ): Single<Response<List<BodyMassResponce>>>

    @POST("/api/v1.0/bodyMassIndexs")
    fun addBodyMass(@Body body: BodyMassRequest): Single<Response<ResponseBody>>

    @GET("/api/v1.0/bodyMassIndexs/{date}")
    fun getHistoryByDate(@Path("date") date: String): Single<Response<List<BodyMassResponce>>>

    @DELETE("/api/v1.0/bodyMassIndexs/{bodyMassIndexID}")
    fun removeBloodPressureById(@Path("bodyMassIndexID") bloodPressureId: String): Single<Response<ResponseBody>>
}