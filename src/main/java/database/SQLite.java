package database;

import main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class SQLite {
    private static final Logger LOGGER = LoggerFactory.getLogger(SQLite.class);
    public static Connection connection;
    public static boolean isConnectionToSQLite;

    // Открытие соединения с базой данных
    public void openSQLiteConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + Main.DIRECTORY_PATH + "news.db");
            isConnectionToSQLite = true;
            LOGGER.warn("Connected to SQLite");
            Thread.sleep(1000L);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.warn(e.getMessage());
        }
    }

    // закрытие соединения SQLite
    public void closeSQLiteConnection() {
        try {
            if (isConnectionToSQLite) {
                connection.close();
                LOGGER.warn("Connection closed");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
