package besmart.elderlycare.service.common

import besmart.elderlycare.service.AuthService
import besmart.elderlycare.service.client.NetworkClientWithOutAuth

class CommonWithOutAuth constructor(private val networkClient: NetworkClientWithOutAuth) {
    fun getAuthService(): AuthService = networkClient.create().create(
        AuthService::class.java)
}