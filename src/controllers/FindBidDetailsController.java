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
public class FindBidDetailsController extends Publisher implements ActionListener, ObserverOutputInterface {

    private ListenerLinkInterface inputPage;

    public FindBidDetailsController(ListenerLinkInterface inputPage) {
        super();
        this.inputPage = inputPage;
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
        ViewManagerService.loadPage(ViewManagerService.FIND_BID_DETAILS);
    }
}
