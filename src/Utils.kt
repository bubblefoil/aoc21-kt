import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import java.time.LocalDate
import kotlin.io.path.*

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

fun readInts(s: String, delimiter: Char = ' '): List<Int> {
    return s.split(delimiter).map { it.toInt() }
}

infix fun Int.toward(to: Int): IntProgression {
    val step = if (this > to) -1 else 1
    return IntProgression.fromClosedRange(this, to, step)
}

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

data class Point(val x: Int, val y: Int)

fun nextDay(date: LocalDate = LocalDate.now()) {
    val d1kt = Path("src/Day01.kt")
    if (d1kt.notExists()) {
        println("Cannot copy Day01.kt, file not found")
        return
    }
    val currentDay = "Day%02d".format(date.dayOfMonth)
    val code = d1kt.readText().replace("Day01", currentDay)
    d1kt.resolveSibling("$currentDay.kt").createFile().writeText(code)
    d1kt.resolveSibling("$currentDay.txt").createFile()
    d1kt.resolveSibling("${currentDay}_test.txt").createFile()
}

fun main() {
    nextDay(LocalDate.of(2021, 12, 16))
}