package data

import java.nio.file.Path

typealias TagName = String
typealias FileName = String

data class TagStore(
    // Map of TagName to List<FilePath>
    private val tagFiles: MutableMap<String, MutableSet<String>> = mutableMapOf(),
    // Map of FileName to List<TagName>
    private val fileTags: MutableMap<String, MutableSet<String>> = mutableMapOf(),
    // Map of FileName to Desc
    private val fileDescs: MutableMap<String, MutableSet<String>> = mutableMapOf()
) {

    // Map of File, then each Tag and a boolean (True for Added, False for Removed)
    // This will help us keep track of what to flush to disk later on
    private val changedFiles = mutableMapOf<String, MutableMap<String, Boolean>>()

    fun getChangedFiles(): Set<String> {
        return changedFiles.filter { it.value.keys.isNotEmpty() }.keys
    }

    fun cloneWithEntireSubset(subsetTags: Set<String>): TagStore {
        // ERROR, is only returning the tags that are in the subset. We should be restricting to tags that are associated with files that have this tag

        // Get files that have these tags, then intersect them all to get only files with all these tags
        val filesWithAllTags = filesMatchingTagSet(subsetTags)
        // Get all tags associated with these files
        val matchingTagsForTheseFiles = filesToTags(filesWithAllTags)

        return copy(
            tagFiles = tagFiles.filter { it.key in matchingTagsForTheseFiles }.toMutableMap(),
            fileTags = fileTags.filter { it.key in filesWithAllTags }.toMutableMap(),
            fileDescs = fileDescs.filter { it.key in filesWithAllTags }.toMutableMap()
        )
    }

    fun filesMatchingTagSet(subsetTags: Set<String>): Set<String> {
        if (subsetTags.isEmpty()) {
            return fileTags.keys
        }
        return subsetTags.mapNotNull { tagFiles[it]?.toSet() }.reduceOrNull { filesA, filesB -> filesA.intersect(filesB) } ?: emptySet()
    }

    fun filesToTags(subsetFiles: Set<String>): Set<String> {
        return subsetFiles.map { getTags(it) }.flatten().toSet()
    }

    fun genFilteredTagSet(subsetTags: Set<String>): Set<String> {
        // Get files that have these tags, then intersect them all to get only files with all these tags
        val filesWithAllTags = filesMatchingTagSet(subsetTags)
        // Get all tags associated with these files
        return filesToTags(filesWithAllTags)
    }

    val allTags: MutableSet<String>
        get() = tagFiles.keys

    private val allFiles: MutableSet<String>
        get() = fileTags.keys

    val orderedFileList: List<String>
        get() = allFiles.sorted()

    fun genTagFrequencies(
        subsetTags: Set<String> = emptySet(),
        comparator: Comparator<Pair<String, Int>> = compareBy({ -it.second }, { it.first })
    ): List<Pair<String, Int>> {
        val tf = genFilteredTagSet(subsetTags)
        return tagFiles.filter { it.key in tf }.map { it.key to it.value.size }.sortedWith(comparator)
    }

    fun getTags(file: String): MutableSet<String> {
        return fileTags.getOrPut(file) { mutableSetOf() }
    }

    fun getFiles(tag: String): MutableSet<String> {
        return tagFiles.getOrPut(tag) { mutableSetOf() }
    }

    fun getChanges(file: String): MutableMap<String, Boolean> {
        return changedFiles.getOrPut(file) { mutableMapOf() }
    }

    private fun getDescs(file: String): MutableSet<String> {
        return fileDescs.getOrPut(file) { mutableSetOf() }
    }

    fun tagFileWithTag(file: String, tag: String, markDirty: Boolean = true) {
        getTags(file).add(tag)
        getFiles(tag).add(file)
        if (markDirty) {
            getChanges(file)[tag] = true
        }
    }

    fun untagFileWithTag(file: String, tag: String) {
        getTags(file).remove(tag)
        getFiles(tag).remove(file)
        getChanges(file)[tag] = false
    }

    fun tagFileWithTags(file: String, tags: Set<String>) {
        getTags(file).addAll(tags)
        for (tag in tags) {
            getFiles(tag).add(file)
        }
    }

    fun insertFileNoTags(file: String) {
        getTags(file)
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

    companion object {
        fun from(folder: Path): TagStore {
            val protoStore = TagStore()

            val testDataSet = folder.toFile()
            val allFiles = testDataSet.walk().toList()

            val imgFiles = allFiles.filter {
                it.isFile && it.extension in AppConstants.DataLoading.allowedMedia
            }.toSet()


            // TODO if there is no caption file, the file will not be added to the db
            val startingCaps = imgFiles.associate {
                val matchingCap = it.resolveSibling("${it.nameWithoutExtension}.txt").takeIf { f -> f.exists() }
                it.absolutePath to matchingCap?.readText()?.split(",")?.map { item ->
                    item.trim()
                }?.toSet()
            }

            for ((path, caps) in startingCaps) {
                if (caps != null) {
                    for (cap in caps) {
                        // Anything longer than three words (e.g. "dog with job") is instead a description
                        if (cap.split(" ").size > 3) {
                            protoStore.addDescToFile(path, cap)
                        } else {
                            protoStore.tagFileWithTag(path, cap, markDirty = false)
                        }
                    }
                } else {
                    protoStore.getTags(path)
                }
            }

            return protoStore
        }
    }
}