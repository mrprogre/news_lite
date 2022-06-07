package team3.gui;

import org.junit.Test;

import javax.swing.*;

import java.awt.event.ItemEvent;

import static org.junit.Assert.*;

public class CheckBoxEditorTest {
    /*
    Purpose : check if the constructor of CheckBoxEditor works
    Expected : checkBoxEditor(instance) is NotNull
     */


    @Test
    public void CheckBoxEditor_constructorTest() {
        CheckBoxEditor checkBoxEditor = new CheckBoxEditor(new JCheckBox());

        assertNotNull(checkBoxEditor);
        assertNotNull(checkBoxEditor.getCheckBox());
        assertNotNull(checkBoxEditor.getCellEditorValue());
        assertEquals(checkBoxEditor.getRow(), 0);


    }
}
