package links;

import application.Application;
import application.ApplicationManager;
import application.bid_pages.MessagesPage;
import application.bid_pages.SeeTutorBidDetail;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SeeMessageLink implements ActionListener {

    private SeeTutorBidDetail inputPage;
    private MessagesPage outputPage;

    public SeeMessageLink(SeeTutorBidDetail inputPage, MessagesPage outputPage){
        this.inputPage = inputPage;
        this.outputPage = outputPage;
        this.inputPage.addMessageBtnListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton thisBtn = (JButton) e.getSource();
        outputPage.update(thisBtn.getName());
        ApplicationManager.loadPage(ApplicationManager.MESSAGES_PAGE);
    }
}
