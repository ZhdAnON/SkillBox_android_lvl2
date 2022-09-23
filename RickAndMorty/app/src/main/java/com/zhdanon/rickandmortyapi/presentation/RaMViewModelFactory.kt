package com.zhdanon.rickandmortyapi.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class RaMViewModelFactory @Inject constructor(
    private val ramViewModel: RaMViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RaMViewModel::class.java))
            return ramViewModel as T
        throw IllegalArgumentException("Unknown class name")
    }
}