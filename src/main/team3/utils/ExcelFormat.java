package team3.utils;

import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.write.DateFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WriteException;

import static jxl.format.Colour.BLACK;

public class ExcelFormat implements FormatManager{

    private ExcelFontFactory fontFactory;

    public ExcelFormat() {
        this.fontFactory = new ExcelFontFactory();
    }
    
    @Override
    public WritableCellFormat hyperlinkCellFormat() throws WriteException {
        WritableCellFormat hyperlinkCellFormat = new WritableCellFormat(fontFactory.getHyperlinkFont());
        hyperlinkCellFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, BLACK);
        hyperlinkCellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        hyperlinkCellFormat.setWrap(true);
        return hyperlinkCellFormat;
    }

    @Override
    public WritableCellFormat titleCellFormat() throws WriteException {
        WritableCellFormat cellTitleFormat = new WritableCellFormat(fontFactory.getNoBoldFont());
        cellTitleFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, BLACK);
        cellTitleFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        cellTitleFormat.setWrap(true);
        return cellTitleFormat;
    }

    @Override
    public WritableCellFormat numberCellFormat() throws WriteException {
        WritableCellFormat cellNumberFormat = new WritableCellFormat(fontFactory.getNoBoldFont());
        setBorderAndVerticalAndAlign(cellNumberFormat);
        return cellNumberFormat;
    }

    @Override
    public WritableCellFormat sourceCellFormat() throws WriteException {
        WritableCellFormat cellSourceFormat = new WritableCellFormat(fontFactory.getNoBoldFont());
        setBorderAndVerticalAndAlign(cellSourceFormat);
        return cellSourceFormat;
    }

    @Override
    public WritableCellFormat headerCellFormat() throws WriteException {
        WritableCellFormat cellHeaderFormat = new WritableCellFormat(fontFactory.getBoldFont());
        setBorderAndVerticalAndAlign(cellHeaderFormat);
        cellHeaderFormat.setBackground(Colour.LIGHT_GREEN);
        return cellHeaderFormat;
    }

    @Override
    public WritableCellFormat dateCellFormat() throws WriteException {
        DateFormat dataFormat = new DateFormat("dd-MM-yyyy HH:mm");
        WritableCellFormat cellDateFormat = new WritableCellFormat(dataFormat);
        setBorderAndVerticalAndAlign(cellDateFormat);
        return cellDateFormat;
    }

    private void setBorderAndVerticalAndAlign(WritableCellFormat cellFormat) throws WriteException{
        cellFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, BLACK);
        cellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        cellFormat.setAlignment(Alignment.CENTRE);
    }
}
