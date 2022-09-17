package com.zhdanon.nasaphotos.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class NasaViewModelFactory @Inject constructor(
    private val nasaViewModel: NasaViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NasaViewModel::class.java)) {
            return nasaViewModel as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}