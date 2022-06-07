package team3.gui.checkboxFactory;

import java.awt.*;

public class autoUpdateNews extends checkBoxFactory{
    public autoUpdateNews(int x, int y, int w) {
        super(x, y, w);
    }

    @Override
    public void checkBoxSetting(Checkbox _chkbox) {
        _chkbox.setState(false);
        _chkbox.setFocusable(false);
        _chkbox.setForeground(Color.WHITE);
        _chkbox.setBounds(x_axis, y_axis, _width, _height);
    }
}
