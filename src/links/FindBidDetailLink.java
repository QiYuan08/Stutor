package links;

import controller.Listener;
import services.ViewManagerService;
import controller.ListenerLinkInterface;
import controller.ObserverOutputInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class FindBidDetailLink extends Listener implements ObserverOutputInterface {

    private ListenerLinkInterface inputPage;
    private ObserverOutputInterface outputPage;
    private FindBidderDetailLink findBidderDetailLink;

    public FindBidDetailLink(ListenerLinkInterface inputPage) {
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
