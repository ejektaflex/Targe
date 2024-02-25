import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.input.key.isCtrlPressed
import data.DataManager
import data.PageManager

fun main() = application {

    PageManager.goToGallery()

    Window(onCloseRequest = ::exitApplication, title = "Targe", onKeyEvent = {
        DataManager.isCtrl = it.isCtrlPressed
        false
    }) {
         MaterialTheme {
            MainAppScaffolding()
        }
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
                        PageManager.apply {
                            if (stateStack.size > 1) {
                                stateStack.removeLast()
                            }
                        }
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




