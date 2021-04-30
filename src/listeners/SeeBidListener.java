package listeners;

import application.ApplicationManager;
import application.bid_pages.SeeBidDetail;
import application.bid_pages.SeeBidsPage;
import controller.ApplicationController;
import controller.ObserverOutputInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SeeBidListener implements ActionListener, ObserverOutputInterface {

    private SeeBidDetail inputPage;

    public SeeBidListener(SeeBidDetail inputPage) {
        this.inputPage = inputPage;
    }

    // when the view detail button in SeeBidsPage are clicked
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton thisBtn = (JButton) e.getSource();
        String bidId = thisBtn.getName();
        ApplicationManager.loadPage(ApplicationManager.SEE_TUTOR_BID_DETAIL);
    }


    /**
     * Method to add action listener to every view detail button in SeeBidsPage
     * @param data None
     */
    @Override
    public void update(String data) {
        this.inputPage.addViewBidListener(this);
    }
}
