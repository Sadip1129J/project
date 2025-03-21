package traffic;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TrafficFineApp extends Application {

    // Database connection details
    final String DRIVER = "com.mysql.cj.jdbc.Driver";
    final String HOST = "localhost";
    final int PORT = 3306;
    final String DBNAME = "traffic_fine";
    final String USER = "root";
    final String PASS = "Sadip@123";
    final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DBNAME;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Traffic Fine Management System");

        // Create TableView to display fines
        TableView<Fine> tableView = new TableView<>();

        // Define columns for the TableView
        TableColumn<Fine, Integer> fineIdCol = new TableColumn<>("Fine ID");
        fineIdCol.setCellValueFactory(new PropertyValueFactory<>("fineId"));

        TableColumn<Fine, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Fine, String> violationCol = new TableColumn<>("Violation");
        violationCol.setCellValueFactory(new PropertyValueFactory<>("violation"));

        TableColumn<Fine, String> issueDateCol = new TableColumn<>("Issue Date");
        issueDateCol.setCellValueFactory(new PropertyValueFactory<>("issueDate"));

        TableColumn<Fine, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Add columns to the TableView
        tableView.getColumns().addAll(fineIdCol, amountCol, violationCol, issueDateCol, statusCol);

        // Fetch data from the database and populate the TableView
        ObservableList<Fine> fines = fetchFinesFromDatabase();
        tableView.setItems(fines);

        // Layout
        VBox vbox = new VBox(10, tableView);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Fetch fines from the database
    private ObservableList<Fine> fetchFinesFromDatabase() {
        ObservableList<Fine> fines = FXCollections.observableArrayList();
        String sql = "SELECT * FROM fines"; // Assuming the table name is 'fines'

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int fineId = rs.getInt("fine_id");
                double amount = rs.getDouble("amount");
                String violation = rs.getString("violation");
                String issueDate = rs.getString("issue_date");
                String status = rs.getString("status");

                fines.add(new Fine(fineId, amount, violation, issueDate, status));
            }
        } catch (SQLException ex) {
            System.out.println("Error fetching fines from the database: " + ex.getMessage());
        }

        return fines;
    }

    // Fine class to represent the data
    public static class Fine {
        private final int fineId;
        private final double amount;
        private final String violation;
        private final String issueDate;
        private final String status;

        public Fine(int fineId, double amount, String violation, String issueDate, String status) {
            this.fineId = fineId;
            this.amount = amount;
            this.violation = violation;
            this.issueDate = issueDate;
            this.status = status;
        }

        public int getFineId() {
            return fineId;
        }

        public double getAmount() {
            return amount;
        }

        public String getViolation() {
            return violation;
        }

        
        public String getIssueDate() {
            return issueDate;
        }

        public String getStatus() {
            return status;
        }
    }
}