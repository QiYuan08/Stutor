package unused;

import abstractions.Publisher;
import services.ViewManagerService;
import abstractions.ListenerLinkInterface;
import abstractions.ObserverOutputInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Links SeeAllBids to SeeBidDetails (student looks into their own bid)
 * Updates SeeBidDetails and the pages that lead from it with the bidId of the bid that the user pressed
 */
public class SeeBidDetailsController extends Publisher implements ActionListener, ObserverOutputInterface {

    private ListenerLinkInterface inputPage;

    public SeeBidDetailsController(ListenerLinkInterface inputPage) {
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
        ViewManagerService.loadPage(ViewManagerService.SEE_BID_DETAILS);
    }
}
