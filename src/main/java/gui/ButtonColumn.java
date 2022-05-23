package gui;

import database.DBQueries;

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

        editButton = initEditButton();

        initColumnModel(table, column);
    }

    private void initColumnModel(JTable table, int column) {
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(column).setCellRenderer(this);
        columnModel.getColumn(column).setCellEditor(this);
    }

    private JButton initEditButton() {

        final JButton editButton;
        editButton = new JButton();
        editButton.setFocusPainted(false);
        editButton.addActionListener(this);
        return editButton;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (hasFocus) {
            setRenderBtn(table);
        } else if (isSelected) {
            configureRenderBtn(table);
        } else {
            setRenderBtn(table);
        }
        //renderButton.setText((value == null) ? ";" : value.toString() );
        return renderButton;
    }

    private void configureRenderBtn(JTable table) {
        renderButton.setForeground(table.getSelectionForeground());
        //renderButton.setBackground(table.getSelectionBackground());
        renderButton.setIcon(Gui.DELETE_ICON);
    }

    private void setRenderBtn(JTable table) {
        renderButton.setForeground(table.getForeground());
        renderButton.setBackground(UIManager.getColor("Button.background"));
        renderButton.setIcon(Gui.DELETE_ICON);
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
        DBQueries dbQueries = new DBQueries();
        fireEditingStopped();
        int rowWithSource = table.getSelectedRow();
        int rowWithExcludeWord = Gui.tableForAnalysis.getSelectedRow();
        int delRowWithExcludeWord = 0;

        // определяем активное окно
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

        // окно таблицы с анализом частоты слов на основной панели (добавляем в базу)
        setActive_1(dbQueries, rowWithExcludeWord, activeWindow);

        // окно источников RSS
        setActive_2(dbQueries, rowWithSource, activeWindow);

        // окно с исключенными из анализа слов (удаляем из базы)
        setActive_3(dbQueries, delRowWithExcludeWord, activeWindow);

    }

    private void setActive_3(DBQueries sqlite, int delRowWithExcludeWord, int activeWindow) {
        if (activeWindow == 3 && delRowWithExcludeWord != -1) {
            delRowWithExcluded(sqlite, delRowWithExcludeWord);
        }
    }

    private void setActive_2(DBQueries sqlite, int rowWithSource, int activeWindow) {
        if (activeWindow == 2 && rowWithSource != -1) {
            delRowWithSource(sqlite, rowWithSource);
        }
    }

    private void setActive_1(DBQueries sqlite, int rowWithExcludeWord, int activeWindow) {
        if (activeWindow == 1 && rowWithExcludeWord != -1) {
            setRowWithExcludeWord(sqlite, rowWithExcludeWord);
        }
    }

    private void delRowWithExcluded(DBQueries sqlite, int delRowWithExcludeWord) {
        delRowWithExcludeWord = Dialogs.table.convertRowIndexToModel(delRowWithExcludeWord);
        String source = (String) Dialogs.model.getValueAt(delRowWithExcludeWord, 1);
        // удаление из диалогового окна
        Dialogs.model.removeRow(delRowWithExcludeWord);
        // удаление из файла excluded.txt
        //Common.delLine(source, Main.excludedPath);
        // удаление из базы данных
        sqlite.deleteExcluded(source);
    }

    private void delRowWithSource(DBQueries sqlite, int rowWithSource) {
        rowWithSource = table.convertRowIndexToModel(rowWithSource);
        String source = (String) Dialogs.model.getValueAt(rowWithSource, 1);
        // удаление из диалогового окна
        Dialogs.model.removeRow(rowWithSource);
        // удаление из файла sources.txt
        //Common.delLine(source, Main.sourcesPath);
        // удаление из базы данных
        sqlite.deleteSource(source);
    }

    private void setRowWithExcludeWord(DBQueries sqlite, int rowWithExcludeWord) {
        rowWithExcludeWord = Gui.tableForAnalysis.convertRowIndexToModel(rowWithExcludeWord);
        String source = (String) Gui.modelForAnalysis.getValueAt(rowWithExcludeWord, 0);
        // удаление из диалогового окна
        Gui.modelForAnalysis.removeRow(rowWithExcludeWord);
        // добавление в базу данных и файл excluded.txt
        sqlite.insertNewExcludedWord(source);
    }

}