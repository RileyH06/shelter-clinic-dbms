package com.shelterclinic;

import java.io.IOException;

import com.shelterclinic.RepositoriesAndEntities.Animal;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;


public class AnimalInfoController {
    
    @FXML private Label nameLabel;
    @FXML private Label summaryLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label tagsLabel;
    @FXML private Button adoptBtn;
    private Animal selectedAnimal;

    void setAnimal(Animal animal) {
        this.selectedAnimal = animal;
        nameLabel.setText(animal.getName());
        summaryLabel.setText(animal.getSpecies() + " | Age: " + Animal.formatAge(animal.getAge()));
        descriptionLabel.setText(animal.getDescription());
        tagsLabel.setText(animal.getTagsTextString());
    }

    @FXML
    private void goBack() throws IOException{
        App.setRoot("home-page");
    }

    @FXML public void initialize() {
        adoptBtn.disableProperty().bind(AppState.loggedInProperty().not());
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
