package com.zhdanon.bikefactory.di.koin

import com.zhdanon.bikefactory.presentation.BicycleViewModel
import org.koin.dsl.module

val appModule = module {
    factory {
        BicycleViewModel(bikeFactory = get())
    }
}