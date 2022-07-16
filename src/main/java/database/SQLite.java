package database;

import lombok.extern.slf4j.Slf4j;
import main.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
public class SQLite {
    public static Connection connection;

    public void openConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + Main.DIRECTORY_PATH + "news.db");
            log.info("Connected to SQLite");
            Thread.sleep(1000L);
        } catch (Exception e) {
            log.error("Connection open failed:\n" + e.getMessage());
        }
    }

}
