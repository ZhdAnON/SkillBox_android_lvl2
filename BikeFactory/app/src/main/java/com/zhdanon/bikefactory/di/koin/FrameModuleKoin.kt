package com.zhdanon.bikefactory.di.koin

import com.zhdanon.bikefactory.data.FactoryFrame
import org.koin.dsl.module

val frameModule = module {
    factory {
        FactoryFrame()
    }
}