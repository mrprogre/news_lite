package gui;

import javax.swing.*;
import java.awt.*;

public abstract class iconButtonFactory {
    protected ImageIcon _icon;
    protected Color _color;
    protected Font _font;
    protected int x_axis, y_axis;
    protected static int _width = 30, _height = 22;

    public iconButtonFactory(ImageIcon icon, Color color, int x, int y) {
        this._icon = icon;
        this._color = color;
        this.x_axis = x; this.y_axis = y;
    }

    public abstract void buttonSetting(JButton _btn);
}
