package team3.gui.checkboxFactory;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Purpose: Checkbox Construct(news_onlyNew)
 * Expected: location:(20,30), width:40 checkbox
 */

public class news_onlyNewTest {
    @Test
    public void _news_onlyNew_constructorTest() {
        news_onlyNew _news_onlyNew = new news_onlyNew(20, 30, 40);
        assertEquals(20, _news_onlyNew.getX());
        assertEquals(30, _news_onlyNew.getY());
        assertEquals(40, _news_onlyNew.getWidth());
    }
}