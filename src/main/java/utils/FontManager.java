package utils;

import jxl.write.WriteException;

import java.util.Optional;

public interface FontManager {

    Object getBaseFont();
    Object getBoldFont() throws WriteException;
}
