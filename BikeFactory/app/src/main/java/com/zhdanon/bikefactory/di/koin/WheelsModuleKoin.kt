package com.zhdanon.bikefactory.di.koin

import com.zhdanon.bikefactory.data.FactoryWheel
import org.koin.dsl.module

val wheelModule = module {
    single {
        FactoryWheel()
    }
}