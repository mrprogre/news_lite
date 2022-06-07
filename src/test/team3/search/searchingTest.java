package team3.search;



import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import team3.database.DBQueries;
import team3.database.SQLite;
import team3.database.Utilities;
import team3.gui.Gui;
import team3.main.Main;

import javax.swing.table.DefaultTableModel;
import java.lang.NullPointerException;
import java.util.Properties;

public class searchingTest {
    /*
    Purpose: test for using search Method.
    Input : mainSearch("")
    Expected :
        Create Search Object
    */
    //(expected = java.lang.NullPointerException.class)
    @Test
    public void mainSearch() throws NullPointerException {
        Search search = new Search();
        SQLite sqLite = new SQLite();
        sqLite.openSQLiteConnection();
        Utilities dbutils = new Utilities();
        Main.prop = new Properties();
        dbutils.loadSQLQueries(Main.prop);
//        DefaultTableModel defa
//        Gui testgui = new Gui();
//        String query= "insert into news_dual(title) values (?)";
//        sqLite.
        search.mainSearch("word");
    }
        /*
     Purpose: test for using search Method.
     Input : searchByConsole("")
     Expected :
         Create Search Object
     */
    @Test
    public void emailSenderTest() throws IOException {
        try {
            new Search().searchByConsole();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
