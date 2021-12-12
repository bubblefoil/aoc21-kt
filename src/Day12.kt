fun readEdges(input: List<String>): Map<String, List<String>> {
    return input
        .map { it.split('-') }
        .flatMap { listOf(it.first() to it.last(), it.last() to it.first()) }
        .groupBy({ it.first }, { it.second })
}

fun isSmall(s: String): Boolean {
    return s.all { it.isLowerCase() }
}

fun maxVisitsP2(visited: Map<String, Int>, v: String): Int {
    if (v in listOf("start", "end")) return 1
    if (!isSmall(v)) return Int.MAX_VALUE
    if ((visited - listOf("start", "end")).filter { isSmall(it.key) && it.value == 2 }.isEmpty()) return 2
    return 1
}

fun findPaths(input: List<String>, maxVisits: (visited: Map<String, Int>, v: String) -> Int): Int {
    val edges: Map<String, List<String>> = readEdges(input)
    val nodes: Set<String> = edges.keys

    val visited = nodes.associateWith { 0 }.toMutableMap()
    val currentPath = mutableListOf<String>()
    val paths = mutableListOf<List<String>>()


    fun dfs(start: String, end: String) {
        if (visited[start]!! >= maxVisits(visited, start))
            return
        visited[start] = visited[start]!! + 1
        currentPath.add(start)
        if (start == end) {
            paths.add(currentPath.toList())
            visited[start] = visited[start]!! - 1
            currentPath.removeLast()
            return
        }
        for (v in edges[start]!!) {
            dfs(v, end)
        }
        currentPath.removeLast()
        visited[start] = visited[start]!! - 1
    }

    dfs("start", "end")
//    paths.forEach { println(it.joinToString(",")) }
    return paths.size
}

fun main() {

    fun part1(input: List<String>): Int {
        return findPaths(input) { _, v -> if (isSmall(v)) 1 else Int.MAX_VALUE }
    }

    fun part2(input: List<String>): Int {
        return findPaths(input, ::maxVisitsP2)
    }

    // test if implementation meets criteria from the description, like:
    val testInputS = """
        start-A
        start-b
        A-c
        A-b
        b-d
        A-end
        b-end
    """.trimIndent().lines()

    val testInputM = """
        dc-end
        HN-start
        start-kj
        dc-start
        dc-HN
        LN-dc
        HN-end
        kj-sa
        kj-HN
        kj-dc
    """.trimIndent().lines()

    val testInputL = readInput("Day12_test")
    check(part1(testInputS) == 10) { "Your part 1 answer was: ${part1(testInputS)}" }
    check(part1(testInputM) == 19) { "Your part 1 answer was: ${part1(testInputM)}" }
    check(part1(testInputL) == 226) { "Your part 1 answer was: ${part1(testInputL)}" }

    val input = readInput("Day12")
    println(part1(input))

    check(part2(testInputS) == 36) { "Your part 2/S answer was: ${part2(testInputS)}" }
    check(part2(testInputM) == 103) { "Your part 2/M answer was: ${part2(testInputM)}" }
    check(part2(testInputL) == 3509) { "Your part 2/L answer was: ${part2(testInputL)}" }
    println(part2(input))
}
