package listeners;

import controller.Controller;
import services.ViewManagerService;
import controller.ListenerLinkInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/***
 * Listener for find bid button in dashboard
 */
public class BidUpdateListener implements ActionListener {

    ListenerLinkInterface inputPage;
    Controller controller;

    public BidUpdateListener(ListenerLinkInterface inputPage, Controller controller) {
        this.inputPage = inputPage;
        this.controller = controller;
        inputPage.addLinkListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        controller.notifySubscribers(null);

        JButton thisBtn = (JButton) e.getSource();
        if (thisBtn.getText().equals("See Your Bids")){
            ViewManagerService.loadPage(ViewManagerService.SEE_ALL_BIDS);
        } else if (thisBtn.getText().equals("Find Bids")){
            ViewManagerService.loadPage(ViewManagerService.FIND_ALL_BIDS);
        }


    }
}
