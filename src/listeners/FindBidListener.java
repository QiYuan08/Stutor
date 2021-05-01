package listeners;

import interfaces.ListenerLinkInterface;
import services.ViewManagerService;
import interfaces.ObserverOutputInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Adds an itself to the FindBidDetails page for all of the buttons in the dynamic list of student bids.
 */
public class FindBidListener implements ObserverOutputInterface, ActionListener {

    private ListenerLinkInterface inputPage;

    public FindBidListener(ListenerLinkInterface inputPage) {
        this.inputPage = inputPage;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ViewManagerService.loadPage(ViewManagerService.FIND_TUTOR_RESPONSE);
    }

    // this method is called whenever there is an update in bids to display to tutor
    // to add listener for all the list of bids;
    @Override
    public void update(String data) {
        inputPage.addLinkListener(this);
    }
}
