package besmart.elderlycare.repository

import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.service.CommonWithAuth
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class ProfileRepository constructor(private val service: CommonWithAuth) {

    fun getAllProfile(): Single<Response<List<ProfileResponce>>> {
        return service.getProfileService().getAllProfile()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}