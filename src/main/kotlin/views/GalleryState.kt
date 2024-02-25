package views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import data.AppPage
import data.TagStore

class GalleryState(private val truthStore: TagStore) : ViewState<GalleryState> {

    override val page = AppPage.GALLERY

    var viewStore by mutableStateOf(truthStore)
    val filterTags = mutableStateMapOf<String, Unit>()



    fun refreshViewStore() {
        viewStore = if (filterTags.keys.isNotEmpty()) {
            truthStore.cloneWithEntireSubset(filterTags.keys)
        } else {
            truthStore
        }
        println("New ViewStore Keys: ${viewStore.allTags}")
    }
}