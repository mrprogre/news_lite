package utils;

import jxl.write.WritableCellFormat;
import jxl.write.WriteException;


public interface FormatManager {
    
    WritableCellFormat hyperlinkCellFormat() throws WriteException;

    WritableCellFormat titleCellFormat() throws WriteException;

    WritableCellFormat numberCellFormat() throws WriteException;

    WritableCellFormat sourceCellFormat() throws WriteException;

    WritableCellFormat headerCellFormat() throws WriteException;

    WritableCellFormat dateCellFormat() throws WriteException;
}
