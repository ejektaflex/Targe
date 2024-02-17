package ui

import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerHoverIcon

// Modifier States

fun Modifier.andIf(test: Boolean, func: Modifier.() -> Modifier): Modifier {
    return if (test) {
        func()
    } else {
        this
    }
}

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.withHoverControl(isHovering: MutableState<Boolean>, hoverIcon: PointerIcon? = null) = onPointerEvent(PointerEventType.Enter) {
    isHovering.value = true
}.onPointerEvent(PointerEventType.Exit) {
    isHovering.value = false
}.run {
    hoverIcon?.let { pointerHoverIcon(it) } ?: this
}