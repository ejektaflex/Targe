import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import java.io.File

typealias MutableStateSet<T> = SnapshotStateMap<T, Unit>

fun <T> MutableStateSet<T>.addSetItem(item: T) {
    this[item] = Unit
}

fun <T> mutableStateSetOf(vararg items: T): MutableStateSet<T> = mutableStateMapOf(*items.map { it to Unit }.toTypedArray())

fun File.toCoilFile(): String {
    return absolutePath.toCoilFile()
}

fun String.toCoilFile(): String {
    return "file://" + replace("\\", "/")
}