package links;

import application.ApplicationManager;
import application.bid_pages.FindBidsDetail;
import application.bid_pages.SeeBidDetail;
import application.bid_pages.SeeTutorBidDetail;
import controller.ObserverOutputInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SeeBidderDetailLink implements ActionListener, ObserverOutputInterface {

    private SeeBidDetail inputPage;
    private ObserverOutputInterface outputPage;

    public SeeBidderDetailLink(SeeBidDetail inputPage, ObserverOutputInterface outputPage) {
        this.inputPage = inputPage;
        this.outputPage = outputPage;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton thisBtn = (JButton) e.getSource();
        String bidId = thisBtn.getName();
        outputPage.update(bidId);
        ApplicationManager.loadPage(ApplicationManager.SEE_TUTOR_BID_DETAIL);
    }

    @Override
    public void update(String data) {
        System.out.println("seebidderdetaillink update method");
        inputPage.addViewBidListener(this);
    }
}
