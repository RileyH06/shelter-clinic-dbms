package com.shelterclinic.RepositoriesAndEntities;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Optional;
import com.shelterclinic.DBUtils;
public class AnimalRepository {
    public static boolean deleteById(int animalId) throws SQLException {
        String deleteTags = "DELETE FROM animal_tag WHERE animal_id = ?";
        String deleteAnimal = "DELETE FROM animal WHERE animal_id = ?";
        try (var connection = DBUtils.getConnection()) {
            connection.setAutoCommit(false);
            try (var tags = connection.prepareStatement(deleteTags);
                 var animal = connection.prepareStatement(deleteAnimal)) {
                tags.setInt(1, animalId);
                tags.executeUpdate();

                animal.setInt(1, animalId);
                boolean deleted = animal.executeUpdate() == 1;
                connection.commit();
                return deleted;
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }
                throw e;
            }
        }
    }

    public static void admit(String name, String description, Integer age, String species,
                             String fixed, String vaccinated, List<String> conditions,
                             List<String> tags) throws SQLException {
        try (var connection = DBUtils.getConnection()) {
            connection.setAutoCommit(false);
            try {
                int animalId;
                String insertAnimal = "INSERT INTO animal (name, description, age, species) VALUES (?, ?, ?, ?)";
                try (var animal = connection.prepareStatement(insertAnimal, Statement.RETURN_GENERATED_KEYS)) {
                    animal.setString(1, name);
                    animal.setString(2, description);
                    animal.setObject(3, age, java.sql.Types.INTEGER);
                    animal.setString(4, species);
                    animal.executeUpdate();
                    try (var keys = animal.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("No ID returned for the admitted animal");
                        }
                        animalId = keys.getInt(1);
                    }
                }

                String insertRecord = "INSERT INTO medical_record (animal_id, is_fixed, is_vaccinated) VALUES (?, ?, ?)";
                try (var record = connection.prepareStatement(insertRecord)) {
                    record.setInt(1, animalId);
                    record.setString(2, fixed);
                    record.setString(3, vaccinated);
                    record.executeUpdate();
                }

                String insertCondition = "INSERT INTO medical_conditions (animal_id, description) VALUES (?, ?) "
                        + "ON DUPLICATE KEY UPDATE animal_id = animal_id";
                try (var condition = connection.prepareStatement(insertCondition)) {
                    condition.setInt(1, animalId);
                    for (String value : conditions) {
                        condition.setString(2, value);
                        condition.addBatch();
                    }
                    condition.executeBatch();
                }

                TagRepository.attachTags(connection, animalId, tags);
                connection.commit();
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }
                throw e;
            }
        }
    }

    // Shelter only: updates name/description (age, species, and id can't be updated),
    // creates or updates the medical record, adds new conditions, and adds/removes tags.
    public static void update(int animalId, String name, String description,
                              String fixed, String vaccinated, List<String> newConditions,
                              List<String> tagsToAdd, List<String> tagsToRemove) throws SQLException {
        try (var connection = DBUtils.getConnection()) {
            connection.setAutoCommit(false);
            try {
                String updateAnimal = "UPDATE animal SET name = ?, description = ? WHERE animal_id = ?";
                try (var animal = connection.prepareStatement(updateAnimal)) {
                    animal.setString(1, name);
                    animal.setString(2, description);
                    animal.setInt(3, animalId);
                    if (animal.executeUpdate() != 1) {
                        throw new SQLException("No animal found with ID " + animalId);
                    }
                }

                //creates the record if the animal doesn't have one yet, otherwise updates it
                String saveRecord = "INSERT INTO medical_record (animal_id, is_fixed, is_vaccinated) VALUES (?, ?, ?) "
                        + "ON DUPLICATE KEY UPDATE is_fixed = ?, is_vaccinated = ?";
                try (var record = connection.prepareStatement(saveRecord)) {
                    record.setInt(1, animalId);
                    record.setString(2, fixed);
                    record.setString(3, vaccinated);
                    record.setString(4, fixed);
                    record.setString(5, vaccinated);
                    record.executeUpdate();
                }

                String insertCondition = "INSERT INTO medical_conditions (animal_id, description) VALUES (?, ?) "
                        + "ON DUPLICATE KEY UPDATE animal_id = animal_id";
                try (var condition = connection.prepareStatement(insertCondition)) {
                    condition.setInt(1, animalId);
                    for (String value : newConditions) {
                        condition.setString(2, value);
                        condition.addBatch();
                    }
                    condition.executeBatch();
                }

                TagRepository.detachTags(connection, animalId, tagsToRemove);
                TagRepository.attachTags(connection, animalId, tagsToAdd);
                connection.commit();
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }
                throw e;
            }
        }
    }

