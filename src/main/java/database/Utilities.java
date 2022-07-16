package database;

import gui.Gui;
import lombok.extern.slf4j.Slf4j;
import model.RssInfoFromUi;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
public class Utilities {

    public RssInfoFromUi getRssInfoFromUi() {
        JTextField sourceName = new JTextField();
        JTextField rssLink = new JTextField();
        Object[] newSource = {"Source:", sourceName, "Link to rss:", rssLink};
        int result = JOptionPane.showConfirmDialog(Gui.scrollPane, newSource,
                "New source", JOptionPane.OK_CANCEL_OPTION);
        return new RssInfoFromUi(sourceName, rssLink, result);
    }

}
