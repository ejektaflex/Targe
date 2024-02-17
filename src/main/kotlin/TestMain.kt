import data.TagStore
import data.perf.TagStoreFast
import kotlin.system.measureTimeMillis

private val source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".lowercase()

fun randName(len: Int): String {
    return (1..len).map { source.random() }.joinToString("")
}

fun main() {

    val ts = TagStore()

    val instancedTags: List<String>
    val images: List<String>

    val setupTime = measureTimeMillis {
        instancedTags = (1..1_000_000).map { randName(3) }
        images = (1..50_000).map { "images/${randName(3)}.png" }
    }

    println("Setup took ${setupTime}ms")

    println("Num Total Tags: ${instancedTags.size}")

    val tagTime = measureTimeMillis {
        for (tag in instancedTags) {
            ts.tagFileWithTag(images.random(), tag)
        }
        println("Num Unique Tags: ${ts.allTags.size}")
        //println(ts.allTags.take(100))
    }

    println("Tagging 1M (Norm) took: ${tagTime}ms")

    val tsf = TagStoreFast()
    val measuredSet = measureTimeMillis {
        for (tag in instancedTags) {
            tsf.tagFileWithTag(images.random(), tag)
        }
    }
    println("Tagging 1M (Fast) took: ${measuredSet}ms")


    // FILTER WORLD!

    val filterNormTime = measureTimeMillis {
        //val newTs = ts.cloneWithSubset(setOf("abc"), FilterType.AND)
        val newTs = ts.cloneWithEntireSubset(setOf("abc"))
        println("Num Filtered Tags: ${newTs.allTags.size}")
    }

    println("Filtering (Norm) took: ${filterNormTime}ms")

    val filterFastTime = measureTimeMillis {
        val newTfs = tsf.linksWithTag("abc")
        println("Num Filtered Tags: ${newTfs.size}")
    }

    println("Filtering (Perf) took: ${filterFastTime}ms")


//    // QUICK PERF!
//
//    val accessNormTime = measureNanoTime {
//        val getTag = ts.getFiles("abc")
//    }
//
//    println("Accessing (Norm) took: ${accessNormTime}ns")
//
//    val accessPerfTime = measureNanoTime {
//        val getTag = tsf.linksWithTag("abc")
//    }
//
//    println("Accessing (Perf) took: ${accessPerfTime}ns")






}