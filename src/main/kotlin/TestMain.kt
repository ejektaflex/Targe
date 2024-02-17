
fun main() {

    val doot = mutableMapOf(1 to "hello", 2 to "there", 3 to "world")

    doot.keys.remove(2)

    println(doot)

}