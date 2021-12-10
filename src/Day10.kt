val brackets = mapOf(
    '(' to ')',
    '[' to ']',
    '{' to '}',
    '<' to '>'
)

val pointsIllegal = mapOf(
    ')' to 3,
    ']' to 57,
    '}' to 1197,
    '>' to 25137
)

val pointsIncomplete = mapOf(
    ')' to 1,
    ']' to 2,
    '}' to 3,
    '>' to 4
)

fun checkLine(line: String): Pair<Int, List<Char>> {
    val stack = mutableListOf<Char>()
    for (c in line) {
        if (c in brackets) {
            stack.add(c)
        } else if (c != brackets[stack.removeLast()]) {
            return pointsIllegal[c]!! to emptyList()
        }
    }
    return if (stack.isNotEmpty())
        (-1 to stack.reversed().map { brackets[it]!! })
    else
        (0 to emptyList())
}

fun scoreIncomplete(closingBrackets: List<Char>): Long {
    return closingBrackets.fold(0) { acc, c -> 5 * acc + pointsIncomplete[c]!! }
}

fun main() {

    fun part1(input: List<String>): Int {
        return input
            .map { checkLine(it).first }
            .filter { it > 0 }
            .sum()
    }

    fun part2(input: List<String>): Long {
        val sortedScores = input
            .map { checkLine(it) }
            .filter { it.first < 0 }
            .map { scoreIncomplete(it.second) }
            .sorted()
        return sortedScores[sortedScores.size / 2]

    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397) { "Your part 1 answer was: ${part1(testInput)}" }

    val input = readInput("Day10")
    println(part1(input))

    check(part2(testInput) == 288957L) { "Your part 2 answer was: ${part2(testInput)}" }
    println(part2(input))
}
