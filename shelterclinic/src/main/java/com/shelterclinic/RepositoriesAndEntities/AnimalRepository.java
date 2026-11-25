package com.shelterclinic.RepositoriesAndEntities;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import com.shelterclinic.DBUtils;
public class AnimalRepository {
    
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
