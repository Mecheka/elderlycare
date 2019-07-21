package besmart.elderlycare.service.common

import besmart.elderlycare.service.DevmanService
import besmart.elderlycare.service.client.NetworkClientDevman

class CommonDevman(private val networkClient: NetworkClientDevman){
    fun getDevmanService():DevmanService = networkClient.create().create(DevmanService::class.java)
}