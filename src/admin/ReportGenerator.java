package admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class ReportGenerator {
    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/traffic_fine";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Sadip123@";

    public static void main(String[] args) {
        generatePaymentReport();
    }

    public static void generatePaymentReport() {
        // Query that retrieves payment data joined with fine details (e.g., violation)
        String query = "SELECT p.payment_id, p.fine_id, f.violation, p.payment_date, p.amount " +
                       "FROM payments p " +
                       "JOIN fines f ON p.fine_id = f.fine_id " +
                       "ORDER BY p.payment_date DESC";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            // Create the report file
            File reportFile = new File("PaymentReport.txt");
            try (PrintWriter writer = new PrintWriter(new FileWriter(reportFile))) {

                // Write report header
                writer.println("Payment Report");
                writer.println("==============");
                writer.println();
                writer.printf("%-10s %-10s %-30s %-15s %-10s%n", 
                        "PaymentID", "FineID", "Violation", "Payment Date", "Amount");
                writer.println("--------------------------------------------------------------------------------");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                // Process the query results
                while (rs.next()) {
                    int paymentId = rs.getInt("payment_id");
                    int fineId = rs.getInt("fine_id");
                    String violation = rs.getString("violation");
                    java.sql.Date paymentDate = rs.getDate("payment_date");
                    double amount = rs.getDouble("amount");

                    writer.printf("%-10d %-10d %-30s %-15s %-10.2f%n", 
                            paymentId, fineId, violation, 
                            paymentDate.toLocalDate().format(formatter), amount);
                }

                writer.println();
                writer.println("End of Report");
                System.out.println("Report generated successfully: " + reportFile.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Error writing report: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.err.println("Error fetching report data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
