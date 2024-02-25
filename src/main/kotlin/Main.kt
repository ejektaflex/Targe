import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import data.AppPage
import data.DataManager
import data.PageManager
import views.GalleryState
import views.GalleryView
import views.InspectState
import views.InspectView

fun main() = application {

    PageManager.goToGallery()

    Window(onCloseRequest = ::exitApplication, title = "Targe") {
        MainAppScaffolding()
    }
}

@Composable
fun MainAppScaffolding() {
    Scaffold(
        topBar = {
            TopAppBar(
                contentColor = AppConstants.Theme.TextDark,
                backgroundColor = AppConstants.Theme.Primary,
                title = {
                    Text("Targe - Gallery")
                },
                actions = {
                    IconButton(onClick = {
                        println("Doot!")
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Menu Here"
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        val stateList = remember { PageManager.stateStack }
        stateList.lastOrNull()?.drawSelf()
    }
}




