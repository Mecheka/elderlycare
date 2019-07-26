package besmart.elderlycare.repository

import besmart.elderlycare.model.history.HistoryResponce
import besmart.elderlycare.service.common.CommonWithAuth
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class HistoryRepository (private val service:CommonWithAuth){

    fun getHistoryByCardId(cardID:String):Single<Response<List<HistoryResponce>>>{
        return service.getHistoryService().getHistoryByCardId(cardID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}