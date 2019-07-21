package besmart.elderlycare.service

import besmart.elderlycare.model.file.FileResponce
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

interface FileService {

    @GET("api/files")
    fun getAllFile(): Single<Response<FileResponce>>
}