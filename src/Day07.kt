import kotlin.math.abs

private fun readPositions() = readInts(readInput("Day07").first(), ',')

fun constantFuelCost(a: Int, b: Int) = abs(b - a)

fun growingFuelCost(a: Int, b: Int): Int {
    val distance = constantFuelCost(a, b)
    return distance * (distance + 1) / 2
}

fun main() {
    fun part1(input: List<Int>): Int {
        val min = input.minOrNull()!!
        val max = input.maxOrNull()!!
        return (min..max).minOf { pos: Int -> input.sumOf { constantFuelCost(pos, it) } }
    }

    fun part2(input: List<Int>): Int {
        val min = input.minOrNull()!!
        val max = input.maxOrNull()!!
        return (min..max).minOf { pos: Int -> input.sumOf { growingFuelCost(pos, it) } }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = listOf(16, 1, 2, 0, 4, 2, 7, 1, 2, 14)
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readPositions()
    println(part1(input))
    println(part2(input))
}
