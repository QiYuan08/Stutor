package controller;

import java.awt.event.ActionListener;

public interface ObserverInputInterface {

    String[] retrieveInputs();
    void addActionListener(ActionListener actionListener);

}
