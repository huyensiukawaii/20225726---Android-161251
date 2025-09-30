// Function to find the greatest common divisor (GCD)
fun gcd(a: Int, b: Int): Int {
    return if (b == 0) a else gcd(b, a % b)
}

class Fraction(var numerator: Int = 1, var denominator: Int = 1) {

    // Method to input a fraction from the keyboard
    fun input() {
        do {
            print("Enter numerator: ")
            numerator = readln().toInt()
            if (numerator == 0) {
                println("Numerator must be non-zero. Please enter again.")
            }
        } while (numerator == 0)

        do {
            print("Enter denominator: ")
            denominator = readln().toInt()
            if (denominator == 0) {
                println("Denominator must be non-zero. Please enter again.")
            }
        } while (denominator == 0)
    }

    // Method to print the fraction to the screen
    fun printFraction() {
        if (denominator == 1) {
            print("$numerator")
        } else {
            print("$numerator/$denominator")
        }
    }

    // Method to simplify the fraction
    fun simplify() {
        val commonDivisor = gcd(Math.abs(numerator), Math.abs(denominator))
        numerator /= commonDivisor
        denominator /= commonDivisor
        if (denominator < 0) {
            numerator = -numerator
            denominator = -denominator
        }
    }

    // Method to compare with another fraction
    fun compareTo(other: Fraction): Int {
        val value1 = this.numerator.toDouble() / this.denominator
        val value2 = other.numerator.toDouble() / other.denominator
        return when {
            value1 < value2 -> -1
            value1 > value2 -> 1
            else -> 0
        }
    }

    // Method to calculate the sum with another fraction
    fun add(other: Fraction): Fraction {
        val sumNumerator = this.numerator * other.denominator + other.numerator * this.denominator
        val sumDenominator = this.denominator * other.denominator
        val result = Fraction(sumNumerator, sumDenominator)
        result.simplify()
        return result
    }
}

fun main() {
    print("Enter the number of fractions: ")
    val n = readln().toInt()

    val fractionArray = Array(n) { Fraction() }

    // 1. Input an array of fractions from the keyboard
    println("--- INPUT FRACTION ARRAY ---")
    for (i in fractionArray.indices) {
        println("Enter fraction ${i + 1}:")
        fractionArray[i].input()
    }

    // 2. Print the newly entered fraction array
    println("\n--- NEWLY ENTERED FRACTION ARRAY ---")
    fractionArray.forEach {
        it.printFraction()
        print("  ")
    }
    println()

    // 3. Simplify the elements of the array and print the result
    println("\n--- ARRAY AFTER SIMPLIFICATION ---")
    fractionArray.forEach { it.simplify() }
    fractionArray.forEach {
        it.printFraction()
        print("  ")
    }
    println()

    // 4. Calculate the sum of fractions and print the result
    println("\n--- SUM OF FRACTIONS ---")
    var sum = Fraction(0, 1) // Initialize sum as 0/1
    fractionArray.forEach { sum = sum.add(it) }
    print("The sum of fractions is: ")
    sum.printFraction()
    println()

    // 5. Find the largest fraction and print the result
    println("\n--- LARGEST FRACTION ---")
    var maxFraction = fractionArray[0]
    for (i in 1 until fractionArray.size) {
        if (fractionArray[i].compareTo(maxFraction) > 0) {
            maxFraction = fractionArray[i]
        }
    }
    print("The largest fraction is: ")
    maxFraction.printFraction()
    println()

    // 6. Sort the array in descending order and print the result
    println("\n--- ARRAY SORTED IN DESCENDING ORDER ---")
    fractionArray.sortWith(Comparator { fr1, fr2 -> fr2.compareTo(fr1) })
    fractionArray.forEach {
        it.printFraction()
        print("  ")
    }
    println()
}
