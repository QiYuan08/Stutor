package links;

import controller.Listener;
import services.ViewManagerService;
import views.tutor_responds.FindBidDetails;
import controller.ObserverOutputInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class FindBidderDetailLink extends Listener implements ObserverOutputInterface {

    private FindBidDetails inputPage;

    public FindBidderDetailLink(FindBidDetails inputPage) {
        super();
        this.inputPage = inputPage;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton thisBtn = (JButton) e.getSource();
        String bidId = thisBtn.getName();
        notifySubscribers(bidId);
        ViewManagerService.loadPage(ViewManagerService.FIND_TUTOR_RESPONSE);
    }

    @Override
    public void update(String data) {
        inputPage.addViewBidListener(this);
    }

}
