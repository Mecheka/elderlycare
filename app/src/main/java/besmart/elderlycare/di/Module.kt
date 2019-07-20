package besmart.elderlycare.di

import besmart.elderlycare.repository.ElderlyRepository
import besmart.elderlycare.repository.LoginRepository
import besmart.elderlycare.repository.RegisterRepository
import besmart.elderlycare.screen.elderlyprofile.ElderlyProfileViewModel
import besmart.elderlycare.screen.login.LoginViewModel
import besmart.elderlycare.screen.register.RegisterViewModel
import besmart.elderlycare.service.Common
import besmart.elderlycare.service.NetworkClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {
    single { NetworkClient(androidContext()) }
    single { Common(get()).getAuthService() }
    single { Common(get()).getElderlyService() }
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