package team3.gui.IconButton;

import org.junit.Test;
import team3.gui.Gui;

import java.awt.*;

import static org.junit.Assert.*;

/**
 * Purpose: button Construct(clearButtonTop)
 * Expected: icon:Not null, Color:(20, 30, 40), location:(20,30) button
 */

public class clearButtonTopTest {
    @Test
    public void clearButtonTop_constructorTest() {
        clearButtonTop _clearButtonTop = new clearButtonTop(Gui.CLEAR_ICON, new Color(20, 30, 40) , 20, 30);
        assertEquals(20, _clearButtonTop.getX());
        assertEquals(30, _clearButtonTop.getY());
        assertNotNull(_clearButtonTop.getImg());
    }
}