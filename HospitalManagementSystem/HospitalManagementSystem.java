package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {

    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "Admin@123";

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); 
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);

        try {
            Connection connection = DriverManager.getConnection(url, username, password);

            Patient patient = new Patient(connection, scanner);
            Doctor doctor = new Doctor(connection);

            while (true) {

                System.out.println("\n===== HOSPITAL MANAGEMENT SYSTEM =====");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();

                switch (choice) {

                    case 1:
                        patient.addPatient();
                        break;

                    case 2:
                        patient.viewPatients();
                        break;

                    case 3:
                        doctor.viewDoctors();
                        break;

                    case 4:
                        bookAppointment(patient, doctor, connection, scanner);
                        break;

                    case 5:
                        System.out.println("Thank you for using Hospital Management System!");
                        return;

                    default:
                        System.out.println("Invalid choice! Try again.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void bookAppointment(Patient patient, Doctor doctor,
                                       Connection connection, Scanner scanner) {

        System.out.print("Enter Patient Id: ");
        int patientId = scanner.nextInt();

        System.out.print("Enter Doctor Id: ");
        int doctorId = scanner.nextInt();

        System.out.print("Enter appointment date (YYYY-MM-DD): ");
        String appointmentDate = scanner.next();

        if (patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)) {

            if (checkDoctorAvailability(doctorId, appointmentDate, connection)) {

                String query = "INSERT INTO appointments(patient_id, doctor_id, appointment_date) VALUES(?, ?, ?)";

                try (PreparedStatement ps = connection.prepareStatement(query)) {

                    ps.setInt(1, patientId);
                    ps.setInt(2, doctorId);
                    ps.setString(3, appointmentDate);

                    int rows = ps.executeUpdate();

                    if (rows > 0) {
                        System.out.println("Appointment Booked Successfully!");
                    } else {
                        System.out.println("Failed to Book Appointment.");
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("Doctor is not available on this date.");
            }

        } else {
            System.out.println("Invalid Patient ID or Doctor ID.");
        }
    }


    public static boolean checkDoctorAvailability(int doctorId, String date, Connection connection) {

        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, doctorId);
            ps.setString(2, date);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) == 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
