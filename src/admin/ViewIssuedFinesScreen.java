package admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewIssuedFinesScreen extends Stage {

    private TableView<Fine> fineTable;

    public ViewIssuedFinesScreen() {
        setTitle("View Issued Fines");

        // Creating a TableView to display fine details
        fineTable = new TableView<>();

        // Creating table columns
        TableColumn<Fine, Integer> fineIdCol = new TableColumn<>("Fine ID");
        fineIdCol.setCellValueFactory(new PropertyValueFactory<>("fineId"));

        TableColumn<Fine, Integer> userIdCol = new TableColumn<>("User ID");
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));

        TableColumn<Fine, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Fine, String> violationCol = new TableColumn<>("Violation");
        violationCol.setCellValueFactory(new PropertyValueFactory<>("violation"));

        TableColumn<Fine, String> issueDateCol = new TableColumn<>("Issue Date");
        issueDateCol.setCellValueFactory(new PropertyValueFactory<>("issueDate"));

        TableColumn<Fine, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Adding columns to TableView
        fineTable.getColumns().addAll(fineIdCol, userIdCol, amountCol, violationCol, issueDateCol, statusCol);

        // Fetch fines from the database and populate the table
        fineTable.setItems(fetchFinesFromDatabase());

        // Buttons
        Button makePaymentButton = new Button("Make Payment");
        makePaymentButton.setOnAction(e -> handleMakePayment());

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> fineTable.setItems(fetchFinesFromDatabase()));

        Button returnButton = new Button("Return");
        returnButton.setOnAction(e -> this.close()); // Close the current window

        // Organizing UI elements inside a VBox layout
        VBox vbox = new VBox(10, fineTable, makePaymentButton, refreshButton, returnButton);
        vbox.setPadding(new Insets(15));

        // Creating a scene and setting it in the stage
        Scene scene = new Scene(vbox, 800, 600);
        setScene(scene);
    }

    /**
     * Fetches fines from the database and returns them as an ObservableList.
     */
    private ObservableList<Fine> fetchFinesFromDatabase() {
        ObservableList<Fine> fines = FXCollections.observableArrayList();

        String sql = "SELECT fine_id, user_id, amount, violation, issue_date, status FROM fines";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/traffic_fine", "root", "Sadip123@");
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int fineId = rs.getInt("fine_id");
                int userId = rs.getInt("user_id");
                double amount = rs.getDouble("amount");
                String violation = rs.getString("violation");
                String issueDate = rs.getDate("issue_date").toString();
                String status = rs.getString("status");

                fines.add(new Fine(fineId, userId, amount, violation, issueDate, status));
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error fetching fines from the database: " + e.getMessage());
        }

        return fines;
    }

    /**
     * Handles the "Make Payment" button action.
     */
    private void handleMakePayment() {
        Fine selectedFine = fineTable.getSelectionModel().getSelectedItem();
        if (selectedFine != null) {
            // Update the status of the selected fine to "Paid"
            updateFineStatus(selectedFine.getFineId(), "Paid");
            // Refresh the table
            fineTable.setItems(fetchFinesFromDatabase());
        } else {
            System.out.println("No fine selected.");
        }
    }

    /**
     * Updates the status of a fine in the database.
     */
    private void updateFineStatus(int fineId, String status) {
        String sql = "UPDATE fines SET status = ? WHERE fine_id = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/traffic_fine", "root", "Sadip123@");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, fineId);

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Fine status updated successfully.");
            } else {
                System.out.println("Failed to update fine status.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error updating fine status: " + e.getMessage());
        }
    }

    /**
     * Fine model class representing a fine record
     */
    public static class Fine {
        private final int fineId;
        private final int userId;
        private final double amount;
        private final String violation;
        private final String issueDate;
        private String status; // Non-final to allow updates

        public Fine(int fineId, int userId, double amount, String violation, String issueDate, String status) {
            this.fineId = fineId;
            this.userId = userId;
            this.amount = amount;
            this.violation = violation;
            this.issueDate = issueDate;
            this.status = status;
        }

        public int getFineId() { return fineId; }
        public int getUserId() { return userId; }
        public double getAmount() { return amount; }
        public String getViolation() { return violation; }
        public String getIssueDate() { return issueDate; }
        public String getStatus() { return status; }

        public void setStatus(String status) { this.status = status; }
    }
}