package listeners;

import services.ViewManagerService;
import views.student_bids.SeeBidDetails;
import interfaces.ObserverOutputInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Adds an itself to the SeeBidDetails page for all of the buttons in the dynamic list of tutor responses.
 */
public class SeeBidListener implements ActionListener, ObserverOutputInterface {

    private SeeBidDetails inputPage;

    public SeeBidListener(SeeBidDetails inputPage) {
        this.inputPage = inputPage;
    }

    // when the view detail button in SeeAllBids are clicked
    @Override
    public void actionPerformed(ActionEvent e) {
        ViewManagerService.loadPage(ViewManagerService.SEE_TUTOR_RESPONSE);
    }

    /**
     * Method to add action listener to every view detail button in SeeAllBids
     * @param data None
     */
    @Override
    public void update(String data) {
        this.inputPage.addLinkListener(this);
    }
}
