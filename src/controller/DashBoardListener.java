package controller;

import application.Application;
import application.DashboardPage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/***
 * Listener for find bid button in dashboard
 */
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
        applicationController.notifySubscribers(inputPage.getUserId());
        Application.loadPage(Application.FIND_BID);
    }
}
