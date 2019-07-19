package besmart.elderlycare.di

import besmart.elderlycare.repository.RegisterRepository
import besmart.elderlycare.screen.register.RegisterViewModel
import besmart.elderlycare.service.Common
import besmart.elderlycare.service.NetworkClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {
    single { NetworkClient(androidContext()) }
    single { Common(get()).getAuthService() }
}

val viewModelModule = module {
    viewModel { RegisterViewModel(repository = get()) }
}

val repositoryModule = module {
    factory { RegisterRepository(get()) }
}