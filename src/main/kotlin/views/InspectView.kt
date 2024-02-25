package views

import AppConstants
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage


@Composable
fun InspectView(state: InspectState) {
    Row(Modifier.padding(5.dp)) {
        Column(Modifier.weight(1f)) {
            Row(Modifier.padding(5.dp)) {
                var text by remember { mutableStateOf("") }
                var active by remember { mutableStateOf(false) }

                OutlinedTextField(text, onValueChange = { text = it }, modifier = Modifier.fillMaxWidth(), placeholder = {
                    Text("Add Tags..")
                })
            }
        }
        Column(Modifier.weight(2f)) {

            Divider(Modifier.width(16.dp).padding(8.dp))

            Row(
                Modifier.weight(1f).fillMaxSize().background(AppConstants.Theme.ImageBG).clip(RoundedCornerShape(5.dp)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(Modifier.clip(RoundedCornerShape(5.dp))) {
                    AsyncImage(
                        model = state.image,
                        "Main Image",
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Divider(Modifier.width(16.dp).padding(8.dp))

            Row {
                var descText by remember { mutableStateOf("Description") }
                OutlinedTextField(descText, { descText = it }, modifier = Modifier.fillMaxWidth(),
                    maxLines = 12
                )
            }

        }
    }
}