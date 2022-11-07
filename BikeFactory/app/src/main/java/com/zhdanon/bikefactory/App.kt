package com.zhdanon.bikefactory

import android.app.Application
import com.zhdanon.bikefactory.di.dagger.AppDaggerComponent
import com.zhdanon.bikefactory.di.dagger.DaggerAppDaggerComponent
import com.zhdanon.bikefactory.di.koin.appModule
import com.zhdanon.bikefactory.di.koin.bicycleModule
import com.zhdanon.bikefactory.di.koin.frameModule
import com.zhdanon.bikefactory.di.koin.wheelModule
import org.koin.core.context.startKoin

class App : Application() {
    lateinit var appDaggerComponent: AppDaggerComponent

    override fun onCreate() {
        super.onCreate()

        appDaggerComponent = DaggerAppDaggerComponent.create()

        startKoin {
            modules(listOf(appModule, bicycleModule, frameModule, wheelModule))
        }
    }
}

const val TAG = "DaggerApp"