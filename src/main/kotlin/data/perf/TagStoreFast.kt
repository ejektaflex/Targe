package data.perf

class TagStoreFast {
    // about 18K tags
    val tagNames = IndexedMutableSet<String>()
    // about 50K images
    val fileNames = IndexedMutableSet<String>()
    //val tagMap = mutableMapOf()

    // Long is just two Ints packed next to each other, so this is an efficient
    val links = mutableSetOf<Long>()

    fun tagFileWithTag(file: String, tag: String) {
        tagNames.add(tag)
        val a = tagNames.posOf(tag)

        fileNames.add(file)
        val b = fileNames.posOf(file)

        // files are shifted right and tags are the smallest 32 bits, since we access tags more often
        val longValue = (b.toLong() shr 32) + a.toLong()
        links.add(longValue)
    }

    fun linksWithTag(tag: String): Set<String> {
        val a = tagNames.posOf(tag)
        return links.filter { it.toInt() == a }.map {
            tagNames.getByPos(it.toInt())
        }.toSet()
    }

//    init {
//        for (i in 1..1_000_000) {
//            associations.add(Random.nextLong(Long.MAX_VALUE))
//        }
//        println("Time to filter:")
//        val newList: List<Long>
//        val measured = measureTimeMillis {
//            newList = associations.filter { it.xor(42535L) == 0L }
//        }
//        println("Measured: ${measured}ms")
//        println(newList.size)
//
//        val id = Integer.MAX_VALUE.toLong() + 2000
//        val a = id.toInt()
//        val b = (id shr 32).toInt() // too big and outside of normal int range
//        println("ID $id, A $a, B $b")
//    }

}
