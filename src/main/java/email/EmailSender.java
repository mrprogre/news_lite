package email;

import gui.Gui;
import lombok.extern.slf4j.Slf4j;
import main.Main;
import search.ConsoleSearch;
import search.Search;
import utils.Common;

import javax.mail.MessagingException;
import java.io.IOException;

@Slf4j
public class EmailSender {
    public static String from;
    public static String from_pwd;
    private final String subject = ("News (" + new Search().today + ")");

    // Отправка письма
    public void sendMessage(){
        Common.getEmailSettingsFromFile();

        if (!Main.IS_CONSOLE_SEARCH.get()) {
            String to = Gui.sendEmailTo.getText();
            // Отправка из GUI
            StringBuilder text = new StringBuilder();
            for (String s : Search.dataForEmail) {
                text.append(s).append("\n\n");
            }
            try {
                // чтобы не было задвоений в настройках - удаляем старую почту и записываем новую при отправке
                try {
                    Common.delSettings("email=");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Common.writeToConfig(to, "email");

                //отправка
                new Sender().send(subject, text.toString(), from, from_pwd, to);
                Common.console("status: e-mail sent successfully");
                log.info("Email has been sent");
                Gui.sendEmailBtn.setIcon(Gui.WHEN_SENT_ICON);
                Common.IS_SENDING.set(true);
                Search.isSearchFinished.set(true);
                Gui.progressBar.setValue(100);
            } catch (Exception mex) {
                mex.printStackTrace();
                Common.console("status: e-mail wasn't send: " + mex.getMessage() + "\n" + mex.getCause());
                Common.IS_SENDING.set(true);
                Search.isSearchFinished.set(true);
                Gui.progressBar.setValue(100);
                log.warn("E-mail wasn't send");
            }
        } else {
            // Отправка из консоли
            StringBuilder text = new StringBuilder();
            for (String s : ConsoleSearch.dataForEmail) {
                text.append(s).append("\n\n");
            }
            try {
                Sender sender = new Sender();
                sender.send(subject, text.toString(), from, from_pwd, Main.emailToFromConsole);
            } catch (MessagingException me) {
                log.warn("E-mail wasn't send");
                Common.console("status: e-mail wasn't send: " + me.getMessage());
            }
            log.info("Email has been sent");
        }
    }
}

