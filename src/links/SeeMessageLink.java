package links;

import application.ApplicationManager;
import application.main_pages.MessagesPage;
import application.student_bids.SeeTutorResponse;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SeeMessageLink implements ActionListener {

    private SeeTutorResponse inputPage;
    private MessagesPage outputPage;

    public SeeMessageLink(SeeTutorResponse inputPage, MessagesPage outputPage){
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
