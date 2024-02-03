package uz.gita.mycontactb7.app

import android.app.Application
import timber.log.Timber
import uz.gita.mycontactb7.data.source.local.AppDatabase
import uz.gita.mycontactb7.data.source.remote.ApiClient
import uz.gita.mycontactb7.domain.impl.ContactRepositoryImpl

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        ApiClient.init(this)
        AppDatabase.init(this)
        ContactRepositoryImpl.init()
    }
}


