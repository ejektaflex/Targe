package views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import data.FilterType
import data.TagStore

class GalleryState(private val truthStore: TagStore) {
    var viewStore by mutableStateOf(truthStore)
    val filterTags = mutableSetOf<String>()

    fun refreshViewStore() {
        viewStore = if (filterTags.isNotEmpty()) {
            truthStore.cloneWithSubset(filterTags, FilterType.AND)
        } else {
            truthStore
        }
    }
}