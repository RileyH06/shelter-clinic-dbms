package com.shelterclinic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {
    private static final String URL =
            "jdbc:mysql://localhost:3306/shelterclinic_schema";

public static Connection getConnection() throws SQLException {
    String username = System.getenv("DB_USER");
    String password = System.getenv("DB_PASSWORD");

    if (username == null || username.isBlank() || password == null || password.isEmpty()) {
        throw new SQLException("Set DB_USER and DB_PASSWORD in the terminal before starting the app.");
    }

    return DriverManager.getConnection(URL, username, password);
}
}
