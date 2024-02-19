import java.io.File

fun File.toCoilFile(): String {
    return absolutePath.toCoilFile()
}

fun String.toCoilFile(): String {
    return "file://" + replace("\\", "/")
}