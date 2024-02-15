import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.Path.Companion.toPath
import java.io.File
import java.io.IOException
import java.nio.file.Paths
import kotlin.math.ceil


val testDataSet = File("test_data/odas")

val imgFiles = testDataSet.walk().toList().filter {
    it.isFile && it.extension in AppConstants.DataLoading.allowedMedia
}



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
        Column(Modifier.weight(2f)) {
            TagImageSelector()
        }
        Column(Modifier.requiredWidth(512.dp)) {
            Text("Col 2")
        }
    }
}

@Composable
fun ScreenTaggerGallery() {
    Row(Modifier.fillMaxSize()) {
        Column(Modifier.weight(2f)) {
            TagImageSelector()
        }
        Column(Modifier.weight(1f)) {
            Text("Col 2")
        }
        Column(Modifier.weight(1.5f)) {
            Text("Col 3")
        }
    }
}

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

@Composable
fun <T> AsyncImage(
    load: suspend () -> T,
    painterFor: @Composable (T) -> Painter,
    contentDescription: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {
    val image: T? by produceState<T?>(null) {
        value = withContext(Dispatchers.IO) {
            try {
                load()
            } catch (e: IOException) {
                // instead of printing to console, you can also write this to log,
                // or show some error placeholder
                e.printStackTrace()
                null
            }
        }
    }

    if (image != null) {
        Image(
            painter = painterFor(image!!),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
        )
    }
}

fun File.toCoilFile(): String {
    return "file://" + absolutePath.replace("\\", "/")
}

