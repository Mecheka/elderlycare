package besmart.elderlycare.di

import besmart.elderlycare.repository.*
import besmart.elderlycare.screen.blood.BloodPressureViewModel
import besmart.elderlycare.screen.bloodadd.AddBloodPressureViewModel
import besmart.elderlycare.screen.bloodhistory.BloodPressureHistoryViewModel
import besmart.elderlycare.screen.bodymass.BodyMassViewModel
import besmart.elderlycare.screen.bodymassadd.AddBodyMassViewModel
import besmart.elderlycare.screen.bodymasshistory.BodyMassHistoryViewModel
import besmart.elderlycare.screen.chat.ChatListUserViewModel
import besmart.elderlycare.screen.chat.ChatViewModel
import besmart.elderlycare.screen.editprofile.EditProfileViewModel
import besmart.elderlycare.screen.elderlyadd.AddMyElderlyViewModel
import besmart.elderlycare.screen.elderlyinfo.ElderlyinfoViewModel
import besmart.elderlycare.screen.evaluation.EvaluationViewModel
import besmart.elderlycare.screen.flie.FileViewModel
import besmart.elderlycare.screen.gps.GPSListUserViewModel
import besmart.elderlycare.screen.history.HistoryViewModel
import besmart.elderlycare.screen.historydetail.HistoryDetailViewModel
import besmart.elderlycare.screen.knowledge.KnowlegeViewModel
import besmart.elderlycare.screen.login.LoginViewModel
import besmart.elderlycare.screen.myelderlyprofile.MyElderlyProfileViewModel
import besmart.elderlycare.screen.news.NewsViewModel
import besmart.elderlycare.screen.notification.NotificationViewModel
import besmart.elderlycare.screen.profile.ProfileViewModel
import besmart.elderlycare.screen.question.QuestionViewModel
import besmart.elderlycare.screen.question.history.QuestHistorySingleViewModel
import besmart.elderlycare.screen.question.history.QuestionHistoryViewModel
import besmart.elderlycare.screen.register.RegisterViewModel
import besmart.elderlycare.screen.sugar.SugarViewModel
import besmart.elderlycare.screen.sugaradd.SugarAddViewModel
import besmart.elderlycare.screen.sugarhistory.SugarHistoryViewModel
import besmart.elderlycare.screen.villagehealthvoluntor.VillageHealthVolunteerViewModel
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
    viewModel { AddMyElderlyViewModel(profileRepo = get(), elderlyRepo = get()) }
    viewModel { ElderlyinfoViewModel(get(),get()) }
    viewModel { FileViewModel(repository = get()) }
    viewModel { KnowlegeViewModel(repository = get()) }
    viewModel { NewsViewModel(repository = get()) }
    viewModel { ProfileViewModel(repository = get()) }
    viewModel { EditProfileViewModel(get()) }
    viewModel { HistoryViewModel(repository = get()) }
    viewModel { HistoryDetailViewModel(get(), get(), get()) }
    viewModel { BodyMassViewModel(repository = get()) }
    viewModel { BodyMassHistoryViewModel(repository = get()) }
    viewModel { AddBodyMassViewModel(repository = get()) }
    viewModel { BloodPressureViewModel(repository = get()) }
    viewModel { AddBloodPressureViewModel(repository = get()) }
    viewModel { BloodPressureHistoryViewModel(repository = get()) }
    viewModel { SugarViewModel(repository = get()) }
    viewModel { SugarAddViewModel(repository = get()) }
    viewModel { SugarHistoryViewModel(repository = get()) }
    viewModel { NotificationViewModel(get()) }
    viewModel { GPSListUserViewModel(get()) }
    viewModel { ChatListUserViewModel(get()) }
    viewModel { ChatViewModel(get()) }
    viewModel { EvaluationViewModel(get()) }
    viewModel { QuestionViewModel(get()) }
    viewModel { QuestionHistoryViewModel(get()) }
    viewModel { QuestHistorySingleViewModel(get()) }
    viewModel { VillageHealthVolunteerViewModel(repository = get(), elderlyRepo = get()) }
}

val repositoryModule = module {
    factory { RegisterRepository(get()) }
    factory { LoginRepository(get()) }
    single { ElderlyRepository(get()) }
    single { ProfileRepository(get()) }
    single { DevmanRepository(get()) }
    single { HistoryRepository(get()) }
    single { BodyMassRepository(get()) }
    single { BloodPresureRepository(get()) }
    single { SugarRepository(get()) }
    single { VeaccineRepository() }
    single { AlertRepository(get()) }
    single { GPSRepository(get()) }
    single { EvaluationRepository(get()) }
    single { VillageHealthVolunteerRepository(get()) }
}