package com.zhdanon.rickandmortycompose.presentation.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zhdanon.rickandmortycompose.presentation.RaMViewModel
import com.zhdanon.rickandmortycompose.presentation.characterDetail.CharacterDetail
import com.zhdanon.rickandmortycompose.presentation.charactersList.CharacterList

@Composable
fun AppNavigation(
    context: Context,
    viewModel: RaMViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.CharacterListScreen.route
    ) {
        composable(route = Screen.CharacterListScreen.route) {
            CharacterList(
                context = context,
                navController = navController,
                viewModel = viewModel
            )
        }
        composable(route = Screen.CharacterDetailScreen.route) {
            CharacterDetail(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}