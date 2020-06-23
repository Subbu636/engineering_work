
package learnJava;

// Design By Contracts or Conventions
// Brings Trust In System Design

import java.util.Arrays;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Objects;
import static java.util.Comparator.*;
import java.util.Random;
import java.util.Objects;

// IntSequence is Type S
interface IntSequence {
    boolean hasNext();
    int next();
}

// DigitSequence is Type T: Subtype of Type S
class DigitSequence implements IntSequence {
    private int number;

    public DigitSequence(int n) { number = n;  }
    public boolean hasNext() 	{ return number != 0; }

    public int next() {
        int result = number % 10;
        number /= 10;
        return result;
    }
    
    public int rest() {  return number;  }
}

// SquareSequence is Type T: Subtype of Type S
class SquareSequence implements IntSequence {
    private int i;

    public boolean hasNext() { return true; }
    
    public int next() {
        i++;
        return i * i;
    }
}

// Hence DigitSequence and SquareSequence Object Can be assigned to
// IntSequence Type

// class CubeSequence implements IntSequence {
//     public boolean hasNext() { return true; }
//     public int next() { return; }
// }

class InterfacesDemo {	
	// Polymorphic In Nature Achieved By
	// Design Towards Abstrat Types
	public static double average(IntSequence seq, int n) {
		int count = 0;
		double sum = 0;
		while(seq.hasNext() && count < n) {
			count++;
			sum += seq.next();
		}
		return count == 0 ? 0 : sum / count;
	}
}

//________________________________________________________
// Abstract Type With Default Implementation
// Multiple Inheritance
// 		In Case Conflict Programmer Has Resolve

interface Identified {
    default int getId() { return Math.abs(hashCode()); } 
}

interface Person {
    String getName();
    default int getId() { return 0; }
}

class Employee implements Person, Identified {
    private String name;
    private double salary;
        
    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    public void raiseSalary(double byPercent) {
        double raise = salary * byPercent / 100;
        salary += raise;    
    }
    
    public String getName() 	{ return name; }
    public double getSalary() 	{ return salary; }
    public int getId() { return Identified.super.getId(); }

// Interfaces.java:68: error: class Employee inherits unrelated defaults for getId() from types Person and Identified
// class Employee implements Person, Identified {
// ^
// 1 error

}

//____________________________________________________________

class ComparableEmployee implements Comparable<Employee> {
    private String name;
    private double salary;
        
    public ComparableEmployee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }
    
    public ComparableEmployee(double salary) {
        this.name = "";
        this.salary = salary;
    }        
    
    public ComparableEmployee(String name) {
        // salary automatically set to zero
        this.name = name;
    } 
    
    public ComparableEmployee() {
        this("", 0);
    }

    public void raiseSalary(double byPercent) {
        double raise = salary * byPercent / 100;
        salary += raise;    
    }
    
    public String getName()   { return name; }
    public double getSalary() { return salary; }
    
    public int compareTo(Employee other) {
        return Double.compare(salary, other.getSalary());
    }
}

//________________________________________________________

class HelloTask implements Runnable {
    public void run() {
        for (int i = 0; i < 1000; i++) {
            System.out.println("Hello, World!");
        }
    }
}

class RunnableDemo {
    public static void main(String[] args) {
        Runnable task = new HelloTask();
        Thread thread = new Thread(task);
        thread.start();
        System.out.println("Thread started");
    }
}


//________________________________________________________
// import java.util.Arrays;
// import java.util.Comparator;

class LengthComparator implements Comparator<String> {
    public int compare(String first, String second) {
        return first.length() - second.length();
    }
}

class SortDemo {
    public static void doSorting() {
        String[] friends = { "Peter", "Paul", "Mary" };
        Arrays.sort(friends); 
        // friends is now ["Mary", "Paul", "Peter"]
        System.out.println(Arrays.toString(friends));
        
        friends = new String[] { "Peter", "Paul", "Mary" };
        Arrays.sort(friends, new LengthComparator());
        System.out.println(Arrays.toString(friends));
    }
}

//________________________________________________________

// import java.util.ArrayList;

// Reference To Employee Constructor
// 		Employee::new

