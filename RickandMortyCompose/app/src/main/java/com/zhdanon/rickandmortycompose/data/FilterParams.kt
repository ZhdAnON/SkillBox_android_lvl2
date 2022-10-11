package com.zhdanon.rickandmortycompose.data

class FilterParams(
    status: String = "",
    gender: String = ""
) {
    var paramStatus = status
        set(value) {
            if (statusList.contains(value)) field = value
        }

    var paramGender = gender
        set(value) {
            if (genderList.contains(value)) field = value
        }

    companion object {
        private val statusList = listOf(
            "",
            "Alive",
            "Dead"
        )

        private val genderList = listOf(
            "",
            "Male",
            "Female",
            "Genderless",
            "Unknown"
        )
    }
}