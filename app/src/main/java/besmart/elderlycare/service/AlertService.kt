package besmart.elderlycare.service

import besmart.elderlycare.model.notification.NotificationResponce
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

interface AlertService {

    @GET("/api/v1.0/alerts")
    fun getAlert(): Single<Response<List<NotificationResponce>>>
}