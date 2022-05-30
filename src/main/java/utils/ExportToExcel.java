package utils;

import gui.Gui;
import jxl.Workbook;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import static javax.swing.JFileChooser.*;


public class ExportToExcel extends ExportManager {

    private File baseExcelFile;
    private ExcelFormat excelFormat;
    
    public ExportToExcel() {
        setExtension(".xls");
        setExtensionFilter(new FileNameExtensionFilter("*.xls", "*.xls", "*.XLS", "*.*"));
        setjFileChooser();
        this.excelFormat = new ExcelFormat();
    }
    
    public void exportResultsToExcel() {

        baseExcelFile = new File(getjFileChooser().getSelectedFile() + getExtension());
        
        try {
            if (getjFileChooser().showDialog(null,"Save") == APPROVE_OPTION) {

                WritableWorkbook newExcel = Workbook.createWorkbook(baseExcelFile);
                
                WritableSheet page = newExcel.createSheet("001", 0);
                initPage(page);
                makeExcelHeaders(page);
                extractGUIDataToPage(page);
               
                newExcel.write();
                newExcel.close();
                
                Common.console("status: export is done");
                LOG.info("Export is done");
                
            } else Common.console("status: export canceled");
        } catch (WriteException | IOException e) {
            Common.console("status: export exception");
            LOG.warn("export exception");
        }
    }
    
    private void initPage(WritableSheet page) throws WriteException, RowsExceededException {
        page.getSettings().setShowGridLines(true);
        page.setColumnView(0, 10);
        page.setColumnView(1, 16);
        page.setColumnView(2, 100);
        page.setColumnView(3, 30);
        page.setColumnView(4, 120);
        page.setRowView(0, 600);
    }

    private void makeExcelHeaders(WritableSheet page) throws WriteException{
        String headers[] = getHeaders();
        for (int col = 0; col < headers.length; col++) {
            Label cellName = new Label(col, 0, headers[col], excelFormat.headerCellFormat());
            page.addCell(cellName);
        }
    }

    private void extractGUIDataToPage(WritableSheet page) throws WriteException, IOException{
        for (int z = 0; z < Gui.model.getRowCount(); z++) {
            jxl.write.Number jxlNumber = new jxl.write.Number(0, z + 1, Integer.parseInt(Gui.model.getValueAt(z, 0).toString()), excelFormat.numberCellFormat()); //num

            Label source = new Label(1, z + 1, Gui.model.getValueAt(z, 1).toString(), excelFormat.sourceCellFormat()); //Source
            Label title = new Label(2, z + 1, Gui.model.getValueAt(z, 2).toString(), excelFormat.titleCellFormat()); //Title
            Label date = new Label(3, z + 1, Gui.model.getValueAt(z, 3).toString(), excelFormat.dateCellFormat()); //Date
            Label link = new Label(4, z + 1, Gui.model.getValueAt(z, 4).toString(), excelFormat.hyperlinkCellFormat()); //Link

            WritableHyperlink hyperlink = new WritableHyperlink(4, z + 1, new URL(Gui.model.getValueAt(z, 4).toString()));

            page.addHyperlink(hyperlink);
            page.addCell(jxlNumber);
            page.addCell(source);
            page.addCell(title);
            page.addCell(date);
            page.addCell(link);
            page.setRowView(z + 1, 600);
        }
    }
}
