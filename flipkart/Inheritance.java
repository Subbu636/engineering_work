package learnJava;

class Employee {
	// private String name;
	protected String name;
	private double salary;

	public Employee(String name, double salary) {
		this.name = name;
		this.salary = salary;
	}

	public void raiseSalary(double byPercent) {
		double raise = salary * byPercent / 100;
		salary += raise;
	}

	public String getName() 	{  return "of Employee: " + name;  }
	// public final String getName() 	{  return name;  }
	public double getSalary() 		{  return salary; }
}

// Inheritance
// 		Inheriting Nature of Employee: Both State and Behviour

// Objectives : Relationships Between Types
//		Type of Types

// Mechanisms
// 		Inheritance
// 		Polymorphism


//
class Manager extends Employee {
	private double bonus;

	public Manager(String name, double salary) {
		super(name, salary);
		bonus = 0;
	}

	public void setBonus(double bonus) {
		this.bonus = bonus;
	}

	public String getName()   {  return "of Manager: " + name;  }
	public double getSalary() { return super.getSalary() + bonus; }
}


public class Inheritance {
	public static void main(String[] args) {
		Employee emp = new Employee("Sambha", 20000);

		// 1. Message To Method Mapping
		// Decision Based On Employee Type
		System.out.println("Name " + emp.getName());
		System.out.println("Salary 	: " + emp.getSalary());

		// Type of boss is Manager
		// 		boss is ALSO Type of Employee
		Manager boss = new Manager("Gabaar Singh", 400000);
		boss.setBonus(50000);

		// 2. Message To Method Mapping
		// Decision Based On Manager Type
		System.out.println("Name " + boss.getName());
		System.out.println("Salary 	: " + boss.getSalary());

		// Sholey Returns
		Employee bossAgain = new Manager("Gabaar Singh Again", 400000);		
		// 3. Message To Method Mapping
		// Decision Based On Employee Type
		// bossAgain.setBonus(0); 
		
		// 4. Message To Method Mapping
		// Decision Based On Employee Type??????
		System.out.println("Name " + bossAgain.getName());
		//Expecation: 
		//		getName Message Should Map to getName() Method(Employee)
		
		//Reality: 
		//		getName Message Getting Map to getName() Method(Manager)

		System.out.println("Salary 	: " + bossAgain.getSalary());
	}
}
