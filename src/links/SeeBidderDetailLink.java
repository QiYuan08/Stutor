package links;

import application.ApplicationManager;
import application.student_bids.SeeBidDetails;
import listeners.ObserverOutputInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SeeBidderDetailLink implements ActionListener, ObserverOutputInterface {

    private SeeBidDetails inputPage;
    private ObserverOutputInterface outputPage;

    public SeeBidderDetailLink(SeeBidDetails inputPage, ObserverOutputInterface outputPage) {
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
