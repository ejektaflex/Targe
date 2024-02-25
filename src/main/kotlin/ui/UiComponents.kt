package ui

import AppConstants
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.SelectableChipColors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import data.DataManager
import data.FilterType
import data.TagStore
import toCoilFile
import kotlin.math.abs

// A simple image grid.
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TImageGrid(
    tagStore: TagStore,
    lsgs: LazyStaggeredGridState,
    selectedIndices: Set<Int>,
    onSingleClick: (img: String, ind: Int) -> Unit = { _, _ -> },
    onDoubleClick: (img: String, ind: Int) -> Unit = { _, _ -> }
) {

    val scrollState = remember { lsgs }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(256.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        state = scrollState
        //verticalArrangement = Arrangement.Center
    ) {
        val shownImages = tagStore.orderedFileList

        items(shownImages.size) {
            val coilFile = shownImages[it].toCoilFile()
            val isHovering = remember { mutableStateOf(false) }

            Box(
                Modifier.padding(5.dp).fillMaxSize().withHoverControl(isHovering, PointerIcon.Hand).clip(
                    RoundedCornerShape(5.dp)
                ).andIf(isHovering.value || it in selectedIndices) {
                    if (it in selectedIndices) {
                        border(8.dp, AppConstants.Theme.TextDark)
                    } else {
                        border(8.dp, AppConstants.Theme.Primary)
                    }
                }.onClick {
                    println("Clicked! Was img: ${shownImages[it].toCoilFile()} //// $coilFile")
                }
            ) {
                ContextMenuArea(items = {
                    val listItems = mutableListOf(
                        ContextMenuItem("Edit Tags On Photo") {/*do something here*/},
                    )

                    if (tagStore.orderedFileList.isNotEmpty()) {
                        listItems.add(ContextMenuItem("Remove selected labels from photo") {/*do something else*/})
                    }

                    listItems
                }) {

                    val lastClicked = remember { mutableStateOf(System.currentTimeMillis()) }

                    AsyncImage(
                        modifier = Modifier.onClick {
                            val currTime = System.currentTimeMillis()
                            if (abs(currTime - lastClicked.value) < 330L) {
                                onDoubleClick(coilFile, it)
                            } else {
                                onSingleClick(coilFile, it)
                            }
                            lastClicked.value = currTime
                        },
                        model = ImageRequest.Builder(LocalPlatformContext.current)
                            .data(coilFile)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        onError = { err ->
                            println("Couldn't do it: $coilFile")
                            err.result.throwable.printStackTrace()
                        }
                    )
                }
            }
        }
    }
}

// Selection States

interface TSelectedState {
    var filterType: FilterType?
}

private class TSelectedStateImpl(startType: FilterType?) : TSelectedState {
    var _selected by mutableStateOf(startType)
    override var filterType: FilterType?
        get() = _selected
        set(value) { _selected = value }
}



class TagMapState(private val tagMap: MutableMap<String, FilterType>, val tag: String): TSelectedState {
    override var filterType: FilterType?
        get() = tagMap[tag]
        set(value) {
            if (value == null) {
                tagMap.remove(tag)
            } else {
                tagMap[tag] = value
            }
        }
}


// A chip used for filtering.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TFilterChip(selectedState: TSelectedState = remember { TSelectedStateImpl(null) }, chipDisplay: String, onSelectionChanged: (old: FilterType?, new: FilterType?) -> Unit = { _, _ -> }) {

    FilterChip(
        onClick = {
            val oldSelected = selectedState.filterType
            selectedState.filterType = when {
                selectedState.filterType != null -> null
                DataManager.isCtrl -> FilterType.WITHOUT
                else -> FilterType.WITH
            }
            onSelectionChanged(oldSelected, selectedState.filterType)
        },
        label = {
            Text(chipDisplay, color = if (selectedState.filterType == FilterType.WITHOUT) AppConstants.Theme.TextRed else AppConstants.Theme.Primary)
        },
        selected = selectedState.filterType != null,
        leadingIcon = if (selectedState.filterType != null) {
            {
                Icon(
                    imageVector = if (selectedState.filterType == FilterType.WITH) Icons.Filled.Done else Icons.Filled.Clear,
                    contentDescription = "Filter Icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        }
    )
}