package project2_HM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HotelManagement {

	static Connection con = null;
	static Scanner sc = new Scanner(System.in);
	static PreparedStatement ps = null;
	
	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_management", "root", "root");
			while (true) {
				System.out.println("\nHOTEL MANAGEMENT SYSTEM");
				System.out.println("\n1.Reserve a room" + "\n2.View Reservation" + "\n3.Geet Room Number"
						+ "\n4.Update Reservation" + "\n5.Delete Reservation" + "\n6.Exit" + "\nChoose a option: ");
				int ch = sc.nextInt();
				switch (ch) {
				case 1: {
					reserveRoom();
				}
				case 2:{
					viewReservation();
				}
				case 3:{
					getRoom();
				}
				case 4:{
					updateReservation();
				}
				case 5:{
					deleteReservation();
				}
				case 6: {
					
					System.out.println("Exiting.....");
					con.close();
					sc.close();
					
					return;
				}

				default:
					System.out.println("Invalid option, please try again.");

				}
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

///==========================================================================================

	private static void reserveRoom() {

		try {
			ps = con.prepareStatement("insert into hotal_data (guest_name, room_number, contact_number) values(?,?,?)");
			System.out.println("Enter Guest Name: ");
			String guestName = sc.next();
			sc.nextLine();
			System.out.println("Enter Room Number: ");
			int roomNumber = sc.nextInt();
			System.out.println("Enter Contact Number: ");
			String contactNumber = sc.next();

			ps.setString(1, guestName);
			ps.setInt(2, roomNumber);
			ps.setString(3, contactNumber);

			int pass = ps.executeUpdate();

			if (pass > 0) {
				System.out.println("Reservation successfully!\n");
			main(new String[]{});
				}
			else {
				System.out.println("Sorry Reservation Failed...");
				reserveRoom();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
///==========================================================================================
	
	private static void viewReservation() {
		try {
			ps = con.prepareStatement("select * from  hotal_data ");
			
			System.out.println("Current Reservations are in the below Table");
			System.out.println("========|===================|=============|===================|=========================|");
			System.out.println("Rerv ID | Guest Name        | Room Number | Contact Number    | Reservation Date        |");
			System.out.println("========|===================|=============|===================|=========================|");
			
			ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	        	System.out.println(
	        		    rs.getInt("reservation_id") + "          " +
	        		    rs.getString("guest_name") + "               " +
	        		    rs.getInt("room_number") + "         " +
	        		    rs.getString("contact_number") + "         " +
	        		    rs.getTimestamp("reservation_date")
	        		);
	        	System.out.println("========|===================|=============|===================|=========================|");
	        } 
	        main(new String[]{});
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
///==========================================================================================

	private static void getRoom() {
		try {
			ps = con.prepareStatement("select room_number from hotal_data where guest_name = ?");

			System.out.print("Enter Guest Name: ");
			String guestName = sc.next();
			ps.setString(1, guestName);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				System.out.println("Guest allocated room number for " + (guestName) + " is: " + rs.getInt("room_number"));
				main(new String[] {});

			} else {
				System.out.println("No guest found with that name.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
///==========================================================================================

	private static void updateReservation() {
	    System.out.println("Enter Contact Number to Update: ");
	    String contact = sc.next();

	    try {
	        ps = con.prepareStatement("select * from hotal_data where contact_number = ? ");
	        ps.setString(1, contact);
	        ResultSet rs = ps.executeQuery();

	        if (!rs.next()) {
	            System.out.println("No reservation found for this contact number.");
	            return;
	        }

	        System.out.println("Existing Reservation on this Number:");
	        System.out.println("-------------------------------------------------------------");
	        System.out.println("Guest Name      : " + rs.getString("guest_name"));
	        System.out.println("Room Number     : " + rs.getInt("room_number"));
	        System.out.println("Reservation Date: " + rs.getTimestamp("reservation_date"));
	        System.out.println("-------------------------------------------------------------");

	        System.out.print("Enter new Guest Name: ");
	        String newName = sc.next();

	        System.out.print("Enter new Room Number: ");
	        int newRoom = sc.nextInt();

	        PreparedStatement ps1 = con.prepareStatement("update hotal_data set guest_name = ?, room_number = ? where contact_number = ?");
	        ps1.setString(1, newName);
	        ps1.setInt(2, newRoom);
	        ps1.setString(3, contact);

	        int updated = ps1.executeUpdate();

	        if (updated > 0) {
	            System.out.println("Reservation updated successfully!");
	            main(new String[]{});
	            
	        } else {
	            System.out.println("Update failed Please try again...");
	            updateReservation();
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

///==========================================================================================
	
	private static void deleteReservation() {
	    System.out.println("Enter Room Number to Delete data: ");
	    int roomNumber = sc.nextInt();

	    try {
	        ps = con.prepareStatement("select * from hotal_data where room_number = ?");
	        ps.setInt(1, roomNumber);
	        ResultSet rs = ps.executeQuery();

	        if (!rs.next()) {
	            System.out.println("No reservation found with this room number.");
	            return;
	        }

	        System.out.println("Reservation Found:");
	        System.out.println("-----------------------------------------");
	        System.out.println("Guest Name      : " + rs.getString("guest_name"));
	        System.out.println("Contact Number  : " + rs.getString("contact_number"));
	        System.out.println("Reservation Date: " + rs.getTimestamp("reservation_date"));
	        System.out.println("-----------------------------------------");

	        ps = con.prepareStatement("delete from hotal_data where room_number = ?");
	        ps.setInt(1, roomNumber);

	        int deleted = ps.executeUpdate();

	        if (deleted > 0) {
	            System.out.println("Reservation deleted successfully!");
	            main(new String[]{});
	            
	        } else {
	            System.out.println("Failed to delete reservation.");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
///==========================================================================================
	
}
