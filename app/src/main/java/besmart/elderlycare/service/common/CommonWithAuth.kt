package besmart.elderlycare.service.common

import besmart.elderlycare.service.BodyMassService
import besmart.elderlycare.service.ElderlyService
import besmart.elderlycare.service.HistoryService
import besmart.elderlycare.service.ProfileService
import besmart.elderlycare.service.client.NetworkClientWithAuth

class CommonWithAuth constructor(private val networkClient: NetworkClientWithAuth) {

    fun getElderlyService(): ElderlyService = networkClient.create().create(
        ElderlyService::class.java)

    fun getProfileService(): ProfileService = networkClient.create().create(
        ProfileService::class.java)

    fun getHistoryService(): HistoryService =
        networkClient.create().create(HistoryService::class.java)

    fun getBodyMassService(): BodyMassService =
        networkClient.create().create(BodyMassService::class.java)
}