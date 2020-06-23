
// imports
import java.util.*;
import java.io.*;
import payroll.*;
import org.json.simple.*;
import org.json.simple.parser.*;

// Feature testing file

public class Main{

	public static void main(String[] args){

		System.out.println("Starting");


		Payroll pr = new Payroll();
		// int id1 = pr.add_employee("emp1", "work by hour", true, "Account", 10,0,15);
		// int id2 = pr.add_employee("emp2", "flat salary", true, "Mail", 10000,50,100);
		// int id3 = pr.add_employee("emp3", "flat salary", false, "Hand", 6000,50,0);
		// int id4 = pr.add_employee("emp4", "work by hour", false, "Mail", 20,0,0);

		// pr.update_json("payroll_data.json");

		pr.load_json("payroll_data.json");
		// pr.de_bug(1);
		// pr.de_bug(4);
		

		// System.out.println();

		// System.out.println(pr.post_time_card(id1, 10));
		// System.out.println(pr.post_sales_reciept(id3, 40));

		// pr.add_service_charge(id1, 15);
		// pr.add_union_membership(id3);
		// System.out.println(pr.get_salary_per_unit(id2));
		// pr.alter_salary_per_unit(id2, 5000);
		// System.out.println(pr.get_salary_per_unit(id2));
		// System.out.println(pr.get_payment_mode(id3));
		// pr.alter_payment_mode(id3, "by hand");
		// System.out.println(pr.get_payment_mode(id3));


		// for(int i = 1;i < 29;i++){
		// 	System.out.println("Day:"+i);
		// 	pr.process_payroll_all();
		// 	pr.increment_day();
		// }



		pr.add_service_charge(1,25);
		pr.add_service_charge(3, 50);
		pr.add_service_charge(2, 100);
		pr.add_service_charge(4, 30);
		for(int i = 1;i < 29;i++){
			System.out.println("Day:"+i);
			pr.post_time_card(1,9);
			pr.post_time_card(4,8);
			pr.post_sales_reciept(2, 5);
			pr.post_sales_reciept(3,10);

			HashMap<Integer,Double> map = pr.process_payroll_all();
			System.out.println(map);
			// pr.de_bug(id1);

			pr.increment_day();

		}




	}
}