package links;

import application.ApplicationManager;
import application.bid_pages.FindBidsDetail;
import controller.ObserverOutputInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FindBidderDetailLink implements ActionListener, ObserverOutputInterface {

    private FindBidsDetail inputPage;
    private ObserverOutputInterface outputPage;

    public FindBidderDetailLink(FindBidsDetail inputPage, ObserverOutputInterface outputPage) {
        this.inputPage = inputPage;
        this.outputPage = outputPage;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Find Bidder Detail Link");
        JButton thisBtn = (JButton) e.getSource();
        String bidId = thisBtn.getName();
        outputPage.update(bidId);
        ApplicationManager.loadPage(ApplicationManager.FIND_TUTOR_BID_DETAIL);
    }

    @Override
    public void update(String data) {
        System.out.println("Find Bidder Detail Link");
        inputPage.addViewBidListener(this);
    }

}
