package besmart.elderlycare.service

import besmart.elderlycare.service.client.NetworkClientWithAuth

class CommonWithAuth constructor(private val networkClient: NetworkClientWithAuth) {
    fun getElderlyService(): ElderlyService = networkClient.create().create(ElderlyService::class.java)
    fun getProfileService(): ProfileService = networkClient.create().create(ProfileService::class.java)
}