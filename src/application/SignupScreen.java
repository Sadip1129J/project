package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SignupScreen extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Signup Form");

        // Form Fields
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setStyle("-fx-background-color: #f0f0f0; -fx-border-radius: 5px; -fx-border-color: #aaa;");

        usernameField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                usernameField.setStyle("-fx-background-color: #d9f7ff; -fx-border-radius: 5px; -fx-border-color: #3a9fd1;");
            } else {
                usernameField.setStyle("-fx-background-color: #f0f0f0; -fx-border-radius: 5px; -fx-border-color: #aaa;");
            }
        });

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setStyle("-fx-background-color: #f0f0f0; -fx-border-radius: 5px; -fx-border-color: #aaa;");

        passwordField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                passwordField.setStyle("-fx-background-color: #d9f7ff; -fx-border-radius: 5px; -fx-border-color: #3a9fd1;");
            } else {
                passwordField.setStyle("-fx-background-color: #f0f0f0; -fx-border-radius: 5px; -fx-border-color: #aaa;");
            }
        });

        Label roleLabel = new Label("Role:");
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Admin", "User", "Traffic");
        roleComboBox.setValue("User"); // Default role

        // Buttons
        Button signupButton = new Button("Sign Up");
        Button backButton = new Button("Back to Login");

        // Button Styling
        signupButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-radius: 5px;");
        backButton.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white; -fx-border-radius: 5px;");

        signupButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String role = roleComboBox.getValue();

            if (username.isEmpty() || password.isEmpty()) {
                showAlert("Error", "Please fill in all fields.");
            } else {
                if (saveUserToDatabase(username, password, role)) {
                    showAlert("Success", "User registered successfully!");
                    primaryStage.close(); // Close the signup form after successful registration
                } else {
                    showAlert("Error", "Failed to register user.");
                }
            }
        });

        // Back Button Action
        backButton.setOnAction(event -> {
            primaryStage.close(); // Close the signup form
            new LoginScreen().start(new Stage()); // Open the login screen
        });

        // Layout
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));
        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(roleLabel, 0, 2);
        grid.add(roleComboBox, 1, 2);
        grid.add(signupButton, 1, 3);
        grid.add(backButton, 1, 4);

        // Centered Layout with Background Color
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #f4f7f6;");  // Light background color
        root.getChildren().add(grid);

        // Scene
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to save user data to the database
    private boolean saveUserToDatabase(String username, String password, String role) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/traffic_fine", "root", "Sadip123@");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);

            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0; // Return true if the user was successfully saved

        } catch (SQLException ex) {
            System.out.println("Database error: " + ex.getMessage());
            return false;
        }
    }

    // Method to show alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to show the signup form (called from LoginScreen)
    public void showSignupForm() {
        start(new Stage());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
