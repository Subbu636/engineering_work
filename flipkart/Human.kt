
package learnKotlin

open class Spiderman {
	open fun fly() 		{ println("Fly Like Spiderman!") }
	open fun saveWorld() { println("Save World Like Spiderman!") }
}

open class Superman {
	open fun fly() 		{ println("Fly Like Superman!") }
	open fun saveWorld() { println("Save World Like Superman!") }
}

open class Heman {
	open fun fly() 		{ println("Fly Like Heman!") }
	open fun saveWorld() { println("Save World Like Heman!") }
}

// Human Class Changing
class Human : Heman() { // class Human extends Superman
	override fun fly() 		{ super.fly() }
	override fun saveWorld() { super.saveWorld() }
}

fun main() {
	val h = Human()
	h.fly()
	h.saveWorld()
}

