// imports
import java.sql.*;

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
		return 0;

	}
}



// Main
public class MySqlA5{
	public static void main(String[] args){
		AcademicInstiQuerier aq = new AcademicInstiQuerier("5");
		aq.connect_to_insti();
		aq.add_course("696","74426","RR");
		aq.close_insti();

	}
}