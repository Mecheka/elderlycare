package besmart.elderlycare.service

import besmart.elderlycare.model.profile.ProfileResponce
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfileService {

    @GET("/api/v1.0/profiles/")
    fun getAllProfile(): Single<Response<List<ProfileResponce>>>

    @GET("/api/v1.0/profiles/cardID/{cardId}")
    fun getProfileByCardId(@Path("cardId") cardId: String): Single<Response<ProfileResponce>>

    @GET("/api/v1.0/users/{userId}/profiles")
    fun getProfileByUserId(@Path("userId") userId: String): Single<Response<ProfileResponce>>
}