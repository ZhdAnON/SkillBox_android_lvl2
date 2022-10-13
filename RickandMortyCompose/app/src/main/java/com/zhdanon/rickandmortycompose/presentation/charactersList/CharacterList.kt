package com.zhdanon.rickandmortycompose.presentation.charactersList

import android.content.Context
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.zhdanon.rickandmortycompose.R
import com.zhdanon.rickandmortycompose.data.characters.ResultCharacterDto
import com.zhdanon.rickandmortycompose.presentation.GlideImageWithPreview
import com.zhdanon.rickandmortycompose.presentation.RaMViewModel
import com.zhdanon.rickandmortycompose.presentation.navigation.Screen

@Composable
fun CharacterList(
    context: Context,
    navController: NavController,
    viewModel: RaMViewModel
) {
    val pagingData by     lazy { CharactersListPagingSource.page(viewModel) }
    val characters: LazyPagingItems<ResultCharacterDto> =
        pagingData.collectAsLazyPagingItems()

    Column {
        MyTopBar(
            context = context,
            onClick = { status: String, gender: String ->
                viewModel.setFilterParams(status, gender)
                characters.refresh()
            },
            onClearClick = {
                viewModel.setFilterParams("", "")
                characters.refresh()
            }
        )
        Spacer(modifier = Modifier.padding(top = 4.dp))
        CharactersListView(
            viewModel = viewModel,
            navController = navController,
            list = characters
        )
        Spacer(modifier = Modifier.padding(bottom = 8.dp))
    }
}

@Composable
private fun CharactersListView(
    viewModel: RaMViewModel,
    navController: NavController,
    list: LazyPagingItems<ResultCharacterDto>,
    modifier: Modifier = Modifier
) {
    LazyColumn {
        items(list) {
            it?.let {
                ItemCharacter(
                    viewModel = viewModel,
                    navController = navController,
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
                            LoaderStatus()
                        }
                    }
                }
                loadState.append is LoadState.Loading -> {
                    item {
                        Box(
                            modifier = modifier.fillParentMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            LoaderStatus()
                        }
                    }
                }
                loadState.refresh is LoadState.Error -> {
                    val e = list.loadState.refresh as LoadState.Error
                    item {
                        Column(
                            modifier = modifier.fillParentMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            e.error.localizedMessage?.let { Text(text = it) }
                            Button(onClick = { retry() }) {
                                Text(text = stringResource(id = R.string.reload))
                            }
                        }
                    }
                }
                loadState.append is LoadState.Error -> {
                    val e = list.loadState.append as LoadState.Error
                    item {
                        Column(
                            modifier = modifier.fillParentMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            e.error.localizedMessage?.let { Text(text = it) }
                            Button(onClick = { retry() }) {
                                Text(text = stringResource(id = R.string.reload))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoaderStatus() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.load_state_lottie))
    LottieAnimation(composition = composition)
}

@Composable
fun MyTopBar(
    context: Context,
    onClick: (String, String) -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.h5,
            modifier = modifier.padding(start = 8.dp)
        )
        Spacer(modifier.weight(1f, true))

        var openDialogValue by remember { mutableStateOf(false) }
        IconButton(
            onClick = {
                openDialogValue = !openDialogValue
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_filter),
                contentDescription = stringResource(id = R.string.btn_filter_title)
            )
        }
        if (openDialogValue) {
            FilterDialog(
                context = context,
                onClick = { status: String, gender: String -> onClick(status, gender) },
                modifier = modifier.background(MaterialTheme.colors.surface)
            ) { openDialogValue = !openDialogValue }
        }
        IconButton(onClick = { onClearClick() }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_cleaning),
                contentDescription = stringResource(id = R.string.btn_clear_filter)
            )
        }
    }
}

@Composable
fun ItemCharacter(
    viewModel: RaMViewModel,
    navController: NavController,
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
                    .clickable {
                        viewModel.setCharacterForDetail(character)
                        navController.navigate(Screen.CharacterDetailScreen.route)
                    }
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
                        character.status ?: "unknown status",
                        character.species
                    ),
                    style = MaterialTheme.typography.body1,
                    modifier = modifier.padding(top = 6.dp)
                )
                Spacer(modifier = modifier.padding(top = 14.dp))
                Text(
                    text = stringResource(id = R.string.title_last_location),
                    style = MaterialTheme.typography.subtitle1.copy(
                        color = Color.Black,
                        fontStyle = FontStyle.Italic,
                        fontSize = 16.sp
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
    context: Context,
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
                .size(width = 200.dp, height = 550.dp)
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
                val labelAlive = stringResource(id = R.string.label_status_alive)
                val labelDead = stringResource(id = R.string.label_status_dead)
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

            // Фильтр по полу мужской/женский/безполый/неопределённый
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
                val labelMale = stringResource(id = R.string.label_gender_male)
                val labelFemale = stringResource(id = R.string.label_gender_female)
                val labelGenderless = stringResource(id = R.string.label_gender_genderless)
                val labelUnknown = stringResource(id = R.string.label_gender_unknown)
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
                        selected = genderChecked == labelGenderless,
                        onClick = { genderChecked = labelGenderless }
                    )
                    Text(
                        text = labelGenderless,
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
                    val resources = context.resources
                    val status = when (statusChecked) {
                        resources.getString(R.string.label_status_alive) ->
                            resources.getString(R.string.status_alive)
                        resources.getString(R.string.label_status_dead) ->
                            resources.getString(R.string.status_dead)
                        else -> ""
                    }
                    val gender = when (genderChecked) {
                        resources.getString(R.string.label_gender_male) ->
                            resources.getString(R.string.gender_male)

                        resources.getString(R.string.label_gender_female) ->
                            resources.getString(R.string.gender_female)

                        resources.getString(R.string.label_gender_genderless) ->
                            resources.getString(R.string.gender_genderless)

                        resources.getString(R.string.label_gender_unknown) ->
                            resources.getString(R.string.gender_unknown)
                        else -> ""
                    }
                    onClick(status, gender)
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