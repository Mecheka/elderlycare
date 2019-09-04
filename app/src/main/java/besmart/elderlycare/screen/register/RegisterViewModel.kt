package besmart.elderlycare.screen.register

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import besmart.elderlycare.model.register.RegisterRequest
import besmart.elderlycare.repository.RegisterRepository
import besmart.elderlycare.screen.SelectType
import besmart.elderlycare.util.ActionLiveData
import besmart.elderlycare.util.BaseViewModel
import besmart.elderlycare.util.HandingNetworkError
import java.util.regex.Pattern


class RegisterViewModel constructor(private val repository: RegisterRepository) : BaseViewModel() {

    var staffId = ObservableField<String>()
    var cardId = ObservableField<String>()
    val password = ObservableField<String>()
    val firstName = ObservableField<String>()
    val lastName = ObservableField<String>()
    val birthday = ObservableField<String>()
    val genderId = ObservableField<String>()
    val address = ObservableField<String>()
    val phone = ObservableField<String>()

    lateinit var selectType: String

    private lateinit var request: RegisterRequest

    private val _errorLiveEvent = ActionLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveEvent

    private val _successLiveEvent = ActionLiveData<Boolean>()
    val successLiveData: LiveData<Boolean>
        get() = _successLiveEvent

    private val _loadingLiveEvent = ActionLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveEvent

    fun register() {
        _loadingLiveEvent.sendAction(true)
        if (validateField()) {
            if(selectType == SelectType.PERSON){
                request = RegisterRequest(
                    typeID = getTypeId(),
                    cardID = cardId.get()!!,
                    password = password.get()!!,
                    verifyPassword = password.get()!!,
                    firstName = firstName.get()!!,
                    lastName = lastName.get()!!,
                    birthday = birthday.get()!!,
                    genderID = genderId.get()!!.toInt() + 1,
                    address = address.get()!!,
                    phone = phone.get()!!,
                    username = getUserNameByType()
                )
            }else{
                request = RegisterRequest(
                    typeID = getTypeId(),
                    staffID = staffId.get()!!,
                    cardID = cardId.get()!!,
                    password = password.get()!!,
                    verifyPassword = password.get()!!,
                    firstName = firstName.get()!!,
                    lastName = lastName.get()!!,
                    birthday = birthday.get()!!,
                    genderID = genderId.get()!!.toInt() + 1,
                    address = address.get()!!,
                    phone = phone.get()!!,
                    username = getUserNameByType()
                )
            }

            addDisposable(repository.register(request).subscribe({ responce ->
                _loadingLiveEvent.sendAction(false)
                _successLiveEvent.sendAction(true)
            }, { e ->
                _errorLiveEvent.sendAction(HandingNetworkError.handingError(e))
            }))
        }
    }

    private fun getTypeId(): Int {
        return when (selectType) {
            SelectType.ORSOMO -> 1
            SelectType.HEALTH -> 2
            else -> 3
        }
    }

    private fun getUserNameByType(): String {
        return when (selectType) {
            SelectType.ORSOMO -> "vhv${staffId.get()}"
            SelectType.HEALTH -> "pho${staffId.get()}"
            else -> cardId.get()!!
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
        if (staffId.get().isNullOrEmpty() && selectType != SelectType.PERSON) {
            _loadingLiveEvent.sendAction(false)
            _errorLiveEvent.sendAction("Please enter your staff id")
            return false
        }
        if (password.get().isNullOrEmpty()) {
            _loadingLiveEvent.sendAction(false)
            _errorLiveEvent.sendAction("Please enter your password")
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

        return true
    }
}