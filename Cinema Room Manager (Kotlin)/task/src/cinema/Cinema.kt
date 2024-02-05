package cinema

fun main() {

    val rows = userInput("Enter the number of rows:").toInt()
    val seats = userInput("Enter the number of seats in each row:").toInt()

    val cinema = MutableList(rows) { MutableList(seats) { "S" } }

    generateSequence {
        println("1. Show the seats")
        println("2. Buy a ticket")
        println("3. Statistics")
        println("0. Exit")
        readln()
    }
        .takeWhile { it != "0" }
        .forEach {
            when (it) {
                "1" -> printCinema(cinema)
                "2" -> reserveSeat(cinema)
                "3" -> statistics(cinema)
            }
        }
}

fun printCinema(cinema: MutableList<MutableList<String>>) {
    println("Cinema:")
    println("  " + (1..cinema.first().size).joinToString(" "))
    cinema.indices.forEach {
        println("${it + 1} " + cinema[it].joinToString(" "))
    }
}

fun reserveSeat(cinema: MutableList<MutableList<String>>) {
    val rows = cinema.size
    val seats = cinema.first().size

    val (row, seat) = generateSequence {
        val row = userInput("Enter a row number:").toInt()
        val seat = userInput("Enter a seat number in that row:").toInt()
        Pair(row, seat)
    }
        .dropWhile {
            if (it.first !in 1..rows || it.second !in 1..seats) {
                println("Wrong input!")
                true
            } else if (cinema[it.first - 1][it.second - 1] == "B") {
                println("That ticket has already been purchased!")
                true
            } else {
                false
            }
        }
        .take(1)
        .single()

    cinema[row - 1][seat - 1] = "B"

    val price = ticketPrice(rows, seats, row)
    println("Ticket price: $${price}")
}

fun ticketPrice(rows: Int, seats: Int, row: Int) =
    if (rows * seats <= 60 || row <= rows / 2) {
        10
    } else {
        8
    }

fun totalPrice(rows: Int, seats: Int) =
    if (rows * seats <= 60) {
        rows * seats * 10
    } else {
        val half = rows / 2
        half * seats * 10 + (rows - half) * seats * 8
    }

fun statistics(cinema: MutableList<MutableList<String>>) {
    val rows = cinema.size
    val seats = cinema.first().size

    val purchased = cinema.flatten().count { it == "B" }
    val percentage = 100.0 * purchased / (rows * seats)
    val currentIncome = cinema.flatMapIndexed { row, list ->
        list.map { IndexedValue(row, it) }
    }.filter { it.value == "B" }.sumOf { ticketPrice(rows, seats, it.index + 1) }
    val totalIncome = totalPrice(rows, seats)

    println("Number of purchased tickets: $purchased")
    println("Percentage: %.2f%%".format(percentage))
    println("Current income: $$currentIncome")
    println("Total income: $$totalIncome")
}

fun userInput(text: String): String = println(text).run { readln() }
