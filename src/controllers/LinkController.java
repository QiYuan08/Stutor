package controllers;

import abstractions.Publisher;
import services.ViewManagerService;
import abstractions.ListenerLinkInterface;
import abstractions.ObserverOutputInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Links FindAllBids to FindBidDetails (tutor looks into a student bid)
 * Updates FindBidDetails and the pages that lead from it with the bidId of the bid that the user pressed
 */
public class LinkController extends Publisher implements ActionListener, ObserverOutputInterface {

    private ListenerLinkInterface inputPage;
    private String linkedPage;

    public LinkController(ListenerLinkInterface inputPage, String linkedPage) {
        super();
        this.inputPage = inputPage;
        this.linkedPage = linkedPage;
    }

    @Override
    public void update(String data) {
        inputPage.addLinkListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton thisBtn = (JButton) e.getSource();
        String bidId = thisBtn.getName();
        notifySubscribers(bidId);
        ViewManagerService.loadPage(linkedPage);
    }
}