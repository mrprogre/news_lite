package team3.gui.IconButton;

import org.junit.Test;
import team3.gui.Gui;

import java.awt.*;

import static org.junit.Assert.*;

public class searchButtonTopTest {
    @Test
    public void searchButtonTop_constructorTest() {
        searchButtonTop _searchButtonTop = new searchButtonTop(Gui.CREATE_ICON, new Color(20, 30, 40), new Font("Tahoma", Font.BOLD, 10), 20, 30);
        assertEquals(20, _searchButtonTop.getX());
        assertEquals(30, _searchButtonTop.getY());
        assertNotNull(_searchButtonTop.getImg());
    }
}