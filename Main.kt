package minesweeper

val field = Array(9) { CharArray(9) { '0' } }
val display = Array(9) {CharArray(9) { '.' } }
val minesList = mutableListOf<IntArray>()
var mines = 0
var marks = 0
var cells = MutableList(81) { it }
var fieldCreated = false
var win = false
var lose = false

fun main() {
    startGame()
}

fun startGame() {
    setCountMines()
    play()
    gameOver()
}

fun setCountMines() {
    println("How many mines do you want on the field?")
    mines = readln().toInt()
    printDisplayField()
}

fun printDisplayField() {
    println()
    println(" │123456789│")
    println("—│—————————│")
    for (i in 1..9) {
        print("$i|")
        for (cell in display[i - 1]) {
            print(cell)
        }
        println("|")
    }
    println("—│—————————│")
}

fun play() {
    while (!fieldCreated) {
        firstTurn()
    }
    while (!win && !lose) {
        nextTurn()
    }
}

fun firstTurn() {
    println("Set/unset mines marks or claim a cell as free:")
    val (y, x, action) = readln().split(" ")
    val i = x.toInt() - 1
    val j = y.toInt() - 1
    when (action) {
        "free" -> createPlayingField(i, j)
        "mine" -> markMine(i, j)
    }
    printDisplayField()
}

fun nextTurn() {
    println("Set/unset mines marks or claim a cell as free:")
    val (y, x, action) = readln().split(" ")
    val i = x.toInt() - 1
    val j = y.toInt() - 1
    when (action) {
        "free" -> explore(i, j)
        "mine" -> markMine(i, j)
    }
    if (!lose) {
        checkWin()
    }
    printDisplayField()
}

fun createPlayingField(x: Int, y: Int) {
    cells.remove(x * 9 + y)
    repeat(mines) {
        val cell = cells.random()
        val i = cell / 9
        val j = cell % 9
        cells.remove(cell)
        field[i][j] = 'X'
        minesList.add(intArrayOf(i, j))
        addNumbers(i, j)
    }
    fieldCreated = true
    explore(x, y)
}

fun addNumbers(i: Int, j: Int) {
    if (i > 0) {
        increase(i - 1, j)
        if (j > 0) {
            increase(i - 1, j - 1)
        }
        if (j < 8) {
            increase(i - 1, j + 1)
        }
    }
    if (i < 8) {
        increase(i + 1, j)
        if (j > 0) {
            increase(i + 1, j - 1)
        }
        if (j < 8) {
            increase(i + 1, j + 1)
        }
    }
    if (j > 0) {
        increase(i, j - 1)
    }
    if (j < 8) {
        increase(i, j + 1)
    }
}

fun increase(i: Int, j: Int) {
    if (field[i][j].isDigit()) {
        field[i][j]++
    }
}

fun markMine(i: Int, j: Int) {
    display[i][j] = if (display[i][j] == '.') {
        marks--
        '*'
    } else {
        marks++
        '.'
    }
}

fun explore(i: Int, j: Int) {
    if (outBounds(i, j) || isExplored(i, j)) {
        return
    }
    display[i][j] = reveal(field[i][j])
    if (field[i][j] == 'X') {
        lose = true
        for (mine in minesList) {
            display[mine[0]][mine[1]] = 'X'
        }
        return
    } else if (field[i][j] == '0') {
        exploreAround(i, j)
    }
}

fun exploreAround(i: Int, j: Int) {
    explore(i - 1, j)
    explore(i - 1, j - 1)
    explore(i - 1, j + 1)
    explore(i + 1, j)
    explore(i + 1, j - 1)
    explore(i + 1, j + 1)
    explore(i, j - 1)
    explore(i, j + 1)
}

fun reveal(ch: Char): Char {
    return when (ch) {
        '0' -> '/'
        else -> ch
    }
}

fun checkWin(): Boolean {
    if (mines != marks) {
        return false
    }
    for (mine in minesList) {
        if (display[mine[0]][mine[1]] != '*') {
            return false
        }
    }
    return true
}

fun outBounds(i: Int, j: Int) = i < 0 || i > 8 || j < 0 || j > 8

fun isExplored(i: Int, j: Int) = display[i][j] != '.' && display[i][j] != '*'

fun gameOver() {
    if (win) {
        println("Congratulations! You found all the mines!")
    } else {
        println("You stepped on a mine and failed!")
    }
}