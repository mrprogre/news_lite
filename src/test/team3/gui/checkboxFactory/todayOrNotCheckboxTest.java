package team3.gui.checkboxFactory;

import org.junit.Test;

import static org.junit.Assert.*;

public class todayOrNotCheckboxTest {
    @Test
    public void todayOrNotCheckbox_constructorTest() {
        todayOrNotCheckbox todayOrNotCheckbox = new todayOrNotCheckbox(20, 30, 40);
        assertEquals(20, todayOrNotCheckbox.getX());
        assertEquals(30, todayOrNotCheckbox.getY());
        assertEquals(40, todayOrNotCheckbox.getWidth());
    }

}