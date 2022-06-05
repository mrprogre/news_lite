package team3.database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;

public class UtilitiesTest {
    private Utilities utilities;

    @Before
    public void setUp() throws Exception {
        utilities = new Utilities();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void loadSQLQueries() {
        Properties props = new Properties();
        utilities.loadSQLQueries(props);
        assertFalse(props.isEmpty());
    }

    @Test
    public void getSQLQueryFromProp() {
        Properties props = new Properties();
        utilities.loadSQLQueries(props);
        assertEquals(utilities.getSQLQueryFromProp("deleteTitles", props),
                "DELETE FROM news_dual");
    }
}