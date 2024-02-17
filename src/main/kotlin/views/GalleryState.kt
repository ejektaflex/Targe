package views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import data.FilterType
import data.TagStore
import ui.Exists

class GalleryState(val truthStore: TagStore) {
    var viewStore by mutableStateOf(truthStore)
    val filterTags = mutableStateMapOf<String, Unit>()

    fun refreshViewStore() {
        viewStore = if (filterTags.keys.isNotEmpty()) {
            truthStore.cloneWithSubset(filterTags.keys, FilterType.AND)
        } else {
            truthStore
        }
        println("New ViewStore Keys: ${viewStore.allTags}")
    }
}