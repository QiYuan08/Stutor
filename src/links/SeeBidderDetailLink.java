package links;

import services.ViewManagerService;
import views.student_bids.SeeBidDetails;
import controller.ObserverOutputInterface;

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
        ViewManagerService.loadPage(ViewManagerService.SEE_TUTOR_RESPONSE);
    }

    @Override
    public void update(String data) {
        inputPage.addViewBidListener(this);
    }
}
