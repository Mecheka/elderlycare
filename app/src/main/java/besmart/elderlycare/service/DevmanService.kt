package besmart.elderlycare.service

import besmart.elderlycare.model.file.FileResponce
import besmart.elderlycare.model.knowlege.KnowlegeResponce
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

interface DevmanService {

    @GET("api/files")
    fun getAllFile(): Single<Response<FileResponce>>

    @GET("api/page")
    fun getAllKnowlege():Single<Response<List<KnowlegeResponce>>>
}