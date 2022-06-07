package team3.gui.IconButton;

import org.junit.Test;
import team3.gui.Gui;

import java.awt.*;

import static org.junit.Assert.*;

/**
 * Purpose: button Construct(searchButtonBottom_constructor)
 * Expected: icon:Not null, Color:(20, 30, 40), location:(20,30) button
 */

public class searchButtonBottomTest {
    @Test
    public void searchButtonBottom_constructorTest() {
        searchButtonBottom _searchButtonBottom = new searchButtonBottom(Gui.SEARCH_ICON, new Font("Tahoma", Font.BOLD, 10), new Color(20, 30, 40) , 20, 30);
        assertEquals(20, _searchButtonBottom.getX());
        assertEquals(30, _searchButtonBottom.getY());
        assertNotNull(_searchButtonBottom.getImg());
    }
}