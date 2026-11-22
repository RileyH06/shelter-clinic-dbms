package com.shelterclinic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    
    @Override
    public void start(Stage stage) throws IOException {
        //temporary
        AppState.setShelterName("SPCA");
        //TODO: GET SHELTER NAME
        String shelterName = AppState.shelterNameProperty().get();
        
        if (shelterName == null || shelterName.isBlank()){
            scene = new Scene(loadFXML("shelter-signup"), 640, 480);
        }
        else {
            scene = new Scene(loadFXML("home-page"), 640, 480);
        }
        System.out.println(AppState.shelterNameProperty());

        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        
        try (var connection = DBUtils.getConnection()) {
            System.out.println("Connected to MySQL: " + connection.getCatalog());
        } catch (java.sql.SQLException e) {
            System.err.println("MySQL connection failed: " + e.getMessage());
        }
        launch();
    }

}
