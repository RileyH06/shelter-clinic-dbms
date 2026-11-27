package com.shelterclinic;

import com.shelterclinic.RepositoriesAndEntities.Animal;
import com.shelterclinic.RepositoriesAndEntities.AnimalRepository;
import com.shelterclinic.RepositoriesAndEntities.MedicalHistoryRepository;
import com.shelterclinic.RepositoriesAndEntities.Tag;
import com.shelterclinic.RepositoriesAndEntities.TagRepository;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class UpdateAnimalController {
    @FXML private TextField animalIdField;
    @FXML private Label findStatus;
    @FXML private VBox formBox;
    @FXML private Label summaryLabel;
    @FXML private TextField name;
    @FXML private TextArea description;
    @FXML private FlowPane tagChoices;
    @FXML private TextField customTag;
    @FXML private Label tagStatus;
    @FXML private ComboBox<String> deleteTagComboBox;
    @FXML private ComboBox<String> fixedComboBox;
    @FXML private ComboBox<String> vaccinatedComboBox;
    @FXML private Label medicalNote;
    @FXML private TableView<String> medicalConditions;
    @FXML private TableColumn<String, String> conditionsCol;
    @FXML private VBox conditionFields;
    @FXML private Label statusLabel;
    @FXML private Button submitButton;

    private Animal selectedAnimal;
    private List<String> originalTags = new ArrayList<>();

    @FXML
    private void initialize() {
        formBox.setDisable(true);
        conditionsCol.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(cell.getValue()));
        medicalConditions.setPlaceholder(new Label("No conditions recorded."));

        if (!AppState.isShelter()) {
            animalIdField.setDisable(true);
            findStatus.setText("Only the shelter can update animals.");
        }
    }

    //called by App.showUpdateAnimal when coming from the animal info page
    void setAnimal(Animal animal) {
        animalIdField.setText(String.valueOf(animal.getAnimalId()));
        findAnimal();
    }

    //shelter enters an animal id, check that it exists before showing the form
    @FXML
    private void findAnimal() {
        if (!AppState.isShelter()) {
            return;
        }

        int animalId;
        try {
            animalId = Integer.parseInt(animalIdField.getText().strip());
        } catch (NumberFormatException e) {
            findStatus.setText("Enter a numeric animal ID.");
            return;
        }

        Optional<Animal> animal;
        try {
            animal = AnimalRepository.findById(animalId);
        } catch (SQLException e) {
            findStatus.setText("Could not look up the animal. Please try again.");
            e.printStackTrace();
            return;
        }

        if (animal.isEmpty()) {
            selectedAnimal = null;
            formBox.setDisable(true);
            findStatus.setText("No animal exists with ID " + animalId + ".");
            return;
        }

        selectedAnimal = animal.get();
        findStatus.setText("");
        statusLabel.setText("");
        fillForm();
    }

    private void fillForm() {
        summaryLabel.setText(selectedAnimal.getSpecies() + " | Age: " + Animal.formatAge(selectedAnimal.getAge())
                + "   (species and age can't be updated)");
        name.setText(selectedAnimal.getName().equals("Unnamed") ? "" : selectedAnimal.getName());
        description.setText(selectedAnimal.getDescription());

        //all tags as checkboxes, the animal's current tags start checked
        originalTags = selectedAnimal.getTags().stream()
                .map(Tag::getTagName)
                .collect(Collectors.toList());
        tagChoices.getChildren().clear();
        try {
            List<String> allTags = TagRepository.getAllTagNames();
            for (String tag : allTags) {
                CheckBox choice = new CheckBox(tag);
                choice.setSelected(originalTags.contains(tag));
                tagChoices.getChildren().add(choice);
            }
            deleteTagComboBox.getItems().setAll(allTags);
        } catch (SQLException e) {
            tagStatus.setText("Could not load existing tags.");
            e.printStackTrace();
        }

        fixedComboBox.setDisable(false);
        vaccinatedComboBox.setDisable(false);
        try {
            var history = MedicalHistoryRepository.findByAnimalId(selectedAnimal.getAnimalId());
            if (history.isPresent() && history.get().fixedStatus != null) {
                var record = history.get();
                fixedComboBox.setValue(record.fixedStatus);
                vaccinatedComboBox.setValue(record.vaccinationStatus);
                medicalConditions.getItems().setAll(record.conditions);
                medicalNote.setText("");

                //once spayed/neutered or vaccinated, the status can't be changed back
                if (record.fixedStatus.equals("SPAYED") || record.fixedStatus.equals("NEUTERED")) {
                    fixedComboBox.setDisable(true);
                }
                if (record.vaccinationStatus.equals("YES")) {
                    vaccinatedComboBox.setDisable(true);
                }
            } else {
                fixedComboBox.setValue(null);
                vaccinatedComboBox.setValue(null);
                medicalConditions.getItems().clear();
                medicalNote.setText("No medical record yet. Submitting will create one.");
            }
        } catch (SQLException e) {
            medicalNote.setText("Could not load medical history.");
            e.printStackTrace();
        }

        conditionFields.getChildren().clear();
        addCondition();

        formBox.setDisable(false);
        submitButton.setDisable(false);
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
        if (tag.split("\\s+").length > 5) {
            tagStatus.setText("Tags can be at most 5 words.");
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
        tagStatus.setText("Custom tags are saved when you submit.");
        return true;
    }

    //deletes a tag from the whole system right away (not on submit)
    @FXML
    private void deleteTag() {
        String tag = deleteTagComboBox.getValue();
        if (tag == null) {
            tagStatus.setText("Select a tag to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete \"" + tag + "\" from the system? It will be removed from every animal.");
        Optional<ButtonType> answer = confirm.showAndWait();
        if (answer.isEmpty() || answer.get() != ButtonType.OK) {
            return;
        }

        try {
            TagRepository.deleteTag(tag);
        } catch (SQLException e) {
            tagStatus.setText("Could not delete the tag. Please try again.");
            e.printStackTrace();
            return;
        }

        tagChoices.getChildren().removeIf(node -> ((CheckBox) node).getText().equals(tag));
        originalTags.remove(tag);
        deleteTagComboBox.getItems().remove(tag);
        deleteTagComboBox.setValue(null);
        tagStatus.setText("Deleted \"" + tag + "\" from the system.");
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
    private void handleExit() throws IOException {
        App.setRoot("home-page");
    }

    @FXML
    private void submitForm() {
        if (selectedAnimal == null || !AppState.isShelter()) {
            return;
        }

        String animalName = name.getText().strip();
        String animalDescription = description.getText().strip();
        int descriptionLength = animalDescription.codePointCount(0, animalDescription.length());
        if (animalName.codePointCount(0, animalName.length()) > 50
                || descriptionLength < 21 || descriptionLength > 500) {
            statusLabel.setText("Enter a description of 21–500 characters and a name of up to 50 characters.");
            return;
        }

        String fixed = fixedComboBox.getValue();
        String vaccinated = vaccinatedComboBox.getValue();
        if (fixed == null || vaccinated == null) {
            statusLabel.setText("Select a fixed status and vaccination status.");
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
        var selectedTags = tagChoices.getChildren().stream()
                .map(CheckBox.class::cast)
                .filter(CheckBox::isSelected)
                .map(CheckBox::getText)
                .collect(Collectors.toList());

        //checked now but not before = add, checked before but not now = remove
        var tagsToAdd = selectedTags.stream()
                .filter(tag -> !originalTags.contains(tag))
                .collect(Collectors.toList());
        var tagsToRemove = originalTags.stream()
                .filter(tag -> !selectedTags.contains(tag))
                .collect(Collectors.toList());

        try {
            AnimalRepository.update(selectedAnimal.getAnimalId(),
                    animalName.isEmpty() ? null : animalName, animalDescription,
                    fixed, vaccinated, conditions, tagsToAdd, tagsToRemove);
        } catch (SQLException e) {
            statusLabel.setText("Could not save the changes. Please try again.");
            e.printStackTrace();
            return;
        }

        submitButton.setDisable(true);
        try {
            App.setRoot("home-page");
        } catch (IOException e) {
            statusLabel.setText("Animal updated successfully. The home page could not be opened.");
            e.printStackTrace();
        }
    }
}