class ConstructorReferenceDemo {
    public static void constructorReference() {
        ArrayList<String> names = new ArrayList<>();
        names.add("Peter");
        names.add("Paul");
        names.add("Mary");
        Employee[] employees = names.stream().map(Employee::new).toArray(Employee[]::new);
        for (Employee e : employees) System.out.println(e.getName());
    }
}

//________________________________________________________

// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.Objects;

// Reference To String Class Method
//		String::compareToIgnoreCase
//    	Objects::isNull
//		System.out::println

class MethodReferenceDemo {
    public static void main(String[] args) {
        String[] strings = { "Mary", "had", "a", "little", "lamb" };
        Arrays.sort(strings, String::compareToIgnoreCase);
        System.out.println(Arrays.toString(strings));
        ArrayList<String> list = new ArrayList<>(Arrays.asList("Malfoy", "Crabbe", "Goyle", null));
        list.removeIf(Objects::isNull);
        list.forEach(System.out::println);
    }
}

//________________________________________________________

// import java.util.Arrays;
// import java.util.Comparator;
// import static java.util.Comparator.*;

class Person {
    private String first;
    private String middle;
    private String last;

    public Person(String first, String middle, String last) {
        this.first = first;
        this.middle = middle;
        this.last = last;
    }

    public Person(String first, String last) {
        this.first = first;
        this.last = last;
    }
    
    public String getFirstName() {
        return first;
    }
    
    public String getMiddleName() {
        return middle;
    }
    
    public String getLastName() {
        return last;
    }
    
    public String getName() {
        if (middle == null) {
            return first + " " + last;
        }
        else {
            return first + " " + middle + " " + last;
        }
    }
    
    public String toString() {
        return getName();
    }
}


class ComparatorDemo {
    public static void comparingPerson() {
        Person[] people = {
                new Person("George", "Washington"),
                new Person("John", "Adams"),
                new Person("Thomas", "Jefferson"),
                new Person("James", "Madison"),
                new Person("James", "Monroe"),
                new Person("John", "Quincy", "Adams"),
                new Person("Andrew", "Jackson"),
                new Person("Martin", "van Buren"),
                new Person("William", "Henry", "Harrison"),
                new Person("John", "Tyler"),
                new Person("James", "Knox", "Polk"),
                new Person("Zachary", "Taylor"),
                new Person("Millard", "Fillmore"),
                new Person("Franklin", "Pierce"),
                new Person("James", "Buchanan"),
                new Person("Abraham", "Lincoln"),
                new Person("Andrew", "Johnson"),
                new Person("Ulysses", "S.", "Grant"),
                new Person("Rutherford", "Birchard", "Hayes"),
                new Person("James", "Abram", "Garfield"),
                new Person("Grover", "Cleveland"),
                new Person("Benjamin", "Harrison"),
                new Person("Grover", "Cleveland"),
                new Person("William", "McKinley"),
                new Person("Theodore", "Roosevelt"),
                new Person("William", "Howard", "Taft"),
                new Person("Woodrow", "Wilson"),
                new Person("Warren", "Gamaliel", "Harding"),
                new Person("Calvin", "Coolidge"),
                new Person("Herbert", "Hoover"),
                new Person("Franklin", "Delano", "Roosevelt"),
                new Person("Harry", "S.", "Truman"),
                new Person("Dwight", "David", "Eisenhower"),
                new Person("John", "Fitzgerald", "Kennedy"),
                new Person("Lyndon", "Baines", "Johnson"),
                new Person("Richard", "Mulhouse", "Nixon"),
                new Person("Gerald", "Ford"),
                new Person("James", "Earl", "Carter"),
                new Person("Ronald", "Reagan"),
                new Person("George", "Herbert Walker", "Bush"),
                new Person("William", "Jefferson", "Clinton"),
                new Person("George", "Walker", "Bush"),
                new Person("Barack", "Hussein", "Obama")
        };
                
        Arrays.sort(people, Comparator.comparing(Person::getName));
        System.out.println(Arrays.toString(people));
        
        Arrays.sort(people,
                Comparator.comparing(Person::getLastName)
                .thenComparing(Person::getFirstName));
        System.out.println(Arrays.toString(people));
        
        Arrays.sort(people, Comparator.comparing(Person::getName,
                (s, t) -> s.length() - t.length() ));
        
        Arrays.sort(people, Comparator.comparingInt( p -> p.getName().length() ));
        System.out.println(Arrays.toString(people));
        
        Arrays.sort(people, comparing(Person::getMiddleName,
                nullsFirst(naturalOrder())));
        System.out.println(Arrays.toString(people));

        Arrays.sort(people, comparing(Person::getName,
                reverseOrder()));
        System.out.println(Arrays.toString(people));
    }
}

