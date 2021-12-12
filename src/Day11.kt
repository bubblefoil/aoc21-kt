fun readHeightmap(input: List<String>): Map<Point, Int> {
    return input.mapIndexed { y, row ->
        row.mapIndexed { x, c -> Point(x, y) to c.digitToInt() }
    }.flatten().toMap()
}

private fun adjacent(p: Point): List<Point> {
    return listOf(
        Point(p.x - 1, p.y - 1),
        Point(p.x + 0, p.y - 1),
        Point(p.x + 1, p.y - 1),
        Point(p.x - 1, p.y),
        Point(p.x + 1, p.y),
        Point(p.x - 1, p.y + 1),
        Point(p.x + 0, p.y + 1),
        Point(p.x + 1, p.y + 1),
    )
}

fun step(octopuses: Map<Point, Int>): Pair<Map<Point, Int>, Int> {
    var flashes = 0
    val next = octopuses.mapValues { it.value + 1 }.toMutableMap()
    var flashing = next.filterValues { it > 9 }
    while (flashing.isNotEmpty()) {
//        find first >= 9, reset, inc neighbors, search again? This may do too many linear searches...
        flashes += flashing.count()
        flashing.keys.forEach { next[it] = 0 }
        flashing.keys
            .flatMap { adjacent(it) }
            .filter(next::contains)
            .forEach { if (next[it]!! > 0) next[it] = next[it]!! + 1 }
        flashing = next.filterValues { it > 9 }
    }
    return (next to flashes)
}

fun printOctopuses(octopuses: Map<Point, Int>) {
    val bb = octopuses.keys.maxByOrNull { it.x + it.y }!!
    for (y in 0..bb.y) {
        for (x in 0..bb.x) {
            print(octopuses[Point(x, y)])
        }
        println()
    }
    println()
}

fun main() {
    fun part1(input: List<String>): Int {
        var octopuses = readHeightmap(input)
//        printOctopuses(octopuses)
        var flashes = 0
        for (i in 0 until 100) {
            val (nextOctopuses, flashesInStep) = step(octopuses)
//            printOctopuses(nextOctopuses)
            flashes += flashesInStep
            octopuses = nextOctopuses
        }
        return flashes
    }

    fun part2(input: List<String>): Int {
        var octopuses = readHeightmap(input)
//        printOctopuses(octopuses)
        var steps = 0
        do {
            val (nextOctopuses, _) = step(octopuses)
            octopuses = nextOctopuses
            steps++
        } while (!octopuses.values.all { it == 0 })
        return steps
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 1656) { "Your part 1 answer was: ${part1(testInput)}" }

    val input = readInput("Day11")
    println(part1(input))

    check(part2(testInput) == 195) { "Your part 2 answer was: ${part2(testInput)}" }
    println(part2(input))
}
