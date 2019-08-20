package besmart.elderlycare.repository

import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.model.user.UserResponse
import besmart.elderlycare.service.common.CommonWithAuth
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class VillageHealthVolunteerRepository(private val service: CommonWithAuth) {

    fun getProfileByCardIdAndTypeId(
        cardId: String,
        typeId: String
    ): Single<Response<UserResponse>> {
        return service.getProfileService().getProfileByCardIdAndTypeId(cardId, typeId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getElderlyByStaffId(staffId: String): Single<Response<List<ProfileResponce>>> {
        return service.getElderlyService().getMyElderlyByStaffId(staffId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}