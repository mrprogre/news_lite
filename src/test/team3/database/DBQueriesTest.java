package team3.database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import team3.main.Main;
import team3.utils.Common;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

import static org.junit.Assert.*;
public class DBQueriesTest {
    private Connection connection;
    private DBQueries dbQueries;
    /*
    Purpose: before test
    Input : connectToSQLite(),dbQueries(),Properties(), Utilities(), loadSQLQueries(Main.prop)
    Expected :
        Create setUp Environment
    */

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


// ----bong-----
    /*
    Purpose: Method selectSqlite Test
    Input : selectSqlite(connection)
    Expected :
            Gui.modelForAnalysis.addRow(row2);

    */
    @Test
    public void selectsqlite() {
        dbQueries.selectSqlite(connection);
    }

    /*
    Purpose: Method selectSources Test
    Input : selectSources("smi")
    Expected :
            Equals(smi_source,Common.SMI_SOURCE);
            Equals(smi_link,Common.SMI_LINK);
    */
    @Test
    public void selectsources_smi(){
        SQLite sqLite = new SQLite();
        sqLite.openSQLiteConnection();

        Utilities dbUtil = new Utilities();
        ArrayList<String> smi_source = new ArrayList<>();
        ArrayList<String> smi_link = new ArrayList<>();
        try {
            Statement st = SQLite.connection.createStatement();
            ResultSet rs = st.executeQuery(dbUtil.getSQLQueryFromProp("smiQuery"));
            while (rs.next()) {
                //int id = rs.getInt("id");
                String source = rs.getString("source");
                String link = rs.getString("link");
                smi_source.add(source);
                smi_link.add(link);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbQueries.selectSources("smi");
        assertEquals(smi_source,Common.SMI_SOURCE);
        assertEquals(smi_link,Common.SMI_LINK);
    }
    /*
    Purpose: Method selectSources Test
    Input : selectSources("excl")
    Expected :
            Equals(exclude_words,Common.EXCLUDED_WORDS);
    */
    @Test
    public void selectsources_excl(){
        SQLite sqLite = new SQLite();
        sqLite.openSQLiteConnection();

        Utilities dbUtil = new Utilities();
        ArrayList<String> exclude_words = new ArrayList<>();
        try {
            Statement st = SQLite.connection.createStatement();
            ResultSet rs = st.executeQuery(dbUtil.getSQLQueryFromProp("exclQuery"));
            while (rs.next()) {
                String word = rs.getString("word");
                exclude_words.add(word);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbQueries.selectSources("excl");
        assertEquals(exclude_words,Common.EXCLUDED_WORDS);

    }

    /*
    Purpose: Method selectSources Test
    Input : selectSources("active_smi")
    Expected :
            Equals(smi_source, Common.SMI_SOURCE);
            Equals(smi_link,Common.SMI_LINK);
            Equals(smi_is_active,Common.SMI_IS_ACTIVE);
    */
    @Test
    public void selectsources_active_smi(){
        SQLite sqLite = new SQLite();
        sqLite.openSQLiteConnection();

        Utilities dbUtil = new Utilities();
        ArrayList<String> smi_source= new ArrayList<>();
        ArrayList<String> smi_link= new ArrayList<>();
        ArrayList<Boolean> smi_is_active= new ArrayList<>();
        try {
            Statement st = SQLite.connection.createStatement();
            ResultSet rs = st.executeQuery(dbUtil.getSQLQueryFromProp("activeSmiQuery"));
            while (rs.next()) {
                //int id = rs.getInt("id");
                String source = rs.getString("source");
                String link = rs.getString("link");
                boolean isActive = rs.getBoolean("is_active");

                smi_source.add(source);
                smi_link.add(link);
                smi_is_active.add(isActive);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbQueries.selectSources("active_smi");
        assertEquals(smi_source, Common.SMI_SOURCE);
        assertEquals(smi_link,Common.SMI_LINK);
        assertEquals(smi_is_active,Common.SMI_IS_ACTIVE);
    }


    /*
    Purpose: Method insertNewSource Test
    Input :insertNewSource(connection)
    Expected :
            getRSSInfoFromUI()
    */
    //source : google
    //link :https://news.google.com/rss
    @Test
    public void insertnewsource(){
        dbQueries.insertNewSource(connection);
    }

    /*
    Purpose: Method deleteDuplicates Test
    Input : deleteDuplicates(connection)
    Expected :
            deleteAllDuplicates
    */
    @Test
    public void deleteduplicates() throws SQLException {
        Utilities dbUtil = new Utilities();
        Statement st = connection.createStatement();
        boolean a = (st.executeUpdate(dbUtil.getSQLQueryFromProp("deleteAllDuplicates")) ==0);
        dbQueries.deleteDuplicates(connection);
        st.close();
        assertTrue(a);
    }


    /*
    Purpose: Method updateIsActiveStatus Test
    Input : updateIsActiveStatus(true, "google", connection);
    Expected :
            preparedStatement.setBoolean(1, pBoolean)
            preparedStatement.setString(2, pSource)
            preparedStatement.executeUpdate()
    */
    @Test
    public void updateisactivestatus() throws SQLException {
        dbQueries.updateIsActiveStatus(true, "google", connection);
    }


    /*
    Purpose: Method deleteExcluded Test
    Input : deleteExcluded("google",connection)
    Expected :
           dbUtil.getSQLQueryFromProp("deleteExcluded")
    */
    @Test
    public void deleteexcluded(){
        dbQueries.deleteExcluded("google",connection);
    }


    /*
    Purpose: Method isTitleExists Test
    Input : isTitleExists("test-title",connection)
    Expected :
           isExist : true
    */
    @Test
    public void istitleexists(){
        String testTitle = "test-title";
        dbQueries.insertTitleIn256(testTitle, connection);
        boolean isExist = dbQueries.isTitleExists(testTitle,connection);
        assertTrue(isExist);
    }


    /*
    Purpose: Method archiveNewsCount Test
    Input : archiveNewsCount(connection)
    Expected :
          dbUtil.getSQLQueryFromProp("archiveNewsCount")
    */
    @Test
    public void archivenewscount(){
        dbQueries.archiveNewsCount(connection);
    }
// ----bong-----
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