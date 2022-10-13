package com.zhdanon.rickandmortycompose.presentation.characterDetail

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.zhdanon.rickandmortycompose.R
import com.zhdanon.rickandmortycompose.data.EpisodeDto
import com.zhdanon.rickandmortycompose.data.characters.ResultCharacterDto
import com.zhdanon.rickandmortycompose.entity.Episode
import com.zhdanon.rickandmortycompose.entity.characters.ResultCharacter
import com.zhdanon.rickandmortycompose.presentation.GlideImageWithPreview
import com.zhdanon.rickandmortycompose.presentation.RaMViewModel
import com.zhdanon.rickandmortycompose.presentation.navigation.Screen
import com.zhdanon.rickandmortycompose.presentation.theme.ColorBackgroundItem

@Composable
fun CharacterDetail(
    navController: NavController,
    viewModel: RaMViewModel
) {
    val episodes: List<EpisodeDto> by viewModel.episodes.collectAsState(initial = emptyList())
    val character = viewModel.getCharacterForDetail()
    viewModel.loadEpisodes(character)
    Column {
        MyTopBar(
            onClick = { navController.navigate(Screen.CharacterListScreen.route) },
            character = character
        )
        CharacterInfoView(
            character = ResultCharacterDto(
                created = character.created,
                episode = character.episode,
                gender = character.gender,
                id = character.id,
                image = character.image,
                location = character.location,
                name = character.name,
                origin = character.origin,
                species = character.species,
                status = character.status,
                type = character.type,
                url = character.url
            ),
            loadEpisodes = { episodes }
        )
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
            text = stringResource(id = R.string.title_episodes),
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