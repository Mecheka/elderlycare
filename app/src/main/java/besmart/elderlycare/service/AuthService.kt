package besmart.elderlycare.service

import besmart.elderlycare.model.login.LoginResponce
import besmart.elderlycare.model.register.RegisterRequest
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface AuthService {

    @POST("/api/v1.0/register/")
    fun register(@Body request: RegisterRequest): Single<ResponseBody>

    @FormUrlEncoded
    @POST("/api/v1.0/login/")
    fun login(
        @Header("Authorization") authToken: String,
        @Field("typeID") typeID: Int
    ): Single<Response<LoginResponce>>
}