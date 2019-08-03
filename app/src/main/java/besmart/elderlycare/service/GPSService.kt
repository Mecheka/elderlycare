package besmart.elderlycare.service

import besmart.elderlycare.model.profile.ProfileResponce
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

interface GPSService {

    @GET("/api/v1.0/gps")
    fun getGPSListUser(): Single<Response<List<ProfileResponce>>>
}