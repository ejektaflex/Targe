package data


class TagStore() {
    // Map of TagName to List<FilePath>
    private val tagFiles = mutableMapOf<String, MutableList<String>>()
    // Map of FileName to List<TagName>
    private val fileTags = mutableMapOf<String, MutableList<String>>()
    // Map of FileName to Desc
    private val fileDesc = mutableMapOf<String, String>()

    val allTags: MutableSet<String>
        get() = tagFiles.keys

    val allFiles: MutableSet<String>
        get() = fileTags.keys

    private fun getTags(file: String): MutableList<String> {
        return fileTags.getOrPut(file) { mutableListOf() }
    }

    private fun getFiles(tag: String): MutableList<String> {
        return tagFiles.getOrPut(tag) { mutableListOf() }
    }

    private fun getDesc(file: String): String? {
        return fileDesc[file]
    }

    // Renames, but won't merge
    private fun renameTag(oldName: String, newName: String) {
        if (newName in allTags) {
            throw Exception("Cannot rename, the new name already exists!")
        }
        if (oldName !in allTags) {
            throw Exception("Cannot rename, the old name does not exist!")
        }

        tagFiles[newName] = tagFiles[oldName]!!
        tagFiles.remove(oldName)
    }

    private fun mergeAndRemove(srcTag: String, destTag: String) {
        if (destTag !in allTags) {
            throw Exception("Dest for merging does not exist!")
        }
        if (srcTag !in allTags) {
            throw Exception("Src for merging does not exist!")
        }
        tagFiles[destTag]!!.addAll(tagFiles[srcTag]!!)
        tagFiles.remove(srcTag)
    }

    fun addTagToFile(tag: String, file: String) {
        getFiles(file).add(tag)
    }

    fun addFileToTag(file: String, tag: String) {
        getTags(tag).add(file)
    }

    fun addDescToFile(file: String, desc: String) {
        fileDesc[file] = desc
    }
}