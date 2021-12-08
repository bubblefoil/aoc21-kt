fun readDisplay(line: String): Display {
    val inOut = line.split('|')
    val signals = inOut.first().split(' ').map { it.trim() }
    val segments = inOut.last().trim().split(' ').map { it.trim() }
    return Display(signals, segments)
}

data class Display(val signals: List<String>, val segments: List<String>)

fun diff(a: String, b: String): String {
    return (a.toSet() - b.toSet()).joinToString("")
}

// Number of segments
// 0: 6
// 1: 2
// 2: 5
// 3: 5
// 4: 4
// 5: 5
// 6: 6
// 7: 3
// 8: 7
// 9: 6
fun decode(display: Display): Int {
    val sig = display.signals
    val one = sig.single { it.length == 2 }
    val four = sig.single { it.length == 4 }
    val seven = sig.single { it.length == 3 }
    val eight = sig.single { it.length == 7 }
    val nine = sig.single { it.length == 6 && four.all(it::contains) }
    val six = sig.single { it.length == 6 && !one.all(it::contains) }
    val zero = sig.single { it.length == 6 && it != nine && it != six }
    // Single segments named as in the printed digits
    val c = diff(eight, six).first()
    val e = diff(eight, nine).first()
    val f = one.first { it != c }
    val two = sig.single { it.length == 5 && it.contains(c) && it.contains(e) && !it.contains(f) }
    val three = sig.single { it.length == 5 && it.contains(c) && it.contains(f) && !it.contains(e) }
    val five = sig.single { it.length == 5 && !it.contains(c) && !it.contains(e) && it.contains(f) }
    val digits = mapOf(
        '0' to zero.toSet(),
        '1' to one.toSet(),
        '2' to two.toSet(),
        '3' to three.toSet(),
        '4' to four.toSet(),
        '5' to five.toSet(),
        '6' to six.toSet(),
        '7' to seven.toSet(),
        '8' to eight.toSet(),
        '9' to nine.toSet(),
    )

    fun decodeDigit(digit: String): Char {
        return digits.entries.find { it.value == digit.toSet() }!!.key
    }
    return display.segments.map(::decodeDigit).joinToString("").toInt()
}

fun main() {
    fun part1(input: List<String>): Int {
        val displays = input.map(::readDisplay)
        return displays.flatMap { it.segments }.count { it.length in setOf(2, 3, 4, 7) }
    }

    fun part2(input: List<String>): Int {
        val displays = input.map(::readDisplay)
        return displays.map(::decode).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 26) { part1(testInput) }
    check(part2(testInput) == 61229) { part2(testInput) }

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
