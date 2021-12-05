import kotlin.math.abs

data class Line(val x1: Int, val y1: Int, val x2: Int, val y2: Int) {

    fun isOrthogonal(): Boolean {
        return x1 == x2 || y1 == y2
    }

    fun isDiagonal(): Boolean {
        return abs(x1 - x2) == abs(y1 - y2)
    }

    fun points(): Sequence<Point> {
        return if (x1 == x2) {
            (y1 toward y2).asSequence().map { Point(x1, it) }
        } else if (y1 == y2) {
            (x1 toward x2).asSequence().map { Point(it, y1) }
        } else {
            (x1 toward x2).zip(y1 toward y2) { x, y -> Point(x, y) }.asSequence()
        }
    }
}

data class Point(val x: Int, val y: Int)


fun parse(line: String): Line {
    val regex = Regex("""(\d+),(\d+) -> (\d+),(\d+)""")
    val result = regex.matchEntire(line)
    if (result == null || result.groupValues.size != 5) {
        throw IllegalArgumentException("Cannot parse line from '$line'")
    }
    val coord = result.groupValues.subList(1, 5).map { it.toInt() }
    return Line(coord[0], coord[1], coord[2], coord[3])
}

fun main() {
    fun part1(input: List<String>): Int {
        return input.map(::parse)
            .filter { it.isOrthogonal() }
            .flatMap { it.points() }
            .groupBy { it }
            .values
            .count { it.size > 1 }
    }

    fun part2(input: List<String>): Int {
        return input.map(::parse)
            .filter { it.isOrthogonal() || it.isDiagonal() }
            .flatMap { it.points() }
            .groupBy { it }
            .values
            .count { it.size > 1 }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    println(part1(testInput))
    println(part2(testInput))
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
