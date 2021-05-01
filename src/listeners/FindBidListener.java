package listeners;

import services.ViewManagerService;
import views.tutor_responds.FindBidDetails;
import controller.ObserverOutputInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FindBidListener implements ObserverOutputInterface, ActionListener {

    private FindBidDetails inputPage;

    public FindBidListener(FindBidDetails inputPage) {
        this.inputPage = inputPage;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton thisBtn = (JButton) e.getSource();
        String bidId = thisBtn.getName();
        ViewManagerService.loadPage(ViewManagerService.FIND_TUTOR_RESPONSE);
    }

    // this method is called whenever there is an update in bids to display to tutor
    // to add listener for all the list of bids;
    @Override
    public void update(String data) {
//        inputPage.addViewBidListener(this);
        inputPage.addLinkListener(this);
    }
}
