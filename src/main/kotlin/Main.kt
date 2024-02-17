import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.FilterChip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.SearchBar
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import data.DataManager
import data.ui.andIf
import data.ui.withHoverControl
import kotlinx.coroutines.flow.firstOrNull
import java.io.File



val testTagNames = listOf(
    "Person", "Location", "Item", "Landscape", "Portrait", "Still Life"
)

fun main() = application {
    DataManager.setup()
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
            TagImageSelector()
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
                FilterChipExample("$tag ($count)")
                Spacer(Modifier.padding(5.dp))
            }
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TagImageSelector() {

    LazyVerticalGrid(
        columns = GridCells.Adaptive(256.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalArrangement = Arrangement.Center
    ) {
        val shownImages = DataManager.gallerySelection

        items(shownImages.size) {

            val coilFile = remember { shownImages[it].toCoilFile() }
            val isHovering = remember { mutableStateOf(false) }

            Box(Modifier.padding(5.dp).fillMaxSize().withHoverControl(isHovering, PointerIcon.Hand).clip(RoundedCornerShape(5.dp)).andIf(isHovering.value) {
                border(8.dp, AppConstants.Theme.Primary)
            }) {
                AsyncImage(
                    modifier = Modifier,
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(coilFile)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

fun File.toCoilFile(): String {
    return "file://" + absolutePath.replace("\\", "/")
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChipExample(text: String) {
    var selected by remember { mutableStateOf(false) }

    FilterChip(
        onClick = { selected = !selected },
        label = {
            Text(text)
        },
        selected = selected,
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        }
    )

}
