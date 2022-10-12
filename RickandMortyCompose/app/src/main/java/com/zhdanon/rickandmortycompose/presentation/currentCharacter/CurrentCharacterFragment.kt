package com.zhdanon.rickandmortycompose.presentation.currentCharacter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.zhdanon.rickandmortycompose.R
import com.zhdanon.rickandmortycompose.data.EpisodeDto
import com.zhdanon.rickandmortycompose.data.characters.LocationDto
import com.zhdanon.rickandmortycompose.data.characters.OriginDto
import com.zhdanon.rickandmortycompose.data.characters.ResultCharacterDto
import com.zhdanon.rickandmortycompose.presentation.RaMViewModel
import com.zhdanon.rickandmortycompose.presentation.theme.RaMTheme

class CurrentCharacterFragment : Fragment() {
    private val viewModel: RaMViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            RaMTheme {
                Surface(
                    color = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary
                ) {
                    val args: CurrentCharacterFragmentArgs by navArgs()
                    viewModel.loadEpisodes(args.character)
                    Column {
                        MyTopBar(
                            onClick = { goBack() },
                            character = args.character
                        )
                        CharacterInfoView(
                            character = ResultCharacterDto(
                                created = args.character.created,
                                episode = args.character.episode,
                                gender = args.character.gender,
                                id = args.character.id,
                                image = args.character.image,
                                location = args.character.location,
                                name = args.character.name,
                                origin = args.character.origin,
                                species = args.character.species,
                                status = args.character.status,
                                type = args.character.type,
                                url = args.character.url
                            ),
                            loadEpisodes = { loadEpisodes() }
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun loadEpisodes(): List<EpisodeDto> {
        val episodes: List<EpisodeDto> by viewModel.episodes.collectAsState(initial = emptyList())
        return episodes
    }

    private fun goBack() {
        findNavController()
            .navigate(R.id.action_currentCharacterFragment_to_charactersListFragment)
    }
}

@Preview(widthDp = 380)
@Composable
fun CharacterInfoPreview() {
    val character = ResultCharacterDto(
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
    val episodes = List(5) {
        EpisodeDto(
            airDate = "December 2, 2013",
            characters = listOf(
                "https://rickandmortyapi.com/api/character/1",
                "https://rickandmortyapi.com/api/character/2"
            ),
            created = "2017-11-10T12:56:33.798Z",
            episode = "S01E01",
            id = 1,
            name = "Pilot",
            url = "https://rickandmortyapi.com/api/episode/1"
        )
    }
    RaMTheme {
        Surface(
            color = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary
        ) {
            Column {
                CharacterInfoView(
                    character = character,
                    loadEpisodes = { episodes }
                )
                episodes.forEach {
                    EpisodeItem(episode = it)
                }
            }
        }
    }
}

@Preview
@Composable
fun MyTopBarPreview() {
    RaMTheme {
        MyTopBar(
            onClick = { },
            character = ResultCharacterDto(
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
        )
    }
}