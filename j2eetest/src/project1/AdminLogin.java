package project1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AdminLogin {

	static Connection con = null;
	static Scanner sc = new Scanner(System.in);

//==============================================================================================

	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");

			boolean opt = true;
			while (opt) {
				System.out.println(
						"\nPlease select one of the below options:\n1. Admin Sign In\n2. User Sign In\n3. User Sign Up\n4. Exit");
				System.out.print("Enter your choice: ");
				int ch = sc.nextInt();
				sc.nextLine();

				switch (ch) {
				case 1 -> {
					admin();
					opt = false;
				}
				case 2 -> {
					userSignIn();
					opt = false;
				}
				case 3 -> {
					userSignUp();
					opt = false;
				}
				case 4 -> {
					System.out.println("Exiting...");
					opt = false;
				}
				default -> System.out.println("Invalid option, please try again.");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null)
					con.close();
//				sc.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

//==============================================================================================

	public static void admin() throws SQLException {
		String email = "admin@gmail.com";
		String password = "admin@123";

		System.out.println("\n===== Admin Login =====");

		boolean loggedIn = false;
		while (!loggedIn) {
			System.out.print("Enter admin email: ");
			String mail = sc.nextLine();
			System.out.print("Enter password: ");
			String pass = sc.nextLine();

			if (mail.equals(email) && pass.equals(password)) {
				System.out.println("\nAdmin login successful!");

				boolean opt = true;
				while (opt) {
					System.out.println(
							"\n-----Employee Table-----\nWhat operation you want to prform...\n1.Insert\n2.Update\n3.Find\n4.Delete\n5.FindAll\n6.Main Menu\n7.Exit");

					int ch = sc.nextInt();
					switch (ch) {
					case 1 -> insert();
					case 2 -> update();
					case 3 -> find();
                    case 4 -> delete();
                    case 5 -> findAll();
                    case 6 -> mainMenu();
					case 7 -> {
									System.out.println("Exiting...");
									opt = false;}
					default -> System.out.println("Invalid option, please try again.");
					}
				}

				loggedIn = true;
			} else {
				System.out.println("\nIncorrect email or password. Please try again.\n");
			}
		}
 }

	private static void mainMenu() {
		main(new String[] {});
	}

//____________________________________________________________________________________
	
	private static void findAll() throws SQLException{
		PreparedStatement ps = con.prepareStatement("select * from user_details");
        ResultSet rs = ps.executeQuery();

        System.out.println("\n--- Employee Records ---");
        while (rs.next()) {
        	System.out.println("eid= "+rs.getInt("eid")+" \nename = "+rs.getString("ename")+" \nsalary = "+rs.getDouble("esal")+
					" \neadress = "+rs.getString("eaddress")+" \ncontact = "+rs.getString("econtact")+" \nemail = "+rs.getString("email")+" \npassword = "+rs.getString("epass"));
        }
}

//____________________________________________________________________________________

	private static void delete() throws SQLException{
		System.out.println("Enter Employee ID to delete:");
        int id = sc.nextInt();

        PreparedStatement ps = con.prepareStatement("delete from user_details where eid=?");
        ps.setInt(1, id);
        int count = ps.executeUpdate();

        if (count > 0)
            System.out.println("Employee " + id + " deleted successfully.");
        else
            System.out.println("No record found for Employee ID " + id);
}
	
//____________________________________________________________________________________

	private static void find() throws SQLException {
		System.out.println("Enter Employee ID to search:");
		int id = sc.nextInt();

		PreparedStatement ps = con.prepareStatement("select * from user_details where eid=?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			System.out.println("\nEmployee Details:");
			System.out.println("eid= " + rs.getInt("eid") + " \nename = " + rs.getString("ename") + " \nsalary = "
					+ rs.getDouble("esal") + " \neadress = " + rs.getString("eaddress") + " \ncontact = "
					+ rs.getString("econtact") + " \nemail = " + rs.getString("email") + " \npassword = "
					+ rs.getString("epass"));
		} else {
			System.out.println("No employee found with ID " + id);
		}
	}

//____________________________________________________________________________________

	private static void update() throws SQLException {
		sc.nextLine();
		System.out.println("Which column do you want to update?");
		System.out.println("(ename / esal / eaddress / econtact / email / epassword)");
		String column = sc.nextLine();

		PreparedStatement ps = con.prepareStatement("update user_details set " + column + "=? where eid=?");

		if (column.equals("esal")) {
			System.out.println("Enter new salary:");
			double newSal = sc.nextDouble();
			ps.setDouble(1, newSal);
		} else {
			System.out.println("Enter new value for " + column + ":");
			String newVal = sc.nextLine();
			ps.setString(1, newVal);
		}

		System.out.println("Enter Employee ID to update:");
		int id = sc.nextInt();
		ps.setInt(2, id);

		ps.executeUpdate();
		System.out.println("Employee data updated successfully.");
	}

//____________________________________________________________________________________

	private static void insert() throws SQLException {
		PreparedStatement ps = null;
		System.out.println("Enter number of records to insert: ");
		int n = sc.nextInt();
		sc.nextLine();

		for (int i = 0; i < n; i++) {
			try {
				ps = con.prepareStatement("insert into user_details values(?,?,?,?,?,?,?)");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("Enter Employee ID: ");
			int eid = sc.nextInt();
			sc.nextLine();

			System.out.println("Enter Name: ");
			String ename = sc.nextLine();

			System.out.println("Enter Salary: ");
			double esal = sc.nextDouble();
			sc.nextLine();

			System.out.println("Enter Address: ");
			String eaddress = sc.nextLine();

			System.out.println("Enter Contact: ");
			String econtact = sc.nextLine();

			System.out.println("Enter Email: ");
			String email = sc.nextLine();

			System.out.println("Enter Password: ");
			String epass = sc.nextLine();

			ps.setInt(1, eid);
			ps.setString(2, ename);
			ps.setDouble(3, esal);
			ps.setString(4, eaddress);
			ps.setString(5, econtact);
			ps.setString(6, email);
			ps.setString(7, epass);
			ps.executeUpdate();
			System.out.println("Record " + (i + 1) + " inserted successfully!\n");
		}
	}

//==============================================================================================
//==============================================================================================

	public static void userSignIn() {
		PreparedStatement ps = null;
		java.sql.ResultSet rs = null;

		try {
			System.out.println("\n===== User Sign In =====");
			System.out.print("Enter your email: ");
			String email = sc.nextLine();
			System.out.print("Enter your password: ");
			String password = sc.nextLine();

			ps = con.prepareStatement("SELECT name FROM emp_details WHERE email = ? AND password = ?");
			ps.setString(1, email);
			ps.setString(2, password);

			rs = ps.executeQuery();

			if (rs.next()) {
				String name = rs.getString("name");
				System.out.println("\nWelcome, " + name);			
				System.out.println("Enter the operation you want to execute...\n1.Update\n2.Find");
				int op = sc.nextInt();
				
				switch(op) {
				case 1-> updateMy();
				case 2-> findMy();
			}
			} else {
				System.out.println("\nWrong credentials. Please try again.\n");
				userSignIn();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
}

	private static void findMy() throws SQLException {
		System.out.println("\nPlease conform your Email address: ");
		sc.nextLine();
		String email = sc.nextLine();
		
		PreparedStatement ps = con.prepareStatement("select * from user_details where email=?");
		ps.setString(1, email);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			System.out.println("\nYour Details are below:");
			System.out.println("eid= " + rs.getInt("eid") + " \nename = " + rs.getString("ename") + " \nsalary = "
					+ rs.getDouble("esal") + " \neadress = " + rs.getString("eaddress") + " \ncontact = "
					+ rs.getString("econtact") + " \nemail = " + rs.getString("email") + " \npassword = "
					+ rs.getString("epass"));
		} else {
			System.out.println("No employee found with email called " + email);
		}
}

	private static void updateMy() throws SQLException {
	sc.nextLine();
	System.out.println("Which column do you want to update?");
	System.out.println("(ename / eaddress / econtact / epassword)");
	String column = sc.nextLine();

		
	if (!(column.equals("ename") || column.equals("eaddress") || column.equals("econtact") || column.equals("epassword"))) {
        System.out.println("Not possible to update " + column + " as a user.");
        return;
    }
	PreparedStatement ps = con.prepareStatement("update user_details set " + column + "=? where email=?");
	
	System.out.println("Enter new value for " + column + ":");
	String newVal = sc.nextLine();
	ps.setString(1, newVal);
	
	System.out.println("Please conform your employee email to update: ");
	String email = sc.nextLine();
	ps.setString(2, email);

	ps.executeUpdate();
	System.out.println("Employee data updated successfully.");
}

//==============================================================================================

	public static void userSignUp() {
		PreparedStatement ps = null;

		try {
			System.out.println("\n===== User Sign Up =====");
			System.out.print("Enter User Name: ");
			String name = sc.nextLine();
			System.out.print("Enter User Email: ");
			String email = sc.nextLine().trim();
			System.out.print("Enter User Password: ");
			String password = sc.nextLine();

			if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
				System.out.println("\nRegistration Failed... Please check again. All fields are required!");
				userSignUp();
				return;
			}
			ps = con.prepareStatement("INSERT INTO emp_details VALUES (?,?,?)");
			ps.setString(1, name);
			ps.setString(2, email); 
			ps.setString(3, password);
			ps.executeUpdate();
			
			System.out.println("Reminder---- Only this email is used display your details.");
			System.out.println("\nRegistration Successful!");
			userSignIn();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
