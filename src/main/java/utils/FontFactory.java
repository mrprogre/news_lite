package utils;

import jxl.write.WritableFont;
import jxl.write.WriteException;


public interface FontFactory {

    WritableFont getNoBoldFont() throws WriteException;
    
    WritableFont getBoldFont() throws WriteException;
    
    WritableFont getHyperlinkFont() throws WriteException;
}
