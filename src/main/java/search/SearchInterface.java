package search;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import database.DBQueries;
import database.SQLite;
import gui.Gui;
import utils.Common;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public interface SearchInterface {
    public void transCommand(String command) throws SQLException;

    public void transDelete() throws SQLException;
    public void getTodayOrNotCbx(DBQueries dbqueries, PreparedStatement st, String smi_source, String title, String newsDescribe, Date pubDate, String dateToEmail, String link, int date_diff) throws SQLException;

    public void mainSearch(String pSearchType);
    public void searchByConsole();
    public boolean isHref(String newsDescribe);
}
