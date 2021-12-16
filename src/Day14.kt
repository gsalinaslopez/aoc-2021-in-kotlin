fun main() {
    fun part1(input: List<String>) {
        val steps = 10
        var polymerTemplate = input[0]
        val pairInsertionMap = mutableMapOf<String, Char>()
        for (i in 2 until input.size) {
            val pairRules = input[i].split(" ")
            pairInsertionMap[pairRules[0]] = pairRules[2].first()
        }

        val elementCount = mutableMapOf<Char, Int>()
        for (c in polymerTemplate) {
            if (!elementCount.containsKey(c)) {
                elementCount[c] = 0
            }
            elementCount[c] = elementCount[c]!! + 1
        }

        for (iter in 1..steps) {
            val builtPolymer = ArrayList<Char>()
            for (i in 1 until polymerTemplate.length) {
                builtPolymer.add(polymerTemplate[i - 1])
                pairInsertionMap[polymerTemplate.substring(i - 1, i + 1)]?.let {
                    builtPolymer.add(it)
                    if (!elementCount.containsKey(it)) {
                        elementCount[it] = 0
                    }
                    elementCount[it] = elementCount[it]!! + 1
                }
            }
            builtPolymer.add(polymerTemplate.last())
            polymerTemplate = builtPolymer.joinToString("")
        }
        var min = Int.MAX_VALUE
        var max = Int.MIN_VALUE
        for (count in elementCount.values) {
            min = Math.min(min, count)
            max = Math.max(max, count)
        }
        print(elementCount)
        println(max - min)
    }

    fun part2(input: List<String>) {
        val steps = 40
        val polymerTemplate = input[0]
        val pairInsertionMap = mutableMapOf<String, Char>()
        for (i in 2 until input.size) {
            val pairRules = input[i].split(" ")
            pairInsertionMap[pairRules[0]] = pairRules[2].first()
        }

        val polymerGeneratorMap = mutableMapOf<String, Map<Char, Long>>()
        fun dfs(polymer: String, depth: Int): Map<Char, Long> {
            if (depth == steps) {
                return emptyMap()
            }

            val middle = pairInsertionMap[polymer]!!
            val polymerMap = mutableMapOf(middle to 1L)
            for (c in polymer) {
                if (!polymerMap.containsKey(c)) {
                    polymerMap[c] = 0
                }
                polymerMap[c] = polymerMap[c]!! + 1
            }

            val polymerKeyStr = "$depth,$polymer"
            polymerGeneratorMap[polymerKeyStr]?.let { return it }

            val leftPolymer = "${polymer[0]}$middle"
            val rightPolymer = "$middle${polymer[1]}"

            val leftPolymerMap = dfs(leftPolymer, depth + 1)
            val rightPolymerMap = dfs(rightPolymer, depth + 1)
            if (leftPolymerMap.isNotEmpty() && rightPolymerMap.isNotEmpty()) {
                polymerMap.clear()
                polymerMap.putAll(leftPolymerMap)
                polymerMap[middle] = polymerMap[middle]!! - 1
                for (k in rightPolymerMap.keys) {
                    polymerMap[k] = if(!polymerMap.containsKey(k)) {
                        rightPolymerMap[k]!!
                    } else {
                        polymerMap[k]!! + rightPolymerMap[k]!!
                    }
                }
            }
            polymerGeneratorMap[polymerKeyStr] = polymerMap.toMap()
            return polymerMap.toMap()
        }

        val polymerCountMap = mutableMapOf<Char, Long>()
        for (i in 1 until polymerTemplate.length) {
            val ret = dfs(polymerTemplate.substring(i - 1, i + 1), 0)
            ret.forEach { (k, v) ->
                polymerCountMap[k] = if (!polymerCountMap.containsKey(k)) {
                    v
                } else {
                    polymerCountMap[k]!! + v
                }
            }
            polymerCountMap[polymerTemplate[i]] = polymerCountMap[polymerTemplate[i]]!! - 1
        }

        polymerCountMap[polymerTemplate.last()] = polymerCountMap[polymerTemplate.last()]!! + 1
        var min = Long.MAX_VALUE
        var max = Long.MIN_VALUE
        for (count in polymerCountMap.values) {
            min = Math.min(min, count)
            max = Math.max(max, count)
        }
        print(polymerCountMap)
        println(max - min)
    }

    val input = readInput("day14")
    part1(input)
    part2(input)
}