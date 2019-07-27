package besmart.elderlycare.di

import besmart.elderlycare.repository.*
import besmart.elderlycare.screen.addelderly.AddElderlyViewModel
import besmart.elderlycare.screen.bodymass.BodyMassViewModel
import besmart.elderlycare.screen.evaluation.EvalustionViewModel
import besmart.elderlycare.screen.flie.FileViewModel
import besmart.elderlycare.screen.history.HistoryViewModel
import besmart.elderlycare.screen.knowledge.KnowlegeViewModel
import besmart.elderlycare.screen.login.LoginViewModel
import besmart.elderlycare.screen.myelderlyprofile.MyElderlyProfileViewModel
import besmart.elderlycare.screen.news.NewsViewModel
import besmart.elderlycare.screen.profile.ProfileViewModel
import besmart.elderlycare.screen.register.RegisterViewModel
import besmart.elderlycare.screen.sugar.SugarViewModel
import besmart.elderlycare.service.client.NetworkClientDevman
import besmart.elderlycare.service.client.NetworkClientWithAuth
import besmart.elderlycare.service.client.NetworkClientWithOutAuth
import besmart.elderlycare.service.common.CommonDevman
import besmart.elderlycare.service.common.CommonWithAuth
import besmart.elderlycare.service.common.CommonWithOutAuth
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
    viewModel { LoginViewModel(loginRepo = get(), profileRepo = get()) }
    viewModel { MyElderlyProfileViewModel(repository = get())}
    viewModel { AddElderlyViewModel(profileRepo = get(), elderlyRepo = get()) }
    viewModel { FileViewModel(repository = get()) }
    viewModel { KnowlegeViewModel(repository = get()) }
    viewModel { NewsViewModel(repository = get()) }
    viewModel { ProfileViewModel(repository = get()) }
    viewModel { HistoryViewModel(repository = get()) }
    viewModel { BodyMassViewModel(repository = get()) }
    viewModel { EvalustionViewModel(repository = get()) }
    viewModel { SugarViewModel(repository = get()) }
}

val repositoryModule = module {
    factory { RegisterRepository(get()) }
    factory { LoginRepository(get()) }
    single { ElderlyRepository(get()) }
    single { ProfileRepository(get()) }
    single { DevmanRepository(get()) }
    single { HistoryRepository(get()) }
    single { BodyMassRepository(get()) }
    single { EvaluationRepository(get()) }
    single { SugarRepository(get()) }
}