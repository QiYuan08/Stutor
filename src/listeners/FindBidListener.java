package listeners;

import controller.ListenerLinkInterface;
import services.ViewManagerService;
import views.tutor_responds.FindBidDetails;
import controller.ObserverOutputInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
