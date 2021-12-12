val dirs = listOf(
    Pair(-1, -1), Pair(-1, 0), Pair(-1, 1),
    Pair(0, -1), Pair(0, 1),
    Pair(1, -1), Pair(1, 0), Pair(1, 1),
)

const val steps = 100

fun main() {
    fun printMatrix(matrix: ArrayList<IntArray>) {
        println("------------------------------")
        matrix.forEach { row ->
            row.forEach { n ->
                print("$n ")
            }
            println()
        }
        println("------------------------------")
    }

    fun incMatrix(matrix: ArrayList<IntArray>) {
        for (i in 0 until matrix.size) {
            for (j in 0 until matrix[i].size) {
                if (matrix[i][j] > 9) {
                    continue
                }
                matrix[i][j]++
                if (matrix[i][j] > 9) {
                    val lightingOctopus = "$i,$j"
                    val queue = mutableListOf(lightingOctopus)
                    val visited = mutableMapOf(lightingOctopus to true)
                    while (queue.size != 0) {
                        val octopusCoord = queue.first().split(",")
                        val x = octopusCoord[0].toInt()
                        val y = octopusCoord[1].toInt()
                        for (d in dirs) {
                            val dx = x + d.first
                            val dy = y + d.second
                            if (dx >= 0 && dx < matrix.size && dy >= 0 && dy < matrix[i].size) {
                                val nextNine = "$dx,$dy"
                                if (matrix[dx][dy] == 9 && !visited.containsKey(nextNine)) {
                                    visited[nextNine] = true
                                    queue.add(nextNine)
                                }
                                matrix[dx][dy]++
                            }
                        }
                        queue.removeFirst()
                    }
                }
            }
        }
    }

    fun matrixCleanup(matrix: ArrayList<IntArray>): Int {
        var count = 0
        matrix.forEachIndexed { i, row ->
            row.forEachIndexed { j, octopus ->
                if (octopus > 9) {
                    matrix[i][j] = 0
                    count++
                }
            }
        }
        return count
    }

    fun part1(input: List<String>): Int {
        val matrix = arrayListOf<IntArray>()
        for (line in input) {
            val row = IntArray(line.length)
            line.forEachIndexed { index, c -> row[index] = c.toString().toInt() }
            matrix.add(row)
        }
        var count = 0
        for (i in 1..steps) {
            incMatrix(matrix)
            count += matrixCleanup(matrix)
            //printMatrix(matrix)
        }
        return count
    }

    fun part2(input: List<String>): Int {
        val matrix = arrayListOf<IntArray>()
        for (line in input) {
            val row = IntArray(line.length)
            line.forEachIndexed { index, c -> row[index] = c.toString().toInt() }
            matrix.add(row)
        }
        var iteration = 0
        var i = 1
        while (iteration == 0) {
            incMatrix(matrix)
            val numberOfLighting = matrixCleanup(matrix)
            if (numberOfLighting == (matrix.size * matrix[0].size)) {
                iteration = i
                break
            }
            i++
            //printMatrix(matrix)
        }
        return iteration
    }
    val input = readInput("day11")
    println(part1(input))
    println(part2(input))
}