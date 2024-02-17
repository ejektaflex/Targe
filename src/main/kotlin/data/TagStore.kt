package data


data class TagStore(
    // Map of TagName to List<FilePath>
    private val tagFiles: MutableMap<String, MutableSet<String>> = mutableMapOf(),
    // Map of FileName to List<TagName>
    private val fileTags: MutableMap<String, MutableSet<String>> = mutableMapOf(),
    // Map of FileName to Desc
    private val fileDescs: MutableMap<String, MutableSet<String>> = mutableMapOf()
) {

    fun cloneWithTagSubset(subsetTags: Set<String>): TagStore {
        return copy(
            tagFiles = tagFiles.filter { it.key in subsetTags }.toMutableMap(),
            fileTags = fileTags.filter { it.value.intersect(subsetTags).isNotEmpty() }.toMutableMap(),
            fileDescs = fileDescs.filter { it.value.intersect(subsetTags).isNotEmpty() }.toMutableMap()
        )
    }

    // eventually we might need a cloneWithFileSubset

    val allTags: MutableSet<String>
        get() = tagFiles.keys

    val allFiles: MutableSet<String>
        get() = fileTags.keys
    
    fun genTagFrequencies(): List<Pair<String, Int>> {
        return tagFiles.map { it.key to it.value.size }.sortedWith(compareBy({ -it.second }, { it.first }))
    }

    private fun getTags(file: String): MutableSet<String> {
        return fileTags.getOrPut(file) { mutableSetOf() }
    }

    private fun getFiles(tag: String): MutableSet<String> {
        return tagFiles.getOrPut(tag) { mutableSetOf() }
    }

    private fun getDescs(file: String): MutableSet<String> {
        return fileDescs.getOrPut(file) { mutableSetOf() }
    }

    fun tagFileWithTag(file: String, tag: String) {
        getTags(file).add(tag)
        getFiles(tag).add(file)
    }

    fun tagFileWithTags(file: String, tags: Set<String>) {
        getTags(file).addAll(tags)
        for (tag in tags) {
            getFiles(tag).add(file)
        }
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
        getDescs(file).add(desc)
    }
}