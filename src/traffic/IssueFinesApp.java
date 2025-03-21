package traffic;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDate;

public class IssueFinesApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Issue Fines");

        // Create a GridPane layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        // Name
        Label nameLabel = new Label("Name:");
        GridPane.setConstraints(nameLabel, 0, 0);
        TextField nameField = new TextField();
        GridPane.setConstraints(nameField, 1, 0);

        // Type of Violation
        Label violationLabel = new Label("Type of Violation:");
        GridPane.setConstraints(violationLabel, 0, 1);
        TextField violationField = new TextField();
        GridPane.setConstraints(violationField, 1, 1);

        // Issue Date
        Label dateLabel = new Label("Issue Date:");
        GridPane.setConstraints(dateLabel, 0, 2);
        DatePicker datePicker = new DatePicker();
        GridPane.setConstraints(datePicker, 1, 2);

        // Address
        Label addressLabel = new Label("Address:");
        GridPane.setConstraints(addressLabel, 0, 3);
        TextField addressField = new TextField();
        GridPane.setConstraints(addressField, 1, 3);

        // Traffic ID
        Label trafficLabel = new Label("Traffic ID:");
        GridPane.setConstraints(trafficLabel, 0, 4);
        TextField trafficField = new TextField();
        GridPane.setConstraints(trafficField, 1, 4);

        // Fine ID
        Label fineLabel = new Label("Fine ID:");
        GridPane.setConstraints(fineLabel, 0, 5);
        TextField fineField = new TextField();
        GridPane.setConstraints(fineField, 1, 5);

        // Submit Button
        Button submitButton = new Button("Submit");
        GridPane.setConstraints(submitButton, 1, 6);
        submitButton.setOnAction(e -> insertFine(trafficField.getText(), fineField.getText(), violationField.getText(), datePicker.getValue()));

        // Return Button
        Button returnButton = new Button("Return");
        GridPane.setConstraints(returnButton, 1, 7);
        returnButton.setOnAction(e -> {
        	TrafficPoliceDashboard tf=new TrafficPoliceDashboard();
        	tf.showDashboard();
            // Add functionality for return button here
        });

        // Add all components to the grid
        grid.getChildren().addAll(nameLabel, nameField, violationLabel, violationField, dateLabel, datePicker,
                addressLabel, addressField, trafficLabel, trafficField, fineLabel, fineField, submitButton, returnButton);

        // Create a scene with the grid as the root node
        Scene scene = new Scene(grid, 400, 350);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void insertFine(String trafficId, String fineId, String violation, LocalDate issueDate) {
        String url = "jdbc:mysql://localhost:3306/traffic_fine";
        String user = "root";
        String password = "Sadip123@";

        String query = "INSERT INTO fines (fine_id, user_id, amount, violation, issue_date, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(fineId));
            stmt.setInt(2, Integer.parseInt(trafficId));
            stmt.setDouble(3, 100.00); // Example amount, adjust accordingly
            stmt.setString(4, violation);
            stmt.setDate(5, java.sql.Date.valueOf(issueDate));
            stmt.setString(6, "Pending");
            stmt.executeUpdate();
            System.out.println("Fine inserted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
