import kotlin.math.abs

fun main() {

    fun getCoordinateSet(coordinates: List<String>): MutableSet<Pair<Int, Int>> {
        val coords = mutableSetOf<Pair<Int, Int>>()
        for (coordinate in coordinates) {
            val coord = coordinate.split(",")
            val x = coord[0].toInt()
            val y = coord[1].toInt()
            coords.add(Pair(x, y))
        }
        return coords
    }

    fun getCoordinatesAndInstructions(input: List<String>): Pair<MutableSet<Pair<Int, Int>>, List<String>> {
        val coordinates = mutableListOf<String>()
        val instructions = mutableListOf<String>()
        for (line in input) {
            if (line.contains(" ")) {
                instructions.add(line.split(" ")[2])
            } else if (!line.contains(" ") && line.isNotEmpty()) {
                coordinates.add(line)
            }
        }
        val coordinateSet = getCoordinateSet(coordinates)
        return Pair(coordinateSet, instructions)
    }

    fun printCoordinates(coordinates: MutableSet<Pair<Int, Int>>) {
        var maxX = 0
        var maxY = 0
        for (coordinate in coordinates) {
            maxX = Math.max(coordinate.first, maxX)
            maxY = Math.max(coordinate.second, maxY)
        }
        maxX++
        maxY++
        val matrix = Array(maxY) { Array(maxX) { "." } }
        for (coordinate in coordinates) {
            matrix[coordinate.second][coordinate.first] = "#"
        }
        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                print("${matrix[i][j]} ")
            }
            println()
        }
    }

    fun horizontalFold(coordinates: MutableSet<Pair<Int, Int>>, foldValue: Int) {
        val newCoords = mutableSetOf<Pair<Int, Int>>()
        for (coordinate in coordinates) {
            if (coordinate.second > foldValue) {
                val diff = abs(coordinate.second - foldValue)
                newCoords.add(Pair(coordinate.first, foldValue - diff))
            }
        }
        coordinates.removeAll { it.second > foldValue }
        coordinates.addAll(newCoords)
    }

    fun verticalFold(coordinates: MutableSet<Pair<Int, Int>>, foldValue: Int) {
        val newCoords = mutableSetOf<Pair<Int, Int>>()
        for (coordinate in coordinates) {
            if (coordinate.first > foldValue) {
                val diff = abs(coordinate.first - foldValue)
                newCoords.add(Pair(foldValue - diff, coordinate.second))
            }
        }
        coordinates.removeAll { it.first > foldValue }
        coordinates.addAll(newCoords)
    }

    fun processInstructions(coordinateSet: MutableSet<Pair<Int, Int>>, instructions: List<String>) {
        for (instruction in instructions) {
            val inst = instruction.split("=")
            val axis = inst[0]
            val foldValue = inst[1].toInt()
            if (axis == "y") {
                horizontalFold(coordinateSet, foldValue)
            } else if (axis == "x") {
                verticalFold(coordinateSet, foldValue)
            }
        }
    }

    fun part1(input: List<String>) {
        val coordinatesAndInstructions = getCoordinatesAndInstructions(input)
        val coordinateSet = coordinatesAndInstructions.first
        val instructions = coordinatesAndInstructions.second
        processInstructions(coordinateSet, listOf(instructions.first()))
        printCoordinates(coordinateSet)
        println(coordinateSet.size)
    }

    fun part2(input: List<String>) {
        val coordinatesAndInstructions = getCoordinatesAndInstructions(input)
        val coordinateSet = coordinatesAndInstructions.first
        val instructions = coordinatesAndInstructions.second
        processInstructions(coordinateSet, instructions)
        printCoordinates(coordinateSet)
        println(coordinateSet.size)
    }

    val input = readInput("day13")
    part1(input)
    part2(input)
}
