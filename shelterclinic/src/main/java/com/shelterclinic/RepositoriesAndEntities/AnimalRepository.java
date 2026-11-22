package com.shelterclinic.RepositoriesAndEntities;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
public class AnimalRepository {
    
    public static void getAllAnimals() {

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
