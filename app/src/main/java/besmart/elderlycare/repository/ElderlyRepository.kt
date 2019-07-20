package besmart.elderlycare.repository

import besmart.elderlycare.service.ElderlyService
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response

class ElderlyRepository constructor(private val service: ElderlyService){

    fun getMyElderly(): Single<Response<ResponseBody>> {
        return service.getMyElderly()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}