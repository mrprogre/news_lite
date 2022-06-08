package team3.gui.IconButton;

import org.junit.Test;
import team3.gui.Gui;

import static org.junit.Assert.*;

/**
 * Purpose: button Construct(buttonDeleteFromList)
 * Expected: icon:Not null, Color:(20, 30, 40), location:(20,30) button
 */

public class buttonDeleteFromListTest {
    @Test
    public void buttonDeleteFromList_constructorTest() {
        buttonDeleteFromList _buttonDeleteFromList = new buttonDeleteFromList(Gui.DELETE_ICON,20, 30);
        assertEquals(20, _buttonDeleteFromList.getX());
        assertEquals(30, _buttonDeleteFromList.getY());
        assertNotNull(_buttonDeleteFromList.getImg());
    }
}