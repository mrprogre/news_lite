package team3.search;



import java.awt.*;
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
import team3.utils.Common;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.lang.NullPointerException;
import java.sql.Connection;
import java.util.Properties;

public class searchingTest {
    private Connection connection;
    @Before
    public void setUp(){

        Search search = new Search();
        SQLite sqLite = new SQLite();
        sqLite.openSQLiteConnection();
        DBQueries dbqueries = new DBQueries();
        Utilities dbutils = new Utilities();
        Main.prop = new Properties();
        dbutils.loadSQLQueries(Main.prop);

        String[] args = new String[2];
        UIManager.put("Component.arc", 10);
        UIManager.put("ProgressBar.arc", 6);
        UIManager.put("Button.arc", 8);
        Common.getColorsSettingsFromFile();
        UIManager.put("Table.alternateRowColor", new Color(59, 59, 59));
        UIManager.put("TextField.background", Color.GRAY);
        UIManager.put("TextField.foreground", Color.BLACK);
        Main.keywordsFromConsole =  new String[args.length];
        dbutils.loadSQLQueries(Main.prop);
    }
    /*
    Purpose: test for using search Method  keyword length =< 15.
    Input : mainSearch("word")
    Expected :
        Searching word
    */
    //(expected = java.lang.NullPointerException.class)
    @Test
    public void mainSearchWord() throws NullPointerException {

//        JScrollPane scrollPane = new JScrollPane();
//        scrollPane.setBounds(10, 40, 860, 500);
//        getContentPane().add(scrollPane);
//        Object[] columns = {"Num", "Source", "Title (double click to open the link)", "Date", "Link"};
//        model = new DefaultTableModel(new Object[][]{

        Search search = new Search();
        Gui testgui = new Gui();
        String query= "insert into news_dual(title) values (?)";
//        sqLite.
        search.mainSearch("word");
    }


    /*
    Purpose: test for using search Method keyword length > 15.
    Input : mainSearch("words")
    Expected :
        Searching words
    */
    @Test
    public void mainSearchWords() throws NullPointerException {
        Search search = new Search();
        Gui testgui = new Gui();
        String query= "insert into news_dual(title) values (?)";
        search.mainSearch("words");
    }



    /*
     Purpose: test for using searchbyconsole Method.
     Input : searchByConsole()
     Expected :
         perform searching transaction
     */
    @Test
    public void searchbyConsole() throws IOException {
        Search search = new Search();
        Gui gui = new Gui();
        Common common = new Common();
        common.writeToConfig("google","writeToConfig");
        common.fill();
        common.console("info: long search - forbes.ru -5 s.");
        search.searchByConsole();
    }
}
