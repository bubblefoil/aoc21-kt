fun main() {
    fun readFishCounts(input: List<String>): Map<Int, Long> {
        val initial = readInts(input[0], ',').map { it }
        return initial.groupBy { it }.mapValues { it.value.size.toLong() }
    }

    fun nextDay(numbersByDay: Map<Int, Long>): Map<Int, Long> {
        val nextDay = numbersByDay.mapKeys { it.key - 1 }
        val neg = nextDay[-1] ?: 0
        return nextDay.filterKeys { it >= 0 } + (6 to (nextDay[6] ?: 0) + neg) + (8 to neg)
    }

    fun countFish(byDay: Map<Int, Long>): Long {
        return byDay.values.sum()
    }

    fun nextDays(numbersByDay: Map<Int, Long>, dayCount: Int) = (0 until dayCount).fold(numbersByDay) { x, _ -> nextDay(x) }

    fun part1(input: List<String>): Long {
        val numbersByDay = readFishCounts(input)
        return countFish(nextDays(numbersByDay, 80))
    }

    fun part2(input: List<String>): Long {
        val numbersByDay = readFishCounts(input)
        return countFish(nextDays(numbersByDay, 256))
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 5934L)
    check(part2(testInput) == 26984457539)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
