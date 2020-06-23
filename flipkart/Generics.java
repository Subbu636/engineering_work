
package learnJava;

import java.util.ArrayList;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.ArrayList;

// Generic Class
class Entry<K, V> {
    private K key;
    private V value;
    
    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }
    
    public K getKey() { return key; }
    public V getValue() { return value; }
}

//___________________________________________________________________

class ArrayUtil {
    // Generic Function
    public static <T> void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static <T> T[] swap(int i, int j, T... values) {
        T temp = values[i];
        values[i] = values[j];
        values[j] = temp;
        return values;
    }
}

//___________________________________________________________________

class Closeables {
    // Generic Type Bounds
    public static <T extends AutoCloseable> void closeAll(ArrayList<T> elems) throws Exception {
        for (T elem : elems) elem.close();
    }
}

//___________________________________________________________________
// import java.util.function.Predicate;

class ArrayUtilOnceMore {
    public static <T> void printAll(T[] elements, Predicate<? super T> filter) {
        for (T e : elements) 
            if (filter.test(e))
                System.out.println(e.toString());
    }
}

//___________________________________________________________________
// import java.util.ArrayList;
// import java.util.function.Predicate;

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
}

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
} 

class Employees {
    public static void printNames(ArrayList<? extends Employee> staff) {
        for (int i = 0; i < staff.size(); i++) {
            Employee e = staff.get(i);
            System.out.println(e.getName());
        }
    }
    
    public static void printAll1(Employee[] staff, Predicate<Employee> filter) {
        for (Employee e : staff) 
            if (filter.test(e))
                System.out.println(e.getName());
    }

    public static void printAll2(Employee[] staff, Predicate<? super Employee> filter) {
        for (Employee e : staff) 
            if (filter.test(e))
                System.out.println(e.getName());
    }    
}

//___________________________________________________________________
//___________________________________________________________________
//___________________________________________________________________


public class Generics {
    public static void playWithArrayUtil() {
        String[] friends = { "Peter", "Paul", "Mary" };
        
        for( String friend: friends) System.out.println(friend);
        ArrayUtil.swap(friends, 0, 1);
        for( String friend: friends) System.out.println(friend);

        // Uncomment to see error message
        // Double[] result = Arrays.swap(0, 1, 1.5, 2, 3);
    }

    public static void playWithClosable() throws Exception {
        PrintStream p1 = new PrintStream("/tmp/1");
        PrintStream p2 = new PrintStream("/tmp/2");
        ArrayList<PrintStream> ps = new ArrayList<>();
        ps.add(p1);
        ps.add(p2);
        Closeables.closeAll(ps);        
        
        // ArrayList<String> strings = new ArrayList<>();
        // strings.add("Ding");
        // strings.add("Donng");
        // Closeables.closeAll(strings);        
    }

    public static void playWithEmployees() {
        Employee[] employees = {
          new Employee("Fred", 50000),
          new Employee("Wilma", 60000),
          new Employee("Gabaar Singh", 5000000),
          new Employee("Sambha", 6000000),
          new Employee("Thakur", 6000000)
        };
        Employees.printAll1(employees, e -> e.getSalary() > 100000);
        Employees.printAll2(employees, e -> e.getSalary() > 100000);
        
        // Predicate<Object> evenLength = e -> e.toString().length() % 2 == 0; 
        // Employees.printAll1(employees, evenLength);
        Employees.printAll2(employees, evenLength);
    }

    public static void main(String[] args) {
    	
    	System.out.println("\nFunction: playWithArrayUtil");
    	playWithArrayUtil();

    	System.out.println("\nFunction: playWithClosable");
            try { playWithClosable(); } 
            catch(Exception ex) {  }

    	System.out.println("\nFunction: playWithEmployees");
        playWithEmployees();

  //   	System.out.println("\nFunction: ");
  //   	System.out.println("\nFunction: ");
  //   	System.out.println("\nFunction: ");
  //   	System.out.println("\nFunction: ");
    }
}

