package links;

import controller.ListenerLinkInterface;
import controller.Listener;
import services.ViewManagerService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SeeMessageLink extends Listener implements ActionListener {

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
