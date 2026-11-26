package com.shelterclinic.RepositoriesAndEntities;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import com.shelterclinic.DBUtils;
public class AnimalRepository {
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
    
public static List<Animal> getAllAnimals() throws SQLException {
    String sql = "SELECT a.animal_id, a.name, a.age, a.description, "
            + "a.species, t.tag_name "
            + "FROM animal a LEFT JOIN animal_tag t ON t.animal_id = a.animal_id "
            + "ORDER BY a.animal_id, t.tag_name";

    Map<Integer, Animal> animals = new LinkedHashMap<>();

    try (var connection = DBUtils.getConnection();
         var statement = connection.prepareStatement(sql);
         var rows = statement.executeQuery()) {
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
