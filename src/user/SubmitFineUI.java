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
    private int userId; // Add userId field

    // Constructor to accept userId
    public SubmitFineUI(int userId) {
        this.userId = userId;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Submit Fine");

        // Labels and TextFields
        Label fineIdLabel = new Label("Enter Fine ID:");
        TextField fineIdField = new TextField();

        Label amountLabel = new Label("Fine Amount:");
        TextField amountField = new TextField();

        Label violationLabel = new Label("Violation:");
        TextField violationField = new TextField();

        Label issueDateLabel = new Label("Issue Date (YYYY-MM-DD):");
        TextField issueDateField = new TextField();

        Label statusLabel = new Label("Status:");
        TextField statusField = new TextField();

        // QR Code Image (Replace with actual QR image URL if needed)
        Image qrImage = new Image("https://via.placeholder.com/100"); // Placeholder QR
        ImageView qrImageView = new ImageView(qrImage);
        qrImageView.setFitWidth(120);
        qrImageView.setFitHeight(120);

        Label sewaLabel = new Label("Sewa");

        // Buttons
        Button submitButton = new Button("Submit");
        Button returnButton = new Button("Return");

        returnButton.setOnAction(e -> primaryStage.close());

        // Submit Button Action
        submitButton.setOnAction(e -> {
            try {
                // Get input values
                int fineId = Integer.parseInt(fineIdField.getText());
                double amount = Double.parseDouble(amountField.getText());
                String violation = violationField.getText();
                String issueDate = issueDateField.getText();
                String status = statusField.getText();

                // Insert data into the database
                insertFineIntoDatabase(fineId, userId, amount, violation, issueDate, status);

                // Clear fields after submission
                fineIdField.clear();
                amountField.clear();
                violationField.clear();
                issueDateField.clear();
                statusField.clear();

                System.out.println("Fine submitted successfully!");
            } catch (NumberFormatException ex) {
                System.out.println("Invalid input. Please enter valid numbers for Fine ID and Amount.");
            } catch (SQLException ex) {
                System.out.println("Error inserting fine into the database: " + ex.getMessage());
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
        formLayout.add(statusLabel, 0, 4);
        formLayout.add(statusField, 1, 4);

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

    // Method to insert fine data into the database
    private void insertFineIntoDatabase(int fineId, int userId, double amount, String violation, String issueDate, String status) throws SQLException {
        // Database connection details
        String url = "jdbc:mysql://localhost:3306/traffic_fines"; // Replace with your database name
        String username = "root"; // Replace with your MySQL username
        String password = "Sadip123@"; // Replace with your MySQL password

        // SQL query to insert data
        String query = "INSERT INTO fines (fine_id, user_id, amount, violation, issue_date, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set parameters for the query
            preparedStatement.setInt(1, fineId);
            preparedStatement.setInt(2, userId);
            preparedStatement.setDouble(3, amount);
            preparedStatement.setString(4, violation);
            preparedStatement.setString(5, issueDate);
            preparedStatement.setString(6, status);

            // Execute the query
            preparedStatement.executeUpdate();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}