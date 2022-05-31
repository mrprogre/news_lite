package gui.checkboxFactory;

import javax.swing.*;
import java.awt.*;

public abstract class checkBoxFactory {
    protected Color _color;
    protected Font _font;
    protected int x_axis, y_axis, _width;
    protected static int _height = 20;

    public checkBoxFactory(int x, int y, int w) {
        this.x_axis = x; this.y_axis = y;
        this._width = w;
    }

    public abstract void checkBoxSetting(Checkbox _chkbox);
}
