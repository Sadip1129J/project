package user;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class ViewFine extends Application {
    private int userId; // Add userId field

    // Constructor to accept userId
    public ViewFine(int userId) {
        this.userId = userId;
    }

   

	@Override
    public void start(Stage primaryStage) {
        // Set up the table columns
        TableColumn<Fine, Integer> idColumn = new TableColumn<>("Fine ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Fine, Double> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Fine, String> violationColumn = new TableColumn<>("Violation");
        violationColumn.setCellValueFactory(new PropertyValueFactory<>("violation"));

        TableColumn<Fine, LocalDate> issueDateColumn = new TableColumn<>("Issue Date");
        issueDateColumn.setCellValueFactory(new PropertyValueFactory<>("issueDate"));

        TableColumn<Fine, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Add columns to the table
        TableView<Fine> fineTable = new TableView<>();
        fineTable.getColumns().addAll(idColumn, amountColumn, violationColumn, issueDateColumn, statusColumn);

        // Load data from the database
        fineTable.setItems(getFinesFromDatabase(userId));

        // Layout
        VBox root = new VBox(fineTable);
        Scene scene = new Scene(root, 800, 400);

        // Set up the stage
        primaryStage.setTitle("View Fines for User ID: " + userId);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to fetch fines from the database for a specific user
    private ObservableList<Fine> getFinesFromDatabase(int userId) {
        ObservableList<Fine> fines = FXCollections.observableArrayList();

        // Database connection details
        String url = "jdbc:mysql://localhost:3306/traffic_fines"; // Replace with your database name
        String username = "root"; // Replace with your MySQL username
        String password = "Sadip123@"; // Replace with your MySQL password

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM fines WHERE user_id = " + userId)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("fine_id");
                double amount = resultSet.getDouble("amount");
                String violation = resultSet.getString("violation");
                LocalDate issueDate = resultSet.getDate("issue_date").toLocalDate();
                String status = resultSet.getString("status");

                fines.add(new Fine(id, userId, amount, violation, issueDate, status));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fines;
    }

    public static void main(String[] args) {
        launch(args);
    }
}