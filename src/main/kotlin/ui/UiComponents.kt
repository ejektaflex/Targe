package ui

import AppConstants
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.onClick
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import data.TagStore
import toCoilFile

// A simple image grid.
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TImageGrid(tagStore: TagStore) {

    LazyVerticalGrid(
        columns = GridCells.Adaptive(256.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalArrangement = Arrangement.Center
    ) {
        val shownImages = tagStore.orderedFileList

        items(shownImages.size) {
            val coilFile = shownImages[it].toCoilFile()
            val isHovering = remember { mutableStateOf(false) }

            Box(
                Modifier.padding(5.dp).fillMaxSize().withHoverControl(isHovering, PointerIcon.Hand).clip(
                    RoundedCornerShape(5.dp)
                ).andIf(isHovering.value) {
                border(8.dp, AppConstants.Theme.Primary)
            }.onClick {
                println("Clicked! Was img: ${shownImages[it].toCoilFile()} //// $coilFile")
            }) {
                AsyncImage(
                    modifier = Modifier,
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(coilFile)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

interface TSelectedState {
    var selected: Boolean
}

private class TSelectedStateImpl(startState: Boolean) : TSelectedState {
    var _selected by mutableStateOf(startState)
    override var selected: Boolean
        get() = _selected
        set(value) { _selected = value }
}



class TagMapState(private val tagMap: MutableMap<String, Boolean>, val tag: String): TSelectedState {
    override var selected: Boolean
        get() = tagMap.containsKey(tag)
        set(value) {
            if (value) {
                tagMap[tag] = value
            } else {
                tagMap.remove(tag)
            }
        }
}


// A chip used for filtering.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TFilterChip(selectedState: TSelectedState = remember { TSelectedStateImpl(false) }, chipDisplay: String, onSelectionChanged: (old: Boolean, new: Boolean) -> Unit = { _, _ -> }) {

    FilterChip(
        onClick = {
            val oldSelected = selectedState.selected
            selectedState.selected = !selectedState.selected
            onSelectionChanged(oldSelected, selectedState.selected)
        },
        label = {
            Text(chipDisplay)
        },
        selected = selectedState.selected,
        leadingIcon = if (selectedState.selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        }
    )
}