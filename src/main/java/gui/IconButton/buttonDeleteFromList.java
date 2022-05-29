package gui.IconButton;

import javax.swing.*;

public class buttonDeleteFromList extends iconButtonFactory{
    public buttonDeleteFromList(ImageIcon icon, int x, int y) {
        super(icon, x, y);
    }

    @Override
    public void buttonSetting(JButton _btn) {
        _btn.setIcon(_icon);
        _btn.setBounds(x_axis, y_axis, _width, _height);
    }
}
