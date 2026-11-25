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

    void setAnimal(Animal animal) {
        String name = animal.getName();
        nameLabel.setText(name == null || name.isBlank() ? "Unnamed" : name);

        Integer age = animal.getAge();
        summaryLabel.setText(animal.getSpecies() + " | Age: "
                + (age == null ? "Unknown" : age.toString()));

        descriptionLabel.setText(animal.getDescription());

        String tags = animal.getTagsTextString();
        tagsLabel.setText(tags.isBlank() ? "None" : tags);
    }

    @FXML
    private void goBack() throws IOException{
        App.setRoot("home-page");
    }

    //TODO: make adopt button unavailable to signed out user
}
