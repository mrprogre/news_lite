package team3.database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class SQLiteTest {
    private SQLite sqLite;

    @Before
    public void setUp() throws Exception {
        sqLite = new SQLite();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void openSQLiteConnection() throws SQLException {
        sqLite.openSQLiteConnection();
        assertTrue(SQLite.connection.isValid(3));
    }

    @Test
    public void closeSQLiteConnection() {
    }

    @Test
    public void getInstance() {
    }
}