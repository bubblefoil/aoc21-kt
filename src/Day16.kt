import kotlin.math.*

@Suppress("DataClassPrivateConstructor")
data class Rect private constructor(val x1: Int, val x2: Int, val y1: Int, val y2: Int) {
    companion object {
        fun fromCoords(x1: Int, x2: Int, y1: Int, y2: Int): Rect {
            return Rect(min(x1, x2), max(x1, x2), min(y1, y2), max(y1, y2))
        }
    }

    fun allPoints(): List<Point> {
        return (x1..x2).flatMap { x -> (y1..y2).map { y -> Point(x, y) } }
    }

    fun isIn(p: Point): Boolean {
        return p.x in (x1..x2) && p.y in (y1..y2)
    }
}

fun main() {
    fun part1(target: Rect): Int {
        val lowestY = target.y1
        // The trajectory above level 0 is always symmetrical, so there will be a y=0 point. We have to hit the target below sea level, so we choose the lowest point to get maximum step.
        // The speed will increase by 1 below 0, so the speed is reduced accordingly not to overshoot.
        val vY = -lowestY - 1
        val sMin = target.x1
        // Find the lowest x velocity to reach the target, so we can maximize the air time and still not to overshoot. After steps, vX goes to 0.
        val vX = ceil((-1 + sqrt((8 * sMin + 1).toDouble())) / 2).toInt()
        println(vX to vY)
        return vY * (vY + 1) / 2
    }

    fun checkHit(velocities: Rect, target: Rect): List<Point> {
        tailrec fun hits(p: Point, v: Point): Boolean {
            if (v.x > target.x2 || v.y < target.y1) {
                return false
            }
            if (target.isIn(p)) return true
            return hits(Point(p.x + v.x, p.y + v.y), Point(v.x - v.x.sign, v.y - 1))
        }
        return velocities
            .allPoints()
            .filter { hits(Point(0, 0), it) }
    }

    fun part2(target: Rect): Int {
        // The trajectory above level 0 is always symmetrical, so there will be a y=0 point. We have to hit the target below sea level, so we choose the lowest point to get maximum step.
        // The speed will increase by 1 below 0, so the speed is reduced accordingly not to overshoot.
        val vYMax = -target.y1 - 1
        val vYMin = -target.y2 - 1
        // Find the lowest x velocity range which hits by the highest trajectory.
        val vXMin = ceil((-1 + sqrt((8 * target.x1 + 1).toDouble())) / 2).toInt()
        val vXMax = floor((-1 + sqrt((8 * target.x2 + 1).toDouble())) / 2).toInt()
        println("x=$vXMin .. $vXMax, y=$vYMin .. $vYMax")
        val sureHits = Rect.fromCoords(vXMin, vXMax, vYMin, vYMax).allPoints() // These are the hits as calculated in p1
        val checkedHits =
            checkHit(Rect.fromCoords(vXMin, target.x2, vYMin, target.y1), target) // These hits have to be checked by running the steps. Velocity search space is limited to possible values.
        val allHits = sureHits.toSet() + checkedHits + target.allPoints() // All target points may be hit after one step if the velocity equals coordinates within the target.
        return allHits.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = Rect.fromCoords(20, 30, -10, -5)
    check(part1(testInput) == 45) { "Your part 1 answer was: ${part1(testInput)}" }

    val input = Rect.fromCoords(94, 151, -156, -103)
    println(part1(input))

    check(part2(testInput) == 112) { "Your part 2 answer was: ${part2(testInput)}" }
    println(part2(input))
}
