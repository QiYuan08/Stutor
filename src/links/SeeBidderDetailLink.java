package links;

import controller.ListenerLinkInterface;
import controller.Listener;
import services.ViewManagerService;
import controller.ObserverOutputInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SeeBidderDetailLink extends Listener implements ObserverOutputInterface {

    private ListenerLinkInterface inputPage;

    public SeeBidderDetailLink(ListenerLinkInterface inputPage) {
        super();
        this.inputPage = inputPage;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton thisBtn = (JButton) e.getSource();
        String bidId = thisBtn.getName();
        notifySubscribers(bidId);
        ViewManagerService.loadPage(ViewManagerService.SEE_TUTOR_RESPONSE);
    }

    @Override
    public void update(String data) {
        inputPage.addLinkListener(this);
    }
}
