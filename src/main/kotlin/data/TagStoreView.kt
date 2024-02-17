package data

// A data view into a tag store
class TagStoreView(val store: TagStore) {
    val filterTags = mutableSetOf<String>()
}