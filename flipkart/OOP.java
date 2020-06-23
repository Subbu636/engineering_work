package learnJava;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.text.NumberFormat;

class Cal {
    public static void playWithDateAndTime(String [] args) {
        LocalDate date = LocalDate.now().withDayOfMonth(1);
        int month;
        if (args.length >= 2) {        
            month = Integer.parseInt(args[0]);
            int year = Integer.parseInt(args[1]);
            date = LocalDate.of(year, month, 1);
        } else {
            month = date.getMonthValue();
        }
        
        System.out.println(" Mon Tue Wed Thu Fri Sat Sun");
        DayOfWeek weekday = date.getDayOfWeek();
        int value = weekday.getValue(); // 1 = Monday, ... 7 = Sunday
        for (int i = 1; i < value; i++) 
            System.out.print("    ");
        while (date.getMonthValue() == month) {
            System.out.printf("%4d", date.getDayOfMonth());
            date = date.plusDays(1);
            if (date.getDayOfWeek().getValue() == 1) 
                System.out.println();
        }
        if (date.getDayOfWeek().getValue() != 1) 
           System.out.println();
    }
}

//import java.util.ArrayList;

class ReferenceDemo {
    public static void playWithReferences() {
        ArrayList<String> friends = new ArrayList<>();
            // friends is empty
        friends.add("Peter");
            // friends has size 1
        ArrayList<String> people = friends;
            // Now people and friends refer to the same object
        people.add("Paul");
        System.out.println(friends);
        System.out.println(people);
    }
}

// Employee Type
// 1. Encapsulation - State is Hidden
class Employee {
    private static int lastID = 0;
    private int id;
    private String name;  	// State -> RANGE
    private double salary; // State -> RANGE
     
     //Following Are Behaviours -> OPERATIONS

	// Bind saySomething Binded with Type means Type Member
    public static void saySomething() {
    	System.out.println("I am Employee Type!");
    }
    
    // Instance Methods means Instance Member
    public Employee(String name, double salary) { // Constructor
    	this();
        this.name = name; // What is this?
        this.salary = salary;
    }

	// Instance Methods means Instance Member
    public Employee(double salary) {
    	this();
        // name already set to ""
        this.salary = salary;
    }        
    
    // Instance Methods means Instance Member
    public Employee(String name) {
    	this();
        // salary automatically set to zero
        this.name = name;
    } 
    
    // Instance Methods means Instance Member
    public Employee() {
    	// this("", 0);
    	lastID++;
    	this.id = lastID;
    }

    // Instance Methods means Instance Member
    public void raiseSalary(double byPercent) {
        double raise = salary * byPercent / 100;
        this.salary += raise;    
    }

    // Instance Methods means Instance Member
    public int getID() {
        return this.id;
    }
    
    // Instance Methods means Instance Member
    public String getName() {
        return this.name;
    }
    
    // Instance Methods means Instance Member
    public double getSalary() {
        return this.salary;
    }
}

class Something {
    private static int objectCount = 0;
    private int value = 10;
    // Instance Methods means Instance Member

    public Something() { // Constructor
    	objectCount++;
    	value++;
    }

    public int getObjectCount() {
    	return objectCount;
    }

    public int getValue() {
    	return value;
    }
}

// import java.util.Random;

// Must Access Type Member Varibles In Type Member Function Only

// Factory Class/Type
class RandomNumbers {
    // Type Member Variable
    // State
    private static Random generator = new Random();
    
    // Type Member Function
    // Factory Methods        // Configuration
    //      Output Based On Configuration
    public static int nextInt(int low, int high) {
        return low + generator.nextInt(high - low + 1);
            // Ok to access the static generator variable
    }
}

// class Item {
//     String description;
//     int quantity;
//     double unitPrice;

//     double price() { return quantity * unitPrice; }
//     public String toString() {
//         return quantity + " x " + description + " @ $" + unitPrice + " each";
//     }
// }

// Nested Classes - Type Inside Type

class Invoice {
    // Creating It As Nested Class
    // private static class Item {
    private class Item {
        String description;
        int quantity;
        double unitPrice;

        double price() { return quantity * unitPrice; }
        public String toString() {
            return quantity + " x " + description + " @ $" + unitPrice + " each";
        }
    }

    private ArrayList<Item> items = new ArrayList<>();

    public void addItem(String description, int quantity, double unitPrice) {
        Item newItem = new Item();
        newItem.description = description;
        newItem.quantity = quantity;
        newItem.unitPrice = unitPrice;
        items.add(newItem);
    }

    public void print() {
        double total = 0;
        for (Item item: items) {
            System.out.println(item);
            total += item.price();
        }
        System.out.println("Invoice Total: " + total);
    }
}

