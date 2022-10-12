package com.zhdanon.rickandmortycompose.presentation.navigation

sealed class Screen(val route: String) {
    object CharacterListScreen : Screen("character_list")
    object CharacterDetailScreen : Screen("character_detail")
    object MyTestScreen : Screen("test_screen")
}

const val SAVE_ARGS = "character"
const val MY_TEST_SAVE_ARGS = "my_test_save_args"