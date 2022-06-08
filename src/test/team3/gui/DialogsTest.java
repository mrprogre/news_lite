package team3.gui;

import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;

public class DialogsTest {

    /*
    Purpose : Unit test to initialize fields in Dialogs.java
    Expected : each instance( smiDialog, exclDialog ) is not null.
               each field is correctly initialized.
     */
    @Test
    public void Dialogs_constructorTest() {
        Dialogs smiDialog = new Dialogs("smiDlg");
        Dialogs exclDialog = new Dialogs("exclDlg");
        assertNotNull(exclDialog);
        assertNotNull(smiDialog);

        assertEquals("12.0", String.valueOf(exclDialog.textAreaForDialogs.getBounds().getX()));
        assertEquals("27.0", String.valueOf(exclDialog.textAreaForDialogs.getBounds().getY()));
        assertEquals("22.0", String.valueOf(exclDialog.textAreaForDialogs.getBounds().getWidth()));
        assertEquals("233.0", String.valueOf(exclDialog.textAreaForDialogs.getBounds().getHeight()));
        assertEquals(10, exclDialog.textAreaForDialogs.getTabSize());

        assertEquals("12.0", String.valueOf(smiDialog.textAreaForDialogs.getBounds().getX()));
        assertEquals("27.0", String.valueOf(smiDialog.textAreaForDialogs.getBounds().getY()));
        assertEquals("22.0", String.valueOf(smiDialog.textAreaForDialogs.getBounds().getWidth()));
        assertEquals("233.0", String.valueOf(smiDialog.textAreaForDialogs.getBounds().getHeight()));
        assertEquals(10, smiDialog.textAreaForDialogs.getTabSize());


        assertEquals(3, exclDialog.table.getColumnCount());
        assertEquals("Num", exclDialog.table.getColumnName(0));
        assertEquals("Word", exclDialog.table.getColumnName(1));
        assertEquals("Del", exclDialog.table.getColumnName(2));

    }
}
