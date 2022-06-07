package team3.gui.IconButton;

import org.junit.Test;
import team3.gui.Gui;

import java.awt.*;

import static org.junit.Assert.*;
public class searchButtonBottomTest {
    @Test
    public void searchButtonBottom_constructorTest() {
        searchButtonBottom _searchButtonBottom = new searchButtonBottom(Gui.CREATE_ICON, new Font("Tahoma", Font.BOLD, 10), new Color(20, 30, 40) , 20, 30);
        assertEquals(20, _searchButtonBottom.getX());
        assertEquals(30, _searchButtonBottom.getY());
        assertNotNull(_searchButtonBottom.getImg());
    }
}