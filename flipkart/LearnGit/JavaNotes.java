
- Polymorphism : API Becomes Far More Flexible
	- You can always assign an object to a variable whose type is an implemented interface, or pass it to a method expecting such an interface.

Type of Type
	Definition: Type, Subtype, Supertype
___________________________________________________________________
	A type S is a supertype of the type T (the subtype) when any value of the subtype can be assigned to a variable of the supertype without a conversion.

In Java
___________________________________________________________________
	- However, Java is an object-oriented language where (just about) everything is an object. 
	- There are no function types in Java. 
		- Instead, functions are expressed as objects, instances of classes that implement a particular interface (Functional)
		- Lambda expressions give you a convenient syntax for creating such instances

	Java is Not Functional Language
	________________________________________________________________
	Because Lambda is Not Fundamental Buidling Block In Java

	Lamdba Syntax In Java
	________________________________________________________________

	(String first, String second) -> first.length() - second.length()
 	
 	Runnable task = () -> { for (int i = 0; i < 1000; i++) doWork(); }

 	Comparator<String> comp = (first, second) -> first.length() - second.length();

Functional Interfaces
________________________________________________________________

- There are many interfaces in Java that express actions,
such as Runnable or Comparator. Lambda expressions are compatible with these.

- You can supply a lambda expression whenever an object of an interface with a single abstract method is expected. Such an interface is called a functional interface
	
	SAM Interfaces
		Single Abstract Method Intefaces
		Expressed Elegantly Using Lambda Expression

 Arrays.sort(words, 
 	(first, second) -> first.length() - second.length() );

- Behind the scenes, the second parameter variable of the
Arrays.sort method receives an object of some class that implements Comparable Interface
Invoking the compare method on that object executes the body of the lambda expression. 

- The management of these objects and classes is completely
implementation-dependent and highly optimized.

- In most programming languages that support function literals, you can declare function types such as
(String, String) -> int, declare variables of those types, put functions into those variables, and invoke them. 

- In Java, there is only one thing you can do with a lambda expression: put it in a variable whose type is a functional interface, so that it is converted to an instance of that interface.

________________________________________________________________

- The Java API provides a large number of functional interfaces

	- One of them is
 
		public interface Predicate<T> {
		       boolean test(T t);
			- // Additional default and static methods
		}

- The ArrayList class has a removeIf method whose parameter is a Predicate. It is specifically designed for receiving a lambda expression.

- For example, the following statement removes all null values from an array list:
	
		 removeIf( Lambda ) 
		// Lambda is Object of Class Which Implements Predicate Interface
	
	list.removeIf(e -> e == null);

________________________________________________________________
. Operator [ Member Access Operator]
VS 
The :: operator [Reference Access Operator]
________________________________________________________________
The :: operator separates the method name from the name of a class or object. There are three variations:

1. Class::instanceMethod 
2. Class::staticMethod 
3. object::instanceMethod

- In the first case, the first parameter becomes the receiver of the method, and any other parameters are passed to the method.

- For example, String::compareToIgnoreCase is the same as 
		(x, y) -> x.compareToIgnoreCase(y).

- In the second case, all parameters are passed to the static method.

- The method expression Objects::isNull is equivalent to 
		x -> Objects.isNull(x).

- In the third case, the method is invoked on the given object, and the parameters are passed to the instance method.

	Therefore, System.out::println is equivalent to 
		x -> System.out.println(x).

Constructor Reference
________________________________________________________________
	Employee::new

Fundamentally [System Perceptive]
________________________________________________________________
	Lambda Can Be Used As Buidling Blocks
	To Simulate Whole System

Lambda Use Cases [Programmer Perceptive]
________________________________________________________________
- The point of using lambdas is deferred execution.

- There are many reasons for executing code later, such as: 
	• Running the code in a separate thread 
	• Running the code multiple times 
	• Running the code at the right point in an algorithm (for example, the comparison operation in sorting) 
	• Running the code when something happens (a button was clicked, data has arrived, and so on) 
	• Running the code only when necessary

	
	// Repeat Usage
	// () -> System.out.println("Hello, World!")
	// Lambda: Which doesn't take any argument and with body

	repeat(10, () -> System.out.println("Hello, World!") );

	// Repeat Implementation
	public static void repeat(int n, Runnable action) {
       for (int i = 0; i < n; i++) action.run();
    }

    // One More Example_____________________________
	
	public interface IntConsumer {
       void accept(int value);
   	}

 	public static void repeat(int n, IntConsumer action) {
       for (int i = 0; i < n; i++) action.accept(i);
    }

	//Write This Way : Elegance In Code
	repeat(10, i -> System.out.println("Countdown: " + (9 - i)));
	// Above Line Will Internally Generate Following Code

	// Or Do This Ways
	class Something implements IntConsumer {
		void accept(int value) {
			System.out.println(value);
		}
	}

	Something some = new Something();
	repeat(10, some) ;	


