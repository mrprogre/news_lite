package utils;

import gui.Gui;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import static javax.swing.JFileChooser.*;


public class ExportToExcel implements ExportManager{
    
    private static final Logger LOG = LoggerFactory.getLogger(ExportToExcel.class);

    private final String extension = ".xls";

    private FileNameExtensionFilter extensionFilter;
    private File baseExcelFile;
    private JFileChooser jFileChooser;
    

    public ExportToExcel() {
        this.extensionFilter = new FileNameExtensionFilter("*.xls", "*.xls", "*.XLS", "*.*");
        this.jFileChooser = setJFileChooser();
        this.baseExcelFile = new File(jFileChooser.getSelectedFile() + extension);
    }
    

    @Override
    public JFileChooser setJFileChooser() {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileFilter(extensionFilter);
        jFileChooser.setCurrentDirectory(new File
                (System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop"));
        return jFileChooser;
    }


    public void exportResultsToExcel() {
        try {
//            JFileChooser jFileChooser = setJFileChooser();

            if (jFileChooser.showDialog(null,"Save") == APPROVE_OPTION) {
//                File file = new File(jFileChooser.getSelectedFile() + ".xls");

                WritableWorkbook newExcel = Workbook.createWorkbook(baseExcelFile);
                WritableSheet page = newExcel.createSheet("001", 0);
                
                page.getSettings().setShowGridLines(true);
                page.setColumnView(0, 10);
                page.setColumnView(1, 16);
                page.setColumnView(2, 100);
                page.setColumnView(3, 30);
                page.setColumnView(4, 120);
                page.setRowView(0, 600);

                //no bold
                WritableFont writableFontNoBold = new WritableFont(WritableFont.ARIAL, 11,
                        WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE,
                        jxl.format.Colour.BLACK);

                //bold
                WritableFont writableFontBold = new WritableFont(WritableFont.ARIAL, 11,
                        WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
                        jxl.format.Colour.BLACK);

                //Hyperlinks
                WritableFont hyperlinkFont = new WritableFont(WritableFont.ARIAL, 11, WritableFont.NO_BOLD);
                hyperlinkFont.setColour(Colour.DARK_GREEN);
                WritableCellFormat hyperlinkCellFormat = new WritableCellFormat(hyperlinkFont);
                hyperlinkCellFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
                hyperlinkCellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                hyperlinkCellFormat.setWrap(true);

                //WritableCellFormat wcf_no_border = new WritableCellFormat(wf);

                WritableCellFormat writableCellFormat = new WritableCellFormat(writableFontNoBold);
                writableCellFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
                writableCellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                writableCellFormat.setWrap(true);

                WritableCellFormat wcfCentreNoBold = new WritableCellFormat(writableFontNoBold);
                wcfCentreNoBold.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
                wcfCentreNoBold.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                wcfCentreNoBold.setAlignment(Alignment.CENTRE);

                //no bold
                WritableCellFormat wcf_centre = new WritableCellFormat(writableFontNoBold);
                wcf_centre.setAlignment(jxl.format.Alignment.CENTRE);
                wcf_centre.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                wcf_centre.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
                wcf_centre.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);

                //HEADERS: color, bold
                WritableCellFormat wcf_centre_bold = new WritableCellFormat(writableFontBold);
                wcf_centre_bold.setAlignment(jxl.format.Alignment.CENTRE);
                wcf_centre_bold.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
                wcf_centre_bold.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                wcf_centre_bold.setBackground(Colour.LIGHT_GREEN);

                //DATE: no bold
                DateFormat dateFormat = new DateFormat("dd-MM-yyyy HH:mm") ;
                WritableCellFormat wcfDate = new WritableCellFormat(dateFormat);
                wcfDate.setAlignment(jxl.format.Alignment.CENTRE);
                wcfDate.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                wcfDate.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
                wcfDate.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                wcfDate.setAlignment(Alignment.CENTRE);

                String[] headers = {"Number", "Source", "Title", "Date", "Link"};
                for (int col = 0; col < headers.length; col++) {
                    Label cellName = new Label(col, 0, headers[col], wcf_centre_bold);
                    page.addCell(cellName);
                }

                for (int z = 0; z < Gui.model.getRowCount(); z++) {
                    jxl.write.Number jxlNumber = new jxl.write.Number(0, z + 1, Integer.parseInt(Gui.model.getValueAt(z, 0).toString()), wcfCentreNoBold); //num
                    
                    Label source = new Label(1, z + 1, Gui.model.getValueAt(z, 1).toString(), wcfCentreNoBold); //Source
                    Label title = new Label(2, z + 1, Gui.model.getValueAt(z, 2).toString(), writableCellFormat); //Title
                    Label date = new Label(3, z + 1, Gui.model.getValueAt(z, 3).toString(), wcfDate); //Date
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
}
