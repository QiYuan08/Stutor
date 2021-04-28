package listeners;

import application.Application;
import application.ApplicationManager;
import application.DashboardPage;
import controller.ApplicationController;

import javax.swing.*;
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
        inputPage.addFindBidListener(this); // add listener for find bid button
        inputPage.addSeeBidListener(this);  // add listener for see bid button
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        applicationController.notifySubscribers(inputPage.getUserId());

        JButton thisBtn = (JButton) e.getSource();
        if (thisBtn.getText() == "See Your Bids"){
            ApplicationManager.loadPage(ApplicationManager.USER_BIDS);
        } else if (thisBtn.getText() == "Find Bids"){
            ApplicationManager.loadPage(ApplicationManager.FIND_BID);
        }


    }
}
