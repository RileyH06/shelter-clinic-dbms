package com.shelterclinic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.SQLException;
import javafx.scene.control.Alert;
import java.io.IOException;
import com.shelterclinic.RepositoriesAndEntities.Animal;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    
@Override
public void start(Stage stage) throws IOException {
    String page = "shelter-signup";
    String sql = "SELECT name FROM shelter WHERE shelter_id = 1";
    //see if the shelter has been initialized to determine whether to route to shelter signup
    //TODO: refactor so shelter id is random num and not 1
    try (var connection = DBUtils.getConnection();
         var statement = connection.prepareStatement(sql);
         var result = statement.executeQuery()) {

        if (result.next()) {
            AppState.setShelterName(result.getString("name"));
            page = "home-page";
        }
    } catch (SQLException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Could not check shelter setup");
        alert.setContentText("Check your MySQL connection and database setup.");
        alert.showAndWait();
        return;
    }

    scene = new Scene(loadFXML(page), 640, 480);
    stage.setScene(scene);
    stage.show();
}

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    
static void showAnimalInfo(Animal animal) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("animal-info.fxml"));
    Parent root = loader.load();

    AnimalInfoController controller = loader.getController();
    controller.setAnimal(animal);

    scene.setRoot(root);
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
