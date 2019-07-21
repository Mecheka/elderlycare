package besmart.elderlycare.di

import besmart.elderlycare.repository.*
import besmart.elderlycare.screen.addelderly.AddElderlyViewModel
import besmart.elderlycare.screen.flie.FileViewModel
import besmart.elderlycare.screen.login.LoginViewModel
import besmart.elderlycare.screen.myelderlyprofile.MyElderlyProfileViewModel
import besmart.elderlycare.screen.register.RegisterViewModel
import besmart.elderlycare.service.client.NetworkClientDevman
import besmart.elderlycare.service.common.CommonWithAuth
import besmart.elderlycare.service.common.CommonWithOutAuth
import besmart.elderlycare.service.client.NetworkClientWithAuth
import besmart.elderlycare.service.client.NetworkClientWithOutAuth
import besmart.elderlycare.service.common.CommonDevman
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {
    single { NetworkClientWithAuth(androidContext()) }
    single { NetworkClientWithOutAuth(androidContext()) }
    single { NetworkClientDevman(androidContext()) }
    single { CommonWithOutAuth(get()) }
    single { CommonWithAuth(get()) }
    single { CommonDevman(get()) }

}

val viewModelModule = module {
    viewModel { RegisterViewModel(repository = get()) }
    viewModel { LoginViewModel(repository = get()) }
    viewModel { MyElderlyProfileViewModel(repository = get())}
    viewModel { AddElderlyViewModel(profileRepo = get(), elderlyRepo = get()) }
    viewModel { FileViewModel(repository = get()) }
}

val repositoryModule = module {
    factory { RegisterRepository(get()) }
    factory { LoginRepository(get()) }
    single { ElderlyRepository(get()) }
    single { ProfileRepository(get()) }
    single { DevmanRepository(get()) }
}