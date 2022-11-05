package com.zhdanon.bikefactory.di.dagger

import com.zhdanon.bikefactory.data.FactoryBicycle
import com.zhdanon.bikefactory.presentation.BicycleViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun provideBicycleViewModelFactory(bicycleFactory: FactoryBicycle): BicycleViewModelFactory {
        return BicycleViewModelFactory(bicycleFactory)
    }
}