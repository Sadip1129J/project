package traffic;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDate;

public class IssueFinesApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Issue Fines");

        // Create a GridPane layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        // Name
        Label nameLabel = new Label("Name:");
        GridPane.setConstraints(nameLabel, 0, 0);
        TextField nameField = new TextField();
        GridPane.setConstraints(nameField, 1, 0);

        // User ID (for the person receiving the fine)
        Label userIdLabel = new Label("User ID:");
        GridPane.setConstraints(userIdLabel, 0, 1);
        TextField userIdField = new TextField();
        GridPane.setConstraints(userIdField, 1, 1);

        // Type of Violation
        Label violationLabel = new Label("Type of Violation:");
        GridPane.setConstraints(violationLabel, 0, 2);
        TextField violationField = new TextField();
        GridPane.setConstraints(violationField, 1, 2);

        // Issue Date
        Label dateLabel = new Label("Issue Date:");
        GridPane.setConstraints(dateLabel, 0, 3);
        DatePicker datePicker = new DatePicker();
        GridPane.setConstraints(datePicker, 1, 3);

        // Address
        Label addressLabel = new Label("Address:");
        GridPane.setConstraints(addressLabel, 0, 4);
        TextField addressField = new TextField();
        GridPane.setConstraints(addressField, 1, 4);

        // Traffic ID
        Label trafficLabel = new Label("Traffic ID:");
        GridPane.setConstraints(trafficLabel, 0, 5);
        TextField trafficField = new TextField();
        GridPane.setConstraints(trafficField, 1, 5);

        // Fine Amount
        Label amountLabel = new Label("Fine Amount:");
        GridPane.setConstraints(amountLabel, 0, 6);
        TextField amountField = new TextField();
        GridPane.setConstraints(amountField, 1, 6);

        // Submit Button
        Button submitButton = new Button("Submit");
        GridPane.setConstraints(submitButton, 1, 7);
        submitButton.setOnAction(e -> {
            boolean success = insertFine(userIdField.getText(), trafficField.getText(), violationField.getText(), datePicker.getValue(), amountField.getText());
            if (success) {
                showAlert("Success", "Fine issued successfully!");
                goToDashboard(primaryStage);
            } else {
                showAlert("Error", "Failed to issue fine. Please try again.");
            }
        });

        // Return Button
        Button returnButton = new Button("Return");
        GridPane.setConstraints(returnButton, 1, 8);
        returnButton.setOnAction(e -> goToDashboard(primaryStage));

        // Add all components to the grid
        grid.getChildren().addAll(nameLabel, nameField, userIdLabel, userIdField, violationLabel, violationField, dateLabel, datePicker,
                addressLabel, addressField, trafficLabel, trafficField, amountLabel, amountField, submitButton, returnButton);

        // Create a scene with the grid as the root node
        Scene scene = new Scene(grid, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean insertFine(String userId, String trafficId, String violation, LocalDate issueDate, String amount) {
        String url = "jdbc:mysql://localhost:3306/traffic_fine";
        String user = "root";
        String password = "Sadip123@";

        // SQL query no longer includes officer_id
        String query = "INSERT INTO fines (user_id, amount, violation, issue_date, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, Integer.parseInt(userId)); // User ID (the person receiving the fine)
            stmt.setDouble(2, Double.parseDouble(amount)); // Fine amount
            stmt.setString(3, violation);
            stmt.setDate(4, java.sql.Date.valueOf(issueDate));
            stmt.setString(5, "Pending"); // Set the status of the fine

            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void goToDashboard(Stage primaryStage) {
        TrafficPoliceDashboard dashboard = new TrafficPoliceDashboard();
        dashboard.showDashboard();
        primaryStage.close(); // Close current window after navigating
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
