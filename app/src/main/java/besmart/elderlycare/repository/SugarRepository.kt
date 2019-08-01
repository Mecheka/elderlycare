package besmart.elderlycare.repository

import besmart.elderlycare.model.sugar.SugarReqeust
import besmart.elderlycare.model.sugar.SugarResponse
import besmart.elderlycare.service.common.CommonWithAuth
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response

class SugarRepository(private val service:CommonWithAuth) {

    fun getSugarLastIndex(cardId: String): Single<Response<List<SugarResponse>>> {
        return service.getSugarService().getSugarLastIndex(cardId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getSugarHistory(cardId: String, year: String, month: String): Single<Response<List<SugarResponse>>> {
        return service.getSugarService().getSugarHistory(cardId, year, month)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun addSugarBlood(body: SugarReqeust): Single<Response<ResponseBody>> {
        return service.getSugarService().addSugarBlood(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}