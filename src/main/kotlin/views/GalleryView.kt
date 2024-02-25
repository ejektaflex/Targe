package views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.DataManager
import ui.TFilterChip
import ui.TImageGrid
import ui.TSelectedState
import ui.TagMapState


@Composable
fun GalleryView(state: GalleryState) {
    val galleryState by remember { mutableStateOf(state) }
    Row(Modifier.fillMaxSize()) {
        Column(Modifier.requiredWidth(320.dp)) {
            GalleryControls(galleryState)
        }
        Column(Modifier.weight(2f)) {
            TImageGrid(galleryState.viewStore, state.lsgs)
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
            val freqs = state.viewStore.genTagFrequencies()
            for ((tag, count) in freqs) {

                val stat = TagMapState(state.filterTags, tag)
                val chipFreq = state.viewStore.genFilteredTagSet(state.filterTags.keys + tag).size

                val sizeToShow = count

                TFilterChip(stat, "$tag ($sizeToShow)") { _, new ->
                    if (new) {
                        state.filterTags[tag] = Unit
                        state.refreshViewStore()
                    } else {
                        state.filterTags.remove(tag)
                        state.refreshViewStore()
                    }
                }
                Spacer(Modifier.padding(5.dp))
            }
        }
    }
}

