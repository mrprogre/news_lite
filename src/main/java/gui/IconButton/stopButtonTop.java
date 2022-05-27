package gui.IconButton;

import javax.swing.*;
import java.awt.*;

public class stopButtonTop extends iconButtonFactory {
    public stopButtonTop(ImageIcon icon, Color color, int x, int y) {
        super(icon, color, x, y);
    }

    @Override
    public void buttonSetting(JButton _btn) {
        _btn.setIcon(_icon);
        _btn.setBackground(_color);
        _btn.setFont(_font);
        _btn.setBounds(x_axis, y_axis, _width, _height);
    }
}
