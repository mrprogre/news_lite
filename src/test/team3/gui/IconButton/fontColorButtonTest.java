package team3.gui.IconButton;

import org.junit.Test;
import team3.gui.Gui;

import java.awt.*;

import static org.junit.Assert.*;

/**
 * Purpose: button Construct(fontColorButton_constructor)
 * Expected: icon:Not null, Color:(20, 30, 40), location:(20,30) button
 */

public class fontColorButtonTest {
    @Test
    public void fontColorButton_constructorTest() {
        fontColorButton _fontColorButton = new fontColorButton(Gui.FONT_ICON, new Color(20, 30, 40) , 20, 30);
        assertEquals(20, _fontColorButton.getX());
        assertEquals(30, _fontColorButton.getY());
        assertNotNull(_fontColorButton.getImg());
    }
}