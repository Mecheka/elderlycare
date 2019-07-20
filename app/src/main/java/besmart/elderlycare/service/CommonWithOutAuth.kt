package besmart.elderlycare.service

class CommonWithOutAuth(val networkClient: NetworkClientWithOutAuth) {
    fun getAuthService(): AuthService = networkClient.create().create(AuthService::class.java)
}