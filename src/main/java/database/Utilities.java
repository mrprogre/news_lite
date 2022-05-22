package database;

import main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Utilities {
    private static final Logger LOGGER = LoggerFactory.getLogger(Utilities.class);

    public void loadSQLQueries() {
        try {
            Main.prop.loadFromXML(SQLite.class.getClassLoader().getResourceAsStream("sql-queries.xml"));
            LOGGER.info("SQL queries loaded.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getSQLQueryFromProp(String entryKey) {
        return Main.prop.getProperty(entryKey);
    }
}
