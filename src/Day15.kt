import kotlin.math.abs

private fun validNeighbors(grid: Collection<Point>, p: Point): List<Point> {
    return listOf(
        Point(p.x - 1, p.y),
        Point(p.x + 1, p.y),
        Point(p.x, p.y - 1),
        Point(p.x, p.y + 1),
    ).filter { it in grid }
}

fun reconstructPath(cameFrom: Map<Point, Point>, current: Point): Collection<Point> {
    val path = ArrayDeque(listOf(current))
    var point = current
    while (point in cameFrom) {
        point = cameFrom[point]!!
        path.addFirst(point)
    }
    return path.toList()
}

fun aStar(start: Point, goal: Point, d: (Point, Point) -> Int, h: (Point) -> Int, neighbors: (Point) -> List<Point>): Collection<Point> {
    val openSet = mutableSetOf(start)
    val cameFrom = mutableMapOf<Point, Point>()
    val gScore = mutableMapOf(start to 0).withDefault { Int.MAX_VALUE }
    val fScore = mutableMapOf(start to 0).withDefault { Int.MAX_VALUE }
    while (openSet.isNotEmpty()) {
        val current = openSet.minByOrNull { fScore[it]!! }!!
        if (current == goal) {
            return reconstructPath(cameFrom, current)
        }
        openSet.remove(current)
        for (neighbor in neighbors(current)) {
            val tentativeScore = gScore.getValue(current) + d(current, neighbor)
            if (tentativeScore < gScore.getValue(neighbor)) {
                cameFrom[neighbor] = current
                gScore[neighbor] = tentativeScore
                fScore[neighbor] = tentativeScore + h(neighbor)
                openSet.add(neighbor)
            }
        }
    }
    return emptyList()
}

fun manhattan(a: Point, b: Point): Int = (abs(b.x - a.x) + abs(b.y - a.y))

fun printPath(points: Iterable<Point>, map: Map<Point, Int>) {
    val w = points.maxOfOrNull { it.x } ?: return
    val h = points.maxOfOrNull { it.y } ?: return
    val pointSet = points.toSet()
    for (y in 0..h) {
        for (x in 0..w) {
            val p = Point(x, y)
            print(if (p in pointSet) map[p] ?: 0 else '.')
        }
        println()
    }
}

fun tileMap(map: Map<Point, Int>): Map<Point, Int> {
    val size = map.keys.maxByOrNull { it.x + it.y }!!.let { Point(it.x + 1, it.y + 1) }

    tailrec fun wrap(x: Int): Int {
        if (x <= 9) return x
        return wrap(x - 9)
    }

    fun risk(p: Point): Int {
        val r = map[p]
        if (r != null) return r
        val x = p.x % size.x
        val y = p.y % size.y
        return wrap(map[Point(x, y)]!! + p.x / size.x + p.y / size.y)
    }
    return ((0 until (size.x * 5))).flatMap {
        (0 until size.y * 5).map { y -> Point(it, y) }
    }.associateWith(::risk)
}

fun main() {
    fun totalRisk(map: Map<Point, Int>): Int {
        val start = Point(0, 0)
        val goal = map.keys.maxByOrNull { it.x + it.y }!!
        val path = aStar(start, goal, { _, b -> map[b] ?: 1 }, { manhattan(it, goal) }, { validNeighbors(map.keys, it) })
        //        printPath(path, map)
        return (path - start).mapNotNull { map[it] }.sum()
    }

    fun part1(input: List<String>): Int {
        val map = readHeightmap(input)
        return totalRisk(map)
    }

    fun part2(input: List<String>): Int {
        val map = tileMap(readHeightmap(input))
        // This could have been done just by supplying different scoring and neighbor function, but whatever...
        // Damn, this is slow...
        return totalRisk(map)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 40) { "Your part 1 answer was: ${part1(testInput)}" }

    val input = readInput("Day15")
    println(part1(input))
//    val testMapTiled = tileMap(readHeightmap(testInput))
//    printPath(testMapTiled.keys, testMapTiled)
    check(part2(testInput) == 315) { "Your part 2 answer was: ${part2(testInput)}" }
    println(part2(input))
}
