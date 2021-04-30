package links;

import application.ApplicationManager;
import controller.ListenerLinkInterface;
import controller.ObserverOutputInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SeeBidDetailLink implements ActionListener, ObserverOutputInterface {

    private ListenerLinkInterface inputPage;
    private ObserverOutputInterface outputPage;
    private SeeBidderDetailLink seeBidderDetailLink;

    public SeeBidDetailLink(ListenerLinkInterface inputPage, ObserverOutputInterface outputPage, SeeBidderDetailLink seeBidderDetailLink) {
        this.inputPage = inputPage;
        this.outputPage = outputPage;
        this.seeBidderDetailLink = seeBidderDetailLink;
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
        seeBidderDetailLink.update(bidId);
        ApplicationManager.loadPage(ApplicationManager.SEE_BID_DETAIL);
    }
}
