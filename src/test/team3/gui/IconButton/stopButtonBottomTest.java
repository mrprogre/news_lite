package team3.gui.IconButton;

import org.junit.Test;
import team3.gui.Gui;

import java.awt.*;

import static org.junit.Assert.*;

/**
 * Purpose: button Construct(stopButtonBottom_constructor)
 * Expected: icon:Not null, Color:(20, 30, 40), location:(20,30) button
 */

public class stopButtonBottomTest {
    @Test
    public void stopButtonBottom_constructorTest() {
        stopButtonBottom _stopButtonBottom = new stopButtonBottom(Gui.STOP_ICON, new Color(20, 30, 40), 20, 30);
        assertEquals(20, _stopButtonBottom.getX());
        assertEquals(30, _stopButtonBottom.getY());
        assertNotNull(_stopButtonBottom.getImg());
    }
}