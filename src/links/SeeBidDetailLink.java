package links;

import controller.Listener;
import services.ViewManagerService;
import controller.ListenerLinkInterface;
import controller.ObserverOutputInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SeeBidDetailLink extends Listener implements ActionListener, ObserverOutputInterface {

    private ListenerLinkInterface inputPage;

    public SeeBidDetailLink(ListenerLinkInterface inputPage) {
        super();
        this.inputPage = inputPage;
    }

    @Override
    public void update(String data) {
        inputPage.addLinkListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton thisBtn = (JButton) e.getSource();
        String bidId = thisBtn.getName();
        notifySubscribers(bidId);
        ViewManagerService.loadPage(ViewManagerService.SEE_BID_DETAILS);
    }
}
