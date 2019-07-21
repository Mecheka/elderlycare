package besmart.elderlycare.screen.login

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import besmart.elderlycare.model.login.LoginResponce
import besmart.elderlycare.repository.LoginRepository
import besmart.elderlycare.repository.ProfileRepository
import besmart.elderlycare.screen.SelectType
import besmart.elderlycare.util.ActionLiveData
import besmart.elderlycare.util.BaseViewModel
import besmart.elderlycare.util.Constance
import besmart.elderlycare.util.HandingNetworkError
import com.orhanobut.hawk.Hawk
import okhttp3.Credentials

class LoginViewModel constructor(
    private val loginRepo: LoginRepository,
    private val profileRepo: ProfileRepository
) : BaseViewModel() {

    val usernameFiled = ObservableField<String>()
    val password = ObservableField<String>()

    lateinit var selectType: String

    private val _errorLiveEvent = ActionLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveEvent

    private val _successLiveEvent = ActionLiveData<Boolean>()
    val successLiveData: LiveData<Boolean>
        get() = _successLiveEvent

    private val _loadingLiveEvent = ActionLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveEvent

    fun onLoginClick() {
        _loadingLiveEvent.sendAction(true)
        if (validateData()) {
            login()
        }
    }

    private fun login() {
        val typeId = getTypeId()
        val username = getUserNameByType()
        val authToken = Credentials.basic(username, password.get()!!)

        addDisposable(loginRepo.login(typeId, authToken).subscribe({ responce ->
            if (responce.isSuccessful) {
                responce.body()?.let {
                    Hawk.put(Constance.TOKEN, it)
                    getProfile(it)
                }
            } else {
                val errorResponce = responce.errorBody()
                errorResponce?.let { _errorLiveEvent.sendAction(HandingNetworkError.getErrorMessage(it)) }
            }
        }, { error ->
            _loadingLiveEvent.sendAction(false)
            _errorLiveEvent.sendAction(HandingNetworkError.handingError(error))
        }))
    }

    private fun getProfile(user: LoginResponce) {
        addDisposable(
            profileRepo.getProfileByUserId(user.userID.toString()).subscribe({ responce ->
                _loadingLiveEvent.sendAction(false)
                if (responce.isSuccessful) {
                    _successLiveEvent.sendAction(true)
                    responce.body()?.let {
                        Hawk.put(Constance.USER, it)
                    }
                } else {
                    val errorResponce = responce.errorBody()
                    errorResponce?.let {
                        _errorLiveEvent.sendAction(HandingNetworkError.getErrorMessage(it))
                    }
                }
            }, { error ->
                _loadingLiveEvent.sendAction(false)
                _errorLiveEvent.sendAction(HandingNetworkError.handingError(error))
            })
        )
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
            SelectType.ORSOMO -> "vhv${usernameFiled.get()}"
            SelectType.HEALTH -> "pho${usernameFiled.get()}"
            else -> usernameFiled.get()!!
        }
    }

    private fun validateData(): Boolean {
        if (usernameFiled.get().isNullOrEmpty()) {
            _loadingLiveEvent.sendAction(false)
            _errorLiveEvent.sendAction("Please enter your username")
            return false
        }

        if (password.get().isNullOrEmpty()) {
            _loadingLiveEvent.sendAction(false)
            _errorLiveEvent.sendAction("Please enter your password")
            return false
        }

        return true
    }
}