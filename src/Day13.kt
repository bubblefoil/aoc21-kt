data class Fold(val axis: Char, val coordinate: Int) {

    private fun fold(a: Int): Int {
        if (a < coordinate) return a
        return coordinate - (a - coordinate)
    }

    fun fold(p: Point): Point {
        if (axis == 'x') {
            return Point(fold(p.x), p.y)
        }
        return Point(p.x, fold(p.y))
    }
}

fun readDots(input: List<String>): List<Point> {
    return input.takeWhile { it.isNotEmpty() }
        .map { it.split(',').map { c -> c.toInt() } }
        .map { Point(it.first(), it.last()) }
}

fun readFolds(input: List<String>): List<Fold> {
    val foldPattern = Regex("fold along ([xy])=(\\d+)")
    return input.dropWhile { it.isNotEmpty() }.drop(1)
        .mapNotNull(foldPattern::matchEntire)
        .map { Fold(it.groups[1]!!.value.first(), it.groups[2]!!.value.toInt()) }
}

fun printPoints(points: Iterable<Point>) {
    val w = points.maxOfOrNull { it.x } ?: return
    val h = points.maxOfOrNull { it.y } ?: return
    val pointSet = points.toSet()
    for (y in 0..h) {
        for (x in 0..w) {
            print(if (Point(x, y) in pointSet) '#' else '.')
        }
        println()
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val dots = readDots(input)
        val folds = readFolds(input)
        val folded = dots.map { folds.first().fold(it) }.toSet()
        return folded.size
    }

    fun part2(input: List<String>): Int {
        val dots = readDots(input)
        val folds = readFolds(input)
        val folded = folds.fold(dots.toSet()){ acc, fold -> acc.map { fold.fold(it) }.toSet() }
        printPoints(folded)
        return folded.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 17) { "Your part 1 answer was: ${part1(testInput)}" }

    val input = readInput("Day13")
    println(part1(input))

    println(part2(input))
}
