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
import javafx.stage.Stage;
import traffic.TrafficPoliceDashboard;
import user.UserScreen;

public class LoginScreen extends Application {

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/traffic_fine";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Sadip123@";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login Screen");

        // Create input fields
        TextField usernameField = createTextField("Enter your username");
        PasswordField passwordField = createPasswordField("Enter your password");
        ComboBox<String> roleComboBox = createRoleComboBox();

        // Create layout for input fields
        GridPane inputGrid = createInputGrid(usernameField, passwordField, roleComboBox);

        // Create buttons
        Button loginBtn = createButton("Login", event -> handleLogin(usernameField, passwordField, roleComboBox, primaryStage));
        Button signUpBtn = createButton("Sign Up", event -> openSignupScreen());

        // Create button layout
        HBox buttonBox = new HBox(10, loginBtn, signUpBtn);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 10, 0));

        // Create main layout
        VBox root = new VBox(20, new Label("Login Screen"), inputGrid, buttonBox);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #f4f7f6;");

        // Set scene and show stage
        Scene scene = new Scene(root, 400, 350);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to validate login credentials
    private Map<String, String> validateLogin(String username, String password, String role) {
        String sql = "SELECT user_id, role FROM users WHERE username = ? AND password = ? AND role = ?";
        Map<String, String> result = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                result.put("role", rs.getString("role"));
                result.put("user_id", rs.getString("user_id"));
                return result;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // Handle login action
    private void handleLogin(TextField usernameField, PasswordField passwordField, ComboBox<String> roleComboBox, Stage primaryStage) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String role = roleComboBox.getValue().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        Map<String, String> loginResult = validateLogin(username, password, role);
        if (loginResult != null) {
            try {
                int userId = Integer.parseInt(loginResult.get("user_id"));
                showAlert("Login Successful", "Welcome, " + username + "\nYou are logged in as: " + role);
                openDashboard(role, userId, primaryStage);
            } catch (NumberFormatException e) {
                showAlert("Error", "Invalid user ID format.");
            }
        } else {
            showAlert("Login Failed", "Invalid Username, Password, or Role.");
        }
    }

    // Open the appropriate dashboard based on role
    private void openDashboard(String role, int userId, Stage primaryStage) {
        primaryStage.close(); // Close login screen

        switch (role) {
            case "Admin":
                new AdminDashboard().showDashboard();
                break;
            case "Traffic":
                new TrafficPoliceDashboard().showDashboard();
                break;
            case "User":
                UserScreen userScreen = new UserScreen(userId);
                userScreen.start(new Stage()); // Start UserScreen with new Stage
                break;
            default:
                showAlert("Error", "Invalid role.");
        }
    }

    // Open the signup screen
    private void openSignupScreen() {
        new SignupScreen().showSignupForm();
    }

    // Show alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Create input field helpers
    private TextField createTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        return textField;
    }

    private PasswordField createPasswordField(String promptText) {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText(promptText);
        return passwordField;
    }

    private ComboBox<String> createRoleComboBox() {
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Admin", "Traffic", "User");
        roleComboBox.setValue("User");
        return roleComboBox;
    }

    private GridPane createInputGrid(TextField usernameField, PasswordField passwordField, ComboBox<String> roleComboBox) {
        GridPane inputGrid = new GridPane();
        inputGrid.setVgap(10);
        inputGrid.setHgap(10);
        inputGrid.setPadding(new Insets(20));
        inputGrid.add(new Label("Username:"), 0, 0);
        inputGrid.add(usernameField, 1, 0);
        inputGrid.add(new Label("Password:"), 0, 1);
        inputGrid.add(passwordField, 1, 1);
        inputGrid.add(new Label("Role:"), 0, 2);
        inputGrid.add(roleComboBox, 1, 2);
        return inputGrid;
    }

    private Button createButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> event) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        button.setOnAction(event);
        return button;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
