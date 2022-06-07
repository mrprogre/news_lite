package team3.gui.IconButton;

import org.junit.Test;
import team3.gui.Gui;

import java.awt.*;

import static org.junit.Assert.*;

public class buttonAddKeywordToListTest {
    @Test
    public void buttonAddKeywordToList_constructorTest() {
        buttonAddKeywordToList _buttonAddKeywordToList = new buttonAddKeywordToList(Gui.CREATE_ICON,20, 30);
        assertEquals(20, _buttonAddKeywordToList.getX());
        assertEquals(30, _buttonAddKeywordToList.getY());
        assertNotNull(_buttonAddKeywordToList.getImg());
    }
}