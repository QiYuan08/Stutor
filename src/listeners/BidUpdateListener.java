package listeners;

import controller.Listener;
import controller.ObserverOutputInterface;
import services.ViewManagerService;
import controller.ListenerLinkInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/***
 * Listener for find bid button in dashboard
 */
public class BidUpdateListener extends Listener implements ActionListener, ObserverOutputInterface {

    ListenerLinkInterface inputPage;

    public BidUpdateListener(ListenerLinkInterface inputPage) {
        super();
        this.inputPage = inputPage;
        inputPage.addLinkListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        notifySubscribers(null);

        JButton thisBtn = (JButton) e.getSource();
        if (thisBtn.getText().equals("See Your Bids")){
            ViewManagerService.loadPage(ViewManagerService.SEE_ALL_BIDS);
        } else if (thisBtn.getText().equals("Find Bids")){
            ViewManagerService.loadPage(ViewManagerService.FIND_ALL_BIDS);
        }

    }

    @Override
    public void update(String data) {
        notifySubscribers(data);
    }
}
