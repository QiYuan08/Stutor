package links;

import application.ApplicationManager;
import controller.ObserverInputInterface;
import controller.ObserverOutputInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FindBidDetailLink implements ActionListener, ObserverOutputInterface {

    private ObserverInputInterface inputPage;
    private ObserverOutputInterface outputPage;

    public FindBidDetailLink(ObserverInputInterface inputPage, ObserverOutputInterface outputPage) {
        this.inputPage = inputPage;
        this.outputPage = outputPage;
    }

    @Override
    public void update(String data) {
        inputPage.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton thisBtn = (JButton) e.getSource();
        String bidId = thisBtn.getName();
        outputPage.update(bidId);
        ApplicationManager.loadPage(ApplicationManager.FIND_BID_DETAIL);
    }
}
