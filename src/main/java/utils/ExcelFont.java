package utils;

import jxl.write.WritableFont;
import jxl.write.WriteException;

import static jxl.format.Colour.BLACK;
import static jxl.format.UnderlineStyle.NO_UNDERLINE;
import static jxl.write.WritableFont.BOLD;
import static jxl.write.WritableFont.NO_BOLD;

public class ExcelFont implements FontManager {

    private WritableFont baseFont;

    public ExcelFont() throws WriteException {
        baseFont = new WritableFont(WritableFont.ARIAL);
        baseFont.setPointSize(11);
        baseFont.setItalic(false);
        baseFont.setBoldStyle(NO_BOLD);
        baseFont.setUnderlineStyle(NO_UNDERLINE);
        baseFont.setColour(BLACK);
    }

    @Override
    public WritableFont getBaseFont() {
        return this.baseFont;
    }

    @Override
    public WritableFont getBoldFont() throws WriteException {
        WritableFont boldFont = baseFont;
        boldFont.setBoldStyle(BOLD);
        return boldFont;
    }
    
}
