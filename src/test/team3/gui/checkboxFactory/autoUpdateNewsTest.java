package team3.gui.checkboxFactory;

import org.junit.Test;

import static org.junit.Assert.*;

public class autoUpdateNewsTest {
    @Test
    public void _autoUpdateNews_cunstructorTest() {
        autoSend _autoUpdateNews = new autoSend(20, 30, 40);
        assertEquals(20, _autoUpdateNews.getX());
        assertEquals(30, _autoUpdateNews.getY());
        assertEquals(40, _autoUpdateNews.getWidth());
    }

}