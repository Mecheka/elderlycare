package besmart.elderlycare.service.common

import besmart.elderlycare.service.FileService
import besmart.elderlycare.service.client.NetworkClientDevman

class CommonDevman(private val networkClient: NetworkClientDevman){
    fun getDevmanService():FileService = networkClient.create().create(FileService::class.java)
}