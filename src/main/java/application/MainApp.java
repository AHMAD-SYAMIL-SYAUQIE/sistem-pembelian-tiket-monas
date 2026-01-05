package application;

import controller.MonasController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Main Application - Sistem Pembelian Tiket MONAS
 * JavaFX GUI Application
 */
public class MainApp extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Create controller
            MonasController controller = new MonasController(primaryStage);
            BorderPane root = controller.createMainLayout();
            
            // Create scene
            Scene scene = new Scene(root, 900, 700);
            
            // Load CSS
            String cssPath = getClass().getResource("/styles.css").toExternalForm();
            scene.getStylesheets().add(cssPath);
            
            // Configure stage
            primaryStage.setTitle("MONAS Ticket System - Sistem Pembelian Tiket Monumen Nasional");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            primaryStage.centerOnScreen();
            
            primaryStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
