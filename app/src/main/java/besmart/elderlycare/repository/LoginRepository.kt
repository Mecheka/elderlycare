package besmart.elderlycare.repository

import besmart.elderlycare.model.login.LoginResponce
import besmart.elderlycare.service.common.CommonWithAuth
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class LoginRepository constructor(private val service: CommonWithAuth){

    fun login(typeID:Int, authToken:String):Single<Response<LoginResponce>>{
        return service.getAuthService().login(authToken, typeID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}