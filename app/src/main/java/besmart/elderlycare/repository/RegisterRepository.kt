package besmart.elderlycare.repository

import besmart.elderlycare.model.register.RegisterRequest
import besmart.elderlycare.service.AuthService
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Response
import okhttp3.ResponseBody

class RegisterRepository(val service: AuthService) {

    fun register(request: RegisterRequest): Single<ResponseBody> {
        return service.register(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}