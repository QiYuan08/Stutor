package controllers;

import abstractions.Publisher;
import abstractions.ObserverOutputInterface;
import services.ViewManagerService;
import abstractions.ListenerLinkInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/***
 * Notifies its subscribers to update themselves with the latest information before they are shown on the application
 */
public class BidUpdateController extends Publisher implements ActionListener, ObserverOutputInterface {

    private ListenerLinkInterface inputPage;
    private String userId;

    public BidUpdateController(ListenerLinkInterface inputPage) {
        super();
        this.inputPage = inputPage;
        inputPage.addLinkListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        notifySubscribers(this.userId);

        JButton thisBtn = (JButton) e.getSource();
        if (thisBtn.getText().equals("See Your Bids")){
            ViewManagerService.loadPage(ViewManagerService.SEE_ALL_BIDS);
        } else if (thisBtn.getText().equals("Find Bids")){
            ViewManagerService.loadPage(ViewManagerService.FIND_ALL_BIDS);
        } else if (thisBtn.getText().equals("View Contracts")){
            ViewManagerService.loadPage(ViewManagerService.VIEW_CONTRACT_PAGE);
        } else if (thisBtn.getText().equals("Monitored Bids")) {
            ViewManagerService.loadPage(ViewManagerService.MONITORED_BIDS);
        }

    }

    /**
     *
     * @param data userId from loginController
     */
    @Override
    public void update(String data) {
        this.userId = data;
        notifySubscribers(data);
    }
}
