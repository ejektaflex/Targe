package views

import androidx.compose.runtime.Composable
import data.AppPage

interface ViewState<T : ViewState<T>> {
    val page: AppPage<T>

    @Composable
    fun drawSelf() {
        page.drawWithState(this as T)
    }
}