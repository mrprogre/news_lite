package team3.database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import team3.main.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class DBQueriesTest {
    private Connection connection;
    private DBQueries dbQueries;

    @Before
    public void setUp() throws Exception {
        dbQueries = new DBQueries();
        connectToSQLite();
    }

    @After
    public void tearDown() throws Exception {
        connection.close();
    }

    @Test
    public void selectSqlite() {
    }

    @Test
    public void selectSources() {
    }

    @Test
    public void insertNewSource() {
    }

    @Test
    public void insertTitleIn256() {
    }

    @Test
    public void insertAllTitles() {
    }

    @Test
    public void insertNewExcludedWord() {
    }

    @Test
    public void deleteTitles() {
    }

    @Test
    public void deleteFrom256() {
    }

    @Test
    public void deleteSource() {
    }

    @Test
    public void deleteDuplicates() {
    }

    @Test
    public void updateIsActiveStatus() {
    }

    @Test
    public void deleteExcluded() {
    }

    @Test
    public void isTitleExists() {
    }

    @Test
    public void archiveNewsCount() {
    }

    private void connectToSQLite() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" +
                    SQLiteTest.class.getClassLoader().getResource("news-test.db"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert connection.isValid(3);
    }
}