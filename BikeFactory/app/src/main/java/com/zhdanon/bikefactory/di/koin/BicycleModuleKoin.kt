package com.zhdanon.bikefactory.di.koin

import com.zhdanon.bikefactory.data.FactoryBicycle
import org.koin.dsl.module

val bicycleModule = module {
    factory {
        FactoryBicycle(
            frameFactory = get(),
            wheelFactory = get()
        )
    }
}