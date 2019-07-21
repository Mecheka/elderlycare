package besmart.elderlycare.service

import besmart.elderlycare.model.devman.DevmanResponce
import besmart.elderlycare.model.file.FileData
import besmart.elderlycare.model.knowlege.KnowlegeResponce
import besmart.elderlycare.model.news.NewsData
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

interface DevmanService {

    @GET("api/files")
    fun getAllFile(): Single<Response<DevmanResponce<FileData>>>

    @GET("api/page")
    fun getAllKnowlege():Single<Response<List<KnowlegeResponce>>>

    @GET("api/news")
    fun getAllNews(): Single<Response<DevmanResponce<NewsData>>>
}