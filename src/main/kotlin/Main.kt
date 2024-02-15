import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import coil3.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import kotlin.math.ceil


val testDataSet = File("test_data/odas")

val imgFiles = testDataSet.walk().toList().filter {
    it.isFile && it.extension in AppConstants.DataLoading.allowedMedia
}

//val desktopConfig = KamelConfig {
//    takeFrom(KamelConfig.Default)
//    // Available only on Desktop.
//    resourcesFetcher()
//}

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
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Menu Here"
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
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
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun TagImageSelector() {

    val numCols = 3
    val numRows = ceil(imgFiles.size.toDouble() / numCols).toInt()


    // 75/4

    println(imgFiles.size)

    val loc = "file://" + imgFiles[1].absolutePath.replace("\\", "/") // .replace("/", "\\")



    LazyVerticalGrid(
        columns = GridCells.Fixed(numCols)
    ) {



        items(imgFiles.size) {

            val coilFile = remember { imgFiles[it].toCoilFile() }

            Box(Modifier.padding(5.dp).clip(RoundedCornerShape(15.dp)).fillMaxWidth()) {
                AsyncImage(

                    model = coilFile,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
            }

//            AsyncImage(
//                load = {
//                    loadImageBitmap(element.inputStream())
//                },
//                contentDescription = "Doot",
//                painterFor = { remember { BitmapPainter(it) } }
//            )
            //asyncPainterResource(data = element)
            //KamelImage(element, "Yeet")
        }
    }

//    LazyColumn {
//        for (rowIndex in 0 until numRows) {
//            val imgStart = rowIndex * numCols
//            val imgEnd = (imgStart + numCols).coerceAtMost(imgFiles.size)
//            //println("From $imgStart to $imgEnd")
//
//            item {
//                Row(Modifier.height(100.dp).fillMaxWidth()) {
//                    for (imgIndex in imgStart until imgEnd) {
//                        val imageFile = imgFiles[imgIndex]
//                        Image(loadImageBitmap(imageFile.inputStream()), "Gallery Image: ${imageFile.path}")
//                        //Text("Hi! $imgIndex")
//                    }
//                }
//
//            }
//
//        }
//    }



    Text("Col 1")
    LazyColumn(Modifier.fillMaxSize()) {
        item {
            Text("Yo")
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

