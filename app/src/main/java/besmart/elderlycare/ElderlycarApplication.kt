package besmart.elderlycare

import android.app.Application
import besmart.elderlycare.di.networkModule
import besmart.elderlycare.di.repositoryModule
import besmart.elderlycare.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class ElderlycarApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ElderlycarApplication)
            androidLogger(Level.DEBUG)
            modules(networkModule, viewModelModule, repositoryModule)
        }
    }
}