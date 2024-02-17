package data.perf

//
//
/**
 * Maintains a 1:1 bimap of all elements and an associated index.
 * Restrictions:
 *      Supports a max size of Int.MAX_VALUE.
 *      Only Int.MAX_VALUE number of items can even be inserted into it, without a full clear
 */
class IndexedMutableSet<T> : MutableSet<T> {


    private val setMapping = mutableMapOf<T, Int>()
    private val numMapping = mutableMapOf<Int, T>()

    private val freeNumSet = mutableSetOf<Int>()
    private val freeNumList = mutableListOf<Int>() // Holds numbers after they've been freed

    override val size by setMapping::size

    // Counter of the next free available index for use.
    private var indexCounter = 0

    fun posOf(item: T): Int {
        return setMapping[item] ?: -1
    }

    fun getByPos(pos: Int): T {
        return numMapping[pos]!!
    }

    override fun clear() {
        setMapping.clear()
        numMapping.clear()
    }

    private fun pushFreeIndex(free: Int) {
        freeNumSet.add(free)
        freeNumList.add(free)
    }

    private fun genIndex(): Int {
        return if (freeNumSet.isNotEmpty()) {
            val picked = freeNumList.removeLast()
            freeNumSet.remove(picked)
            picked
        } else {
            val curr = indexCounter
            indexCounter += 1
            curr
        }
    }

    override fun addAll(elements: Collection<T>): Boolean {
        TODO("Not yet implemented")
    }

    override fun add(element: T): Boolean {
        val newIndex = genIndex()
        setMapping[element] = newIndex
        numMapping[newIndex] = element
        return true
    }

    override fun contains(element: T): Boolean {
        return element in setMapping
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        return setMapping.keys.containsAll(elements)
    }

    override fun isEmpty(): Boolean {
        return size == 0
    }

    override fun iterator(): MutableIterator<T> {
        return setMapping.keys.iterator()
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        TODO("Not yet implemented")
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        return elements.map {
            remove(it)
        }.reduce { a, b -> a || b }
    }

    override fun remove(element: T): Boolean {
        return if (element !in setMapping) {
            false
        } else {
            val numIndex = setMapping[element]!!
            setMapping.remove(element)
            numMapping.remove(numIndex)
            pushFreeIndex(numIndex)
            true
        }
    }
}