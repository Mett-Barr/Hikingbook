package com.example.hikingbook.ui

import android.app.SearchManager
import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hikingbook.MainViewModel
import com.example.hikingbook.QuoteState
import com.example.hikingbook.Route
import com.example.hikingbook.tool.translate
import com.example.hikingbook.ui.component.TaskList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(
    viewModel: MainViewModel = viewModel(),
) {

    val context = LocalContext.current

//    val list by viewModel.allTasks.observeAsState()

//    val systemBarsPadding = WindowInsets.systemBars.asPaddingValues()
    val horizonPadding = 8.dp


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.route = Route.NEW_TASK }) {
//                Icon(painter = painterResource(id = ), contentDescription = )

                Icon(imageVector = Icons.Rounded.Add, contentDescription = "add")
            }
        },
        topBar = {

            var contentCN by remember {
                mutableStateOf("")
            }
            var authorCN by remember {
                mutableStateOf("")
            }


            var isChecking by remember {
                mutableStateOf(false)
            }

            LaunchedEffect(isChecking) {
                if (isChecking) {
                    withContext(Dispatchers.Default) {
                        translate(viewModel.quote.content, context) { contentCN = it }
                        translate(viewModel.quote.author, context) { authorCN = it }
                    }

                    delay(5000)
                    isChecking = false
                } else {
                    while (true) {

                        contentCN = ""
                        authorCN = ""

                        viewModel.getRandomQuote()


                        delay(5000)
                    }
                }
            }


            Column(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .clickable {
                        if (!isChecking) isChecking = true
                        else {
                            val intent = Intent(Intent.ACTION_WEB_SEARCH)
                            intent.putExtra(
                                SearchManager.QUERY,
                                "${viewModel.quote.content}  ${viewModel.quote.author}"
                            )
                            context.startActivity(intent)
                        }
                    }
                    .animateContentSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(
                        top = WindowInsets.systemBars
                            .asPaddingValues()
                            .calculateTopPadding()
                    )
            ) {
                AnimatedContent(targetState = viewModel.quoteState) {
                    when (it) {
                        QuoteState.SUCCESS -> {
                            Column {
                                AnimatedContent(targetState = viewModel.quote.content) {
                                    if (it.isNotBlank()) {
                                        Text(
                                            text = it,
                                            modifier = Modifier
                                                .padding(8.dp)
                                                .wrapContentHeight(),
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                }
                                AnimatedContent(targetState = viewModel.quote.author) {
                                    if (it.isNotBlank()) {
                                        Row {
                                            Spacer(modifier = Modifier.weight(1f))
                                            Text(
                                                text = " - $it",
                                                modifier = Modifier
                                                    .padding(8.dp)
                                                    .wrapContentHeight(),
                                                style = MaterialTheme.typography.bodyLarge,
                                                fontStyle = FontStyle.Italic
                                            )
                                        }
                                    }
                                }

                                AnimatedContent(targetState = contentCN) {
                                    if (it.isNotBlank()) {
                                        Text(
                                            text = it,
                                            modifier = Modifier
                                                .padding(8.dp)
                                                .wrapContentHeight(),
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                }
                                AnimatedContent(targetState = authorCN) {
                                    if (it.isNotBlank()) {
                                        Row {
                                            Spacer(modifier = Modifier.weight(1f))
                                            Text(
                                                text = " - $it",
                                                modifier = Modifier
                                                    .padding(8.dp)
                                                    .wrapContentHeight(),
                                                style = MaterialTheme.typography.bodyLarge,
                                                fontStyle = FontStyle.Italic
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        QuoteState.FAILURE -> {
                            Text(text = "${viewModel.errorCode}  ${viewModel.errorMessage}")
                        }
                    }
                }
            }

        }
    ) { paddingValues ->

        paddingValues.TaskList()

//        LazyColumn(
//            verticalArrangement = Arrangement.spacedBy(8.dp),
//            contentPadding = PaddingValues(
//                top = paddingValues.calculateTopPadding(),
//                bottom = paddingValues.calculateBottomPadding(),
//                start = horizonPadding,
//                end = horizonPadding
//            )
////            contentPadding = paddingValues
////            contentPadding = PaddingValues(
////                top = systemBarsPadding.calculateTopPadding(),
////                bottom = systemBarsPadding.calculateBottomPadding(),
////                start = horizonPadding,
////                end = horizonPadding
////            )
//        ) {
//            items(list ?: emptyList()) {
//                TaskCard(task = it) {
//                    viewModel.editedTask = it
//                    viewModel.location = viewModel.editedTask.locationCoordinate.toLatLng() ?: viewModel.singapore
//                    viewModel.navigate(Route.EDIT_TASK)
//                }
//            }
//        }
    }

}