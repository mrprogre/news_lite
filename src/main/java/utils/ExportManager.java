package utils;

import jxl.write.WriteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public abstract class ExportManager {

    protected static final Logger LOG = LoggerFactory.getLogger(ExportToExcel.class);

    private final String[] headers = {"Number", "Source", "Title", "Date", "Link"};
    
    private String extension;
    private FileNameExtensionFilter extensionFilter;
    private JFileChooser jFileChooser;
    
    public String getExtension() {
        return extension;
    }
    
    public String[] getHeaders() {
        return headers;
    }

    public JFileChooser getjFileChooser() {
        return jFileChooser;
    }

    protected void setExtension(String extension) {
        this.extension = extension;
    }

    protected void setExtensionFilter(FileNameExtensionFilter extensionFilter) {
        this.extensionFilter = extensionFilter;
    }

    protected void setjFileChooser() {
        jFileChooser = new JFileChooser();
        jFileChooser.setFileFilter(extensionFilter);
        jFileChooser.setCurrentDirectory(new File
                (System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop"));
    }
}
