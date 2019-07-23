package besmart.elderlycare.service

import besmart.elderlycare.model.history.HistoryResponce
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HistoryService {

    @GET("/api/v1.0/histories/cardID/{cardID}")
    fun getHistoryByCardId(@Path("cardID") cardID:String):Single<Response<List<HistoryResponce>>>
}