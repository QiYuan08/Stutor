package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewBidListener implements ActionListener {

    private ObserverInputInterface inputPage;
    private ApplicationController applicationController;

    public ViewBidListener(ObserverInputInterface inputPage, ApplicationController applicationController) {
        this.inputPage = inputPage;
        this.applicationController = applicationController;
        inputPage.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
