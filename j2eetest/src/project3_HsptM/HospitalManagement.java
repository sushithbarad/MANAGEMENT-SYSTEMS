package project3_HsptM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HospitalManagement {

	static Connection con = null;
	static Scanner sc;
	static PreparedStatement ps = null;
	

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		sc = new Scanner(System.in);
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital", "root", "root");

		while (true) {
			System.out.println(
					"Hospital Management System\n1. Add Patient\n2. Add Doctor\n3. View Patients\n4. View Doctors\n5. Book Appointment\n6. Exit");
			System.out.println("Enter your choice: ");

			int ch = sc.nextInt();

			switch (ch) {
			case 1:
				addPatient();
				break;
			case 2:
				addDoctor();
				break;
			case 3:
				viewPatients();
				break;
			case 4:
				viewDoctors();
				break;
			case 5:
				bookAppointment();
				break;
			case 6:{
				con.close();
				sc.close();
				System.out.println("Exiting....");
			}
				return;
			default:
				System.out.println("Please enter valid choice.");
				break;
			}

		}

	}

	//===========================================================================================
	
	private static void addPatient() {
		try {
			System.out.print("Enter Name: ");
			String name = sc.next();

			System.out.print("Enter Age: ");
			int age = sc.nextInt();

			System.out.print("Enter Gender: ");
			String gender = sc.next();

			System.out.print("Enter Bloog Group: ");
			String blood_group = sc.next();

			ps = con.prepareStatement("insert into patients(name, age, gender, blood_group) values (?, ?, ?, ?)");
			ps.setString(1, name);
			ps.setInt(2, age);
			ps.setString(3, gender);
			ps.setString(4, blood_group);

			int pd = ps.executeUpdate();

			if (pd > 0) {
				System.out.println("Patient details added successfully.\n");
			} else {
				System.out.println("Failed to add patient details.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//=========================================================================================
	
	private static void addDoctor() {
	    try {
	        System.out.print("Enter Doctor ID: ");
	        int id = sc.nextInt();

	        System.out.print("Enter Doctor Name: ");
	        String name = sc.next();

	        System.out.print("Enter Department: ");
	        String department = sc.next();

	        ps = con.prepareStatement("insert into doctors(id, name, department) values (?, ?, ?)");
	            ps.setInt(1, id);
	            ps.setString(2, name);
	            ps.setString(3, department);

	            int dd = ps.executeUpdate();

	            if (dd > 0) {
	                System.out.println("Doctor added successfully.\n");
	            } else {
	                System.out.println("Failed to add doctor details.");
	            }
	        

	    } catch (SQLException e) {
	    	e.printStackTrace();
	    }
	}

	//==================================================================
	
	private static void viewPatients() {
	    try {

	        ps = con.prepareStatement("select * from patients");
	             ResultSet rs = ps.executeQuery();

	            System.out.println("---- Patient Details ----");

	            while (rs.next()) {
	                System.out.println("Patient ID  : " + rs.getInt("id"));
	                System.out.println("Name        : " + rs.getString("name"));
	                System.out.println("Patient Age : " + rs.getInt("age"));
	                System.out.println("Gender      : " + rs.getString("gender"));
	                System.out.println("Blood Group : " + rs.getString("blood_group"));
	                System.out.println("-----------------------------");
	            }

	    } catch (SQLException e) {
	    	e.printStackTrace();
	    }
	}

	//==============================================================================
	
	private static void viewDoctors() {
	    try {

	         ps = con.prepareStatement("SELECT * FROM doctors");
	             ResultSet rs = ps.executeQuery();

	            System.out.println("---- Doctor Details ----");

	            while (rs.next()) {
	                System.out.println("Doctor ID    : " + rs.getInt("id"));
	                System.out.println("Doctor Name  : " + rs.getString("name"));
	                System.out.println("Department   : " + rs.getString("department"));
	                System.out.println("-----------------------------");
	            }
	        

	    } catch (SQLException e) {
	    	e.printStackTrace();
	    }
	}

	//==============================================================================

	private static void bookAppointment() {
	    try {
	        System.out.print("Enter Patient Id: ");
	        int patientId = sc.nextInt();

	        System.out.print("Enter Doctor Id: ");
	        int doctorId = sc.nextInt();

	        System.out.print("Enter appointment date (yyyy-mm-dd): ");
	        String appointmentDate = sc.next();
	        

	        if (!getPatientById(patientId)) {
                System.out.println("Please provide valid patient id.\n");
                return;
            }

            if (!getDoctorById(doctorId)) {
                System.out.println("Please provide valid doctor id.\n");
                return;
            }

	        
	        if (!checkAvailability(doctorId, appointmentDate)) {
	            System.out.println("Doctor not available on this date.\n");
	            return;
	        }


	        ps = con.prepareStatement("INSERT INTO appointment (p_id, d_id, date) VALUES (?, ?, ?)"); 
	            ps.setInt(1, patientId);
	            ps.setInt(2, doctorId);
	            ps.setString(3, appointmentDate);

	            if (ps.executeUpdate() > 0) {
	                System.out.println("Appointment booked successfully.\n");
	            } else {
	                System.out.println("Doctor is not free on this date please book another date.");
	            }
	        

	    } catch (SQLException e) {
	        System.out.println("Error while booking appointment: " + e.getMessage());
	    }
	}
	
	//==============================================================================

	private static boolean checkAvailability(int doctorId, String appointmentDate) {
	    try {
	        ps = con.prepareStatement("SELECT COUNT(1) FROM appointment WHERE d_id = ? AND date = ?");

	        ps.setInt(1, doctorId);
	        ps.setString(2, appointmentDate);

	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            return rs.getInt(1) == 0; 
	        }

	    } catch (SQLException e) {
	        System.out.println("Error checking availability: " + e.getMessage());
	    }

	    return false;
	}

	//==============================================================================

	 private static boolean getPatientById(int id) {
	        try {
	            ps = con.prepareStatement("SELECT COUNT(1) FROM patients WHERE id = ?");
	            ps.setInt(1, id);

	            ResultSet rs = ps.executeQuery();
	            if (rs.next()) {
	                return rs.getInt(1) > 0;
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return false;
	    }

	 private static boolean getDoctorById(int id) {
			try {
				PreparedStatement ps = con.prepareStatement("SELECT COUNT(1) FROM doctors WHERE id = ?");
				ps.setInt(1, id);

				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

			return false;
		}

	}

//=========================================================================
