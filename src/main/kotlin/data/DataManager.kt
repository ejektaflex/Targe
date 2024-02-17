package data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import java.io.File

object DataManager {
    val store = TagStore()

    val testDataSet = File("test_data/odas")

    val allFiles = testDataSet.walk().toList()

    val imgFiles = allFiles.filter {
        it.isFile && it.extension in AppConstants.DataLoading.allowedMedia
    }

    val capFiles = allFiles.filter {
        it.isFile && it.extension == "txt"
    }

    var gallerySelection = imgFiles.toMutableStateList()

    fun setup() {
        val startingCaps = capFiles.associate {
            it.path to it.readText().split(",").map { item ->
                item.trim()
            }.toSet()
        }

        for ((path, caps) in startingCaps) {
            for (cap in caps) {
                // Anything longer than three words (e.g. "dog with job") is instead a description
                if (cap.split(" ").size > 3) {
                    store.addDescToFile(path, cap)
                } else {
                    store.tagFileWithTag(path, cap)
                }
            }
        }
    }

}