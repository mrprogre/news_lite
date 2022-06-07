package team3.gui.checkboxFactory;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Purpose: Checkbox Construct(autoSend)
 * Expected: location:(20,30), width:40 checkbox
 */
public class autoSendTest {

    @Test
    public void autoSend_constructorTest() {
        autoSend _autoSend = new autoSend(20, 30, 40);
        assertEquals(20, _autoSend.getX());
        assertEquals(30, _autoSend.getY());
        assertEquals(40, _autoSend.getWidth());
    }
}