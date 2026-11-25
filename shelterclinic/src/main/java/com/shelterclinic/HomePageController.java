package com.shelterclinic;

import java.io.IOException;
import com.shelterclinic.RepositoriesAndEntities.Animal;
import com.shelterclinic.RepositoriesAndEntities.AnimalRepository;
import java.sql.SQLException;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;

public class HomePageController {
    @FXML private Label shelterNameLabel;
    @FXML private Button admitBtn;
    @FXML private Button loginButton;
    @FXML private TableView<Animal> animalTable;
    @FXML private TableColumn<Animal, String> nameCol;
    @FXML private TableColumn<Animal, Integer> ageCol;
    @FXML private TableColumn<Animal, String> speciesCol;
    @FXML private TableColumn<Animal, String> tagsCol;

    @FXML
    private void handleLoginButton() throws IOException {
        if (AppState.isLoggedIn()) {
            AppState.logout();
        } else {
            App.setRoot("login");
        }
    }

    @FXML
    private void openAdmission() throws IOException {
        App.setRoot("home-page");
    }

    @FXML public void initialize(){
        shelterNameLabel.textProperty().bind(AppState.shelterNameProperty());
        admitBtn.disableProperty().bind(AppState.loggedInProperty().not());
        loginButton.textProperty().bind(
            Bindings.when(AppState.loggedInProperty())
                .then("Logout")
                .otherwise("Login")
        );   
        renderTable();  
     }

     @FXML private void renderTable() {
        nameCol.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(cell.getValue().getName()));
        ageCol.setCellValueFactory(cell ->
                new ReadOnlyObjectWrapper<>(cell.getValue().getAge()));
        speciesCol.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(cell.getValue().getSpecies()));
        tagsCol.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(cell.getValue().getTagsTextString()));

        animalTable.setPlaceholder(new Label("No animals available for adoption."));

        try {
            animalTable.getItems().setAll(AnimalRepository.getAllAnimals());
        } catch (SQLException e) {
            animalTable.setPlaceholder(new Label("Could not load animals from the database."));
        }
     }
}
