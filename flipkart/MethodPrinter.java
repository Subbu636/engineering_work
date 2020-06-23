
package learnJava;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Scanner;

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
    
    public Employee clone() throws CloneNotSupportedException {
        return (Employee) super.clone();
    }
}

public class MethodPrinter {
    public static void main(String[] args) throws ReflectiveOperationException {
        System.out.print("Class name: ");
        Scanner in = new Scanner(System.in);
        String className = in.nextLine();
        Class<?> cl = Class.forName(className);

        while (cl != null) {
            for (Method m : cl.getDeclaredMethods()) { 
                System.out.println(
                    Modifier.toString(m.getModifiers()) + " " +
                    m.getReturnType().getCanonicalName() + " " +
                    m.getName() +
                    Arrays.toString(m.getParameters()));                    
            }
            cl = cl.getSuperclass();
        }
    }
}

