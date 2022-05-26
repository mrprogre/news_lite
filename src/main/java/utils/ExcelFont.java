package utils;

import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.WritableFont;
import jxl.write.WriteException;

public class ExcelFont {
    
    private WritableFont writableFont;

    private ExcelFont(Builder builder) {
        writableFont = builder.writableFont;
    }

    public static class Builder {
        
        private WritableFont writableFont;

        public Builder() {
            this.writableFont = new WritableFont(WritableFont.ARIAL);
        }
        
        public WritableFont build() {
            return this.writableFont;
        }
        
        public Builder pointSize(int size) throws WriteException {
            this.writableFont.setPointSize(size);
            return this;
        }

        public Builder isItalic(boolean italic) throws WriteException{
            this.writableFont.setItalic(italic);
            return this;
        }

        public Builder bold() throws WriteException{
            this.writableFont.setBoldStyle(WritableFont.BOLD);
            return this;
        }

        public Builder noBold() throws WriteException{
            this.writableFont.setBoldStyle(WritableFont.NO_BOLD);
            return this;
        }

        public Builder underlineStyle(UnderlineStyle underlineStyle) throws WriteException {
            this.writableFont.setUnderlineStyle(underlineStyle);
            return this;
        }

        public Builder color(Colour color) throws WriteException {
            this.writableFont.setColour(color);
            return this;
        }
    }
}
