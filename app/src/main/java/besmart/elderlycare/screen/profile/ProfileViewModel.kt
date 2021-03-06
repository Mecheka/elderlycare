package besmart.elderlycare.screen.profile

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.repository.ProfileRepository
import besmart.elderlycare.util.*
import com.orhanobut.hawk.Hawk
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ProfileViewModel(private val repository: ProfileRepository) : BaseViewModel(),
    CreateImage.OnCallBackListener {

    val name = ObservableField<String>()
    val cardId = ObservableField<String>()
    val gender = ObservableField<String>()
    val birthday = ObservableField<String>()
    val address = ObservableField<String>()
    private val imagePath = ObservableField<String>()

    lateinit var profile: ProfileResponce

    private val _errorLiveEvent = ActionLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveEvent

    private val _loadingLiveEvent = ActionLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveEvent

    private val _profileLiveData = MutableLiveData<ProfileResponce>()
    val profileLiveData: LiveData<ProfileResponce>
        get() = _profileLiveData

    private val user = Hawk.get(Constance.USER, ProfileResponce())

    fun getProfileByCardId() {
        _loadingLiveEvent.sendAction(true)
        addDisposable(
            repository.getProfileByCardId(user.cardID!!).subscribe({ responce ->
                _loadingLiveEvent.sendAction(false)
                if (responce.isSuccessful) {
                    responce.body()?.let {
                        name.set(it.firstName + " " + it.lastName)
                        cardId.set(it.cardID)
                        gender.set(getGenderById(it.genderID))
                        birthday.set(it.birthday)
                        address.set(it.address)
                        imagePath.set(it.imagePath)
                        profile = it
                        _profileLiveData.value = it
                    }
                } else {
                    responce.errorBody()?.let {
                        _errorLiveEvent.sendAction(HandingNetworkError.getErrorMessage(it))
                    }
                }
            }, { error ->
                _loadingLiveEvent.sendAction(false)
                _errorLiveEvent.sendAction(HandingNetworkError.handingError(error))
            })
        )
    }

    private fun getGenderById(genderID: Int?): String {
        return when (genderID) {
            1 -> "ชาย"
            else -> "หญิง"
        }
    }

    private fun uploadImage(result: File) {
        _loadingLiveEvent.sendAction(true)
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        val requestImage = RequestBody.create(MediaType.parse("image/*"), result)
        builder.addFormDataPart("image", result.name, requestImage)
        addDisposable(
            repository.uploadImageProfile(
                builder.build(),
                user.id.toString()
            ).subscribe(
                { response ->
                    _loadingLiveEvent.sendAction(false)
                    if (response.isSuccessful){
                        Log.e("upload image :", "image")
                    }else{
                        response.errorBody()?.let {
                            _errorLiveEvent.sendAction(HandingNetworkError.getErrorMessage(it))
                        }
                    }
                },
                { errpr ->
                    _loadingLiveEvent.sendAction(false)
                })
        )
    }

    override fun onCreateImageSuccess(result: File, width: String, height: String) {
        uploadImage(result)
    }

}

//[size=240 text={"longitude":0,"lastName":"edit","address":"test","id":6,"create…]