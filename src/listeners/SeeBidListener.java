package listeners;

import services.ViewManagerService;
import views.student_bids.SeeBidDetails;
import controller.ObserverOutputInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SeeBidListener implements ActionListener, ObserverOutputInterface {

    private SeeBidDetails inputPage;

    public SeeBidListener(SeeBidDetails inputPage) {
        this.inputPage = inputPage;
    }

    // when the view detail button in SeeAllBids are clicked
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton thisBtn = (JButton) e.getSource();
        String bidId = thisBtn.getName();
        ViewManagerService.loadPage(ViewManagerService.SEE_TUTOR_BID_DETAIL);
    }


    /**
     * Method to add action listener to every view detail button in SeeAllBids
     * @param data None
     */
    @Override
    public void update(String data) {
        this.inputPage.addViewBidListener(this);
    }
}
