package besmart.elderlycare.di

import besmart.elderlycare.repository.ElderlyRepository
import besmart.elderlycare.repository.LoginRepository
import besmart.elderlycare.repository.ProfileRepository
import besmart.elderlycare.repository.RegisterRepository
import besmart.elderlycare.screen.addelderly.AddElderlyViewModel
import besmart.elderlycare.screen.myelderlyprofile.MyElderlyProfileViewModel
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
    single { CommonWithOutAuth(get()) }
    single { CommonWithAuth(get()) }
}

val viewModelModule = module {
    viewModel { RegisterViewModel(repository = get()) }
    viewModel { LoginViewModel(repository = get()) }
    viewModel { MyElderlyProfileViewModel(repository = get())}
    viewModel { AddElderlyViewModel(profileRepo = get(), elderlyRepo = get()) }
}

val repositoryModule = module {
    factory { RegisterRepository(get()) }
    factory { LoginRepository(get()) }
    single { ElderlyRepository(get()) }
    single { ProfileRepository(get()) }
}