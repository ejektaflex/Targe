package data

import androidx.compose.runtime.Composable
import views.*

class AppPage<T : ViewState<T>>( val func: @Composable (T) -> Unit) {

    @Composable
    fun drawWithState(state: T) {
        func(state)
    }

    companion object {
        val GALLERY = AppPage<GalleryState>(func = @Composable { GalleryView(it) })
        val INSPECTOR = AppPage<InspectState> { InspectView(it) }
    }
}