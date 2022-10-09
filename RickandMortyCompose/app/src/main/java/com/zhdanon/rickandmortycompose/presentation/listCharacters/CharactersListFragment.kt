package com.zhdanon.rickandmortycompose.presentation.listCharacters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.zhdanon.rickandmortycompose.R
import com.zhdanon.rickandmortycompose.data.characters.LocationDto
import com.zhdanon.rickandmortycompose.data.characters.OriginDto
import com.zhdanon.rickandmortycompose.data.characters.ResultCharacterDto
import com.zhdanon.rickandmortycompose.presentation.GlideImageWithPreview
import com.zhdanon.rickandmortycompose.presentation.RaMViewModel
import com.zhdanon.rickandmortycompose.presentation.theme.RaMTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharactersListFragment : Fragment() {
    private val viewModel: RaMViewModel by viewModels()

    private val pagingData by lazy { CharactersListPagingSource.page(viewModel).flow }

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
                    val characters: LazyPagingItems<ResultCharacterDto> =
                        pagingData.collectAsLazyPagingItems()
                    Column {
                        MyTopBar(
                            onClearClick = { viewModel.setFilterParams("", "") },
                            onClick = { status: String, gender: String ->
                                viewModel.setFilterParams(status, gender)
                                characters.refresh()
                            }
                        )
                        Spacer(modifier = Modifier.padding(top = 4.dp))
                        CharactersListView(
                            list = characters,
                            onImageClick = {
                                findNavController().navigate(R.id.action_charactersListFragment_to_currentCharacterFragment)
                            }
                        )
                        Spacer(modifier = Modifier.padding(bottom = 8.dp))
                    }
                }
            }
        }
    }

    @Composable
    private fun CharactersListView(
        list: LazyPagingItems<ResultCharacterDto>,
        onImageClick: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        LazyColumn {
            items(list) {
                it?.let {
                    ItemCharacter(
                        onImageClick = { onImageClick() },
                        character = it,
                        modifier = modifier.padding(vertical = 4.dp)
                    )
                }
            }
            list.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item {
                            Box(
                                modifier = modifier.fillParentMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                    loadState.append is LoadState.Loading -> {
                        item {
                            Box(
                                modifier = modifier.fillParentMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                    loadState.refresh is LoadState.Error -> {
                        val e = list.loadState.refresh as LoadState.Error
                        item {
                            Column(modifier = modifier.fillParentMaxSize()) {
                                e.error.localizedMessage?.let { Text(text = it) }
                                Button(onClick = { retry() }) {
                                    Text(text = "Retry")
                                }
                            }
                        }
                    }
                    loadState.append is LoadState.Error -> {
                        val e = list.loadState.append as LoadState.Error
                        item {
                            Column(
                                modifier = modifier.fillParentMaxSize(),
                                verticalArrangement = Arrangement.Center
                            ) {
                                e.error.localizedMessage?.let { Text(text = it) }
                                Button(onClick = { retry() }) {
                                    Text(text = "Retry")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun MyTopBar(
        onClearClick: () -> Unit,
        onClick: (String, String) -> Unit,
        modifier: Modifier = Modifier
    ) {
        TopAppBar {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.h5
            )
            Spacer(modifier.weight(1f, true))

            var openDialogValue by remember { mutableStateOf(false) }
            IconButton(
                onClick = {
//                    onFilterClick()
                    openDialogValue = !openDialogValue
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_filter),
                    contentDescription = "Информация о приложении"
                )
            }
            if (openDialogValue) {
                FilterDialog(
                    onClick = { status: String, gender: String -> onClick(status, gender) },
                    modifier = modifier.background(MaterialTheme.colors.surface)
                ) { openDialogValue = !openDialogValue }
            }
            IconButton(onClick = { onClearClick() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cleaning),
                    contentDescription = "Избранное"
                )
            }
        }
    }

    @Composable
    fun ItemCharacter(
        onImageClick: () -> Unit,
        character: ResultCharacterDto,
        modifier: Modifier = Modifier
    ) {
        Surface(
            color = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.onPrimary,
            modifier = modifier.padding(horizontal = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                GlideImageWithPreview(
                    data = character.image,
                    modifier = modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .clickable { onImageClick() }
                )
                Spacer(
                    modifier
                        .weight(1f, true)
                )
                Column(
                    modifier = modifier
                        .padding(horizontal = 6.dp)
                        .padding(start = 8.dp)
                        .fillMaxWidth(),
                ) {
                    var isShowFull by remember { mutableStateOf(false) }
                    Text(
                        text = character.name,
                        maxLines = 1,
                        style = MaterialTheme.typography.h5
                    )
                    Text(
                        text = stringResource(
                            id = R.string.status_line,
                            character.status ?: "unknown",
                            character.species
                        ),
                        style = MaterialTheme.typography.body1,
                        modifier = modifier.padding(top = 6.dp)
                    )
                    Spacer(modifier = modifier.padding(top = 14.dp))
                    Text(
                        text = stringResource(id = R.string.tv_title_last_location),
                        style = MaterialTheme.typography.subtitle1.copy(
                            color = Color.Black,
                            fontStyle = FontStyle.Italic
                        )
                    )
                    Text(
                        text = character.location?.name ?: "Unknown location",
                        style = MaterialTheme.typography.body1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = modifier.padding(top = 6.dp)
                    )
                    Text(
                        text = if (isShowFull) "Show less" else "Show more...",
                        style = MaterialTheme.typography.body2.copy(
                            color = Color.Black,
                            textAlign = TextAlign.End
                        ),
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp)
                            .clickable { isShowFull = !isShowFull }
                    )
                    if (isShowFull) {
                        Column(modifier) {
                            Text(text = stringResource(id = R.string.label_episodes))
                            val episodeList = StringBuilder()
                            character.episode.map { episode ->
                                val lastIndex = episode.lastIndexOf('/')
                                val temp = episode.removeRange(0, lastIndex + 1)
                                if (episodeList.isBlank()) episodeList.append(temp) else episodeList.append(
                                    ", $temp"
                                )
                            }
                            Text(
                                text = episodeList.toString(),
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun FilterDialog(
        onClick: (String, String) -> Unit,
        modifier: Modifier = Modifier,
        openDialog: () -> Unit
    ) {
        Dialog(onDismissRequest = { openDialog() }) {
            var statusShow by remember { mutableStateOf(false) }
            var genderShow by remember { mutableStateOf(false) }
            var statusChecked by remember { mutableStateOf("") }
            var genderChecked by remember { mutableStateOf("") }
            Column(
                modifier = modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .size(width = 200.dp, height = 500.dp)
            ) {

                // Заголовок
                Text(
                    text = stringResource(id = R.string.dialog_title),
                    style = MaterialTheme.typography.h5
                )

                // Фильтр по статусу жив/мёртв
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier.padding(top = 4.dp, start = 8.dp)
                ) {
                    Checkbox(
                        checked = statusShow,
                        onCheckedChange = {
                            statusShow = !statusShow
                            if (!statusShow) statusChecked = ""
                        }
                    )
                    Text(
                        text = stringResource(id = R.string.label_filter_status),
                        style = MaterialTheme.typography.subtitle1
                    )
                }
                if (statusShow) {
                    val labelAlive = stringResource(id = R.string.status_alive)
                    val labelDead = stringResource(id = R.string.status_dead)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier.padding(top = 4.dp, start = 32.dp)
                    ) {
                        RadioButton(
                            selected = statusChecked == labelAlive,
                            onClick = { statusChecked = labelAlive }
                        )
                        Text(
                            text = labelAlive,
                            style = MaterialTheme.typography.body1
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier.padding(top = 4.dp, start = 32.dp)
                    ) {
                        RadioButton(
                            selected = statusChecked == labelDead,
                            onClick = { statusChecked = labelDead }
                        )
                        Text(
                            text = labelDead,
                            style = MaterialTheme.typography.body1
                        )
                    }
                }

                // Фильтр по полу мужской/женский/неопределённый
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier.padding(top = 4.dp, start = 8.dp)
                ) {
                    Checkbox(
                        checked = genderShow,
                        onCheckedChange = {
                            genderShow = !genderShow
                            if (!genderShow) genderChecked = ""
                        }
                    )
                    Text(
                        text = stringResource(id = R.string.label_filter_gender),
                        style = MaterialTheme.typography.subtitle1
                    )
                }
                if (genderShow) {
                    val labelMale = stringResource(id = R.string.gender_male)
                    val labelFemale = stringResource(id = R.string.gender_female)
                    val labelUnknown = stringResource(id = R.string.gender_unknown)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier.padding(top = 4.dp, start = 32.dp)
                    ) {
                        RadioButton(
                            selected = genderChecked == labelMale,
                            onClick = { genderChecked = labelMale }
                        )
                        Text(
                            text = labelMale,
                            style = MaterialTheme.typography.body1
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier.padding(top = 4.dp, start = 32.dp)
                    ) {
                        RadioButton(
                            selected = genderChecked == labelFemale,
                            onClick = { genderChecked = labelFemale }
                        )
                        Text(
                            text = labelFemale,
                            style = MaterialTheme.typography.body1
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier.padding(top = 4.dp, start = 32.dp)
                    ) {
                        RadioButton(
                            selected = genderChecked == labelUnknown,
                            onClick = { genderChecked = labelUnknown }
                        )
                        Text(
                            text = labelUnknown,
                            style = MaterialTheme.typography.body1
                        )
                    }
                }

                // Сохранение параметров фильтра
                Button(
                    onClick = {
                        onClick(statusChecked, genderChecked)
                        openDialog()
                    },
                    modifier = modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = stringResource(id = R.string.btn_filter_title),
                        style = MaterialTheme.typography.button
                    )
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun MyTopBarPreview() {
        RaMTheme {
            MyTopBar(
                onClearClick = {},
                onClick = { _: String, _: String ->
                    TODO()
                }
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun ItemCharacterPreview() {
        RaMTheme {
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
            ItemCharacter(onImageClick = {}, character = character)
        }
    }
}