public static Optional<Animal> findById(int animalId) throws SQLException {
    String sql = "SELECT a.animal_id, a.name, a.age, a.description, a.species, t.tag_name "
            + "FROM animal a LEFT JOIN animal_tag t ON t.animal_id = a.animal_id "
            + "WHERE a.animal_id = ? "
            + "ORDER BY t.tag_name";

    try (var connection = DBUtils.getConnection();
         var statement = connection.prepareStatement(sql)) {
        statement.setInt(1, animalId);

        try (var rows = statement.executeQuery()) {
            if (!rows.next()) {
                return Optional.empty();
            }

            Animal animal = new Animal(
                    rows.getInt("animal_id"),
                    rows.getString("name"),
                    rows.getObject("age", Integer.class),
                    rows.getString("description"),
                    rows.getString("species")
            );

            do {
                String tagName = rows.getString("tag_name");
                if (tagName != null) {
                    animal.getTags().add(new Tag(tagName));
                }
            } while (rows.next());

            return Optional.of(animal);
        }
    }
}

public static List<Animal> getAllAnimals() throws SQLException {
    return findAnimals(null, null, List.of());
}

public static List<Animal> findAnimals(String species, String vaccinated, List<String> tags) throws SQLException {
    String matchTags = "";
    if (!tags.isEmpty()) {
        String placeholders = String.join(", ", Collections.nCopies(tags.size(), "?"));
        matchTags = "AND EXISTS (SELECT 1 FROM animal_tag selected_tag "
                + "WHERE selected_tag.animal_id = a.animal_id "
                + "AND selected_tag.tag_name IN (" + placeholders + ")) ";
    }
    String sql = "SELECT a.animal_id, a.name, a.age, a.description, "
            + "a.species, t.tag_name "
            + "FROM animal a LEFT JOIN animal_tag t ON t.animal_id = a.animal_id "
            + "LEFT JOIN medical_record r ON r.animal_id = a.animal_id "
            + "WHERE (? IS NULL OR a.species = ?) "
            + "AND (? IS NULL OR COALESCE(r.is_vaccinated, 'UNKNOWN') = ?) "
            + matchTags
            + "ORDER BY a.animal_id, t.tag_name";

    Map<Integer, Animal> animals = new LinkedHashMap<>();

    try (var connection = DBUtils.getConnection();
         var statement = connection.prepareStatement(sql)) {
        statement.setString(1, species);
        statement.setString(2, species);
        statement.setString(3, vaccinated);
        statement.setString(4, vaccinated);
        for (int i = 0; i < tags.size(); i++) {
            statement.setString(5 + i, tags.get(i));
        }
        try (var rows = statement.executeQuery()) {
            while (rows.next()) {
                int id = rows.getInt("animal_id");
                Animal animal = animals.get(id);

                if (animal == null) {
                    animal = new Animal(
                            id,
                            rows.getString("name"),
                            rows.getObject("age", Integer.class),
                            rows.getString("description"),
                            rows.getString("species")
                    );
                    animals.put(id, animal);
                }

                String tagName = rows.getString("tag_name");
                if (tagName != null) {
                    animal.getTags().add(new Tag(tagName));
                }
            }
        }
    }

    return new ArrayList<>(animals.values());
}
    
    public static List<Tag> getAnimalTags(Connection conn, int animalId) throws SQLException{
        List<Tag> tags = new ArrayList<>();
        String sql = "SELECT tag_name FROM animal_tag WHERE animal_id = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            System.out.println("working!");
        }
        
        return null;
    }
}
