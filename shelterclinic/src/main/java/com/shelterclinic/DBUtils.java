package com.shelterclinic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {
    private static final String URL =
            "jdbc:mysql://localhost:3306/shelterclinic_schema";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                URL, System.getenv("DB_USER"), System.getenv("DB_PASSWORD"));
    }
}
