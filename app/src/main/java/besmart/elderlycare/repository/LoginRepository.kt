package besmart.elderlycare.repository

import besmart.elderlycare.model.login.LoginResponce
import besmart.elderlycare.service.AuthService
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response

class LoginRepository constructor(private val service: AuthService){

    fun login(typeID:Int, authToken:String):Single<Response<LoginResponce>>{
        return service.login(authToken, typeID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}