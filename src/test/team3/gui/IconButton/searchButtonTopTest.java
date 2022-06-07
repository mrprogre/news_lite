package team3.gui.IconButton;

import org.junit.Test;
import team3.gui.Gui;

import java.awt.*;

import static org.junit.Assert.*;

/**
 * Purpose: button Construct(searchButtonTop_constructor)
 * Expected: icon:Not null, Color:(20, 30, 40), location:(20,30) button
 */

public class searchButtonTopTest {
    @Test
    public void searchButtonTop_constructorTest() {
        searchButtonTop _searchButtonTop = new searchButtonTop(Gui.SEARCH_ICON, new Color(20, 30, 40), new Font("Tahoma", Font.BOLD, 10), 20, 30);
        assertEquals(20, _searchButtonTop.getX());
        assertEquals(30, _searchButtonTop.getY());
        assertNotNull(_searchButtonTop.getImg());
    }
}