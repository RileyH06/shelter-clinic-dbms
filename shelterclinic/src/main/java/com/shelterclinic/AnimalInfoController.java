package com.shelterclinic;

import java.io.IOException;
import java.sql.SQLException;

import com.shelterclinic.RepositoriesAndEntities.Animal;
import com.shelterclinic.RepositoriesAndEntities.MedicalHistoryRepository;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;


public class AnimalInfoController {
    
    @FXML private Label nameLabel;
    @FXML private Label summaryLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label tagsLabel;
    @FXML private Button adoptBtn;
    @FXML private Label isFixedLabel;
    @FXML private Label isVaccinatedLabel;
    @FXML private TableView<String> medicalConditions;
    @FXML private TableColumn<String, String> conditionsCol;

    private Animal selectedAnimal;

    void setAnimal(Animal animal) {
        this.selectedAnimal = animal;
        nameLabel.setText(animal.getName());
        summaryLabel.setText(animal.getSpecies() + " | Age: " + Animal.formatAge(animal.getAge()));
        descriptionLabel.setText(animal.getDescription());
        tagsLabel.setText(animal.getTagsTextString());
        try {
            var history = MedicalHistoryRepository.findByAnimalId(animal.getAnimalId());

            if (history.isPresent()) {
                var record = history.get();
                isFixedLabel.setText(record.fixedStatus);
                isVaccinatedLabel.setText(record.vaccinationStatus);
                isFixedLabel.setText(record.fixedStatus == null
                        ? "No medical record" : record.fixedStatus);
                isVaccinatedLabel.setText(record.vaccinationStatus == null
                        ? "No medical record" : record.vaccinationStatus);
                medicalConditions.getItems().setAll(record.conditions);
            } else {
                isFixedLabel.setText("No medical record");
                isVaccinatedLabel.setText("No medical record");
            }
        } catch (SQLException e) {
            isFixedLabel.setText("Could not load medical history");
            isVaccinatedLabel.setText("Could not load medical history");
        }

        
    }

    @FXML
    private void goBack() throws IOException{
        App.setRoot("home-page");
    }

    @FXML public void initialize() {
        adoptBtn.disableProperty().bind(AppState.loggedInProperty().not());
        conditionsCol.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(cell.getValue()));
        medicalConditions.setPlaceholder(new Label("No conditions recorded."));
    }

    //TODO: pass animal id and implement 
    @FXML
    private void adoptAnimal(){
        if(selectedAnimal == null || !AppState.isLoggedIn()){
            return;
        }
        int animalId = selectedAnimal.getAnimalId();
    }

}
