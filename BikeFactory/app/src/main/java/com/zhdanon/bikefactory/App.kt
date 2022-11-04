package com.zhdanon.bikefactory

import android.app.Application
import com.zhdanon.bikefactory.di.dagger.AppDaggerComponent
import com.zhdanon.bikefactory.di.dagger.DaggerAppDaggerComponent

class App : Application() {
    lateinit var appDaggerComponent: AppDaggerComponent

    override fun onCreate() {
        super.onCreate()

        appDaggerComponent = DaggerAppDaggerComponent.create()
    }
}