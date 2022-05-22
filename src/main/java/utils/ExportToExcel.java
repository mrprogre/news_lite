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

public class ExportToExcel {
    private static final Logger log = LoggerFactory.getLogger(ExportToExcel.class);

    public void exportResultsToExcel() {
        try {
            //Save file to
            FileNameExtensionFilter filter = new FileNameExtensionFilter("*.xls", "*.xls", "*.XLS", "*.*");
            JFileChooser save_to = new JFileChooser();
            save_to.setFileFilter(filter);
            save_to.setCurrentDirectory(new File
                    (System.getProperty("user.home") + System.getProperty("file.separator")+ "Desktop"));
            int ret = save_to.showDialog(null,"Save");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = new File(save_to.getSelectedFile() + ".xls");

                WritableWorkbook new_excel = Workbook.createWorkbook(file);
                WritableSheet page = new_excel.createSheet("001", 0);
                page.getSettings().setShowGridLines(true);
                page.setColumnView(0, 10);
                page.setColumnView(1, 16);
                page.setColumnView(2, 100);
                page.setColumnView(3, 30);
                page.setColumnView(4, 120);
                page.setRowView(0, 600);

                //no bold
                WritableFont wf = new WritableFont(WritableFont.ARIAL, 11,
                        WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE,
                        jxl.format.Colour.BLACK);

                //bold
                WritableFont wf_bold = new WritableFont(WritableFont.ARIAL, 11,
                        WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
                        jxl.format.Colour.BLACK);

                //Hyperlinks
                WritableFont wf_link = new WritableFont(WritableFont.ARIAL, 11, WritableFont.NO_BOLD);
                wf_link.setColour(Colour.DARK_GREEN);
                WritableCellFormat wcf_link = new WritableCellFormat(wf_link);
                wcf_link.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
                wcf_link.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                wcf_link.setWrap(true);

                //WritableCellFormat wcf_no_border = new WritableCellFormat(wf);

                WritableCellFormat wcf = new WritableCellFormat(wf);
                wcf.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
                wcf.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                wcf.setWrap(true);

                WritableCellFormat wcf_centre_no_bold = new WritableCellFormat(wf);
                wcf_centre_no_bold.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
                wcf_centre_no_bold.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                wcf_centre_no_bold.setAlignment(Alignment.CENTRE);

                //no bold
                WritableCellFormat wcf_centre = new WritableCellFormat(wf);
                wcf_centre.setAlignment(jxl.format.Alignment.CENTRE);
                wcf_centre.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                wcf_centre.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
                wcf_centre.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);

                //HEADERS: color, bold
                WritableCellFormat wcf_centre_bold = new WritableCellFormat(wf_bold);
                wcf_centre_bold.setAlignment(jxl.format.Alignment.CENTRE);
                wcf_centre_bold.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
                wcf_centre_bold.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                wcf_centre_bold.setBackground(Colour.LIGHT_GREEN);

                //DATE: no bold
                DateFormat dateFormat = new DateFormat("dd-MM-yyyy HH:mm") ;
                WritableCellFormat wcf_date = new WritableCellFormat(dateFormat);
                wcf_date.setAlignment(jxl.format.Alignment.CENTRE);
                wcf_date.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                wcf_date.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
                wcf_date.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                wcf_date.setAlignment(Alignment.CENTRE);

                String[] headers = {"Number", "Source", "Title", "Date", "Link"};
                for (int s = 0; s < headers.length; s++) {
                    Label x = new Label(s, 0, headers[s], wcf_centre_bold);
                    page.addCell(x);
                }

                for (int z = 0; z < Gui.model.getRowCount(); z++) {
                    jxl.write.Number y1 = new jxl.write.Number(0, z + 1, Integer.parseInt(Gui.model.getValueAt(z, 0).toString()), wcf_centre_no_bold); //num
                    Label y2 = new Label(1, z + 1, Gui.model.getValueAt(z, 1).toString(), wcf_centre_no_bold); //Source
                    Label y3 = new Label(2, z + 1, Gui.model.getValueAt(z, 2).toString(), wcf); //Title
                    Label y4 = new Label(3, z + 1, Gui.model.getValueAt(z, 3).toString(), wcf_date); //Date
                    //Link
                    Label y5 = new Label(4, z + 1, Gui.model.getValueAt(z, 4).toString(), wcf_link);
                    WritableHyperlink hl = new WritableHyperlink(4, z + 1, new URL(Gui.model.getValueAt(z, 4).toString()));
                    page.addHyperlink(hl);
                    page.addCell(y1);
                    page.addCell(y2);
                    page.addCell(y3);
                    page.addCell(y4);
                    page.addCell(y5);
                    page.setRowView(z + 1, 600);
                }
                new_excel.write();
                new_excel.close();
                Common.console("status: export is done");
            } else Common.console("status: export canceled");
        } catch (WriteException | IOException e) {
            Common.console("status: export exception");
            log.warn("export exception");
        }
    }
}
