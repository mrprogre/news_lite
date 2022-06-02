package team3.database;

import javax.swing.*;

public class RSSInfoFromUI {
    private final JTextField sourceName;
    private final JTextField rssLink;
    private final int result;

    public RSSInfoFromUI(JTextField rssName, JTextField rssSource, int result) {
        this.sourceName = rssName;
        this.rssLink = rssSource;
        this.result = result;
    }
    public int getResult() {
        return result;
    }

    public JTextField getRssLink() {
        return rssLink;
    }

    public JTextField getSourceName() {
        return sourceName;
    }
}
