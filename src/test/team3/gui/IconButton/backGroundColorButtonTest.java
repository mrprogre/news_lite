package team3.gui.IconButton;

import org.junit.Test;
import team3.gui.Gui;
import team3.gui.checkboxFactory.autoSend;
import team3.gui.checkboxFactory.todayOrNotCheckbox;

import java.awt.*;

import static org.junit.Assert.*;

public class backGroundColorButtonTest {
    @Test
    public void backGroundColor_constructorTest() {
        backGroundColorButton _backGroundColorButton= new backGroundColorButton(Gui.BG_ICON, new Color(20, 30, 40), 20, 30);
        assertEquals(20, _backGroundColorButton.getX());
        assertEquals(30, _backGroundColorButton.getY());
        assertNotNull(_backGroundColorButton.getImg());
    }
}