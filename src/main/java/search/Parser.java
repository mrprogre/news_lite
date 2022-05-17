package search;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.time.LocalTime;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import gui.Gui;
import utils.Common;

public class Parser {
    LocalTime timeStart;
    LocalTime timeEnd;
    Duration searchTime;
    final static int LONG_SEARCH = 2;

    public SyndFeed parseFeed(String url) throws IllegalArgumentException, FeedException, IOException {
        timeStart = LocalTime.now();
        URLConnection urlConnection = new URL(url).openConnection();
        urlConnection.setConnectTimeout(1000);
        XmlReader reader = new XmlReader(urlConnection);

        // подсчёт времени поиска
        timeEnd = LocalTime.now();
        searchTime = Duration.between(timeStart, timeEnd);

        String urlToConsole = url.replaceAll(("https://|http://|www."), "");
        urlToConsole = urlToConsole.substring(0, urlToConsole.indexOf("/"));

        if (!Gui.GUI_IN_TRAY.get() && searchTime.getSeconds() > LONG_SEARCH) Common.console("info: long search - " + urlToConsole
                + " - " + searchTime.getSeconds() + " s.");

        return new SyndFeedInput().build(reader);
    }
}