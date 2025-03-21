package admin;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import user.User;

import java.sql.*;

public class ManageUsersApp extends Application {

    private TableView<User> userTable;
    private ObservableList<User> userList;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/traffic_fine";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Sadip123@"; // Replace with your MySQL password

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Manage Users");

        // Create a TableView to display users
        userTable = new TableView<>();
        userList = FXCollections.observableArrayList();
        userTable.setItems(userList);

        // Create table columns
        TableColumn<User, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> data.getValue().idProperty().asObject());

        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> data.getValue().nameProperty());

        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(data -> data.getValue().emailProperty());

        // Add columns to TableView
        userTable.getColumns().addAll(idCol, nameCol, emailCol);

        // Buttons for actions
        Button addUserButton = new Button("Add User");
        addUserButton.setOnAction(e -> showAddUserDialog());

        Button removeUserButton = new Button("Remove User");
        removeUserButton.setOnAction(e -> handleRemoveUser());

        Button returnButton = new Button("Return");
        returnButton.setOnAction(e -> primaryStage.close());

        // Organize buttons in a VBox
        VBox buttonBox = new VBox(10, addUserButton, removeUserButton, returnButton);
        buttonBox.setPadding(new Insets(15));

        // Main layout
        BorderPane root = new BorderPane();
        root.setCenter(userTable);
        root.setRight(buttonBox);

        // Load users from the database
        loadUsersFromDatabase();

        // Set the scene and show the stage
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Loads users from the database into the TableView
     */
    private void loadUsersFromDatabase() {
        userList.clear();
        String query = "SELECT * FROM users";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                userList.add(new User(id, name, email));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load users from the database.");
        }
    }

    /**
     * Shows a dialog to add a new user
     */
    private void showAddUserDialog() {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Add User");

        // Set up the dialog buttons
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create input fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameField = new TextField();
        TextField emailField = new TextField();

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a User object when the Save button is clicked
        dialog.setResultConverter(buttonType -> {
            if (buttonType == saveButtonType) {
                return new User(-1, nameField.getText(), emailField.getText());
            }
            return null;
        });

        // Handle the Save button action
        dialog.showAndWait().ifPresent(user -> {
            if (addUserToDatabase(user)) {
                loadUsersFromDatabase(); // Refresh the table
            }
        });
    }

    /**
     * Adds a user to the database
     */
    private boolean addUserToDatabase(User user) {
        String query = "INSERT INTO users (name, email) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add user to the database.");
            return false;
        }
    }

    /**
     * Handles the Remove User button action
     */
    private void handleRemoveUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a user to remove.");
            return;
        }

        if (removeUserFromDatabase(selectedUser)) {
            userList.remove(selectedUser); // Remove from the TableView
        }
    }

    /**
     * Removes a user from the database
     */
    private boolean removeUserFromDatabase(User user) {
        String query = "DELETE FROM users WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, user.getId());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to remove user from the database.");
            return false;
        }
    }

    /**
     * Shows an alert dialog
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args); // Launch JavaFX application
    }
}