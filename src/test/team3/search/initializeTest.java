package team3.search;

import org.junit.jupiter.api.Test;

import java.io.IOException;

class initializeTest {
    //SearchObject Test
    /*
    Purpose: test for using search object.
    Input : new Search()
    Expected :
        Create Search Object
    */
    @Test
    public void objectCreateTest() throws IOException {
        try {
            Search search = new Search();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //emailSenderTest
    @Test
    public void emailSenderTest() throws IOException {
        try {
            String subject = ("News (" + new Search().today + ")");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

