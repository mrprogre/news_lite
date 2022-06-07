package team3.gui.IconButton;

import org.junit.Test;
import team3.gui.Gui;

import java.awt.*;

import static org.junit.Assert.*;

public class exportButtonTest {
    @Test
    public void exportButton_constructorTest() {
        exportButton _exportButton = new exportButton(Gui.CREATE_ICON, new Color(20, 30, 40) , 20, 30);
        assertEquals(20, _exportButton.getX());
        assertEquals(30, _exportButton.getY());
        assertNotNull(_exportButton.getImg());
    }
}