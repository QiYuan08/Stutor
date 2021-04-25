package controller;

import application.AllBidPage;
import application.Application;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AllBidListener implements ActionListener {

    private AllBidPage inputPage;
    private ApplicationController applicationController;

    public AllBidListener(AllBidPage inputPage, ApplicationController applicationController) {
        this.inputPage = inputPage;
        this.applicationController = applicationController;
        inputPage.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton thisBtn = (JButton) e.getSource();
        String bidId = thisBtn.getName();
        applicationController.notifySubscribers(bidId);
        System.out.println("Hi from AllBidListener");
        Application.loadPage(Application.VIEW_BID);
    }
}
