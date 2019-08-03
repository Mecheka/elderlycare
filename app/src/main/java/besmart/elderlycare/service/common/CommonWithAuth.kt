package besmart.elderlycare.service.common

import besmart.elderlycare.service.*
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

    fun getEvaluationService(): EvaluationService =
        networkClient.create().create(EvaluationService::class.java)

    fun getSugarService(): SugarService = networkClient.create().create(SugarService::class.java)

    fun getAlertService(): AlertService = networkClient.create().create(AlertService::class.java)

    fun getGPSService(): GPSService = networkClient.create().create(GPSService::class.java)
}