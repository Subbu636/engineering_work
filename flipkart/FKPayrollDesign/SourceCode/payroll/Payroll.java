
// package
package payroll;

// imports
import java.util.*;
import java.io.*;
import org.json.simple.*;
import org.json.simple.parser.*;

class Employee{

	int id, start_date, payment_frequency,sales_count,commission_frequency, last_collection;
	String name;
	boolean is_union_member;
	String payment_mode;
	double salary_per_unit, service_charges, commission_rate, weekly_dues;

	Employee(int id,String name,boolean is_union_member,String payment_mode,double salary_per_unit,int start_date,double commission_rate, double weekly_dues){
		this.id = id;
		this.name = name;
		this.is_union_member = is_union_member;
		this.payment_mode = payment_mode;
		this.salary_per_unit = salary_per_unit;
		this.start_date = start_date;
		this.commission_rate = commission_rate;
		this.weekly_dues = weekly_dues;
		last_collection = 0;
		sales_count = 0;
		service_charges = 0;
		payment_frequency = -1;
		commission_frequency = 14;
	}

	double payroll(int day_count){
		return -1.0;
	}

	void pay(double amount){
		if (amount != 0.0){
			System.out.println(amount+" units salary paid by "+payment_mode+" to id:"+id);
		}
	}
}

class Work_by_hour extends Employee{

	int normal_hrs;
	int extra_hrs;
	double extra_hour_factor = 1.5;

	Work_by_hour(int id,String name,boolean is_union_member,String payment_mode,double salary_per_unit,int start_date,double commission_rate,double weekly_dues){
		super(id, name, is_union_member, payment_mode, salary_per_unit, start_date,commission_rate,weekly_dues);
		normal_hrs = 0;
		extra_hrs = 0;
		payment_frequency = 7;
	}

	double payroll(int day_count){

		double ret_sum = (double)normal_hrs*salary_per_unit + (double)extra_hrs*salary_per_unit*extra_hour_factor;
		double ret_comm = (double)sales_count*commission_rate;
		double sum = 0;
		if(is_union_member){
			int num_weeks = (day_count + 1 - last_collection)/7;
			last_collection = day_count + 1- ((day_count + 1 - last_collection)%7);
			service_charges += ((double)num_weeks*weekly_dues);
		}
		if ((day_count+1)%payment_frequency == 0){
			sum += ret_sum;
		}
		if ((day_count+1)%commission_frequency == 0){
			sum += ret_comm;
			sales_count = 0;
		}
		if (sum > service_charges){
			sum -= (service_charges);
			service_charges = 0;
		}
		else{
			sum = 0.0;
			service_charges = service_charges - sum;
		}
		super.pay(sum);
		return ret_sum + ret_comm - service_charges;
	}
}

class Flat_salary extends Employee{


	Flat_salary(int id,String name,boolean is_union_member,String payment_mode,double salary_per_unit,int start_date,double commission_rate,double weekly_dues){
		super(id, name, is_union_member, payment_mode, salary_per_unit, start_date,commission_rate,weekly_dues);
		payment_frequency = 28;
	}

	double payroll(int day_count){

		double ret_sum = salary_per_unit;
		double ret_comm = (double)sales_count*commission_rate;
		double sum = 0;
		if(is_union_member){
			int num_weeks = (day_count + 1 - last_collection)/7;
			last_collection = day_count + 1 - ((day_count + 1 - last_collection)%7);
			service_charges += ((double)num_weeks*weekly_dues);
		}
		if ((day_count+1)%payment_frequency == 0){
			sum += ret_sum;
		}
		if ((day_count+1)%commission_frequency == 0){
			sum += ret_comm;
			sales_count = 0;
		}
		if (sum > service_charges){
			sum -= (service_charges);
			service_charges = 0;
		}
		else{
			sum = 0.0;
			service_charges = service_charges - sum;
		}
		super.pay(sum);
		return ret_sum + ret_comm - service_charges;
	}
}

public class Payroll{

	private HashMap<Integer, Employee> employees;
	private HashMap<Integer, String> type_matching;
	private int avail_id = 0;
	private int day_count = 1;

	public Payroll(){

		this.day_count = 0;
		employees = new HashMap<>();
		type_matching = new HashMap<>();
	}

	public void increment_day(){

		day_count += 1;
	}

