package links;

import application.ApplicationManager;
import application.bid_pages.FindBidsDetail;
import controller.ObserverInputInterface;
import controller.ObserverOutputInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FindBidDetailLink implements ActionListener, ObserverOutputInterface {

    private ObserverInputInterface inputPage;
    private ObserverOutputInterface outputPage;
    private FindBidderDetailLink findBidderDetailLink;

    public FindBidDetailLink(ObserverInputInterface inputPage, ObserverOutputInterface outputPage, FindBidderDetailLink findBidderDetailLink) {
        this.inputPage = inputPage;
        this.outputPage = outputPage;
        this.findBidderDetailLink = findBidderDetailLink;
    }

    @Override
    public void update(String data) {
        inputPage.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("1 Hi from find bid detail link");
        JButton thisBtn = (JButton) e.getSource();
        String bidId = thisBtn.getName();
        outputPage.update(bidId);
        this.findBidderDetailLink.update(bidId);
        ApplicationManager.loadPage(ApplicationManager.FIND_BID_DETAIL);

    }

}
