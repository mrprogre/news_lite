package model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.swing.*;

@Getter
@AllArgsConstructor
public class RssInfoFromUi {
    private final JTextField sourceName;
    private final JTextField rssLink;
    private final int result;
}
