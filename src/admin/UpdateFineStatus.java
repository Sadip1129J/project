package admin;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UpdateFineStatus extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Update Fine Status");

        // Labels and Input Fields
        Label titleLabel = new Label("Update Fine Status");

        Label fineIdLabel = new Label("Enter Fine ID:");
        TextField fineIdField = new TextField();

        Label transactionIdLabel = new Label("Transaction ID:");
        TextField transactionIdField = new TextField();

        Label amountLabel = new Label("Fine Amount:");
        TextField amountField = new TextField();

        Label licenseLabel = new Label("License Number:");
        TextField licenseField = new TextField();

        Label statusLabel = new Label("Fine Status:");
        ComboBox<String> statusDropdown = new ComboBox<>();
        statusDropdown.getItems().addAll("Paid", "Unpaid");
        statusDropdown.setValue("Unpaid"); // Default selection

        // Buttons
        Button updateButton = new Button("Update");
        Button returnButton = new Button("Return");

        // Button Actions (You can add database logic here)
        updateButton.setOnAction(e -> {
            String fineId = fineIdField.getText();
            String transactionId = transactionIdField.getText();
            String amount = amountField.getText();
            String license = licenseField.getText();
            String status = statusDropdown.getValue();

            System.out.println("Fine ID: " + fineId);
            System.out.println("Transaction ID: " + transactionId);
            System.out.println("Fine Amount: " + amount);
            System.out.println("License Number: " + license);
            System.out.println("Status: " + status);
            System.out.println("Fine Status Updated!");
        });

        returnButton.setOnAction(e -> primaryStage.close()); // Close window

        // Layout for form fields
        GridPane formLayout = new GridPane();
        formLayout.setPadding(new Insets(10));
        formLayout.setHgap(10);
        formLayout.setVgap(10);

        formLayout.add(fineIdLabel, 0, 0);
        formLayout.add(fineIdField, 1, 0);
        formLayout.add(transactionIdLabel, 0, 1);
        formLayout.add(transactionIdField, 1, 1);
        formLayout.add(amountLabel, 0, 2);
        formLayout.add(amountField, 1, 2);
        formLayout.add(licenseLabel, 0, 3);
        formLayout.add(licenseField, 1, 3);
        formLayout.add(statusLabel, 0, 4);
        formLayout.add(statusDropdown, 1, 4);

        // Buttons Layout
        VBox buttonLayout = new VBox(10, updateButton, returnButton);
        buttonLayout.setAlignment(Pos.CENTER);

        // Main Layout
        VBox mainLayout = new VBox(15, titleLabel, formLayout, buttonLayout);
        mainLayout.setPadding(new Insets(15));
        mainLayout.setAlignment(Pos.CENTER);

        // Scene
        Scene scene = new Scene(mainLayout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
