package team3.search;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import team3.database.DBQueries;
import team3.database.SQLite;
import team3.gui.Gui;
import team3.utils.Common;

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


    public boolean isHref(String newsDescribe);
}