//________________________________________________________

// import java.util.Random;

class AnonymousClassDemo {
    private static Random generator = new Random();

    public static IntSequence randomInts(int low, int high) {
        
        // These Following Code Get Converted
        // Anonomous Classes
        return new IntSequence() {
            public int next() { return low + generator.nextInt(high - low + 1); }
            public boolean hasNext() { return true; }
        };

        // START - Compiler Generated Code
        class IntSequenceClass implements IntSequence {
            public int next() { return low + generator.nextInt(high - low + 1); }
            public boolean hasNext() { return true; }            
        }

        IntSequenceClass tempObject = IntSequenceClass()
        // END - Compiler Generated Code
    }

    public static void main(String[] args) {
        IntSequence dieTosses = randomInts(1, 6);
        for (int i = 0; i < 10; i++) System.out.println(dieTosses.next());
    }
}

//_______________________________________________________________________________________

interface IntSequence {
    boolean hasNext();
    int next();
}

class LocalClassDemo {
    private static Random generator = new Random();

    public static IntSequence randomInts(int low, int high) {
        // Type Localisation
        class RandomSequence implements IntSequence {
            public int next() { return low + generator.nextInt(high - low + 1); }
            public boolean hasNext() { return true; }
        }

        return new RandomSequence();
    }

    public static void doWorkObject() {
        IntSequence dieTosses = randomInts(1, 6);
        for (int i = 0; i < 10; i++) System.out.println(dieTosses.next());
    }
}

//________________________________________________________

public interface Named {
    default String getName() { return ""; }
}

public abstract class Person {
    private String name;

    public Person(String name) { this.name = name; }
    public final String getName() { return name; }

    public abstract int getId();
}

class Student extends Person implements Named {
    private int id;

    public Student(String name, int id) { super(name); this.id = id; }
    public int getId() { return id; }
}

class StudentDemo {
    public static void doAbstractClassesAndInterfaces() {
        Person p = new Student("Fred", 1729); // OK, a concrete subclass
        System.out.println(p.getName());
        Student s = (Student) p;
        System.out.println(s.getName());
        Named n = s;
        System.out.println(n.getName());
    }
}

//________________________________________________________

class Worker {
    public void work() { 
        for (int i = 0; i < 100; i++) System.out.println("Working"); 
    }
}

class ConcurrentWorker extends Worker {
    public void work() { 
        Thread t = new Thread(super::work);
        t.start();
    }
}

//________________________________________________________

// import java.util.Objects;

class Item extends Object {
    private String description;
    private double price;
        
    public Item(String description, double price) {
        this.description = description;
        this.price = price;
    }

    public boolean equals(Object otherObject) {
        // A quick test to see if the objects are identical
        if (this == otherObject) return true;

        // Must return false if the explicit parameter is null
        if (otherObject == null) return false;

        // Check that otherObject is a Item
        if (getClass() != otherObject.getClass()) return false;

        // Test whether the instance variables have identical values
        Item other = (Item) otherObject;
        return Objects.equals(description, other.description)
            && price == other.price;
    }
    
    public int hashCode() {
        return Objects.hash(description, price);
    }
}

//________________________________________________________
// import java.util.Objects;

class DiscountedItem extends Item {
    private double discount;

    public DiscountedItem(String description, double price, double discount) {
        super(description, price);
        this.discount = discount;
    }

    public boolean equals(Object otherObject) {
        if (!super.equals(otherObject)) return false;
        DiscountedItem other = (DiscountedItem) otherObject;
        return discount == other.discount;
    }
    
