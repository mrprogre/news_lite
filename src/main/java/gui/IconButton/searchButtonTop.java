package gui.IconButton;

import search.Search;

import javax.swing.*;
import java.awt.*;

public class searchButtonTop extends iconButtonFactory{
    public searchButtonTop(ImageIcon icon, Color color, Font font, int x, int y) {
        super(icon, color, x, y);
        this._font = font;
    }

    @Override
    public void buttonSetting(JButton _btn) {
        _btn.setToolTipText("Без заголовков со словами " + Search.excludeFromSearch);
        _btn.setIcon(_icon);
        _btn.setBackground(_color);
        _btn.setFont(_font);
        _btn.setBounds(x_axis, y_axis, _width, _height);
    }
}
