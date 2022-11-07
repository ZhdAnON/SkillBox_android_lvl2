package com.zhdanon.bikefactory.di.dagger

import com.zhdanon.bikefactory.presentation.FragmentDagger
import com.zhdanon.bikefactory.presentation.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, BicycleFactoryModule::class])
interface AppDaggerComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(fragment: FragmentDagger)
}