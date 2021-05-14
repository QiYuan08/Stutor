package controllers;

import abstractions.ListenerLinkInterface;
import abstractions.ObserverOutputInterface;
import abstractions.Publisher;
import services.ViewManagerService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewContractDetailController extends Publisher implements ActionListener, ObserverOutputInterface {

    private ListenerLinkInterface inputPage;

    public ViewContractDetailController(ListenerLinkInterface inputPage) {
        super();
        this.inputPage = inputPage;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton thisBtn = (JButton) e.getSource();
        String bidId = thisBtn.getName();
        notifySubscribers(bidId);
        ViewManagerService.loadPage(ViewManagerService.VIEW_CONTRACT_DETAIL);
    }

    @Override
    public void update(String data) {
        inputPage.addLinkListener(this);
    }
}
