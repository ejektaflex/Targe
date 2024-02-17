import java.io.File

fun File.toCoilFile(): String {
    return "file://" + absolutePath.replace("\\", "/")
}

fun String.toCoilFile(): String {
    return "file://" + replace("\\", "/")
}