	public int add_employee(String name,String salary_type,boolean is_union_member,String payment_mode,double salary_per_unit,double commission_rate,double weekly_dues){

		avail_id += 1;
		int id = avail_id;
		if (salary_type.equals("work by hour")){
			Employee emp = new Work_by_hour(id, name, is_union_member, payment_mode, salary_per_unit,day_count,commission_rate,weekly_dues);
			employees.put(id, emp);
			type_matching.put(id, salary_type);
			// System.out.println("Employee "+name+" successfully added");
		}
		else if(salary_type.equals("flat salary")){
			Employee emp = new Flat_salary(id, name, is_union_member, payment_mode, salary_per_unit,day_count,commission_rate,weekly_dues);
			employees.put(id, emp);
			type_matching.put(id, salary_type);
			// System.out.println("Employee "+name+" successfully added");
		}
		else{
			avail_id -= 1;
			id = -1;
			// System.out.println("No such salary type");
		}
		return id;
	}

	public int delete_employee(int id){

		if (employees.containsKey(id)){
			Employee emp = employees.get(id);
			emp = null;
			employees.remove(id);
			type_matching.remove(id);
			// System.out.println("Deleted Successfully:"+id);
			return id;
		}
		else{
			// System.out.println("Not present:"+id);
			return -1;
		}

	}

