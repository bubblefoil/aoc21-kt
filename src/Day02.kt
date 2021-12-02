data class Int2(val x: Int, val y: Int)

operator fun Int2.plus(b: Int2): Int2 {
    return Int2(this.x + b.x, this.y + b.y)
}

fun commandToVec(dir: String, units: Int): Int2 {
    return when (dir) {
        "forward" -> Int2(units, 0)
        "down" -> Int2(0, units)
        "up" -> Int2(0, -units)
        else -> Int2(0, 0)
    }
}

fun toCommands(input: List<String>) = input.asSequence()
    .map { it.split(' ') }
    .map { commandToVec(it.first(), it.last().toInt()) }


fun main() {

    fun part1(input: List<String>): Int {
        return toCommands(input)
            .reduce { acc, pos -> acc + pos }
            .let { it.x * it.y }
    }

    fun part2(input: List<String>): Int {
        return toCommands(input)
            .fold(arrayOf(0, 0, 0)) { acc: Array<Int>, cmd: Int2 -> arrayOf(acc[0] + cmd.x, acc[1] + acc[2] * cmd.x, acc[2] + cmd.y) }//horizontal, depth, aim
            .let { it[0] * it[1] }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
