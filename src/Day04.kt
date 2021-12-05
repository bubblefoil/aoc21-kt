data class Board(val rows: List<List<Int>>, val cols: List<List<Int>>) {

    val unmarked: List<MutableSet<Int>> = (rows + cols).map { it.toMutableSet() }

    fun mark(number: Int) {
        unmarked.forEach { it.remove(number) }
    }

    fun isWinner(): Boolean {
        return unmarked.any { it.isEmpty() }
    }
}

fun readRow(row: String): List<Int> {
    return row.chunked(3).map { it.trim().toInt() }
}

fun readBoard(input: List<String>): Board {
    val rows = input.filter { it.isNotEmpty() }.map { readRow(it) }
    val cols = (0 until 5).map { i -> rows.map { row -> row[i] } }
    return Board(rows, cols)
}

fun readBoards(input: List<String>): List<Board> {
    return input
        .chunked(6)
        .map { readBoard(it) }
}

fun main() {
    fun part1(input: List<String>): Int {
        val drawn = readInts(input[0], ',')
        val boards = readBoards(input.drop(1))
        for (x in drawn) {
            boards.forEach { it.mark(x) }
            val winner = boards.find { it.isWinner() }
            if (winner != null) {
                return winner.unmarked.flatten().toSet().sum() * x
            }
        }
        return -1
    }

    fun part2(input: List<String>): Int {
        val drawn = readInts(input[0], ',')
        val boards = readBoards(input.drop(1)).toMutableList()
        for (x in drawn) {
            boards.forEach { it.mark(x) }
            val winners = boards.filter { it.isWinner() }
            boards.removeAll(winners)
            if (winners.size == 1 && boards.isEmpty()) {
                return winners.first().unmarked.flatten().toSet().sum() * x
            }
        }
        return -1
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
