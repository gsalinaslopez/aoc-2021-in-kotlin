fun main() {
    fun generateAdjList(input: List<String>): Map<String, ArrayList<String>> {
        val adjList = mutableMapOf<String, ArrayList<String>>()
        for (line in input) {
            val nodes = line.split('-')
            if (!adjList.containsKey(nodes[0])) {
                adjList[nodes[0]] = ArrayList()
            }
            adjList[nodes[0]]?.add(nodes[1])

            if (!adjList.containsKey(nodes[1])) {
                adjList[nodes[1]] = ArrayList()
            }
            adjList[nodes[1]]?.add(nodes[0])
        }
        return adjList
    }

    fun part1(input: List<String>) {
        val adjList = generateAdjList(input)

        var pathCount = 0
        val visited = mutableMapOf<String, Boolean>()
        fun dfs(node: String) {
            if (node == "end") {
                pathCount++
                return
            }
            visited[node] = true
            adjList[node]?.forEach { next ->
                if (!visited.containsKey(next)) {
                    dfs(next)
                } else {
                    if (next != "start" && next != "end" && next.first().isUpperCase()) {
                        dfs(next)
                    }
                }
            }
            visited.remove(node)
        }
        dfs("start")
        println(pathCount)
    }

    fun part2(input: List<String>) {
        val adjList = generateAdjList(input)
        val lower = adjList.keys.filter { it != "start" && it != "end" && it.first().isLowerCase() }
        println(lower)

        var pathCount = 0
        val visited = mutableMapOf<String, Boolean>()
        val pathMap = mutableMapOf<String, Boolean>()
        fun dfs(node: String, path: String, canRepeat: Pair<String, Int>) {
            if (node == "end") {
                if (!pathMap.containsKey(path)) {
                    pathCount++
                }
                pathMap[path] = true
                return
            }
            visited[node] = true
            adjList[node]?.forEach { next ->
                if (!visited.containsKey(next)) {
                    if (next == canRepeat.first) {
                        dfs(next, "$path,$next", Pair(canRepeat.first, canRepeat.second + 1))
                    } else {
                        dfs(next, "$path,$next", canRepeat)
                    }
                } else {
                    if (next != "start" && next != "end") {
                        if (next.first().isUpperCase()) {
                            dfs(next, "$path,$next", canRepeat)
                        } else if (next == canRepeat.first && canRepeat.second == 1) {
                            dfs(next, "$path,$next", Pair(canRepeat.first, canRepeat.second + 1))
                        }
                    }
                }
            }
            visited.remove(node)
        }
        for (low in lower) {
            dfs("start", "start", Pair(low, 0))
        }
        println(pathCount)
    }

    val input = readInput("day12")
    part1(input)
    part2(input)
}