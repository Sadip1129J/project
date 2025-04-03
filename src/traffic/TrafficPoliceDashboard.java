package traffic;

import application.LoginScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TrafficPoliceDashboard extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage; // Store reference to primary stage

        // Creating UI components
        Label titleLabel = new Label("Traffic Police Dashboard");
        Label welcomeLabel = new Label("Welcome");
        welcomeLabel.setStyle("-fx-background-color: lightgray; -fx-padding: 5px;");

        Button issueFinesButton = new Button("Issue Fines");
        issueFinesButton.setOnAction(event -> openIssueFines());

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(event -> logout());

        // Layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(titleLabel, welcomeLabel, issueFinesButton, logoutButton);
        layout.setStyle("-fx-padding: 20px;");

        // Scene setup
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Traffic Police Dashboard");
        primaryStage.show();
    }

    private void openIssueFines() {
        IssueFinesApp issueFinesApp = new IssueFinesApp();
        Stage finesStage = new Stage();
        issueFinesApp.start(finesStage);

        // Hide dashboard while issuing fines
        primaryStage.hide();

        // Show dashboard again when fines window closes
        finesStage.setOnCloseRequest(e -> primaryStage.show());
    }

    private void logout() {
        primaryStage.close(); // Close dashboard
        new LoginScreen().start(new Stage()); // Open Sign In screen
    }

    public void showDashboard() {
        if (primaryStage != null) {
            primaryStage.show();
        } else {
            start(new Stage());
        }
    }
}
