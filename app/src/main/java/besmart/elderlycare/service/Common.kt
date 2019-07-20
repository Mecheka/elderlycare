package besmart.elderlycare.service

class Common(val networkClient: NetworkClient) {
    fun getAuthService(): AuthService = networkClient.create().create(AuthService::class.java)
    fun getElderlyService(): ElderlyService = networkClient.create().create(ElderlyService::class.java)
}