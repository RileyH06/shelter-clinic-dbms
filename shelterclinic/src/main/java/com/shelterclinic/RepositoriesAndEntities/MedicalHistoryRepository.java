package com.shelterclinic.RepositoriesAndEntities;
import com.shelterclinic.DBUtils;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class MedicalHistoryRepository {
    private MedicalHistoryRepository() {
    }

public static Optional<MedicalHistory> findByAnimalId(int animalId)
        throws SQLException {
    String sql = "SELECT r.is_fixed, r.is_vaccinated, c.description "
            + "FROM animal a "
            + "LEFT JOIN medical_record r ON r.animal_id = a.animal_id "
            + "LEFT JOIN medical_conditions c ON c.animal_id = a.animal_id "
            + "WHERE a.animal_id = ? "
            + "ORDER BY c.description";

    try (var connection = DBUtils.getConnection();
         var statement = connection.prepareStatement(sql)) {
        statement.setInt(1, animalId);

        try (var rows = statement.executeQuery()) {
            if (!rows.next()) {
                return Optional.empty();
            }

            String fixedStatus = rows.getString("is_fixed");
            String vaccinationStatus = rows.getString("is_vaccinated");
            List<String> conditions = new ArrayList<>();

            do {
                String description = rows.getString("description");
                if (description != null) {
                    conditions.add(description);
                }
            } while (rows.next());

            return Optional.of(new MedicalHistory(
                    fixedStatus, vaccinationStatus, conditions));
        }
    }
}

    public static final class MedicalHistory {
        public final String fixedStatus;
        public final String vaccinationStatus;
        public final List<String> conditions;

        private MedicalHistory(String fixedStatus, String vaccinationStatus,
                               List<String> conditions) {
            this.fixedStatus = fixedStatus;
            this.vaccinationStatus = vaccinationStatus;
            this.conditions = List.copyOf(conditions);
        }
    }
}