package team3.gui.checkboxFactory;

import org.junit.Test;

import static org.junit.Assert.*;

public class news_onlyNewTest {
    @Test
    public void _news_onlyNew_cunstructorTest() {
        autoSend _news_onlyNew = new autoSend(20, 30, 40);
        assertEquals(20, _news_onlyNew.getX());
        assertEquals(30, _news_onlyNew.getY());
        assertEquals(40, _news_onlyNew.getWidth());
    }
}