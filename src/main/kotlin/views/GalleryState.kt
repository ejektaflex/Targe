package views

import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.runtime.*
import data.AppPage
import data.FilterType
import data.TagStore

class GalleryState(val truthStore: TagStore) : ViewState<GalleryState> {

    override val page = AppPage.GALLERY

    var viewStore by mutableStateOf(truthStore)
    val filterTags = mutableStateMapOf<String, FilterType>()
    val lsgs by mutableStateOf(LazyStaggeredGridState())
    val selectedItems = mutableStateMapOf<Int, Unit>()

    val filterWith: Set<String>
        get() = filterTags.filter { it.value == FilterType.WITH }.keys

    val filterWithout: Set<String>
        get() = filterTags.filter { it.value == FilterType.WITHOUT }.keys

    fun selectItem(index: Int) {
        selectedItems.clear()
        addSelectedItem(index)
    }

    fun addSelectedItem(index: Int) {
        selectedItems[index] = Unit
    }

    fun refreshViewStore() {
        viewStore = if (filterTags.keys.isNotEmpty()) {
            truthStore.cloneWithEntireSubset(filterWith, filterWithout)
        } else {
            truthStore
        }
        println("New ViewStore Keys: ${viewStore.allTags}")
    }
}