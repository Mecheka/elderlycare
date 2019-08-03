package besmart.elderlycare.repository

import besmart.elderlycare.model.editprofile.EditProfileRequest
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.service.common.CommonWithAuth
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response

class ProfileRepository constructor(private val service: CommonWithAuth) {

    fun getAllProfile(): Single<Response<List<ProfileResponce>>> {
        return service.getProfileService().getAllProfile()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getProfileByCardId(cardId: String): Single<Response<ProfileResponce>> {
        return service.getProfileService().getProfileByCardId(cardId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getProfileByUserId(userId:String):Single<Response<ProfileResponce>>{
        return service.getProfileService().getProfileByUserId(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun editProfile(cardId: String, body: EditProfileRequest): Single<Response<ResponseBody>> {
        return service.getProfileService().editProfile(cardId, body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}