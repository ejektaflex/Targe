import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.isCtrlPressed
import data.DataManager
import data.PageManager

fun main() = application {

    PageManager.goToGallery()

    Window(onCloseRequest = ::exitApplication, title = "Targe", onKeyEvent = {
        DataManager.isCtrl = it.isCtrlPressed
        false
    }) {
         MaterialTheme(colorScheme = AppConstants.ColorSet.OfLight) {
             Box(Modifier.fillMaxSize().background(AppConstants.Theme.TextDark)) {
                 MainAppScaffolding()
             }
        }
    }
}

@Composable
fun MainAppScaffolding() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Targe - Gallery")
                },
                backgroundColor = DataManager.Theme.surface,
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
        Box(Modifier.padding(innerPadding)) {
            val stateList = remember { PageManager.stateStack }
            stateList.lastOrNull()?.drawSelf()
        }
    }
}




