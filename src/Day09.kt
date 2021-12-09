fun getHeight(map: List<IntArray>, x: Int, y: Int): Int {
    if (x !in map[0].indices || y !in map.indices) return Int.MAX_VALUE
    return map[y][x]
}

fun getHeight(map: List<IntArray>, p: Point): Int {
    return getHeight(map, p.x, p.y)
}

fun isLocalLowest(heightMap: List<IntArray>, x: Int, y: Int): Boolean {
    val h = getHeight(heightMap, x, y)
    return isLocalLowest(heightMap, x, y, h)
}

private fun isLocalLowest(heightMap: List<IntArray>, x: Int, y: Int, h: Int): Boolean {
    return (getHeight(heightMap, x - 1, y) > h) && (getHeight(heightMap, x + 1, y) > h) &&
            (getHeight(heightMap, x, y - 1) > h) && (getHeight(heightMap, x, y + 1) > h)
}

private fun neighborsWithing(heightMap: List<IntArray>, p: Point): List<Point> {
    return listOf(
        Point(p.x - 1, p.y),
        Point(p.x + 1, p.y),
        Point(p.x, p.y - 1),
        Point(p.x, p.y + 1),
    ).filter { it.x in heightMap[0].indices && it.y in heightMap.indices }
}

fun getBasinSeed(heightMap: List<IntArray>, lowest: Point): List<Point> {
    val basin = mutableSetOf(lowest)
    val checkQueue = ArrayDeque(neighborsWithing(heightMap, lowest))
    while (checkQueue.isNotEmpty()) {
        val p = checkQueue.removeFirst()
        val neighbors = neighborsWithing(heightMap, p) - basin
        if (getHeight(heightMap, p) != 9 && neighbors.any { getHeight(heightMap, it) > getHeight(heightMap, p) }) {
            checkQueue.addAll(neighbors)
            basin += p
        }
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


fun mergeBasins(basins: Collection<List<Point>>): List<List<Point>> {
    val toMerge = ArrayDeque(basins.map { it.toMutableSet() })
    val merged = mutableListOf<MutableSet<Point>>()
    while (toMerge.isNotEmpty()) {
        val basin = toMerge.removeFirst()
        val overlapping = merged.find { otherBasin -> basin.any(otherBasin::contains) }
        if (overlapping != null) {
            overlapping.addAll(basin)
        } else {
            merged.add(basin)
        }
    }
    return merged.map { it.toList() }.toList()
}

data class PointProps(val h: Int, val basin: Int) {
    fun withBasin(b: Int): PointProps {
        return PointProps(h, b)
    }
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

    fun getBasinsSeed(heightMap: List<IntArray>): List<List<Point>> {
        val lowestPoints = lowestPoints(heightMap)
        val allBasins = lowestPoints.map { getBasinSeed(heightMap, it) }
        return allBasins
//        return mergeBasins(allBasins)
    }

    fun getBasinSizesChecked(basins: Collection<Collection<Any>>): List<Int> {
        val basinSizes = basins.map { it.size }
        check(basins.flatten().toSet().size == basins.sumOf { it.size }) { "Some basins overlap" }
        return basinSizes
    }

    fun getBasinsSweep(heightMap: List<IntArray>): Collection<List<PointProps>> {
        var pointMap = allPoints(heightMap).associateWith { PointProps(getHeight(heightMap, it), -1) }.toMutableMap()
        // Mark basins
        val lowestPoints = lowestPoints(heightMap)
        lowestPoints.mapIndexed { index, point -> pointMap[point] = pointMap[point]!!.withBasin(index) }
        for (i in 0 until 9) {
            pointMap = pointMap.mapValues { p ->
                val neighborBasin = neighborsWithing(heightMap, p.key).map { pointMap[it] }.find { it!!.basin >= 0 && it.h in p.value.h..8 }
                if (neighborBasin != null) {
                    p.value.withBasin(neighborBasin.basin)
                }
                p.value
            }.toMutableMap()
        }
        return pointMap.values.groupBy { it.basin }.values
    }

    fun productOfLargest3(basins: Collection<Collection<Any>>): Int {
        val basinSizes = getBasinSizesChecked(basins)
        val threeLargest = basinSizes.sortedDescending().take(3)
        return threeLargest.reduce { acc, size -> acc * size }
    }

    fun seedAlgorithm(input: List<String>): Int {
        val heightMap = input.map { line -> line.map { it.digitToInt() }.toIntArray() }
        val basins = getBasinsSeed(heightMap)
        return productOfLargest3(basins)
    }


    fun sweepAlgorithm(input: List<String>): Int {
        val heightMap = input.map { line -> line.map { it.digitToInt() }.toIntArray() }
        val basins = getBasinsSweep(heightMap)
        return productOfLargest3(basins)
    }

    fun part2(input: List<String>): Int {
        return seedAlgorithm(input)
//        return sweepAlgorithm(input)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 15) { "Your part 1 answer was: ${part1(testInput)}" }

    val input = readInput("Day09")
    println(part1(input))

    check(part2(testInput) == 1134) { "Your part 2 answer was: ${part2(testInput)}" }
    println(part2(input))
    // 981708
    // 1474032 - seed with neighbors.any {higher}
}
