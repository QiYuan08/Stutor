package links;

import services.ViewManagerService;
import views.tutor_responds.FindBidDetails;
import controller.ObserverOutputInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FindBidderDetailLink implements ActionListener, ObserverOutputInterface {

    private FindBidDetails inputPage;
    private ObserverOutputInterface outputPage;

    public FindBidderDetailLink(FindBidDetails inputPage, ObserverOutputInterface outputPage) {
        this.inputPage = inputPage;
        this.outputPage = outputPage;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton thisBtn = (JButton) e.getSource();
        String bidId = thisBtn.getName();
        outputPage.update(bidId);
        ViewManagerService.loadPage(ViewManagerService.FIND_TUTOR_RESPONSE);
    }

    @Override
    public void update(String data) {
        inputPage.addViewBidListener(this);
    }

}
