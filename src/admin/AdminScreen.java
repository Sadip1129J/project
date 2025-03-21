package admin;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminScreen extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Admin Screen");

        // Title Label
        Label titleLabel = new Label("Admin Screen");

        // Buttons
        Button manageUsersButton = new Button("Manage Users");
        Button managePoliceButton = new Button("Manage Traffic Police");
        Button viewFinesButton = new Button("View Issued Fines");
        Button updatePaymentButton = new Button("Update Payment Status");
        Button logoutButton = new Button("Logout");

        // Setting button actions (You can replace these with actual scene navigation)
        manageUsersButton.setOnAction(e -> System.out.println("Navigating to Manage Users"));
        managePoliceButton.setOnAction(e -> System.out.println("Navigating to Manage Traffic Police"));
        viewFinesButton.setOnAction(e -> System.out.println("Navigating to View Issued Fines"));
        updatePaymentButton.setOnAction(e -> System.out.println("Navigating to Update Payment Status"));
        logoutButton.setOnAction(e -> primaryStage.close()); // Closes the window

        // Layout Configuration
        VBox layout = new VBox(10, titleLabel, manageUsersButton, managePoliceButton, viewFinesButton, updatePaymentButton, logoutButton);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        // Scene
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
