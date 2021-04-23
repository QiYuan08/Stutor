package controller;

import javax.swing.*;
import java.awt.event.ActionListener;

public abstract class EventListener implements ActionListener {

    private ObserverInputInterface inputPage;

    public EventListener(ObserverInputInterface inputPage) {this.inputPage = inputPage;}
}
