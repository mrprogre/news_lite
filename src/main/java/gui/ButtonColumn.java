package gui;

import database.DatabaseQueries;
import database.SQLite;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonColumn extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener {
    final JTable table;
    final JButton renderButton;
    final JButton editButton;
    //String text;

    public ButtonColumn(JTable table, int column) {
        super();
        this.table = table;
        renderButton = new JButton();

        editButton = new JButton();
        editButton.setFocusPainted(false);
        editButton.addActionListener(this);

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(column).setCellRenderer(this);
        columnModel.getColumn(column).setCellEditor(this);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (hasFocus) {
            renderButton.setForeground(table.getForeground());
            renderButton.setBackground(UIManager.getColor("Button.background"));
            renderButton.setIcon(Gui.DELETE_FROM_KEYWORDS_ICON);
        } else if (isSelected) {
            renderButton.setForeground(table.getSelectionForeground());
            //renderButton.setBackground(table.getSelectionBackground());
            renderButton.setIcon(Gui.DELETE_FROM_KEYWORDS_ICON);
        } else {
            renderButton.setForeground(table.getForeground());
            renderButton.setBackground(UIManager.getColor("Button.background"));
            renderButton.setIcon(Gui.DELETE_FROM_KEYWORDS_ICON);
        }
        //renderButton.setText((value == null) ? ";" : value.toString() );
        return renderButton;
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return editButton;
    }

    public Object getCellEditorValue() {
        try {
            return ButtonColumn.class.getMethod("getCellEditorValue");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public void actionPerformed(ActionEvent e) {
        DatabaseQueries sqlite = new DatabaseQueries();
        fireEditingStopped();
        int rowWithSource = table.getSelectedRow();
        int rowWithExcludeWord = Gui.tableForAnalysis.getSelectedRow();
        int delRowWithExcludeWord = 0;

        // ???????????????????? ???????????????? ????????
        Window window = FocusManager.getCurrentManager().getActiveWindow();
        int activeWindow = 0;
        if (window.toString().contains("Avandy")) {
            activeWindow = 1;
        }
        if (window.toString().contains("Sources")) {
            activeWindow = 2;
        }
        if (window.toString().contains("Excluded")) {
            activeWindow = 3;
            delRowWithExcludeWord = Dialogs.table.getSelectedRow();
        }

        // ???????? ?????????????? ?? ???????????????? ?????????????? ???????? ???? ???????????????? ???????????? (?????????????????? ?? ????????)
        if (activeWindow == 1 && rowWithExcludeWord != -1) {
            rowWithExcludeWord = Gui.tableForAnalysis.convertRowIndexToModel(rowWithExcludeWord);
            String source = (String) Gui.modelForAnalysis.getValueAt(rowWithExcludeWord, 0);
            // ???????????????? ???? ?????????????????????? ????????
            Gui.modelForAnalysis.removeRow(rowWithExcludeWord);
            // ???????????????????? ?? ???????? ???????????? ?? ???????? excluded.txt
            sqlite.insertNewExcludedWord(source, SQLite.connection);
        }

        // ???????? ???????????????????? RSS
        if (activeWindow == 2 && rowWithSource != -1) {
            rowWithSource = table.convertRowIndexToModel(rowWithSource);
            String source = (String) Dialogs.model.getValueAt(rowWithSource, 1);
            // ???????????????? ???? ?????????????????????? ????????
            Dialogs.model.removeRow(rowWithSource);
            sqlite.deleteSource(source, SQLite.connection);
        }

        // ???????? ?? ???????????????????????? ???? ?????????????? ???????? (?????????????? ???? ????????)
        if (activeWindow == 3 && delRowWithExcludeWord != -1) {
            delRowWithExcludeWord = Dialogs.table.convertRowIndexToModel(delRowWithExcludeWord);
            String source = (String) Dialogs.model.getValueAt(delRowWithExcludeWord, 1);
            // ???????????????? ???? ?????????????????????? ????????
            Dialogs.model.removeRow(delRowWithExcludeWord);
            sqlite.deleteExcluded(source, SQLite.connection);
        }

    }

}