fun countCharsAt(xs: Iterable<String>, at: Int, c: Char): Int {
    return xs.map { it[at] }.count { it == c }
}

fun flip(bin: String): String {
    return String(bin.map { if (it == '0') '1' else '0' }.toCharArray())
}

fun filterByMostCommonAt(xs: Iterable<String>, at: Int): List<String> {
    val c = '1'
    val n = countCharsAt(xs, at, c)
    if (n >= xs.count() - n)
        return xs.filter { it[at] == c }
    return xs.filter { it[at] != c }
}

fun filterByLeastCommonAt(xs: Iterable<String>, at: Int): List<String> {
    val c = '0'
    val n = countCharsAt(xs, at, c)
    if (n <= xs.count() - n)
        return xs.filter { it[at] == c }
    return xs.filter { it[at] != c }
}

fun findOxygen(input: List<String>): String {
    var oxNums = input
    var at = 0
    do {
        oxNums = filterByMostCommonAt(oxNums, at)
        at++
    } while (oxNums.size > 1)
    return oxNums.first()
}

fun findCo2(input: List<String>): String {
    var oxNums = input
    var at = 0
    do {
        oxNums = filterByLeastCommonAt(oxNums, at)
        at++
    } while (oxNums.size > 1)
    return oxNums.first()
}

fun main() {
    fun part1(input: List<String>): Int {
        val h = input.size
        val w = input[0].length
        val bin = (0 until w)
            .map { countCharsAt(input, it, '1') }
            .map { if (2 * it > h) '1' else '0' }
            .let { String(it.toCharArray()) }
        val gamma = bin.toInt(2)
        val epsilon = flip(bin).toInt(2)
        return gamma * epsilon
    }

    fun part2(input: List<String>): Int {
        val ox = findOxygen(input).toInt(2)
        val co2 = findCo2(input).toInt(2)
        return ox * co2
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(countCharsAt(testInput, 0, '1') == 7)
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
