package unused;

import abstractions.ListenerLinkInterface;
import abstractions.Publisher;
import services.ViewManagerService;
import views.tutor_responds.FindBidDetails;
import abstractions.ObserverOutputInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Updates the FindTutorResponse page with the messageId and userId before bringing it into view.
 */
public class FindTutorResponseLink extends Publisher implements ActionListener, ObserverOutputInterface {

    private ListenerLinkInterface inputPage;

    public FindTutorResponseLink(ListenerLinkInterface inputPage) {
        super();
        this.inputPage = inputPage;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton thisBtn = (JButton) e.getSource();
        String bidInfo = thisBtn.getName();
        notifySubscribers(bidInfo);
        ViewManagerService.loadPage(ViewManagerService.FIND_TUTOR_RESPONSE);
    }

    @Override //TODO: find out if the addViewBidListener method is even needed (currently works without)
    public void update(String data) {
        inputPage.addLinkListener(this);
    }

}
