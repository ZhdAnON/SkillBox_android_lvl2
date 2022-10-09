package com.zhdanon.rickandmortycompose.presentation.currentCharacter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.zhdanon.rickandmortycompose.R
import com.zhdanon.rickandmortycompose.data.EpisodeDto
import com.zhdanon.rickandmortycompose.data.characters.LocationDto
import com.zhdanon.rickandmortycompose.data.characters.OriginDto
import com.zhdanon.rickandmortycompose.data.characters.ResultCharacterDto
import com.zhdanon.rickandmortycompose.entity.Episode
import com.zhdanon.rickandmortycompose.entity.ResultCharacter
import com.zhdanon.rickandmortycompose.presentation.GlideImageWithPreview
import com.zhdanon.rickandmortycompose.presentation.RaMViewModel
import com.zhdanon.rickandmortycompose.presentation.theme.ColorBackgroundItem
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
    private fun loadEpisodes(): List<Episode> {
        val episodes: List<Episode> by viewModel.episodes.collectAsState(initial = emptyList())
        return episodes
    }

    private fun goBack() {
        findNavController()
            .navigate(R.id.action_currentCharacterFragment_to_charactersListFragment)
    }
}

@Composable
fun CharacterInfoView(
    character: ResultCharacter,
    loadEpisodes: @Composable () -> List<Episode>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        val episodes = loadEpisodes()
        GlideImageWithPreview(
            data = character.image,
            modifier = modifier
                .size(300.dp)
                .align(Alignment.CenterHorizontally)
        )
        CurrentParamView(
            title = stringResource(id = R.string.title_status),
            text = character.status ?: "Unknown"
        )
        CurrentParamView(
            title = stringResource(id = R.string.title_species_gender),
            text = "${character.species} (${character.gender ?: "Unknown"})"
        )
        CurrentParamView(
            title = stringResource(id = R.string.title_last_location),
            text = character.location?.name ?: "Unknown"
        )
        CurrentParamView(
            title = stringResource(id = R.string.title_first_seen),
            text = character.episode[0]
        )

        Text(
            text = "Episodes:",
            style = MaterialTheme.typography.h5,
            modifier = modifier.padding(top = 16.dp, start = 8.dp)
        )
        episodes.forEach { EpisodeItem(episode = it) }
    }
}

@Composable
fun CurrentParamView(title: String, text: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.subtitle1.copy(
                color = Color.Black
            )
        )
        Text(
            text = text,
            style = MaterialTheme.typography.body1,
            modifier = modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun MyTopBar(
    onClick: () -> Unit,
    character: ResultCharacter,
    modifier: Modifier = Modifier
) {
    TopAppBar {
        IconButton(
            onClick = { onClick() }
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = null
            )
        }
        Text(
            text = character.name,
            style = MaterialTheme.typography.h5,
            modifier = modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun EpisodeItem(episode: Episode, modifier: Modifier = Modifier) {
    Surface(
        color = ColorBackgroundItem,
        contentColor = MaterialTheme.colors.onPrimary,
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(
                CircleShape.copy(
                    topStart = CornerSize(0),
                    topEnd = CornerSize(20),
                    bottomStart = CornerSize(20),
                    bottomEnd = CornerSize(20)
                )
            )
            .border(
                width = 1.dp,
                color = Color.Black,
                shape =
                CircleShape.copy(
                    topStart = CornerSize(0),
                    topEnd = CornerSize(20),
                    bottomStart = CornerSize(20),
                    bottomEnd = CornerSize(20)
                )
            )
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Column(
                modifier = modifier
                    .padding(start = 8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = episode.name,
                    style = MaterialTheme.typography.subtitle1,
                    modifier = modifier.padding(top = 4.dp, bottom = 4.dp),
                    maxLines = 1
                )
                Text(
                    text = episode.airDate,
                    style = MaterialTheme.typography.body1,
                    modifier = modifier.padding(vertical = 4.dp)
                )
            }
            Text(
                text = episode.episode,
                style = MaterialTheme.typography.body1,
                modifier = modifier.padding(end = 8.dp, top = 4.dp)
            )
        }
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