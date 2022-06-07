package team3.database;

import team3.gui.Gui;
import team3.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.IOException;
import java.util.Properties;

public class Utilities {
    private static final Logger LOGGER = LoggerFactory.getLogger(Utilities.class);

    public void loadSQLQueries(Properties props) {
        try {
            props.loadFromXML(SQLite.class.getClassLoader().getResourceAsStream("sql-queries.xml"));
            LOGGER.info("SQL queries loaded.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getSQLQueryFromProp(String entryKey) {
        return Main.prop.getProperty(entryKey);
    }

    public String getSQLQueryFromProp(String entryKey, Properties props) {
        return props.getProperty(entryKey);
    }

    public RSSInfoFromUI getRSSInfoFromUI() {
        JTextField sourceName = new JTextField();
        JTextField rssLink = new JTextField();
        Object[] newSource = {"Source:", sourceName, "Link to rss:", rssLink};
        int result =
                JOptionPane.showConfirmDialog(Gui.scrollPane, newSource,
                        "New source", JOptionPane.OK_CANCEL_OPTION);
        return new RSSInfoFromUI(sourceName, rssLink, result);
    }
}
