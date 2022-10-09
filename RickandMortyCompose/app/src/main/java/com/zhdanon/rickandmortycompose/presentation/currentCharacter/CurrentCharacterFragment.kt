package com.zhdanon.rickandmortycompose.presentation.currentCharacter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.zhdanon.rickandmortycompose.R
import com.zhdanon.rickandmortycompose.data.EpisodeDto
import com.zhdanon.rickandmortycompose.data.characters.LocationDto
import com.zhdanon.rickandmortycompose.data.characters.OriginDto
import com.zhdanon.rickandmortycompose.data.characters.ResultCharacterDto
import com.zhdanon.rickandmortycompose.entity.Episode
import com.zhdanon.rickandmortycompose.presentation.GlideImageWithPreview
import com.zhdanon.rickandmortycompose.presentation.theme.ColorBackgroundItem
import com.zhdanon.rickandmortycompose.presentation.theme.RaMTheme

class CurrentCharacterFragment : Fragment() {
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
                    CharacterInfoView(
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
        }
    }
}

@Composable
fun CharacterInfoView(character: ResultCharacterDto, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp)
            .scrollable(
                state = ScrollState(1),
                orientation = Orientation.Vertical,
                enabled = true
            )
    ) {
        GlideImageWithPreview(
            data = character.image,
            modifier = modifier
                .size(300.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = character.name,
            style = MaterialTheme.typography.h4,
            modifier = modifier.padding(8.dp)
        )
        LazyColumn {

        }
        CurrentParamView(
            title = "Live status",
            text = character.status ?: "Unknown"
        )
        CurrentParamView(
            title = "Species and gender",
            text = "${character.species}(${character.gender ?: "Unknown"})"
        )
        CurrentParamView(
            title = stringResource(id = R.string.tv_title_last_location),
            text = character.location?.name ?: "Unknown"
        )
        CurrentParamView(
            title = "First seen in:",
            text = character.episode[0]
        )

        Text(
            text = "Episodes:",
            style = MaterialTheme.typography.h5,
            modifier = modifier.padding(top = 16.dp, start = 8.dp)
        )

        val episodeList = mutableListOf<Int>()
        character.episode.map { episode ->
            val lastIndex = episode.lastIndexOf('/')
            val temp = episode.removeRange(0, lastIndex + 1)
            episodeList.add(temp.toInt())
        }

        val list = List (10) {
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

        LazyColumn {
            items(list) { item ->
                EpisodeItem(episode = item)
            }
        }
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
                modifier = modifier.padding(start = 8.dp)
            ) {
                Text(
                    text = episode.name,
                    style = MaterialTheme.typography.h5,
                    modifier = modifier.padding(top = 4.dp)
                )
                Text(
                    text = episode.airDate,
                    style = MaterialTheme.typography.body1,
                    modifier = modifier.padding(vertical = 4.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f, true))
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
    RaMTheme {
        Surface(
            color = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary
        ) {
            CharacterInfoView(character = character)
        }
    }
}