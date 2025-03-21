package user;

import application.LoginScreen;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class UserScreen extends Application {
    private int user_id; // Store user_id

    // Constructor to accept user_id
    public UserScreen(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public void start(Stage primaryStage) {
        // Display the user_id on the screen
        Label welcomeLabel = new Label("Welcome, User ID: " + user_id);
        welcomeLabel.setStyle("-fx-font-size: 16px; -fx-padding: 10px; -fx-background-color: lightgray;");

        Button viewFinesButton = new Button("View Fines");
        Button submitFineButton = new Button("Submit Fine");
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");

        Circle powerIcon = new Circle(10, Color.BLACK);
        powerIcon.setOnMouseClicked(e -> logout(primaryStage));

        // Open ViewFines page with user_id
        viewFinesButton.setOnAction(e -> openViewFinesPage(user_id));

        // Open SubmitFine page with user_id
        submitFineButton.setOnAction(e -> openSubmitFinePage(user_id));

        logoutButton.setOnAction(e -> logout(primaryStage));

        VBox buttonBox = new VBox(10, viewFinesButton, submitFineButton);
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        HBox bottomBox = new HBox(10, logoutButton, powerIcon);
        bottomBox.setAlignment(Pos.BOTTOM_RIGHT);
        bottomBox.setStyle("-fx-padding: 10px;");

        VBox root = new VBox(20, new Label("User Screen"), welcomeLabel, buttonBox, bottomBox);
        root.setAlignment(Pos.TOP_LEFT);
        root.setStyle("-fx-padding: 20px;");

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("User Screen");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openViewFinesPage(int userId) {
        // Open ViewFine with user_id
        ViewFine viewFine = new ViewFine(userId);
        Stage viewFineStage = new Stage();
        viewFine.start(viewFineStage);
    }

    private void openSubmitFinePage(int userId) {
        // Open SubmitFineUI with user_id
        SubmitFineUI submitFineUI = new SubmitFineUI(userId);
        Stage submitFineStage = new Stage();
        submitFineUI.start(submitFineStage);
    }

    private void logout(Stage stage) {
        // Close the current stage (UserScreen)
        stage.close();

        // Open the LoginScreen
        LoginScreen loginScreen = new LoginScreen();
        Stage loginStage = new Stage();
        loginScreen.start(loginStage);
    }

    public void showDashboard() {
        start(new Stage());
    }
}