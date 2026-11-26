package com.shelterclinic;

import java.io.IOException;
import com.shelterclinic.RepositoriesAndEntities.Animal;
import com.shelterclinic.RepositoriesAndEntities.AnimalRepository;
import com.shelterclinic.RepositoriesAndEntities.TagRepository;
import java.sql.SQLException;
import java.util.stream.Collectors;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;

public class HomePageController {
    @FXML private Label shelterNameLabel;
    @FXML private Button admitBtn;
    @FXML private Button loginButton;
    @FXML private ComboBox<String> speciesComboBox;
    @FXML private ComboBox<String> vaccinatedComboBox;
    @FXML private FlowPane tagChoices;
    @FXML private Label tagFilterStatus;
    @FXML private TableView<Animal> animalTable;
    @FXML private TableColumn<Animal, String> nameCol;
    @FXML private TableColumn<Animal, Integer> ageCol;
    @FXML private TableColumn<Animal, String> speciesCol;
    @FXML private TableColumn<Animal, String> tagsCol;
    @FXML private TableColumn<Animal, Animal> moreDetailsCol;

    @FXML
    private void handleLoginButton() throws IOException {
        if (AppState.isLoggedIn()) {
            AppState.logout();
        } else {
            App.setRoot("login");
        }
    }

    @FXML private void admitAnimal() throws IOException{
        App.setRoot("admit-animal");
    }
    @FXML
    private void openAdmission() throws IOException {
        App.setRoot("home-page");
    }

    @FXML
    private void handleShowMore(Animal a) {
        try {
            App.showAnimalInfo(a);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Could not open animal details.")
                .showAndWait();
        }
    }

    @FXML 
    public void initialize(){
        shelterNameLabel.textProperty().bind(AppState.shelterNameProperty());
        admitBtn.disableProperty().bind(AppState.loggedInProperty().not());
        loginButton.textProperty().bind(
            Bindings.when(AppState.loggedInProperty())
                .then("Logout")
                .otherwise("Login")
        );   
        try {
            for (String tag : TagRepository.getAllTagNames()) {
                tagChoices.getChildren().add(new CheckBox(tag));
            }
        } catch (SQLException e) {
            tagFilterStatus.setText("Could not load tags from the database.");
            tagFilterStatus.setVisible(true);
        }
        renderTable();
     }

     @FXML private void renderTable() {
        nameCol.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(cell.getValue().getName()));
        ageCol.setCellValueFactory(cell ->
                new ReadOnlyObjectWrapper<>(cell.getValue().getAge()));
        ageCol.setCellFactory(column -> new TableCell<Animal, Integer>() {
        @Override
            protected void updateItem(Integer age, boolean empty) {
                super.updateItem(age, empty);

                setText(empty ? null : Animal.formatAge(age));
            }
        });
        speciesCol.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(cell.getValue().getSpecies()));
        tagsCol.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(cell.getValue().getTagsTextString()));
        moreDetailsCol.setCellValueFactory(cell ->
                new ReadOnlyObjectWrapper<>(cell.getValue()));
       moreDetailsCol.setCellFactory(column -> new TableCell<Animal, Animal>() {
            private final Button showMoreButton = new Button("Show more");

            {
                showMoreButton.setOnAction(event -> {
                    Animal animal = getItem();
                    if (animal != null) {
                        handleShowMore(animal);
                    }
                });
            }

        @Override
        protected void updateItem(Animal animal, boolean empty) {
                super.updateItem(animal, empty);
                setGraphic(empty || animal == null ? null : showMoreButton);
            }
        });

        applyFilters();
     }

    @FXML
    private void applyFilters() {
        String species = speciesComboBox.getValue();
        String vaccinated = vaccinatedComboBox.getValue();
        var tags = tagChoices.getChildren().stream()
                .map(CheckBox.class::cast)
                .filter(CheckBox::isSelected)
                .map(CheckBox::getText)
                .collect(Collectors.toList());
        boolean filtering = species != null || vaccinated != null || !tags.isEmpty();
        animalTable.setPlaceholder(new Label(filtering ? "No animals match these filters."
                : "No animals available for adoption."));

        try {
            animalTable.getItems().setAll(AnimalRepository.findAnimals(species, vaccinated, tags));
        } catch (SQLException e) {
            animalTable.getItems().clear();
            animalTable.setPlaceholder(new Label("Could not load animals from the database."));
        }
    }

    @FXML
    private void clearFilters() {
        speciesComboBox.setValue(null);
        vaccinatedComboBox.setValue(null);
        for (var choice : tagChoices.getChildren()) {
            ((CheckBox) choice).setSelected(false);
        }
        applyFilters();
    }
}
