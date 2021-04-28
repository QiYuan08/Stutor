package listeners;

import controller.ApplicationController;
import controller.ObserverInputInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ContractListener implements ActionListener {

    private ObserverInputInterface inputPage;
    private ApplicationController applicationController;

    public ContractListener(ObserverInputInterface inputPage, ApplicationController applicationController) {
        this.inputPage = inputPage;
        this.applicationController = applicationController;
        inputPage.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
