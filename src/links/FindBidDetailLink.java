package links;

import services.ViewManagerService;
import controller.ListenerLinkInterface;
import controller.ObserverOutputInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FindBidDetailLink implements ActionListener, ObserverOutputInterface {

    private ListenerLinkInterface inputPage;
    private ObserverOutputInterface outputPage;
    private FindBidderDetailLink findBidderDetailLink;

    public FindBidDetailLink(ListenerLinkInterface inputPage, ObserverOutputInterface outputPage, FindBidderDetailLink findBidderDetailLink) {
        this.inputPage = inputPage;
        this.outputPage = outputPage;
        this.findBidderDetailLink = findBidderDetailLink;
    }

    @Override
    public void update(String data) {
        inputPage.addLinkListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton thisBtn = (JButton) e.getSource();
        String bidId = thisBtn.getName();
        outputPage.update(bidId);
        this.findBidderDetailLink.update(bidId);
        ViewManagerService.loadPage(ViewManagerService.FIND_BID_DETAILS);
    }
}
