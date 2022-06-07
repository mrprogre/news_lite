package search;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import database.DBQueries;
import database.SQLite;
import email.EmailSender;
import gui.Gui;
import main.Main;
import utils.Common;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import static search.Search.isStop;


public class SearchMethod implements SearchInterface {

    public boolean isHref(String newsDescribe) {
        return newsDescribe.contains("<img")
                || newsDescribe.contains("href")
                || newsDescribe.contains("<div")
                || newsDescribe.contains("&#34")
                || newsDescribe.contains("<p lang")
                || newsDescribe.contains("&quot")
                || newsDescribe.contains("<span")
                || newsDescribe.contains("<ol")
                || newsDescribe.equals("");
    }

    public void transCommand(String command) throws SQLException {
        Statement st_command = SQLite.connection.createStatement();
        st_command.execute(command);
        st_command.close();
    }

    public void transDelete() throws SQLException {
        String q_del = "delete from news_dual where title = ''";
        Statement st_del = SQLite.connection.createStatement();
        st_del.executeUpdate(q_del);
        st_del.close();
    }


}
