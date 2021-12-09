fun getHeight(map: List<IntArray>, x: Int, y: Int): Int {
    if (x !in map[0].indices || y !in map.indices) return Int.MIN_VALUE
    return map[y][x]
}

fun getHeight(map: List<IntArray>, p: Point): Int = getHeight(map, p.x, p.y)

private fun isLocalLowest(heightMap: List<IntArray>, x: Int, y: Int, h: Int): Boolean {
    return validNeighbors(heightMap, Point(x, y)).all { getHeight(heightMap, it) > h }
}

fun isLocalLowest(heightMap: List<IntArray>, x: Int, y: Int): Boolean = isLocalLowest(heightMap, x, y, getHeight(heightMap, x, y))

private fun validNeighbors(heightMap: List<IntArray>, p: Point): List<Point> {
    return listOf(
        Point(p.x - 1, p.y),
        Point(p.x + 1, p.y),
        Point(p.x, p.y - 1),
        Point(p.x, p.y + 1),
    ).filter { it.x in heightMap[0].indices && it.y in heightMap.indices }
}

fun getBasin(heightMap: List<IntArray>, lowest: Point): List<Point> {
    val basin = mutableSetOf(lowest)
    val checkQueue = ArrayDeque(validNeighbors(heightMap, lowest).filter { getHeight(heightMap, it) < 9 })
    while (checkQueue.isNotEmpty()) {
        val p = checkQueue.removeFirst()
        val neighbors = validNeighbors(heightMap, p).filter { getHeight(heightMap, it) < 9 } - basin
        checkQueue.addAll(neighbors)
        basin += p
    }
    return basin.toList()
}

private fun allPoints(heightMap: List<IntArray>) = heightMap.flatMapIndexed { i, row ->
    row.indices.map { j ->
        Point(j, i)
    }
}

fun lowestPoints(heightMap: List<IntArray>): List<Point> {
    return allPoints(heightMap).filter { isLocalLowest(heightMap, it.x, it.y) }
}

fun main() {

    fun part1(input: List<String>): Int {
        val heightMap = input.map { line -> line.map { it.digitToInt() }.toIntArray() }
        val lowPoints = heightMap.mapIndexed { y, row ->
            row.filterIndexed { x, _ ->
                isLocalLowest(heightMap, x, y)
            }
        }
        return lowPoints
            .flatten()
            .sumOf { it + 1 }
    }

    fun part2(input: List<String>): Int {
        val heightMap = input.map { line -> line.map { it.digitToInt() }.toIntArray() }
        val lowestPoints = lowestPoints(heightMap)
        val basins = lowestPoints.map { getBasin(heightMap, it) }
        val basinSizes = basins.map { it.size }
        val threeLargest = basinSizes.sortedDescending().take(3)
        return threeLargest.reduce { acc, size -> acc * size }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 15) { "Your part 1 answer was: ${part1(testInput)}" }

    val input = readInput("Day09")
    println(part1(input))

    check(part2(testInput) == 1134) { "Your part 2 answer was: ${part2(testInput)}" }
    println(part2(input))
}
