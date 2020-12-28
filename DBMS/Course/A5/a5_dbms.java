// imports
import java.sql.*;
import java.util.*;

// Code
class AcademicInstiQuerier{
	private Connection conn = null;
	public String dept_id;

	AcademicInstiQuerier(String dept_id){
		this.dept_id = dept_id;
	}

	public int connect_to_insti(){
		try {
		    String url       = "jdbc:mysql://localhost:3306/academic_insti";
		    String user      = "root";
		    String password  = "sorted";
		    conn = DriverManager.getConnection(url, user, password);
		    System.out.println("Connection Established");
		    return 1;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int close_insti(){
		try{
			conn.close();
			System.out.println("Connection Closed");
			return 1;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int add_course(String c_id, String t_id, String c_room){
		String insert = "insert into teaching(empId,courseId,sem,year,classRoom) "+
				"values(?,?,?,?,?)";
		try(Statement stmt = conn.createStatement(); 
			PreparedStatement pstmt = conn.prepareStatement(insert);){
			String val_course = "select courseId from course where courseId = "+c_id+" && deptNo = "+dept_id;
			ResultSet rs   = stmt.executeQuery(val_course);
			if(!rs.next()){
				System.out.println("No Such Course:"+c_id+" in Department:"+dept_id);
				return 0;
			}
			String val_prof = "select empId from professor where empId = "+t_id+" && deptNo = "+dept_id;
			rs = stmt.executeQuery(val_prof);
			if(!rs.next()){
				System.out.println("No Such Professor:"+t_id+" in Department:"+dept_id);
				return 0;
			}
			System.out.println("Validated");
			pstmt.setString(1, t_id);
			pstmt.setString(2, c_id);
			pstmt.setString(3, "Even");
			pstmt.setString(4, "2006");
			pstmt.setString(5, c_room);
			int rowAffected = pstmt.executeUpdate();
			if(rowAffected != 1){
				System.out.println("Insertion Failed");
				return 0;
			}
			System.out.println("Successfully Inserted");
			return 1;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int enroll_student(String roll_no, String c_id){
		String insert = "insert into enrollment(rollNo,courseId,sem,year,grade) "+
				"values(?,?,?,?,?)";
		try(Statement stmt = conn.createStatement(); 
			PreparedStatement pstmt = conn.prepareStatement(insert);){

			String val_emp = "select empId from teaching where courseId = "+c_id+" && sem = 'Even' && year = 2006";
			ResultSet rs   = stmt.executeQuery(val_emp);
			if(!rs.next()){
				System.out.println("No Such Course:"+c_id+" in sem:Even and year:2006");
				return 0;
			}
			String val_prereqs = "select p.preReqCourse as c_id from prerequisite as p where p.courseId = "+c_id
					+" and p.preReqCourse not in (select e.courseId as c_id from enrollment as e where e.rollNo = "
					+roll_no+" && e.grade <> 'U')";
			rs   = stmt.executeQuery(val_prereqs);
			if(rs.next()){
				System.out.println("The following prerequisite course(s) are not completed by the student: ");
				System.out.println(rs.getString("c_id"));
				while(rs.next()){
					System.out.println(rs.getString("c_id"));
				}
				return 0;
			}
			System.out.println("Validated");
			pstmt.setString(1, roll_no);
			pstmt.setString(2, c_id);
			pstmt.setString(3, "Even");
			pstmt.setString(4, "2006");
			pstmt.setString(5, "NA");
			int rowAffected = pstmt.executeUpdate();
			if(rowAffected != 1){
				System.out.println("Insertion Failed");
				return 0;
			}
			System.out.println("Successfully Inserted");
			return 1;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}



// Main
public class a5_dbms{
	public static void main(String[] args){
		// AcademicInstiQuerier aq = new AcademicInstiQuerier("20");
		// aq.connect_to_insti();
		// aq.add_course("101","97302","RR");
		// aq.enroll_student("1000","101");
		// aq.close_insti();
		Scanner sc= new Scanner(System.in);
		System.out.print("Enter the department Id into which you want to add courses: ");
		String deptNo = Integer.toString(sc.nextInt());
		AcademicInstiQuerier aq = new AcademicInstiQuerier(deptNo);
		System.out.print("Enter the number of courses you want to add: ");
		int courses = sc.nextInt();
		System.out.print("Enter the number of enrollments you want to add: ");
		int enrolls = sc.nextInt();
		aq.connect_to_insti();
		sc.nextLine(); 
		System.out.print("Input courseId, teacherId, classroom with space seperation and in one line for each course: ");
		for(int i=0; i<courses; i++){
			String str= sc.nextLine(); 
			String[] inputs = str.split(" ");
			aq.add_course(inputs[0],inputs[1],inputs[2]);
		}
		System.out.print("Input rollNo, courseId with space seperation and in one line for each enrollment: ");
		for(int i=0; i<enrolls; i++){
			String str= sc.nextLine(); 
			String[] inputs = str.split(" ");
			aq.enroll_student(inputs[0],inputs[1]);
		}
		aq.close_insti();
	}
}