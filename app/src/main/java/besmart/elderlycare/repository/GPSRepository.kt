package besmart.elderlycare.repository

import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.service.common.CommonWithAuth
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class GPSRepository(private val service: CommonWithAuth) {

    fun getGPSListUser(): Single<Response<List<ProfileResponce>>> {
        return service.getGPSService().getGPSListUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}