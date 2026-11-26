package com.shelterclinic;

import com.shelterclinic.RepositoriesAndEntities.AnimalRepository;
import com.shelterclinic.RepositoriesAndEntities.TagRepository;
import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class AdmitAnimalController {
    @FXML private TextField name;
    @FXML private TextField age;
    @FXML private TextArea description;
    @FXML private ComboBox<String> speciesComboBox;
    @FXML private ComboBox<String> fixedComboBox;
    @FXML private ComboBox<String> vaccinatedComboBox;
    @FXML private VBox conditionFields;
    @FXML private FlowPane tagChoices;
    @FXML private TextField customTag;
    @FXML private Label tagStatus;
    @FXML private Label statusLabel;
    @FXML private Button submitButton;

    @FXML
    private void initialize() {
        try {
            for (String tag : TagRepository.getAllTagNames()) {
                tagChoices.getChildren().add(new CheckBox(tag));
            }
        } catch (SQLException e) {
            tagStatus.setText("Could not load existing tags. You can still enter a custom tag.");
            e.printStackTrace();
        }
    }

    @FXML
    private void addTag() {
        queueCustomTag();
    }

    private boolean queueCustomTag() {
        String tag = customTag.getText().strip();
        if (tag.isEmpty() || tag.codePointCount(0, tag.length()) > 50) {
            tagStatus.setText("Enter a tag between 1 and 50 characters.");
            customTag.requestFocus();
            return false;
        }

        for (var node : tagChoices.getChildren()) {
            CheckBox choice = (CheckBox) node;
            if (choice.getText().equalsIgnoreCase(tag)) {
                choice.setSelected(true);
                customTag.clear();
                tagStatus.setText("");
                return true;
            }
        }

        CheckBox choice = new CheckBox(tag);
        choice.setSelected(true);
        tagChoices.getChildren().add(choice);
        customTag.clear();
        tagStatus.setText("Custom tags are saved when you submit the admission.");
        return true;
    }

    @FXML
    private void addCondition() {
        TextArea condition = new TextArea();
        condition.setPrefRowCount(4);
        condition.setWrapText(true);
        condition.setAccessibleText("Condition " + (conditionFields.getChildren().size() + 1));
        conditionFields.getChildren().add(condition);
        condition.requestFocus();
    }
    
    @FXML
    private void handleExit() throws IOException{
        App.setRoot("home-page");
    }

    @FXML
    private void submitForm() {
        String animalName = name.getText().strip();
        String animalDescription = description.getText().strip();
        int descriptionLength = animalDescription.codePointCount(0, animalDescription.length());
        if (animalName.codePointCount(0, animalName.length()) > 50
                || descriptionLength < 21 || descriptionLength > 500) {
            statusLabel.setText("Enter a description of 21–500 characters and a name of up to 50 characters.");
            return;
        }

        Integer animalAge = null;
        try {
            String ageText = age.getText().strip();
            if (!ageText.isEmpty()) {
                animalAge = Integer.valueOf(ageText);
                if (animalAge < 0) {
                    throw new NumberFormatException();
                }
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Enter a nonnegative whole number for age or leave it blank.");
            return;
        }

        String species = speciesComboBox.getValue();
        String fixed = fixedComboBox.getValue();
        String vaccinated = vaccinatedComboBox.getValue();
        if (species == null || fixed == null || vaccinated == null) {
            statusLabel.setText("Select a species, fixed status, and vaccination status.");
            return;
        }

        var conditions = conditionFields.getChildren().stream()
                .map(TextArea.class::cast)
                .map(field -> field.getText().strip())
                .filter(value -> !value.isEmpty())
                .collect(Collectors.toList());
        if (conditions.stream().anyMatch(value -> value.codePointCount(0, value.length()) > 255)) {
            statusLabel.setText("Each condition must be 255 characters or fewer.");
            return;
        }

        if (!customTag.getText().isBlank() && !queueCustomTag()) {
            statusLabel.setText(tagStatus.getText());
            return;
        }
        var tags = tagChoices.getChildren().stream()
                .map(CheckBox.class::cast)
                .filter(CheckBox::isSelected)
                .map(CheckBox::getText)
                .collect(Collectors.toList());

        try {
            AnimalRepository.admit(animalName.isEmpty() ? null : animalName, animalDescription,
                    animalAge, species, fixed, vaccinated, conditions, tags);
        } catch (SQLException e) {
            statusLabel.setText("Could not save the admission. Please try again.");
            e.printStackTrace();
            return;
        }

        submitButton.setDisable(true);
        try {
            App.setRoot("home-page");
        } catch (IOException e) {
            statusLabel.setText("Animal admitted successfully. The home page could not be opened.");
            e.printStackTrace();
        }
    }
}
