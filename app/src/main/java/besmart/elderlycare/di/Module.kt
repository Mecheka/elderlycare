package besmart.elderlycare.di

import besmart.elderlycare.repository.ElderlyRepository
import besmart.elderlycare.repository.LoginRepository
import besmart.elderlycare.repository.RegisterRepository
import besmart.elderlycare.screen.elderlyprofile.ElderlyProfileViewModel
import besmart.elderlycare.screen.login.LoginViewModel
import besmart.elderlycare.screen.register.RegisterViewModel
import besmart.elderlycare.service.CommonWithAuth
import besmart.elderlycare.service.CommonWithOutAuth
import besmart.elderlycare.service.NetworkClientWithAuth
import besmart.elderlycare.service.NetworkClientWithOutAuth
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {
    single { NetworkClientWithAuth(androidContext()) }
    single { NetworkClientWithOutAuth(androidContext()) }
    single { CommonWithOutAuth(get()).getAuthService() }
    single { CommonWithAuth(get()).getElderlyService() }
}

val viewModelModule = module {
    viewModel { RegisterViewModel(repository = get()) }
    viewModel { LoginViewModel(repository = get()) }
    viewModel { ElderlyProfileViewModel(repository = get())}
}

val repositoryModule = module {
    factory { RegisterRepository(get()) }
    factory { LoginRepository(get()) }
    factory { ElderlyRepository(get()) }
}