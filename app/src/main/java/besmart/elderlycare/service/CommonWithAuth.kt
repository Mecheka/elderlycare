package besmart.elderlycare.service

class CommonWithAuth(val networkClient: NetworkClientWithAuth) {
    fun getElderlyService(): ElderlyService = networkClient.create().create(ElderlyService::class.java)
}