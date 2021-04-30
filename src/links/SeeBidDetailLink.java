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

    public SeeBidDetailLink(ListenerLinkInterface inputPage, ObserverOutputInterface outputPage) {
        this.inputPage = inputPage;
        this.outputPage = outputPage;
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
        ApplicationManager.loadPage(ApplicationManager.FIND_BID_DETAIL);
    }
}
