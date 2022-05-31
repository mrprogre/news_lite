package gui;
import javax.swing.*;
public class InitContext {
    private InitStrategy initStrategy;

    public InitContext(InitStrategy initStrategy){
        this.initStrategy = initStrategy;
    }

    public void excuteStrategy(JButton button){
        initStrategy.initButton(button);
    }
}