// Nested Classes/Types
//      When Some Type Doesn't Make Sense as a Independent

// class CardDeck
//      class CardSuits


public class OOP {
	
	// Behaviour Driven Programming	
	public static void playWithEmployee() {
		Employee emp = new Employee("Ram Singh", 25000);
		
		System.out.println(emp.getName()); 	 // Sending Msg to emp
		System.out.println(emp.getSalary()); // Sending Msg to emp
		emp.raiseSalary(20000);
		System.out.println(emp.getSalary()); // Sending Msg to emp

		Employee emp1 = new Employee("Shyam Singh", 25000);
		System.out.println(emp1.getName()); 	 // Sending Msg to emp

		Employee emp2 = new Employee(25000);
		Employee emp3 = new Employee("Ding Dong");

		// No Arguments Constructor : Default Constructor
		Employee e = new Employee();
	}

	public static void playWithEmployeeOnceMore() {
		Employee emp0 = new Employee("Ram Singh", 25000);
		
		System.out.println(emp0.getID()); 	 // Sending Msg to emp
		System.out.println(emp0.getName()); 	 // Sending Msg to emp
		System.out.println(emp0.getSalary()); // Sending Msg to emp

		Employee emp1 = new Employee("Shyam Singh", 25000);
		System.out.println(emp1.getID()); 	 // Sending Msg to emp
		System.out.println(emp1.getName()); 	 // Sending Msg to emp
		System.out.println(emp1.getSalary()); // Sending Msg to emp
		
		Employee emp2 = new Employee(25000);
		System.out.println(emp2.getID()); 	 // Sending Msg to emp
		Employee emp3 = new Employee("Ding Dong");
		System.out.println(emp3.getID()); 	 // Sending Msg to emp

		// No Arguments Constructor : Default Constructor
		Employee emp4 = new Employee();
		System.out.println(emp4.getID()); 	 // Sending Msg to emp
	}

	public static void playWithTypeMembers() {
		// Sending Message To Type
		Employee.saySomething();
	}

	public static void playWithSomething() {
		Something some1 = new Something();
		System.out.println(some1.getObjectCount());
		System.out.println(some1.getValue());

		Something some2 = new Something();
		System.out.println(some2.getObjectCount());
		System.out.println(some2.getValue());

		Something some3 = new Something();
		System.out.println(some3.getObjectCount());
		System.out.println(some3.getValue());

		Something some4 = new Something();
		System.out.println(some4.getObjectCount());
		System.out.println(some4.getValue());
	}

    public static void playWithRandomNumbers() {
        // Sending Messages Type
        // Using RandomNumbers as Factory
        //      Take Configuration and Give Me Int
        int dieToss = RandomNumbers.nextInt(1, 6);

        System.out.println(dieToss);

        // Sending Messages Type        
        // NumberFormat Act As Factory
            // Producing Various Products Based Configruation
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
        NumberFormat percentFormatter = NumberFormat.getPercentInstance();
        
        double x = 0.1;
        System.out.println(currencyFormatter.format(x)); // Prints $0.1
        System.out.println(percentFormatter.format(x)); // Prints 10%
    }

    public static void playWithInovices() {
        Invoice invoice = new Invoice();
        invoice.addItem("Blackwell Toaster", 2, 24.95);
        invoice.addItem("ZapXpress Microwave Oven", 1, 49.95);
        invoice.print();
    }

	public static void main(String[] args) {
		System.out.println("\nFunction : Cal.playWithDateAndTime()");
		Cal.playWithDateAndTime(args);

		System.out.println("\nFunction : ReferenceDemo.playWithReferences()");
		ReferenceDemo.playWithReferences();

		System.out.println("\nFunction : playWithEmployee");
		playWithEmployee();

		System.out.println("\nFunction : playWithTypeMembers");
		playWithTypeMembers();

		System.out.println("\nFunction : playWithEmployeeOnceMore");
		playWithEmployeeOnceMore();

		System.out.println("\nFunction : playWithSomething");
		playWithSomething();

		System.out.println("\nFunction : playWithRandomNumbers");
        playWithRandomNumbers();

		System.out.println("\nFunction : playWithInovices");
        playWithInovices();

		// System.out.println("\nFunction : ");
		// System.out.println("\nFunction : ");
		// System.out.println("\nFunction : ");
		// System.out.println("\nFunction : ");
		// System.out.println("\nFunction : ");
		// System.out.println("\nFunction : ");
		// System.out.println("\nFunction : ");
        // System.out.println("\nFunction : ");
        // System.out.println("\nFunction : ");
        // System.out.println("\nFunction : ");
        // System.out.println("\nFunction : ");
	}
}

