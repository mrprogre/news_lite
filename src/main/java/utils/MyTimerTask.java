package utils;

import gui.Gui;
import search.Search;
import search.SearchMethod;

import java.util.TimerTask;

public class MyTimerTask extends TimerTask {
    @Override
    public void run() {
        SearchMethod search = new SearchMethod();
        if (Gui.autoUpdateNewsTop.getState()) {
            search.mainSearch("word");
        } else if (Gui.autoUpdateNewsBottom.getState()) {
            search.mainSearch("words");
        }
    }
}