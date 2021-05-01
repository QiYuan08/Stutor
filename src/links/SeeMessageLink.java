package links;

import services.ViewManagerService;
import views.main_pages.MessagesPage;
import views.student_bids.SeeTutorResponse;

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
        ViewManagerService.loadPage(ViewManagerService.MESSAGES_PAGE);
    }
}
