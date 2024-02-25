package data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import views.GalleryState
import views.InspectState
import views.ViewState

object PageManager {

    val stateStack = mutableStateListOf<ViewState<out ViewState<*>>>()

    val currentState: ViewState<*>?
        get() = stateStack.lastOrNull()

    val currentPage: AppPage<out ViewState<*>>?
        get() = currentState?.page

    @Composable
    fun draw() {
        currentState?.drawSelf()
    }

    fun goToGallery() {
        val newState = GalleryState(DataManager.Store)
        stateStack.add(newState)
    }

    fun goToInspector(file: String) {
        val newState = InspectState(DataManager.Store, file)
        stateStack.add(newState)
    }

}