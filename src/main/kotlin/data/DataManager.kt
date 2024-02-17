package data

import androidx.compose.runtime.*
import java.io.File
import kotlin.io.path.Path

object DataManager {
    val store by mutableStateOf(TagStore.from(Path("test_data/odas")))

    var gallerySelection by mutableStateOf(store)
}