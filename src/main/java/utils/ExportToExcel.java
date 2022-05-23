package utils;

import gui.Gui;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import static javax.swing.JFileChooser.*;
import static jxl.format.Colour.BLACK;
import static jxl.format.Colour.DARK_GREEN;
import static jxl.format.UnderlineStyle.NO_UNDERLINE;


public class ExportToExcel extends ExportManager {

    private File baseExcelFile;
    
    public ExportToExcel() {
        setExtension(".xls");
        setExtensionFilter(new FileNameExtensionFilter("*.xls", "*.xls", "*.XLS", "*.*"));
        setjFileChooser();
    }
    
    public void exportResultsToExcel() {

        baseExcelFile = new File(getjFileChooser().getSelectedFile() + getExtension());
        
        try {
            if (getjFileChooser().showDialog(null,"Save") == APPROVE_OPTION) {

                WritableWorkbook newExcel = Workbook.createWorkbook(baseExcelFile);
                WritableSheet page = newExcel.createSheet("001", 0);

                initPage(page);
                

                //no bold
                WritableFont writableFontNoBold = new ExcelFont
                        .Builder()
                        .pointSize(11)
                        .noBold()
                        .underlineStyle(NO_UNDERLINE)
                        .color(BLACK)
                        .build();

                //bold
                WritableFont writableFontBold = new ExcelFont
                        .Builder()
                        .pointSize(11)
                        .bold()
                        .underlineStyle(NO_UNDERLINE)
                        .color(BLACK)
                        .build();
                

                // Hyperlinks Format
                WritableFont hyperlinkFont = new ExcelFont
                        .Builder()
                        .pointSize(11)
                        .noBold()
                        .color(DARK_GREEN)
                        .build();
                
                
                WritableCellFormat hyperlinkCellFormat = new WritableCellFormat(hyperlinkFont);
                hyperlinkCellFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, BLACK);
                hyperlinkCellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                hyperlinkCellFormat.setWrap(true);

                // Title format
                WritableCellFormat cellTitleFormat = new WritableCellFormat(writableFontNoBold);
                cellTitleFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, BLACK);
                cellTitleFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                cellTitleFormat.setWrap(true);

                // Number Format
                WritableCellFormat cellNumberFormat = new WritableCellFormat(writableFontNoBold);
                cellNumberFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, BLACK);
                cellNumberFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                cellNumberFormat.setAlignment(Alignment.CENTRE);
                
                // Source Format
                WritableCellFormat cellSourceFormat = new WritableCellFormat(writableFontNoBold);
                cellSourceFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, BLACK);
                cellSourceFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                cellSourceFormat.setAlignment(Alignment.CENTRE);

                //HEADERS: color, bold
                WritableCellFormat cellHeaderFormat = new WritableCellFormat(writableFontBold);
                cellHeaderFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, BLACK);
                cellHeaderFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                cellHeaderFormat.setAlignment(jxl.format.Alignment.CENTRE);
                cellHeaderFormat.setBackground(Colour.LIGHT_GREEN);

                //DATE: no bold
                DateFormat dataFormat = new DateFormat("dd-MM-yyyy HH:mm");
                WritableCellFormat cellDateFormat = new WritableCellFormat(dataFormat);
                
                cellDateFormat.setAlignment(jxl.format.Alignment.CENTRE);
                cellDateFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                cellDateFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, BLACK);
                cellDateFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                cellDateFormat.setAlignment(Alignment.CENTRE);

                makeExcelHeaders(page, cellHeaderFormat);

                for (int z = 0; z < Gui.model.getRowCount(); z++) {
                    jxl.write.Number jxlNumber = new jxl.write.Number(0, z + 1, Integer.parseInt(Gui.model.getValueAt(z, 0).toString()), cellNumberFormat); //num

                    Label source = new Label(1, z + 1, Gui.model.getValueAt(z, 1).toString(), cellSourceFormat); //Source
                    Label title = new Label(2, z + 1, Gui.model.getValueAt(z, 2).toString(), cellTitleFormat); //Title
                    Label date = new Label(3, z + 1, Gui.model.getValueAt(z, 3).toString(), cellDateFormat); //Date
                    Label link = new Label(4, z + 1, Gui.model.getValueAt(z, 4).toString(), hyperlinkCellFormat); //Link

                    WritableHyperlink hyperlink = new WritableHyperlink(4, z + 1, new URL(Gui.model.getValueAt(z, 4).toString()));

                    page.addHyperlink(hyperlink);
                    page.addCell(jxlNumber);
                    page.addCell(source);
                    page.addCell(title);
                    page.addCell(date);
                    page.addCell(link);
                    page.setRowView(z + 1, 600);
                }
               
                newExcel.write();
                newExcel.close();
                
                Common.console("status: export is done");
                
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

    private void makeExcelHeaders(WritableSheet page, WritableCellFormat cellHeaderFormat) throws WriteException{
        String headers[] = getHeaders();
        for (int col = 0; col < headers.length; col++) {
            Label cellName = new Label(col, 0, headers[col], cellHeaderFormat);
            page.addCell(cellName);
        }
    }
}
