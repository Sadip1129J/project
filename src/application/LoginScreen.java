package application;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import admin.AdminDashboard;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import traffic.TrafficPoliceDashboard;
import user.UserScreen;

public class LoginScreen extends Application {

    // Method to validate login credentials
    public static Map<String, String> validateLogin(String username, String password, String role) {
        String sql = "SELECT user_id, role FROM users WHERE username = ? AND password = ? AND role = ?";
        Map<String, String> result = new HashMap<>();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/traffic_fine", "root", "Sadip123@");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String userId = rs.getString("user_id");  // Retrieve user_id as a String
                String userRole = rs.getString("role");

                if (userId != null && userRole != null) {
                    result.put("role", userRole);
                    result.put("user_id", userId); // Add user_id to the result
                    return result;
                }
            }

        } catch (SQLException ex) {
            System.out.println("Login error: " + ex.getMessage());
        }
        return null;  // Return null if login fails
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login Screen");

        // Username and Password Fields
        Label userLabel = new Label("Username:");
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

        Label passLabel = new Label("Password:");
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

        // Role Selection
        Label roleLabel = new Label("Role:");
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Admin", "Traffic", "User");  // Add roles from the ENUM
        roleComboBox.setValue("User");  // Set default role

        // Grid Layout for the input fields
        GridPane inputGrid = new GridPane();
        inputGrid.setVgap(10);
        inputGrid.setHgap(10);
        inputGrid.setPadding(new Insets(20));
        inputGrid.add(userLabel, 0, 0);
        inputGrid.add(usernameField, 1, 0);
        inputGrid.add(passLabel, 0, 1);
        inputGrid.add(passwordField, 1, 1);
        inputGrid.add(roleLabel, 0, 2);
        inputGrid.add(roleComboBox, 1, 2);

        // Buttons
        Button loginBtn = new Button("Login");
        Button signUpBtn = new Button("Sign Up");

        // Style buttons
        loginBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-radius: 5px;");
        signUpBtn.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white; -fx-border-radius: 5px;");
        
        loginBtn.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String role = roleComboBox.getValue();  // Get selected role

            if (username.isEmpty() || password.isEmpty()) {
                showAlert("Error", "Please fill in all fields.");
            } else {
                // Validate login with role
                Map<String, String> loginResult = validateLogin(username, password, role);

                if (loginResult != null) {
                    String validatedRole = loginResult.get("role");
                    String userIdStr = loginResult.get("user_id");  // Get user_id as a String

                    if (userIdStr != null) {
                        try {
                            int userId = Integer.parseInt(userIdStr);  // Parse user_id to int
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Login Successful");
                            alert.setHeaderText("Welcome, " + username);
                            alert.setContentText("You are logged in as: " + validatedRole);
                            alert.showAndWait();

                            // Open the appropriate screen based on role
                            switch (validatedRole) {
                                case "Admin":
                                    openAdminScreen();
                                    break;
                                case "Traffic":
                                    openOperatorScreen();
                                    break;
                                case "User":
                                    openUserScreen(userId);  // Pass user_id to UserScreen
                                    break;
                                default:
                                    showAlert("Error", "Invalid role.");
                            }

                        } catch (NumberFormatException e) {
                            showAlert("Error", "Invalid user ID format. Please contact support.");
                        }
                    } else {
                        showAlert("Error", "User ID not found. Please contact support.");
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Login Failed");
                    alert.setHeaderText("Invalid Username, Password, or Role");
                    alert.setContentText("Please try again.");
                    alert.showAndWait();
                }
            }
        });

        // SignUp Button Action
        signUpBtn.setOnAction(event -> {
            SignupScreen signupScreen = new SignupScreen();
            signupScreen.showSignupForm();  // Open the signup form
        });

        // Horizontal Box for buttons
        HBox buttonBox = new HBox(10, loginBtn, signUpBtn);
        buttonBox.setAlignment(Pos.CENTER);  // Use Pos.CENTER for alignment
        buttonBox.setPadding(new Insets(20, 0, 10, 0));

        // Main Layout with Background color
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);  // Use Pos.CENTER for alignment
        root.setStyle("-fx-background-color: #f4f7f6;");  // Light background color
        root.getChildren().addAll(new Label("Login Screen"), inputGrid, buttonBox);

        Scene scene = new Scene(root, 400, 350);  // Increased height to accommodate role selection
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Methods to open different screens after login
    private void openAdminScreen() {
        AdminDashboard adminDashboard = new AdminDashboard();
        adminDashboard.showDashboard();
    }

    private void openOperatorScreen() {
        TrafficPoliceDashboard operatorDashboard = new TrafficPoliceDashboard();
        operatorDashboard.showDashboard();
    }

    private void openUserScreen(int userId) {
        UserScreen userScreen = new UserScreen(userId);  // Pass user_id to UserScreen
        userScreen.showDashboard();
    }

    // Method to show alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Main method to launch the application
    public static void main(String[] args) {
        launch(args);
    }
}
