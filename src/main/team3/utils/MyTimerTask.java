package team3.utils;

import team3.search.Search;
import team3.gui.Gui;

import java.util.TimerTask;

public class MyTimerTask extends TimerTask {
    @Override
    public void run() {
        Search search = new Search();
        if (Gui.autoUpdateNewsTop.getState()) {
            search.mainSearch("word");
        } else if (Gui.autoUpdateNewsBottom.getState()) {
            search.mainSearch("words");
        }
    }
}