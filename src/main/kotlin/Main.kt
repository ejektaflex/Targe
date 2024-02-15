import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import java.io.File

val testDataSet = File("test_data/odas")

val imgFiles = testDataSet.walk().toList().filter {
    it.isFile && it.extension in AppConstants.DataLoading.allowedMedia
}

val testTagNames = listOf(
    "Person", "Location", "Item", "Landscape", "Portrait", "Still Life"
)


@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }
    MaterialTheme {
        Button(onClick = {
            text = "Hello, Desktop!"
        }) {
            Text(text)
        }
    }

//    LazyColumn {
//        for (i in 0 until 1000000) {
//            item {
//                Text("Hello! $i")
//            }
//        }
//    }







}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Targe") {
        MaterialTheme {
            SmallTopAppBarExample()
        }
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
            TagImageSelector()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun GalleryControls() {
    Column(Modifier.padding(5.dp)) {
        Row(Modifier.padding(5.dp)) {
            var text by remember { mutableStateOf("") }
            var active by remember { mutableStateOf(false) }

            OutlinedTextField(text, onValueChange = { text = it }, modifier = Modifier.fillMaxWidth(), placeholder = {
                Text("Search")
            })

//        SearchBar(text, onQueryChange = { text = it }, onSearch = { println("Searching!") }, active = active, onActiveChange = { active = it }) {
//            Text("hi")
//        }

        }

        Row {

            FilterChipExample()

        }
    }
}




//@Composable
//fun ScreenTaggerGallery() {
//    Row(Modifier.fillMaxSize()) {
//        Column(Modifier.weight(2f)) {
//            TagImageSelector()
//        }
//        Column(Modifier.weight(1f)) {
//            Text("Col 2")
//        }
//        Column(Modifier.weight(1.5f)) {
//            Text("Col 3")
//        }
//    }
//}

@Composable
fun TagImageSelector() {

    LazyVerticalGrid(
        columns = GridCells.Adaptive(256.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalArrangement = Arrangement.Center
    ) {

        items(imgFiles.size) {

            val coilFile = remember { imgFiles[it].toCoilFile() }

            Box(Modifier.padding(5.dp).fillMaxSize()) {

                AsyncImage(
                    modifier = Modifier.clip(RoundedCornerShape(5.dp)),
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(coilFile)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                )

            }
        }
    }
}

fun File.toCoilFile(): String {
    return "file://" + absolutePath.replace("\\", "/")
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterChipExample() {
    var selected by remember { mutableStateOf(false) }

    FilterChip(
        onClick = { selected = !selected },
        selected = selected,
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(ChipDefaults.LeadingIconSize)
                )
            }
        } else {
            null
        },

    ) {
        Text("Hey")
    }
}
