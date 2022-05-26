package utils;

import jxl.write.WritableFont;
import jxl.write.WriteException;

import static jxl.format.Colour.BLACK;
import static jxl.format.Colour.DARK_GREEN;
import static jxl.format.UnderlineStyle.NO_UNDERLINE;

public class ExcelFontFactory implements FontFactory{

    private WritableFont noBoldFont = null;
    private WritableFont boldFont = null;
    private WritableFont hyperlinkFont = null;

    public ExcelFontFactory() {
    }

    @Override
    public WritableFont getNoBoldFont() throws WriteException {
        if (noBoldFont == null) {
            noBoldFont = new ExcelFont
                    .Builder()
                    .pointSize(11)
                    .noBold()
                    .isItalic(false)
                    .underlineStyle(NO_UNDERLINE)
                    .color(BLACK)
                    .build();
        }
        return noBoldFont;
    }
    
    @Override
    public WritableFont getBoldFont() throws WriteException {
        if (boldFont == null) {
            boldFont = new ExcelFont
                    .Builder()
                    .pointSize(11)
                    .bold()
                    .isItalic(false)
                    .underlineStyle(NO_UNDERLINE)
                    .color(BLACK)
                    .build();
        }
        return boldFont;
    }
    
    @Override
    public WritableFont getHyperlinkFont() throws WriteException {
        if (hyperlinkFont == null) {
            hyperlinkFont = new ExcelFont
                    .Builder()
                    .pointSize(11)
                    .noBold()
                    .color(DARK_GREEN)
                    .build();
        }
        return hyperlinkFont;
    }
}
