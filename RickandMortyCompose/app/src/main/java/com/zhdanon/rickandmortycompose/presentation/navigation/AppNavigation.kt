package com.zhdanon.rickandmortycompose.presentation.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.zhdanon.rickandmortycompose.data.characters.ResultCharacterDto
import com.zhdanon.rickandmortycompose.presentation.RaMViewModel
import com.zhdanon.rickandmortycompose.presentation.currentCharacter.CharacterDetail
import com.zhdanon.rickandmortycompose.presentation.listCharacters.CharacterList

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
        composable(
            route = Screen.CharacterDetailScreen.route + "/$SAVE_ARGS",
            arguments = listOf(navArgument(SAVE_ARGS) { type = NavType.StringType })
        ) { entry ->
            entry.arguments?.getString(SAVE_ARGS)?.let { json ->
                val character = Gson().fromJson(json, ResultCharacterDto::class.java)
                CharacterDetail(
                    navController = navController,
                    viewModel = viewModel,
                    character = character
                )
            }
        }

        composable(
            route = Screen.MyTestScreen.route + "/{$MY_TEST_SAVE_ARGS}",
            arguments = listOf(
                navArgument(MY_TEST_SAVE_ARGS) { type = NavType.StringType }
            )
        ) { entry ->
            entry.arguments?.getString(MY_TEST_SAVE_ARGS)?.let { json ->
                val person = Gson().fromJson(json, MyTestClass::class.java)
                TestScreenView(person)
            }
        }

//        composable(
//            route = Screen.CharacterDetailScreen.route + "/{character}",
//            arguments = listOf(
//                navArgument("character") { type = NavType.StringType }
//            )
//        ) { entry ->
//            entry.arguments?.getString("character")?.let { json ->
//                val character = Gson().fromJson(json, ResultCharacterDto::class.java)
//                CharacterDetail(
//                    navController = navController,
//                    viewModel = viewModel,
//                    character = character
//                )
//            }
//        }
    }
}