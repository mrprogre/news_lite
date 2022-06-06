package team3.gui.IconButton;

import org.junit.Test;
import team3.gui.Gui;

import java.awt.*;

import static org.junit.Assert.*;

public class fontColorButtonTest {
    @Test
    public void fontColorButton_constructorTest() {
        fontColorButton _fontColorButton = new fontColorButton(Gui.CREATE_ICON, new Color(20, 30, 40) , 20, 30);
        assertEquals(20, _fontColorButton.getX());
        assertEquals(30, _fontColorButton.getY());
        assertNotNull(_fontColorButton.getImg());
    }
}