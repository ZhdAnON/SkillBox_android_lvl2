package com.zhdanon.rickandmortycompose.presentation.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MyTestClass(
    val name: String,
    val age: Int
) : Parcelable
