package besmart.elderlycare

import android.app.Application
import besmart.elderlycare.di.networkModule
import besmart.elderlycare.di.repositoryModule
import besmart.elderlycare.di.viewModelModule
import com.crashlytics.android.Crashlytics
import com.orhanobut.hawk.Hawk
import io.fabric.sdk.android.Fabric
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class ElderlycarApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val fabric = Fabric.Builder(this)
            .kits(Crashlytics())
            .debuggable(true) // Enables Crashlytics debugger
            .build()
        Fabric.with(fabric)
        Hawk.init(this).build()
        startKoin {
            androidContext(this@ElderlycarApplication)
            androidLogger(Level.DEBUG)
            modules(networkModule, viewModelModule, repositoryModule)
        }
    }
}