package gui.IconButton;

import javax.swing.*;
import java.awt.*;

public class stopButtonBottom extends iconButtonFactory{
    public stopButtonBottom(ImageIcon icon, Color color, int x, int y) {
        super(icon, x, y);
        this._color = color;
    }

    @Override
    public void buttonSetting(JButton _btn) {
        _btn.setIcon(_icon);
        _btn.setBackground(_color);
        _btn.setBounds(x_axis, y_axis, _width, _height);
    }
}