	public void load_json(String file_name){

		JSONParser jsonParser = new JSONParser();
		try{

			FileReader fileReader = new FileReader(file_name);
			JSONObject employees_json = (JSONObject) jsonParser.parse(fileReader);
			day_count = Integer.parseInt((String)employees_json.get("last_update"));
			JSONArray arr = (JSONArray)employees_json.get("employees");
			for (int i = 0;i < arr.size();i++){
				JSONObject jo = (JSONObject) arr.get(i);
				int id = Integer.parseInt((String)jo.get("id"));
				int start_date = Integer.parseInt((String)jo.get("start_date"));
				int sales_count = Integer.parseInt((String)jo.get("sales_count"));
				int commission_frequency = Integer.parseInt((String)jo.get("commission_frequency"));
				int last_collection = Integer.parseInt((String)jo.get("last_collection"));
				String name = (String)jo.get("name");
				String payment_mode = (String)jo.get("payment_mode");
				String salary_type = (String)jo.get("salary_type");
				double salary_per_unit = Double.parseDouble((String)jo.get("salary_per_unit"));
				double service_charges = Double.parseDouble((String)jo.get("service_charges"));
				double commission_rate = Double.parseDouble((String)jo.get("commission_rate"));
				double weekly_dues = Double.parseDouble((String)jo.get("weekly_dues"));
				boolean is_union_member = Boolean.parseBoolean(((String)jo.get("is_union_member")));
				if (salary_type.equals("work by hour")){
					int normal_hrs = Integer.parseInt((String)jo.get("normal_hrs"));
					int extra_hrs = Integer.parseInt((String)jo.get("extra_hrs"));
					Work_by_hour emp = new Work_by_hour(id, name, is_union_member, payment_mode, salary_per_unit,day_count,commission_rate,weekly_dues);
					emp.normal_hrs = normal_hrs;
					emp.extra_hrs = extra_hrs;
					employees.put(id, emp);
					type_matching.put(id, salary_type);
					// System.out.println("Employee "+name+" successfully added");
				}
				else if(salary_type.equals("flat salary")){
					Employee emp = new Flat_salary(id, name, is_union_member, payment_mode, salary_per_unit,day_count,commission_rate,weekly_dues);
					employees.put(id, emp);
					type_matching.put(id, salary_type);
					// System.out.println("Employee "+name+" successfully added");
				}
			}
		}
		catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } 
	    catch (IOException e) {
	        e.printStackTrace();
	    } 
	    catch (ParseException e) {
	        e.printStackTrace();
	    }
	}

	public void update_json(String file_name){

		JSONObject employees_json = new JSONObject();
		employees_json.put("project","Payroll");
		employees_json.put("last_update",Integer.toString(day_count));
		JSONArray arr = new JSONArray();
		for (Map.Entry val : employees.entrySet()){
			Employee emp = (Employee)val.getValue();
			int emp_id = (int)val.getKey();
			JSONObject jo = new JSONObject();
			jo.put("id",Integer.toString(emp_id));
			jo.put("name",emp.name);
			jo.put("is_union_member",Boolean.toString(emp.is_union_member));
			jo.put("start_date",Integer.toString(emp.start_date));
			jo.put("sales_count",Integer.toString(emp.sales_count));
			jo.put("commission_frequency",Integer.toString(emp.commission_frequency));
			jo.put("last_collection",Integer.toString(emp.last_collection));
			jo.put("payment_mode",emp.payment_mode);
			jo.put("salary_per_unit",Double.toString(emp.salary_per_unit));
			jo.put("service_charges",Double.toString(emp.service_charges));
			jo.put("commission_rate",Double.toString(emp.commission_rate));
			jo.put("weekly_dues",Double.toString(emp.weekly_dues));
			if(type_matching.get(emp_id).equals("work by hour")){
				Work_by_hour wh = (Work_by_hour)emp;
				jo.put("normal_hrs",Integer.toString(wh.normal_hrs));
				jo.put("extra_hrs", Integer.toString(wh.extra_hrs));
				jo.put("salary_type","work by hour");
			}
			else{
				jo.put("salary_type","flat salary");
			}
			arr.add(jo);
		}
		employees_json.put("employees",arr);

		try (FileWriter file = new FileWriter(file_name)) {
 
            file.write(employees_json.toString());
            file.flush();
 
        } catch (IOException e) {
            e.printStackTrace();
        }

	}

	public int post_time_card(int id, int work_hours){

		if (employees.containsKey(id) && type_matching.get(id).equals("work by hour")){
			Work_by_hour emp = (Work_by_hour)employees.get(id);
			if (work_hours > 8){
				emp.normal_hrs += 8;
				emp.extra_hrs += (work_hours-8);
			}
			else{
				emp.normal_hrs += work_hours;
			}
			// System.out.println("Time card added successfully:"+id);
			return emp.normal_hrs + emp.extra_hrs;
		}
		else{
			// System.out.println("Not appliacable or no id:"+id);
			return -1;
		}
	}

	public int post_sales_reciept(int id, int sales_count){

		if (employees.containsKey(id)){
			Employee emp = employees.get(id);
			emp.sales_count += sales_count;
			// System.out.println("Sales reciept added successfully:"+id);
			return emp.sales_count;
		}
		else{
			// System.out.println("Not appliacable or no id:"+id);
			return -1;
		}
	}

	public String add_union_membership(int id){

		if (employees.containsKey(id)){
			Employee emp = employees.get(id);
			if (emp.is_union_member){
				return "Already a union member";
			}
			else{
				emp.is_union_member = true;
				return "Union membership added";
			}
		}
		else{
			return "invalid id";
		}
	}

	public double add_service_charge(int id, double amount){

		if (employees.containsKey(id) && employees.get(id).is_union_member){
			Employee emp = employees.get(id);
			emp.service_charges += amount;
			// System.out.println("Service Charges added:"+id);
			return emp.service_charges;
		}
		else{
			// System.out.println("Not a union member or invalid id:"+id);
			return -1;
		}
	}

	public int alter_salary_per_unit(int id, double new_rate){

		if(employees.containsKey(id)){
			Employee emp = employees.get(id);
			emp.salary_per_unit = new_rate;
			// System.out.println("Changed salary rate:"+id);
			return 1;
		}
		else{
			// System.out.println("invalid id:"+id);
			return -1;
		}
	}

	public int alter_payment_mode(int id, String new_mode){

		if(employees.containsKey(id)){
			Employee emp = employees.get(id);
			emp.payment_mode = new_mode;
			// System.out.println("Changed payment mode:"+id);
			return 1;
		}
		else{
			// System.out.println("invalid id:"+id);
			return -1;
		}
	}

	public int alter_commission_rate(int id, int new_rate){

		if(employees.containsKey(id)){
			Employee emp = employees.get(id);
			emp.commission_rate = new_rate;
			// System.out.println("Changed payment mode:"+id);
			return 1;
		}
		else{
			// System.out.println("invalid id:"+id);
			return -1;
		}
	}

	public double process_payroll(int id){

		if (employees.containsKey(id)){
			Employee emp = employees.get(id);
			return emp.payroll(day_count);
		}
		else{
			// System.out.println("invalid id");
			return -1.0;
		}
	}

	public HashMap<Integer,Double> process_payroll_all(){
		HashMap <Integer,Double> pay_table = new HashMap<>();
		for (Map.Entry val : employees.entrySet()){
			Employee emp = (Employee)val.getValue();
			pay_table.put((Integer)val.getKey(),emp.payroll(day_count));
		}
		return pay_table;
	}

	public void de_bug(int id){

		if (employees.containsKey(id)){
			Employee emp = employees.get(id);
			System.out.println(emp.name);
			System.out.println(emp.payment_mode);
			System.out.println(emp.last_collection);
		}
		else{
			System.out.println("No Such id");
		}
	}

	public double get_salary_per_unit(int id){

		if (employees.containsKey(id)){
			return employees.get(id).salary_per_unit;
		}
		else{
			return -1.0;
		}
	}

	public String get_payment_mode(int id){

		if (employees.containsKey(id)){
			return employees.get(id).payment_mode;
		}
		else{
			return "invalid id";
		}
	}

	public boolean union_membership(int id){
		if (employees.containsKey(id)){
			return employees.get(id).is_union_member;
		}
		else{
			return false;
		}
	}
            
}