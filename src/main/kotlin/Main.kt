import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import data.DataManager
import ui.TFilterChip
import ui.TImageGrid

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Targe") {
        SmallTopAppBarExample()
    }
}

@Composable
fun SmallTopAppBarExample() {
    Scaffold(
        topBar = {
            TopAppBar(
                contentColor = AppConstants.Theme.TextDark,
                backgroundColor = AppConstants.Theme.Primary,
                title = {
                    Text("Targe - Gallery")
                },
                actions = {
                    IconButton(onClick = {
                        println("Doot!")
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Menu Here"
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        ScreenGallery()
    }
}

@Composable
fun ScreenGallery() {
    Row(Modifier.fillMaxSize()) {
        Column(Modifier.requiredWidth(320.dp)) {
            GalleryControls()
        }
        Column(Modifier.weight(2f)) {
            TImageGrid(DataManager.gallerySelection)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GalleryControls() {
    Column(Modifier.padding(5.dp)) {
        Row(Modifier.padding(5.dp)) {
            var text by remember { mutableStateOf("") }
            var active by remember { mutableStateOf(false) }

            OutlinedTextField(text, onValueChange = { text = it }, modifier = Modifier.fillMaxWidth(), placeholder = {
                Text("Search")
            })


        }

        FlowRow(Modifier.verticalScroll(rememberScrollState())
        ) {
            for ((tag, count) in DataManager.store.genTagFrequencies()) {
                TFilterChip("$tag ($count)") { _, new ->
                    if (new) {
                        DataManager.gallerySelection = DataManager.gallerySelection.cloneWithTagSubset(tag)
                    } else {
                        DataManager.gallerySelection = DataManager.store
                    }
                }
                Spacer(Modifier.padding(5.dp))
            }
        }
    }
}


