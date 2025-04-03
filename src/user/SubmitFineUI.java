package user;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SubmitFineUI extends Application {
    private int userId; // Store userId

    // Proper no-argument constructor (required for JavaFX)
    public SubmitFineUI() {
    }

    // Constructor with userId parameter
    public SubmitFineUI(int userId) {
        this.userId = userId;
    }

    // Setter method to set userId
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Submit Fine Payment");

        // Labels and TextFields for fine submission
        Label fineIdLabel = new Label("Enter Fine ID:");
        TextField fineIdField = new TextField();

        Label amountLabel = new Label("Fine Amount:");
        TextField amountField = new TextField();

        Label violationLabel = new Label("Violation:");
        TextField violationField = new TextField();
        
        Label issueDateLabel = new Label("Payment Date (YYYY-MM-DD):");
        TextField issueDateField = new TextField();

        // Note: Status field removed

        // QR Code Image - Load from resources
        ImageView qrImageView;
        try {
            // Load image from resources
            Image qrImage = new Image(getClass().getResourceAsStream("qr.png"));
            qrImageView = new ImageView(qrImage);
        } catch (Exception e) {
            System.err.println("Error loading QR image: " + e.getMessage());
            // Fallback to placeholder if image not found
            qrImageView = new ImageView(new Image("https://via.placeholder.com/100"));
        }
        qrImageView.setFitWidth(120);
        qrImageView.setFitHeight(120);

        Label sewaLabel = new Label("Sewa");

        // Buttons
        Button submitButton = new Button("Submit");
        Button returnButton = new Button("Return");

        returnButton.setOnAction(e -> {
            primaryStage.close(); // Close the current window

            // Open UserScreen with userId
            UserScreen userDashboard = new UserScreen(userId); // Pass the userId
            Stage userStage = new Stage();
            try {
                userDashboard.start(userStage);
            } catch (Exception ex) {
                System.err.println("Error opening UserScreen: " + ex.getMessage());
            }
        });

        // Submit Button Action: Insert payment record into payments table
        submitButton.setOnAction(e -> {
            try {
                // Get input values
                int fineId = Integer.parseInt(fineIdField.getText());
                double amount = Double.parseDouble(amountField.getText());
                // Although violation is collected, it is not used in payment insertion.
                String violation = violationField.getText();  
                String paymentDate = issueDateField.getText();  // Using issueDateField as payment_date

                // Insert payment data into the payments table
                insertPaymentIntoDatabase(fineId, paymentDate, amount);

                // Clear fields after submission
                fineIdField.clear();
                amountField.clear();
                violationField.clear();
                issueDateField.clear();

                System.out.println("Payment submitted successfully!");
            } catch (NumberFormatException ex) {
                System.out.println("Invalid input. Please enter valid numbers for Fine ID and Amount.");
            } catch (SQLException ex) {
                System.out.println("Error inserting payment into the database: " + ex.getMessage());
            }
        });

        // Layout for form inputs
        GridPane formLayout = new GridPane();
        formLayout.setPadding(new Insets(10));
        formLayout.setHgap(10);
        formLayout.setVgap(10);

        formLayout.add(fineIdLabel, 0, 0);
        formLayout.add(fineIdField, 1, 0);
        formLayout.add(amountLabel, 0, 1);
        formLayout.add(amountField, 1, 1);
        formLayout.add(violationLabel, 0, 2);
        formLayout.add(violationField, 1, 2);
        formLayout.add(issueDateLabel, 0, 3);
        formLayout.add(issueDateField, 1, 3);
        // Status field removed

        // QR Code and buttons layout
        VBox qrBox = new VBox(5, qrImageView, sewaLabel, submitButton, returnButton);
        qrBox.setAlignment(Pos.CENTER);

        // Main Layout
        VBox mainLayout = new VBox(10, formLayout, qrBox);
        mainLayout.setPadding(new Insets(15));
        mainLayout.setAlignment(Pos.CENTER);

        // Scene
        Scene scene = new Scene(mainLayout, 400, 550);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to insert payment data into the payments table
    private void insertPaymentIntoDatabase(int fineId, String paymentDate, double amount) throws SQLException {
        // Database connection details (make sure the database name is correct)
        String url = "jdbc:mysql://localhost:3306/traffic_fine";
        String username = "root";
        String password = "Sadip123@";

        // SQL query to insert data into payments table
        String query = "INSERT INTO payments(fine_id, payment_date, amount) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set parameters for the query
            preparedStatement.setInt(1, fineId);
            preparedStatement.setDate(2, java.sql.Date.valueOf(paymentDate));
            preparedStatement.setDouble(3, amount);

            // Execute the query
            preparedStatement.executeUpdate();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
