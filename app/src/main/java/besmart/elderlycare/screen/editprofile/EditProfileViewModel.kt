package besmart.elderlycare.screen.editprofile

import android.annotation.SuppressLint
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import besmart.elderlycare.model.editprofile.EditProfileRequest
import besmart.elderlycare.repository.ProfileRepository
import besmart.elderlycare.util.ActionLiveData
import besmart.elderlycare.util.BaseViewModel
import besmart.elderlycare.util.HandingNetworkError
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class EditProfileViewModel(private val repository: ProfileRepository) : BaseViewModel() {

    var cardId = ObservableField<String>()
    val firstName = ObservableField<String>()
    val lastName = ObservableField<String>()
    val birthday = ObservableField<String>()
    val genderId = ObservableField<String>()
    val address = ObservableField<String>()
    val phone = ObservableField<String>()

    private val _errorLiveEvent = ActionLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveEvent

    private val _successLiveEvent = ActionLiveData<Boolean>()
    val successLiveData: LiveData<Boolean>
        get() = _successLiveEvent

    private val _loadingLiveEvent = ActionLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveEvent

    @SuppressLint("SimpleDateFormat")
    fun onSaveClick(cardID: String?) {
        _loadingLiveEvent.sendAction(true)
        if (validateField()) {
            val inputFormat = SimpleDateFormat("dd MMM yyyy", Locale("TH"))
            val outputFormat = SimpleDateFormat("dd/MM/yyyy")
            val newDate = outputFormat.format(inputFormat.parse(birthday.get()))
            val body = EditProfileRequest(
                address.get()!!,
                newDate,
                firstName.get()!!,
                genderId.get()!!.toInt(),
                lastName.get()!!,
                0.0,
                0.0,
                phone.get()!!
            )
            addDisposable(
                repository.editProfile(cardID!!, body).subscribe(
                    { response ->
                        _loadingLiveEvent.sendAction(false)
                        if (response.isSuccessful) {
                            _successLiveEvent.sendAction(true)
                        } else {
                            response.errorBody()?.let {
                                _errorLiveEvent.sendAction(HandingNetworkError.getErrorMessage(it))
                            }
                        }
                    },
                    { error ->
                        _loadingLiveEvent.sendAction(false)
                        _errorLiveEvent.sendAction(HandingNetworkError.handingError(error))
                    })
            )
        }
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun validateField(): Boolean {
        val numberFormat = Pattern.compile("([^0-9]+)")
        if (cardId.get().isNullOrEmpty()) {
            _loadingLiveEvent.sendAction(false)
            _errorLiveEvent.sendAction("Please enter your card id")
            return false
        }
        if (cardId.get()?.length!! < 12) {
            _loadingLiveEvent.sendAction(false)
            _errorLiveEvent.sendAction("Please enter your card id 13")
            return false
        }
        if (firstName.get().isNullOrEmpty()) {
            _loadingLiveEvent.sendAction(false)
            _errorLiveEvent.sendAction("Please enter your first name")
            return false
        }
        if (lastName.get().isNullOrEmpty()) {
            _loadingLiveEvent.sendAction(false)
            _errorLiveEvent.sendAction("Please enter your last name")
            return false
        }
        if (birthday.get().isNullOrEmpty()) {
            _loadingLiveEvent.sendAction(false)
            _errorLiveEvent.sendAction("Please enter your birthday")
            return false
        }
        if (genderId.get().isNullOrEmpty()) {
            _loadingLiveEvent.sendAction(false)
            _errorLiveEvent.sendAction("Please select gender")
            return false
        }
        if (address.get().isNullOrEmpty()) {
            _loadingLiveEvent.sendAction(false)
            _errorLiveEvent.sendAction("Please enter your address")
            return false
        }
        if (phone.get().isNullOrEmpty()) {
            _errorLiveEvent.sendAction("Please enter your phone number")
            return false
        }
        if (numberFormat.matcher(phone.get()).matches()) {
            _loadingLiveEvent.sendAction(false)
            _errorLiveEvent.sendAction("Please enter your phone number invalidate format")
            return false
        }
        if (phone.get()?.length!! < 9) {
            _loadingLiveEvent.sendAction(false)
            _errorLiveEvent.sendAction("Please enter your phone number 10")
            return false
        }

        return true
    }
}