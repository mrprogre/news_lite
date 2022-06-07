package team3.database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.*;

public class DBQueriesTest {
    private Connection connection;
    private DBQueries dbQueries;
    @Before
    public void setUp() throws Exception {
        connectToSQLite();
    }

    @After
    public void tearDown() throws Exception {
        connection.close();
    }

    @Test
    public void insertNewSource() {
        // Tests the actual SQL query and not the function insertNewSource()
        String query = "INSERT INTO rss_list(id, source, link, is_active) VALUES ( ? , ?, ?, " + 1 + ")";
        String[] queryResult = {"", ""};
        String testSource = "google";
        String testLink = "https://news.google.com/rss";
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(query);
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, testSource);
            preparedStatement.setString(3, testLink);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM rss_list WHERE id=1");
            while (rs.next()) {
                queryResult[0] = rs.getString("source");
                queryResult[1] = rs.getString("link");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(testSource, queryResult[0]);
        assertEquals(testLink, queryResult[1]);
    }

    @Test
    public void insertTitleIn256() {
        String testTitle = "google";
        String query = "INSERT INTO titles256(title) VALUES (?)";
        String queryResult = "";
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(query);
            preparedStatement.setString(1, testTitle);
            preparedStatement.executeUpdate();
        } catch (SQLException t) {
            t.printStackTrace();
        }

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM titles256");
            while (rs.next()) {
                queryResult = rs.getString("title");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(testTitle, queryResult);
    }

    @Test
    public void insertAllTitles() {
        String query = "INSERT INTO all_news(title, news_date) VALUES (?, ?)";
        String testTitle = "test-title";
        String testDate = "test-date";
        String[] queryResult = {"", ""};
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(query);
            preparedStatement.setString(1, testTitle);
            preparedStatement.setString(2, testDate);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

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

        assertEquals(testTitle, queryResult[0]);
        assertEquals(testDate, queryResult[1]);
    }

    @Test
    public void insertNewExcludedWord() {
        String query = "INSERT INTO exclude(word) VALUES (?)";
        String testWord = "test-word";
        String queryResult = "";
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(query);
            preparedStatement.setString(1, testWord);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM exclude");
            while (rs.next()) {
                queryResult = rs.getString("word");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(testWord, queryResult);

    }

    @Test
    public void deleteTitles() {
        String query = "DELETE FROM news_dual";
        boolean isEmpty = false;
        try {
            Statement st = connection.createStatement();
            st.executeUpdate(query);
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM news_dual");
            if (!rs.next()) {
                isEmpty = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertTrue(isEmpty);
    }

    @Test
    public void deleteFrom256() {
        String query = "DELETE FROM titles256";
        boolean isEmpty = false;
        try {
            Statement st = connection.createStatement();
            st.executeUpdate(query);
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM titles256");
            if (!rs.next()) {
                isEmpty = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertTrue(isEmpty);
    }

    @Test
    public void deleteSource() {
        String query = "DELETE FROM rss_list WHERE source = ?";
        String testSource = "google";
        String testLink = "https://news.google.com/rss";
        String insertQuery = "INSERT INTO rss_list(id, source, link, is_active) VALUES ( ? , ?, ?, " + 1 + ")";
        boolean sourceExists = true;

        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, testSource);
            preparedStatement.setString(3, testLink);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try { // Actual test query
            PreparedStatement preparedStatement =
                    connection.prepareStatement(query);
            preparedStatement.setString(1, testSource);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

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

        assertFalse(sourceExists);
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