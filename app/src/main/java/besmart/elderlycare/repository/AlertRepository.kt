package besmart.elderlycare.repository

import besmart.elderlycare.model.notification.NotificationResponce
import besmart.elderlycare.service.common.CommonWithAuth
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class AlertRepository(private val service: CommonWithAuth) {

    fun getAlert(): Single<Response<List<NotificationResponce>>> {
        return service.getAlertService().getAlert()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}