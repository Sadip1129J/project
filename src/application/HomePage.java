package application;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class HomePage extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        // Create root layout
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #2c3e50, #3498db);");
        
        // Create animated elements
        Circle pulseCircle = new Circle(80, Color.TRANSPARENT);
        pulseCircle.setStroke(Color.WHITE);
        pulseCircle.setStrokeWidth(3);
        pulseCircle.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(255,255,255,0.8), 0, 10, 0, 0);");
        
        // Add traffic light icon
        ImageView trafficIcon = new ImageView(new Image(getClass().getResourceAsStream("/traffic_light.png")));
        trafficIcon.setFitWidth(120);
        trafficIcon.setFitHeight(120);
        
        // Add system name
        Text systemName = new Text("TRAFFIC CONTROL SYSTEM");
        systemName.setFont(Font.font("Arial", 28));
        systemName.setFill(Color.WHITE);
        systemName.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 5, 0, 0, 1);");
        systemName.setTranslateY(100);
        
        // Add version text
        Text versionText = new Text("v2.0.1");
        versionText.setFont(Font.font("Arial", 12));
        versionText.setFill(Color.WHITE);
        versionText.setTranslateY(150);
        
        // Add all elements to root
        root.getChildren().addAll(pulseCircle, trafficIcon, systemName, versionText);
        
        // Create animations
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5), trafficIcon);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        
        RotateTransition rotate = new RotateTransition(Duration.seconds(3), pulseCircle);
        rotate.setByAngle(360);
        rotate.setCycleCount(2);
        
        ScaleTransition pulse = new ScaleTransition(Duration.seconds(1.5), pulseCircle);
        pulse.setFromX(0.8);
        pulse.setFromY(0.8);
        pulse.setToX(1.2);
        pulse.setToY(1.2);
        pulse.setAutoReverse(true);
        pulse.setCycleCount(2);
        
        // Combine animations
        ParallelTransition parallelTransition = new ParallelTransition(fadeIn, rotate, pulse);
        
        // When animation finishes, open login screen
        parallelTransition.setOnFinished(event -> {
            try {
                Stage loginStage = new Stage();
                new LoginScreen().start(loginStage);
                primaryStage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        // Set up scene and stage
        Scene scene = new Scene(root, 800, 500);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Start animation
        parallelTransition.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}