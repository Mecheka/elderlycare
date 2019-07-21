package besmart.elderlycare.repository

import besmart.elderlycare.model.file.FileResponce
import besmart.elderlycare.service.common.CommonDevman
import besmart.elderlycare.service.common.CommonWithOutAuth
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class DevmanRepository constructor(private val service: CommonDevman) {

    fun getAllFile():Single<Response<FileResponce>>{
        return service.getDevmanService().getAllFile()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}