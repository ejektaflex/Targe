import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object AppConstants {
    object Theme {
        val Primary = Color(255, 192, 70)
        val White = Color(0xeeeeee)
        val TextDark = Color(24, 24, 24)
        val TextRed = Color(192, 0, 0)
        val ImageBG = Color(192, 192, 192)

    }

    object ColorSet {
        val OfLight = lightColorScheme(
            primary = Color(254, 196, 117),
            secondary = Color(254, 196, 117),
            background = Color(254, 196, 117),
            surface = Color(254, 196, 117)
        )
    }

    object DataLoading {
        val allowedMedia = mutableSetOf("jpg", "jpeg", "png")
    }
}