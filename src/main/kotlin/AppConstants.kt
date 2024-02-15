import androidx.compose.material.TextFieldColors
import androidx.compose.ui.graphics.Color

object AppConstants {
    object Theme {
        val Primary = Color(255, 192, 70)
        val White = Color(0xeeeeee)
        val TextDark = Color(24, 24, 24)
    }

    object DataLoading {
        val allowedMedia = mutableSetOf("jpg", "jpeg", "png")
    }
}