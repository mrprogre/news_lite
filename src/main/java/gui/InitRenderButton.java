package gui;
import javax.swing.*;
public class InitRenderButton implements InitStrategy {
    //JTable table;
    public void initButton(JButton button){
        //button = new JButton();
        //button.setForeground(table.getForeground());
        button.setBackground(UIManager.getColor("Button.background"));
        button.setIcon(Gui.DELETE_ICON);
    }
}
