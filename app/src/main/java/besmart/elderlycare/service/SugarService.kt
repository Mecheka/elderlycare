package besmart.elderlycare.service

import besmart.elderlycare.model.sugar.SugarResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SugarService {

    @GET("/api/v1.0/bloodSugars/cardID/{cardID}")
    fun getSugarLastIndex(@Path("cardID") cardID: String): Single<Response<List<SugarResponse>>>

    @GET("/api/v1.0/bloodSugars/cardID/{cardID}/history/{year}/{month}")
    fun getSugarHistory(
        @Path("cardID") cardID: String,
        @Path("year") year: String,
        @Path("month") month: String
    ): Single<Response<List<SugarResponse>>>
}