package learnKotlin

// Fundamental Principle
//__________________________________________________________
// Design Towards Interfaces Rather Than Concerte Classes


//__________________________________________________________
// Corollary
// SOLID Principle
// Single Repsonility Design
// Classes/Types Are Open Extension But Close Modification

// Creates Pure Abstract Type
	// State Set if Phi
	// Operation Set = { fly, saveWorld }

interface Superpower {
	fun fly() 
	fun saveWorld()
}

inferface Dancing {
	fun dance()
}

inferface Singing {
	fun sing()
}

interface SuperpowerAndDancing : Superpower, Dancing {

}

// Single Unit
class Spiderman : SuperpowerAndDancing {
//class Spiderman : Superpower, Dancing {
	override fun fly() 		 { println("Fly Like Spiderman!") }
	override fun saveWorld() { println("Save World Like Spiderman!") }
	override fun dance()     { println("Dance Like Spiderman!");}
}

// Single Unit
class BADSuperman : Superpower {
	override fun fly() 		 { println("Fly Like Superman!") }
	override fun saveWorld() { println("Save World Like Superman!") }
}

// Single Unit
class Superman : Superpower {
	override fun fly() 		 { println("Fly Like Superman!") }
	override fun saveWorld() { println("Save World Like Superman!") }
	// override fun dance() { }
}

// Single Unit
class Heman : Superpower {
	override fun fly() 		 { println("Fly Like Heman!") }
	override fun saveWorld() { println("Save World Like Heman!") }
	// override fun dance() { }
}

// Single Unit
// Human Class NOT Changing
// Human Type is Polymporphic
// Human is Open For Extension, But Close For Modifcation

// Composite Design Patterns
// Substration is Possible

class Human {
	var power: Superpower? = null 
	// Can Assign Any Object Which Confirms to Superpower Contract
	
	fun fly() 		{ power?.fly() }
	fun saveWorld() { power?.saveWorld() }
}

fun main() {
	val h = Human()

	// Configuring Human
	h.power = Spiderman() // new Spiderman()

	h.fly()
	h.saveWorld()

	// h.power = BADSuperman()
	
	h.power = Superman()
	
	h.fly()
	h.saveWorld()

	h.power = Heman()
	h.fly()
	h.saveWorld()
}


