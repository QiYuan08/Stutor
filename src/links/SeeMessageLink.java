package links;

import abstractions.ListenerLinkInterface;
import abstractions.Publisher;
import services.ViewManagerService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Links from the SeeTutorResponse page to the messages page, and updates it with existing messages by the tutor (and student)
 */
public class SeeMessageLink extends Publisher implements ActionListener {

    private ListenerLinkInterface inputPage;

    public SeeMessageLink(ListenerLinkInterface inputPage){
        super();
        this.inputPage = inputPage;
        this.inputPage.addLinkListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton thisBtn = (JButton) e.getSource();
        notifySubscribers(thisBtn.getName());
        ViewManagerService.loadPage(ViewManagerService.MESSAGES_PAGE);
    }
}
