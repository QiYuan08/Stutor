package listeners;

import application.ApplicationManager;
import application.bid_pages.SeeBidsPage;
import controller.ApplicationController;
import controller.ObserverOutputInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SeeBidListener implements ActionListener, ObserverOutputInterface {

    private SeeBidsPage inputPage;
    private ApplicationController controller;

    public SeeBidListener(SeeBidsPage inputPage, ApplicationController controller) {
        this.inputPage = inputPage;
        this.controller = controller;
    }


    // when the view detail button in SeeBidsPage are clicked
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton thisBtn = (JButton) e.getSource();
        String bidId = thisBtn.getName();
        System.out.println(bidId);
        controller.notifySubscribers(bidId);
        ApplicationManager.loadPage(ApplicationManager.VIEW_BID);
    }


    /**
     * Method to add action listener to every view detail button in SeeBidsPage
     * @param data None
     */
    @Override
    public void update(String data) {
        this.inputPage.addActionListener(this);
    }
}
