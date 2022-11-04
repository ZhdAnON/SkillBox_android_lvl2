package com.zhdanon.bikefactory

import android.app.Application
import com.zhdanon.bikefactory.data.FactoryBicycle
import com.zhdanon.bikefactory.data.FactoryFrame
import com.zhdanon.bikefactory.data.FactoryWheel
import com.zhdanon.bikefactory.dagger.AppDaggerComponent
import com.zhdanon.bikefactory.dagger.DaggerAppDaggerComponent
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {
    lateinit var appDaggerComponent: AppDaggerComponent

    override fun onCreate() {
        super.onCreate()

        appDaggerComponent = DaggerAppDaggerComponent.create()

        startKoin {
            modules(module {
                single { FactoryWheel() }
                factory { FactoryBicycle(FactoryFrame(), FactoryWheel()) }
            })
        }
    }
}