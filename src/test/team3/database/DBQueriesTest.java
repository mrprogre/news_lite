package team3.database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import team3.main.Main;

import java.sql.*;
import java.util.Properties;

import static org.junit.Assert.*;

public class DBQueriesTest {
    private Connection connection;
    private DBQueries dbQueries;

    @Before
    public void setUp() throws Exception {
        connectToSQLite();
        dbQueries = new DBQueries();
        Main.prop = new Properties();
        Utilities utilities = new Utilities();
        utilities.loadSQLQueries(Main.prop);
    }

    @After
    public void tearDown() throws Exception {
        connection.close();
    }

    @Test
    public void insertTitleIn256() {
        String testTitle = "google";
        dbQueries.insertTitleIn256(testTitle, connection);
        assertEquals(testTitle, getTestTitleFrom256());
    }

    @Test
    public void insertAllTitles() {
        String testTitle = "test-title";
        String testDate = "test-date";
        dbQueries.insertAllTitles(testTitle, testDate, connection);
        String[] queryResult = getTitleFromAllNews();

        assertEquals(testTitle, queryResult[0]);
        assertEquals(testDate, queryResult[1]);
    }


    @Test
    public void insertNewExcludedWord() {
        String testWord = "test-word";
        dbQueries.insertNewExcludedWord(testWord, connection);
        assertEquals(testWord, getTestWordFromExcluded());
    }

    @Test
    public void deleteTitles() {
        dbQueries.deleteTitles(connection);
        assertTrue(getTitlesFromNewsDual());
    }

    @Test
    public void deleteFrom256() {
        dbQueries.deleteFrom256(connection);
        assertTrue(getTitlesFromTitles256());
    }

    @Test
    public void deleteSource() {
        String testSource = "google";
        String testLink = "https://news.google.com/rss";

        addFakeSource(testSource, testLink);
        dbQueries.deleteSource(testSource, connection);
        assertFalse(testSourceExists());
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

    private String getTestTitleFrom256() {
        String queryResult = "";
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM titles256");
            while (rs.next()) {
                queryResult = rs.getString("title");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return queryResult;
    }

    private String[] getTitleFromAllNews() {
        String[] queryResult = {"", ""};
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM all_news");
            while (rs.next()) {
                queryResult[0] = rs.getString("title");
                queryResult[1] = rs.getString("news_date");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return queryResult;
    }

    private String getTestWordFromExcluded() {
        String queryResult = "";
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM exclude");
            while (rs.next()) {
                queryResult = rs.getString("word");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return queryResult;
    }

    private boolean getTitlesFromNewsDual() {
        boolean isEmpty = false;
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM news_dual");
            if (!rs.next()) {
                isEmpty = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isEmpty;
    }

    private boolean getTitlesFromTitles256() {
        boolean isEmpty = false;
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM titles256");
            if (!rs.next()) {
                isEmpty = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isEmpty;
    }

    private boolean testSourceExists() {
        boolean sourceExists = true;
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM rss_list WHERE source=?");
            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.next()) {
                sourceExists = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sourceExists;
    }

    private void addFakeSource(String fakeSource, String fakeLink) {
        String insertQuery = "INSERT INTO rss_list(id, source, link, is_active) VALUES ( ? , ?, ?, " + 1 + ")";

        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, fakeSource);
            preparedStatement.setString(3, fakeLink);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}