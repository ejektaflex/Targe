package views

import addSetItem
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.DataManager
import data.PageManager
import ui.TFilterChip
import ui.TImageGrid
import ui.TagMapState


@Composable
fun GalleryView(state: GalleryState) {
    val galleryState by remember { mutableStateOf(state) }
    Row(Modifier.fillMaxSize()) {
        Column(Modifier.requiredWidth(360.dp)) {
            GalleryControls(galleryState)
        }
        Column(Modifier.weight(2f)) {
            TImageGrid(galleryState.viewStore, galleryState.lsgs, galleryState.selectedItems.keys, onSingleClick = { img, ind ->
                galleryState.selectedItems.addSetItem(ind)
                if (DataManager.isCtrl) {
                    galleryState.addSelectedItem(ind)
                } else {
                    galleryState.selectItem(ind)
                }
            }, onDoubleClick = { img, ind ->
                PageManager.goToInspector(img)
            })
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GalleryControls(state: GalleryState) {
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
            val freqs = state.truthStore.genTagsForDisplay(subsetTags = state.filterWith, minusTags = state.filterWithout)

            //println(state.filterWithout)
            //println(freqs.toMap().keys.intersect(state.filterWithout))

            for ((tag, count) in freqs) {

                val stat = TagMapState(state.filterTags, tag)
                //val chipFreq = state.viewStore.genFilteredTagSet(state.filterWith + tag, state.filterWithout).size

                val sizeToShow = count

                TFilterChip(stat, "$tag ($sizeToShow)") { _, new ->
                    if (new != null) {
                        state.filterTags[tag] = new
                    } else {
                        state.filterTags.remove(tag)
                    }
                    state.refreshViewStore()
                }
                Spacer(Modifier.padding(5.dp))
            }
        }
    }
}

