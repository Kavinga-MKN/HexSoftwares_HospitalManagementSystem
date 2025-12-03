package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class Patient {

    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPatient() {

        scanner.nextLine(); // Clear newline

        System.out.print("Enter Patient Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Patient Age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); 

        System.out.print("Enter Patient Gender: ");
        String gender = scanner.nextLine();

        String query = "INSERT INTO patients(name, age, gender) VALUES(?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, gender);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Patient Added Successfully!");
            } else {
                System.out.println("Failed to Add Patient!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewPatients() {

        String query = "SELECT * FROM patients";

        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("Patients:");
            System.out.println("+------------+--------------------+----------+------------+");
            System.out.println("| Patient Id | Name               | Age      | Gender     |");
            System.out.println("+------------+--------------------+----------+------------+");

            while (rs.next()) {
                System.out.printf("| %-10d | %-18s | %-8d | %-10s |\n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender")
                );
                System.out.println("+------------+--------------------+----------+------------+");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getPatientById(int id) {

        String query = "SELECT id FROM patients WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
 {
    
}
