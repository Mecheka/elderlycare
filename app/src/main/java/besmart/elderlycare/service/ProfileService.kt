package besmart.elderlycare.service

import besmart.elderlycare.model.profile.ProfileResponce
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

interface ProfileService {

    @GET("/api/v1.0/profiles/")
    fun getAllProfile(): Single<Response<List<ProfileResponce>>>
}