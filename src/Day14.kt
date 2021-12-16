fun main() {

    fun createInserter(rules: Map<String, List<Char>>) = { polymer: List<Char> ->
        polymer.subList(0, 1) + polymer
            .windowed(2)
            .flatMap { rules[it.joinToString("")] ?: it }
    }

    fun add(a: Map<Char, Long>, b: Map<Char, Long>): Map<Char, Long> = (a.keys + b.keys).associateWith { k -> (a[k] ?: 0L) + (b[k] ?: 0L) }

    fun calculateResult(frequencies: Map<Char, Long>): Long {
        val mostCommon = frequencies.maxByOrNull { it.value }?.value ?: 0L
        val leastCommon = frequencies.minByOrNull { it.value }?.value ?: 0L
        return mostCommon - leastCommon
    }

    fun recursive(input: List<String>, steps: Int): Long {
        val template = input.first().toList()
        val rules = input.drop(2).associate { it.substring(0, 2) to it.last() }
        // The cache key is XYn where X,Y are elements and n is the number of steps (inverse of the depth)
        val elementCountCache = mutableMapOf<String, Map<Char, Long>>()

        // For a single polymer insertion between a pair of elements
        fun getElementCounts(pair: String, steps: Int, inserter: (String) -> Pair<String, String>): Map<Char, Long> {
            if (steps == 0) {
                return mapOf(pair.first() to 1)
            }
            val nextSteps = steps - 1
            return elementCountCache.getOrPut(pair + steps) {
                inserter(pair)
                    .toList()
                    .map { elementCountCache.getOrPut(it + nextSteps) { getElementCounts(it, nextSteps, inserter) } }
                    .reduce(::add)
            }
        }

        fun inserter(s: String): Pair<String, String> {
            val inserted = rules[s]!!
            return String(charArrayOf(s.first(), inserted)) to String(charArrayOf(inserted, s.last()))
        }

        val frequency = template
            .windowed(2)
            .map { getElementCounts(it.joinToString(""), steps, ::inserter) }
            .reduce(::add)
            // The recursive algorithm always counts only the first element of a pair because the second one is count as the first one in the following pair.
            // So the very last element of the template was not count so far. Add it now:
            .let { add(it, mapOf(template.last() to 1)) }
        return calculateResult(frequency)
    }

    fun part1(input: List<String>): Long {
        // A naive solution which expands the whole polymer template
        val template = input.first().toList()
        val rules = input.drop(2).associate { it.substring(0, 2) to listOf(it.last(), it[1]) }
        val inserter = createInserter(rules)
        val polymer = (0 until 10).fold(template) { acc, _ -> inserter(acc) }
        val elementCounts = polymer.groupBy { it }.mapValues { it.value.size.toLong() }
        return calculateResult(elementCounts)
    }


    fun part2(input: List<String>): Long {
        // Uses a "memoized" recursive function of polymer pair and the step number and calculates element counts in the depth-first manner to get as many cache hits as possible.
        return recursive(input, 40)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 1588L) { "Your part 1 answer was: ${part1(testInput)}" }

    val input = readInput("Day14")
    println(part1(input))

    check(part2(testInput) == 2188189693529) { "Your part 2 answer was: ${part2(testInput)}" }
    println(part2(input))
}
