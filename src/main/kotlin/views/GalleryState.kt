package views

import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.runtime.*
import data.AppPage
import data.TagStore

class GalleryState(private val truthStore: TagStore) : ViewState<GalleryState> {

    override val page = AppPage.GALLERY

    var viewStore by mutableStateOf(truthStore)
    val filterTags = mutableStateMapOf<String, Unit>()
    val lsgs by mutableStateOf(LazyStaggeredGridState())


    fun refreshViewStore() {
        viewStore = if (filterTags.keys.isNotEmpty()) {
            truthStore.cloneWithEntireSubset(filterTags.keys)
        } else {
            truthStore
        }
        println("New ViewStore Keys: ${viewStore.allTags}")
    }
}