package learnKotlin

val baseValue= 100
// Encapsulation
fun sum(x: Int, y: Int ) : Int {  return baseValue + x + y  }

// Encapsulation
fun sub(x: Int, y: Int ) : Int {  return baseValue + x - y  }

// Encapsulation
fun calculator(x: Int, y: Int, operation: (Int, Int) -> Int): Int {
	return operation(x, y) ;
}

fun main() {
	val a = 10
	val b = 20
	
	var result = sum(a, b)
	println(result)

	// What is Type of Something?
	val something: (Int, Int) -> Int = ::sum // Reference To Sum Function
	
	//Sum Function Is Treated As Value of Type (Int, Int) -> Int
	result = something(a, b)
	println(result)

	// Lambda Expression
	val add: (Int, Int) -> Int = { x: Int, y: Int -> x + y }
	result = add(a, b)
	println(result)
}
