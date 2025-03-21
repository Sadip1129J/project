package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ButtonHandlers {

    private TableView<Fine> fineTable;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/traffic_fine";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Sadip123@"; // Add your MySQL password

    public ButtonHandlers(TableView<Fine> fineTable) {
        this.fineTable = fineTable;
    }

    /**
     * Handles the Display Fines button action
     */
    public ObservableList<Fine> handleDisplayFines() {
        return fetchFineDataFromDatabase();
    }

    /**
     * Handles the Make Payment button action
     */
    public void handleMakePayment() {
        Fine selectedFine = fineTable.getSelectionModel().getSelectedItem();

        if (selectedFine == null) {
            showAlert(Alert.AlertType.ERROR, "No Selection", "Please select a fine to make a payment.");
            return;
        }

        if ("Paid".equals(selectedFine.getStatus())) {
            showAlert(Alert.AlertType.WARNING, "Already Paid", "This fine is already marked as paid.");
            return;
        }

        boolean paymentSuccess = processPayment(selectedFine);
        if (paymentSuccess) {
            selectedFine.setStatus("Paid"); // Update status to Paid
            updateFineStatusInDatabase(selectedFine); // Update status in the database
            fineTable.setItems(fetchFineDataFromDatabase()); // Refresh table data
            fineTable.refresh(); // Refresh table to reflect the updated status
            showAlert(Alert.AlertType.INFORMATION, "Payment Successful", "The fine has been successfully paid.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Payment Failed", "There was an issue processing the payment.");
        }
    }

    /**
     * Simulates payment processing logic
     */
    private boolean processPayment(Fine fine) {
        // Simulate a successful payment
        return true; // Assuming payment succeeds
    }

    /**
     * Handles the Return button action
     */
    public void handleReturn(Stage stage) {
        stage.close(); // Close the current stage
    }

    /**
     * Fetches fine data from the database
     */
    private ObservableList<Fine> fetchFineDataFromDatabase() {
        ObservableList<Fine> fineList = FXCollections.observableArrayList();
        String query = "SELECT fine_id, user_id, name, amount, status FROM payments";

        try (Connection connection = getDatabaseConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int fineId = resultSet.getInt("fine_id");
                String userId = resultSet.getString("user_id");
                String name = resultSet.getString("name");
                double amount = resultSet.getDouble("amount");
                String status = resultSet.getString("status");

                fineList.add(new Fine(fineId, userId, name, amount, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to fetch data from the database.");
        }

        return fineList;
    }

    /**
     * Updates the status of a fine in the database
     */
    private void updateFineStatusInDatabase(Fine fine) {
        String query = "UPDATE payments SET status = ? WHERE fine_id = ?";

        try (Connection connection = getDatabaseConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, fine.getStatus());
            preparedStatement.setInt(2, fine.getFineId());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Fine status updated successfully.");
            } else {
                System.out.println("Failed to update fine status.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update fine status in the database.");
        }
    }

    /**
     * Helper method to establish a database connection
     */
    private Connection getDatabaseConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
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

    /**
     * Checks if the database connection is successful
     */
    public boolean isDatabaseConnected() {
        try (Connection connection = getDatabaseConnection()) {
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to connect to the database.");
            return false;
        }
    }

    /**
     * Inner class representing a Fine
     */
    public static class Fine {
        private final int fineId;
        private final String userId;
        private final String name;
        private final double amount;
        private String status;

        public Fine(int fineId, String userId, String name, double amount, String status) {
            this.fineId = fineId;
            this.userId = userId;
            this.name = name;
            this.amount = amount;
            this.status = status;
        }

        public int getFineId() {
            return fineId;
        }

        public String getUserId() {
            return userId;
        }

        public String getName() {
            return name;
        }

        public double getAmount() {
            return amount;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}