package com.zhdanon.bikefactory.di.dagger

import com.zhdanon.bikefactory.data.FactoryBicycle
import com.zhdanon.bikefactory.data.FactoryFrame
import com.zhdanon.bikefactory.data.FactoryWheel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class BicycleFactoryModule {
    @Provides
    fun provideFrameFactory(): FactoryFrame {
        return FactoryFrame()
    }

    @Provides
    @Singleton
    fun provideWheelFactory(): FactoryWheel {
        return FactoryWheel()
    }

    @Provides
    fun provideBicycleFactory(
        frameFactory: FactoryFrame,
        wheelFactory: FactoryWheel
    ): FactoryBicycle {
        return FactoryBicycle(frameFactory, wheelFactory)
    }
}