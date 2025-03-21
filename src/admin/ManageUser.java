package admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class ManageUser {

    private TableView<User> table;
    private TextField usernameField, passwordField;
    private ComboBox<String> roleComboBox;

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Manage Users");

        // Table setup
        table = new TableView<>();
        TableColumn<User, Integer> idColumn = new TableColumn<>("User ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));

        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        table.getColumns().addAll(idColumn, usernameColumn, roleColumn);

        // Input fields
        usernameField = new TextField();
        usernameField.setPromptText("Username");

        passwordField = new TextField();
        passwordField.setPromptText("Password");

        roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Admin", "traffic", "User");
        roleComboBox.setPromptText("Role");

        // Buttons
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> addUser());

        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> updateUser());

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> deleteUser());

        HBox buttonBox = new HBox(10, addButton, updateButton, deleteButton);

        // Layout
        VBox vbox = new VBox(10, table, usernameField, passwordField, roleComboBox, buttonBox);
        Scene scene = new Scene(vbox, 400, 300);
        stage.setScene(scene);
        stage.show();

        // Load data
        loadData();
    }

    private void loadData() {
        ObservableList<User> users = FXCollections.observableArrayList();
        String query = "SELECT * FROM users";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("role")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        table.setItems(users);
    }

    private void addUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();

        if (username.isEmpty() || password.isEmpty() || role == null) {
            showAlert("Input Error", "Please fill all fields.");
            return;
        }

        String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            pstmt.executeUpdate();

            loadData();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateUser() {
        User selectedUser = table.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            showAlert("Selection Error", "Please select a user to update.");
            return;
        }

        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();

        if (username.isEmpty() || password.isEmpty() || role == null) {
            showAlert("Input Error", "Please fill all fields.");
            return;
        }

        String query = "UPDATE users SET username = ?, password = ?, role = ? WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            pstmt.setInt(4, selectedUser.getUserId());
            pstmt.executeUpdate();

            loadData();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteUser() {
        User selectedUser = table.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            showAlert("Selection Error", "Please select a user to delete.");
            return;
        }

        String query = "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, selectedUser.getUserId());
            pstmt.executeUpdate();

            loadData();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
        roleComboBox.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class User {
        private final int userId;
        private final String username;
        private final String role;

        public User(int userId, String username, String role) {
            this.userId = userId;
            this.username = username;
            this.role = role;
        }

        public int getUserId() {
            return userId;
        }

        public String getUsername() {
            return username;
        }

        public String getRole() {
            return role;
        }
    }
}