package besmart.elderlycare.repository

import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.service.common.CommonWithAuth
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response

class ElderlyRepository constructor(private val service: CommonWithAuth) {

    fun getMyElderly(): Single<Response<List<ProfileResponce>>> {
        return service.getElderlyService().getMyElderly()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun createElderly(peopleCardID: String?): Single<Response<ResponseBody>> {
        return service.getElderlyService().createMyElderly(peopleCardID!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}