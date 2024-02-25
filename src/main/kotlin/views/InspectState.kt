package views

import data.AppPage
import data.TagStore

class InspectState(val store: TagStore, val image: String) : ViewState<InspectState> {

    override val page = AppPage.INSPECTOR
    val imgTags: Set<String>
        get() = store.getTags(image)
}