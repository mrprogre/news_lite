package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class ExportManager {

    protected static final Logger LOG = LoggerFactory.getLogger(ExportToExcel.class);

    private String[] headers = {"Number", "Source", "Title", "Date", "Link"};
    
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

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public void setExtensionFilter(FileNameExtensionFilter extensionFilter) {
        this.extensionFilter = extensionFilter;
    }
    
    public void setjFileChooser() {
        jFileChooser = new JFileChooser();
        jFileChooser.setFileFilter(extensionFilter);
        jFileChooser.setCurrentDirectory(new File
                (System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop"));
    }
}