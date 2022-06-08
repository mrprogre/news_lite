package team3.gui;

import org.junit.Test;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;

import static org.junit.Assert.*;
import static team3.gui.Gui.tableForAnalysis;


public class ButtonColumnTest {

    /*
    Purpose : test to initialize ButtonColumn class fields (table, renderButton, editButton)
    Expected : buttonColumn(instance) is not null
               each field is not null
     */



    @Test
    public void ButtonColumn_constructorTest() {
        new Gui();
        ButtonColumn buttonColumn = new ButtonColumn(tableForAnalysis, 2);

        assertNotNull(buttonColumn);
        assertSame(buttonColumn.getTable(), tableForAnalysis);

        assertNotNull(buttonColumn.renderButton);
        assertNotNull(buttonColumn.renderButton.getUI());
        assertNotNull(buttonColumn.editButton);
        assertNotNull(buttonColumn.editButton.getUI());

        assertEquals(buttonColumn.getTable().getTableHeader(), tableForAnalysis.getTableHeader());
    }
}
