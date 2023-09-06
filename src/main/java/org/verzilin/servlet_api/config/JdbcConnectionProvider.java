package org.verzilin.servlet_api.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcConnectionProvider {
    private JdbcConnectionProvider() {
    }

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    static {
        Properties properties = new Properties();
        try (InputStream fileInputStream = JdbcConnectionProvider.class.getResourceAsStream("/hikari.properties")) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        config.setJdbcUrl(properties.getProperty("dataSource.jdbcUrl"));
        config.setUsername(properties.getProperty("dataSource.username"));
        config.setPassword(properties.getProperty("dataSource.password"));
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}