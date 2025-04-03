package admin;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminDashboard extends Application {

    public AdminDashboard() {
        // Constructor (if needed for initialization)
    }

    @Override
    public void start(Stage primaryStage) {
        // Creating UI components
        Label titleLabel = new Label("Admin Screen");

        Button manageUsersButton = new Button("Manage Users");
        Button generateReportButton = new Button("Generate Report");
        Button viewIssuedFinesButton = new Button("View Issued Fines");
        Button logoutButton = new Button("Logout");

        // Button Actions
        manageUsersButton.setOnAction(event -> {
            ManageUser mu = new ManageUser();
            mu.show();
        });

        generateReportButton.setOnAction(event -> {
            // Call the report generation method (assumes ReportGenerator.generatePaymentReport() is static)
            ReportGenerator.generatePaymentReport();
            
            // Display an alert to notify that the report has been generated
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Report Generation");
            alert.setHeaderText(null);
            alert.setContentText("The report has been generated successfully.");
            alert.showAndWait();
        });

        viewIssuedFinesButton.setOnAction(event -> {
            System.out.println("View Issued Fines button clicked");
            // Create and show the ViewIssuedFinesScreen
            ViewIssuedFinesScreen viewIssuedFinesScreen = new ViewIssuedFinesScreen();
            viewIssuedFinesScreen.show();
        });

        logoutButton.setOnAction(event -> {
            // Close the admin dashboard and return to the login screen
            ((Stage) logoutButton.getScene().getWindow()).close();
        });

        // Layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(titleLabel, manageUsersButton, generateReportButton, viewIssuedFinesButton, logoutButton);
        layout.setSpacing(10);
        layout.setStyle("-fx-padding: 20px;");

        // Scene setup
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Admin Dashboard");
        primaryStage.show();
    }

    // Method to show the dashboard (called from LoginScreen)
    public void showDashboard() {
        start(new Stage());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
