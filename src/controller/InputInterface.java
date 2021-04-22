package controller;

import java.awt.event.ActionListener;

public interface InputInterface {

    public String[] retrieveInputs();
    void addActionListener(ActionListener actionListener);

}