    public int hashCode() {
        return Objects.hash(super.hashCode(), discount);
    }
}

//________________________________________________________

class Employee {
    private String name;
    private double salary;
        
    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    public void raiseSalary(double byPercent) {
        double raise = salary * byPercent / 100;
        salary += raise;    
    }
    
    public final String getName() {
        return name;
    }
    
    public double getSalary() {
        return salary;
    }
    
    public String toString() {
        return getClass().getName() + "[name=" + name
            + ",salary=" + salary + "]";
    }    
}


// Manager is a Type, Type of This Type is Class<?>
// Type of This Type is Class<?>

class Manager extends Employee {
    private double bonus;
    
    public Manager(String name, double salary) {
        super(name, salary);
        bonus = 0;
    }
    
    public void setBonus(double bonus) {
        this.bonus = bonus;
    }
    
    public double getSalary() { // Overrides superclass method
        return super.getSalary() + bonus;
    }
    
    public String toString() {
        return super.toString() + "[bonus=" + bonus + "]";
    }
}    

//________________________________________________________

enum Size {
    SMALL("S"), MEDIUM("M"), LARGE("L"), EXTRA_LARGE("XL");

    private String abbreviation;

    Size(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() { return abbreviation; }
}

class EnumDemo {
    public static void doWithEnums() {
        Size notMySize = Size.valueOf("SMALL");
        System.out.println(notMySize);
        
        for (Size s : Size.values()) { System.out.println(s); }
        
        System.out.println(Size.MEDIUM.ordinal());
    }
}

//________________________________________________________

// Reflection APIs
    // We Can Ask Object Itself: 
    //    Tell Me What's Your Type

class ClassDemo {
    public static void doClassNames() throws ReflectiveOperationException {
        Object obj = System.out;
        Class<?> cl = obj.getClass();
        
        System.out.println("This object is an instance of " + cl.getName());

        String className = "java.util.Scanner";
        cl = Class.forName(className);
            // An object describing the java.util.Scanner class
        cl = java.util.Scanner.class;
        System.out.println(cl.getName());
        Class<?> cl2 = String[].class; // Describes the array type String[]
        System.out.println(cl2.getName());
        System.out.println(cl2.getCanonicalName());
        Class<?> cl3 = Runnable.class; // Describes the Runnable interface 
        System.out.println(cl3.getName());
        Class<?> cl4 = int.class; // Describes the int type
        System.out.println(cl4.getName());
        Class<?> cl5 = void.class; // Describes the void type
        System.out.println(cl5.getName());
    }
}

//________________________________________________________

//________________________________________________________

public class Interfaces {
	
	public static void playWithInterfacesDemo() {
		SquareSequence squares = new SquareSequence();
		double avg = InterfacesDemo.average(squares, 100);
		System.out.println("Average of First 100 Squares: ");

		IntSequence digits = new DigitSequence(1729);
		while (digits.hasNext()) System.out.println(digits.next() + " ");
		System.out.println();

		digits = new DigitSequence(1729);
		avg = InterfacesDemo.average(digits, 100);
		System.out.println("Average of Digits: " + avg);
	}

	public static void playWithEmployee() {
		Employee emp = new Employee("Sambha!", 25000);
		System.out.println(emp.getName());
		System.out.println(emp.getSalary());
		System.out.println(emp.getId());
	}

	public static void playWithSortDemo() {
		SortDemo.doSorting();
	}

	public static void main(String[] args) {
		
		System.out.println("\n Function : playWithInterfacesDemo");
		playWithInterfacesDemo();

		System.out.println("\n Function : playWithEmployee");
		playWithEmployee();

		System.out.println("\n Function : playWithSortDemo");
		playWithSortDemo();

		// System.out.println("\n Function : ");
		// System.out.println("\n Function : ");
		// System.out.println("\n Function : ");
		// System.out.println("\n Function : ");
		// System.out.println("\n Function : ");
		// System.out.println("\n Function : ");
		// System.out.println("\n Function : ");
		// System.out.println("\n Function : ");
	}
}
