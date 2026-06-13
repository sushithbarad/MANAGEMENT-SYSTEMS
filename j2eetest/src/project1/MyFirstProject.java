package project1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MyFirstProject{

    static Connection con = null;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");
            
            boolean opt = true;
            while (opt) {
				System.out.println("\n-----Employee Table-----\nWhat operation you want to prform...\n1.Insert\n2.Update\n3.Find\n4.Delete\n5.FindAll\n6.Exit");

                int ch = sc.nextInt();
                switch (ch) {
                    case 1 -> insert();
                    case 2 -> update();
                    case 3 -> find();
                    case 4 -> delete();
                    case 5 -> findAll();
                    case 6 -> opt = false;
                    default -> System.out.println("Invalid option, please try again.");
                }
            }
            con.close();
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insert() throws SQLException {
        System.out.println("Enter number of records to insert: ");
        int n = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < n; i++) {
            PreparedStatement ps = con.prepareStatement("insert into emp_details values(?,?,?,?,?,?,?)");

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
            System.out.println("Record " +(i+1)+ " inserted successfully!\n");
        }
    }

    public static void update() throws SQLException {
        sc.nextLine();
        System.out.println("Which column do you want to update?");
        System.out.println("(ename / esal / eaddress / econtact / email / epassword)");
        String column = sc.nextLine();

        PreparedStatement ps = con.prepareStatement("update emp_details set " + column + "=? where eid=?");

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

    public static void find() throws SQLException {
        System.out.println("Enter Employee ID to search:");
        int id = sc.nextInt();

        PreparedStatement ps = con.prepareStatement("select * from emp_details where eid=?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            System.out.println("\nEmployee Details:");
            System.out.println("eid= "+rs.getInt("eid")+" \nename = "+rs.getString("ename")+" \nsalary = "+rs.getDouble("esal")+
					" \neadress = "+rs.getString("eaddress")+" \ncontact = "+rs.getString("econtact")+" \nemail = "+rs.getString("email")+" \npassword = "+rs.getString("epassword"));
        } else {
            System.out.println("No employee found with ID " + id);
        }
    }

    public static void delete() throws SQLException {
        System.out.println("Enter Employee ID to delete:");
        int id = sc.nextInt();

        PreparedStatement ps = con.prepareStatement("delete from emp_details where eid=?");
        ps.setInt(1, id);
        int count = ps.executeUpdate();

        if (count > 0)
            System.out.println("Employee " + id + " deleted successfully.");
        else
            System.out.println("No record found for Employee ID " + id);
    }

    public static void findAll() throws SQLException {
        PreparedStatement ps = con.prepareStatement("select * from emp_details");
        ResultSet rs = ps.executeQuery();

        System.out.println("\n--- Employee Records ---");
        while (rs.next()) {
        	System.out.println("eid= "+rs.getInt("eid")+" \nename = "+rs.getString("ename")+" \nsalary = "+rs.getDouble("esal")+
					" \neadress = "+rs.getString("eaddress")+" \ncontact = "+rs.getString("econtact")+" \nemail = "+rs.getString("email")+" \npassword = "+rs.getString("epassword"));
        }
    }
}

