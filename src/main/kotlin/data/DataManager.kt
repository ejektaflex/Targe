package data

import androidx.compose.runtime.*
import kotlin.io.path.Path

object DataManager {
    // Holds all of our real tags, the actual source of truth
    val Store by mutableStateOf(TagStore.from(Path("test_data/odas")))

    var gallerySelection by mutableStateOf(Store)



}