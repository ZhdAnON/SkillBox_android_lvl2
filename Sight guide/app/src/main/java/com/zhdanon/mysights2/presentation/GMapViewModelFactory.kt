package com.zhdanon.mysights2.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.zhdanon.mysights2.data.OTMRepository
import com.zhdanon.mysights2.domain.GetOTMUseCase
import javax.inject.Inject

class GMapViewModelFactory @Inject constructor(
    private val application: Application,
    private val getOTMUseCase: GetOTMUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(GMapViewModel::class.java)) {
            return GMapViewModel(application, getOTMUseCase) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}