package besmart.elderlycare.repository

import besmart.elderlycare.model.bodymass.BodyMassRequest
import besmart.elderlycare.model.bodymass.BodyMassResponce
import besmart.elderlycare.service.common.CommonWithAuth
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response

class BodyMassRepository(private val service: CommonWithAuth) {

    fun getBodyMassLastIndex(cardId: String): Single<Response<List<BodyMassResponce>>> {
        return service.getBodyMassService().getBodyMassLastIndex(cardId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getBodyMassHistory(cardId: String, year: String, month: String): Single<Response<List<BodyMassResponce>>> {
        return service.getBodyMassService().getBodyMassHistory(cardId, year, month)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun addBodyMass(body: BodyMassRequest): Single<Response<ResponseBody>> {
        return service.getBodyMassService().addBodyMass(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getHistoryByDate(date: String): Single<Response<List<BodyMassResponce>>> {
        return service.getBodyMassService().getHistoryByDate(date)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}