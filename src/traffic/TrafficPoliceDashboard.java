package traffic;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TrafficPoliceDashboard extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Creating UI components
        Label titleLabel = new Label("Traffic Police Dashboard");
        Label welcomeLabel = new Label("Welcome");
        welcomeLabel.setStyle("-fx-background-color: lightgray; -fx-padding: 5px;");

        Button issueFinesButton = new Button("Issue Fines");
//        issueFinesButton.setStyle("-fx-background-color: lightgray;");

        issueFinesButton.setOnAction(event -> {
            IssueFinesApp issueFinesApp = new IssueFinesApp();
            Stage stage = new Stage();
            issueFinesApp.start(stage);
        });


        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(event -> {
            primaryStage.close(); // Logout functionality to close the current stage
        });

        // Layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(titleLabel, welcomeLabel, issueFinesButton, logoutButton);
        layout.setSpacing(10);
        layout.setStyle("-fx-padding: 20px;");

        // Scene setup
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Traffic Police Dashboard");
        primaryStage.show();
    }

    // Method to show the dashboard (called from LoginScreen)
    public void showDashboard() {
        start(new Stage());
    }
//    public static void main(String[] args) {
//    	new TrafficPoliceDashboard();
//	}
}