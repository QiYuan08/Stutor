package controller;

import application.FindBidPage;
import application.Application;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FindBidListener implements ObserverOutputInterface, ActionListener {

    private FindBidPage inputPage;
    private ApplicationController applicationController;

    public FindBidListener(FindBidPage inputPage, ApplicationController applicationController) {
        this.inputPage = inputPage;
        this.applicationController = applicationController;
//        inputPage.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton thisBtn = (JButton) e.getSource();
        String bidId = thisBtn.getName();
        System.out.println(bidId);
        applicationController.notifySubscribers(bidId);
        Application.loadPage(Application.VIEW_BID);
    }

    // this method is called whenever there is an update in bids to display to tutor
    // to add listener for all the list of bids;
    @Override
    public void update(String data) {
        inputPage.addActionListener(this);
    }
}
