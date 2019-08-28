package besmart.elderlycare.service

import besmart.elderlycare.model.editprofile.EditProfileRequest
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.model.user.UserResponse
import io.reactivex.Single
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ProfileService {
    // Not filter
    //    @GET("/api/v1.0/profiles/")
    //    fun getAllProfileWithVillageHealthVolunteer(): Single<Response<List<ProfileResponce>>>

    // Filter by type
    @GET("/api/v1.0/users/villageHealthVolunteer/add")
    fun getAllProfileWithVillageHealthVolunteer(): Single<Response<List<ProfileResponce>>>

    @GET("/api/v1.0/myElderlies/add")
    fun getAllProfileWithOrsomor(): Single<Response<List<ProfileResponce>>>

    @GET("/api/v1.0/profiles/cardID/{cardId}")
    fun getProfileByCardId(@Path("cardId") cardId: String): Single<Response<ProfileResponce>>

    @GET("/api/v1.0/users/cardID/{cardId}/typeID/{typeId}")
    fun getProfileByCardIdAndTypeId(@Path("cardId") cardId: String,
                                    @Path("typeId") typeId:String): Single<Response<UserResponse>>

    @GET("/api/v1.0/users/{userId}/profiles")
    fun getProfileByUserId(@Path("userId") userId: String): Single<Response<ProfileResponce>>

    @PATCH("/api/v1.0/profiles/{cardID}")
    fun editProfile(
        @Path("cardID") cardId: String,
        @Body body: EditProfileRequest
    ): Single<Response<ResponseBody>>

    @POST("/api/v1.0/profiles/{profileID}/upload/")
    fun uploadImageProfile(
        @Path("profileID") profileID: String,
        @Body body: RequestBody
    ): Single<Response<ResponseBody>>
}