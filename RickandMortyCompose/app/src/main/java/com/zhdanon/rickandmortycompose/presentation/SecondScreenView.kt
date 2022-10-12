package com.zhdanon.rickandmortycompose.presentation

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zhdanon.rickandmortycompose.data.characters.LocationDto
import com.zhdanon.rickandmortycompose.data.characters.OriginDto
import com.zhdanon.rickandmortycompose.data.characters.ResultCharacterDto

@Composable
fun SecondScreenView(character: ResultCharacterDto?) {
    Log.d("SecondScreenView", "name - ${character?.name} | age - ${character?.id}")
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Text(
            text = character?.name ?: "Кто-то",
            fontSize = 32.sp,
        )
        Spacer(modifier = Modifier.size(24.dp))
        Text(
            text = character?.id.toString(),
            fontSize = 24.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SecondPreview() {
//    val newObject = MyClass("Andrey", 37)
    val newObject = ResultCharacterDto(
        created = "2017-11-04T18:48:46.250Z",
        episode = listOf("1", "2", "3"),
        gender = null,
        id = 1,
        image = "",
        location = LocationDto(name = "Earth", url = null),
        name = "Rick Sanchez",
        origin = OriginDto(
            name = "Earth",
            url = "https://rickandmortyapi.com/api/location/1"
        ),
        species = "Human",
        status = "Alive",
        type = "",
        url = "https://rickandmortyapi.com/api/character/1"
    )
    SecondScreenView(character = newObject)
}