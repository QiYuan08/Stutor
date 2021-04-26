package controller;

import application.Application;
import application.DashboardPage;
import application.ProfilePage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashBoardListener implements ActionListener {

    DashboardPage inputPage;
    ApplicationController applicationController;

    public DashBoardListener(DashboardPage inputPage, ApplicationController applicationController) {
        this.inputPage = inputPage;
        this.applicationController = applicationController;
        inputPage.addFindBidListener(this);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        applicationController.notifySubscribers(ProfilePage.userId);
        Application.loadPage(Application.ALL_BID);
    }
}
