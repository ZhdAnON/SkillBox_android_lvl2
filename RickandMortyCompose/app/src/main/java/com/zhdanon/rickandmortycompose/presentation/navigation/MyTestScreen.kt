package com.zhdanon.rickandmortycompose.presentation.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zhdanon.rickandmortycompose.presentation.theme.RaMTheme

@Composable
fun TestScreenView(person: MyTestClass?) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = person?.name ?: "Unknown name",
            fontSize = 32.sp
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = person?.age.toString(),
            fontSize = 32.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TestPreview() {
    RaMTheme {
        Surface(
            color = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary
        ) {
            val person = MyTestClass("Andrey", 37)
            TestScreenView(person = person)
        }
    }
}