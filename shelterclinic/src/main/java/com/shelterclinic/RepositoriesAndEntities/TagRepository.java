package com.shelterclinic.RepositoriesAndEntities;

import com.shelterclinic.DBUtils;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TagRepository {
    public static List<String> getAllTagNames() throws SQLException {
        List<String> names = new ArrayList<>();
        try (var connection = DBUtils.getConnection();
             var statement = connection.prepareStatement("SELECT tag_name FROM Tag ORDER BY tag_name");
             var rows = statement.executeQuery()) {
            while (rows.next()) {
                names.add(rows.getString("tag_name"));
            }
        }
        return names;
    }

    static void attachTags(Connection connection, int animalId, List<String> names) throws SQLException {
        String ensureTag = "INSERT INTO Tag (tag_name) VALUES (?) ON DUPLICATE KEY UPDATE tag_name = tag_name";
        String attachTag = "INSERT INTO animal_tag (animal_id, tag_name) "
                + "SELECT ?, t.tag_name FROM Tag t WHERE t.tag_name = ? "
                + "ON DUPLICATE KEY UPDATE animal_id = animal_tag.animal_id";
        try (var tag = connection.prepareStatement(ensureTag);
             var link = connection.prepareStatement(attachTag)) {
            for (String name : names) {
                tag.setString(1, name);
                tag.executeUpdate();
                link.setInt(1, animalId);
                link.setString(2, name);
                link.executeUpdate();
            }
        }
    }
}
