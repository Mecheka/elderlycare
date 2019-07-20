package besmart.elderlycare.service

class CommonWithOutAuth constructor(private val networkClient: NetworkClientWithOutAuth) {
    fun getAuthService(): AuthService = networkClient.create().create(AuthService::class.java)
}