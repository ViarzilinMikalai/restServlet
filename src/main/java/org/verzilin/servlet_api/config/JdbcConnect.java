package org.verzilin.servlet_api.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnect {
    private static final String DB_URL = "jdbc:h2:~/testDB;DB_CLOSE_DELAY=-1;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE";
    private static final String LOGIN = "sa";
    private static final String PASSWORD = "";
    private static final String DRIVER = "org.h2.Driver";

    private JdbcConnect() {
    }

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName(DRIVER);
        return DriverManager.getConnection(DB_URL, LOGIN, PASSWORD);
    }
}
