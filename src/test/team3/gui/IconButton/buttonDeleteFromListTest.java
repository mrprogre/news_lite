package team3.gui.IconButton;

import org.junit.Test;
import team3.gui.Gui;

import static org.junit.Assert.*;

public class buttonDeleteFromListTest {
    @Test
    public void buttonDeleteFromList_constructorTest() {
        buttonDeleteFromList _buttonDeleteFromList = new buttonDeleteFromList(Gui.CREATE_ICON,20, 30);
        assertEquals(20, _buttonDeleteFromList.getX());
        assertEquals(30, _buttonDeleteFromList.getY());
        assertNotNull(_buttonDeleteFromList.getImg());
    }
}