package team3.gui.IconButton;

import org.junit.Test;
import team3.gui.Gui;

import java.awt.*;

import static org.junit.Assert.*;

public class stopButtonTopTest {
    @Test
    public void stopButtonTop_constructorTest() {
        stopButtonTop _stopButtonTop = new stopButtonTop(Gui.CREATE_ICON, new Color(20, 30, 40), 20, 30);
        assertEquals(20, _stopButtonTop.getX());
        assertEquals(30, _stopButtonTop.getY());
        assertNotNull(_stopButtonTop.getImg());
    }
}