package listener;

import abstractions.ObserverInputInterface;
import abstractions.Publisher;
import org.json.JSONObject;
import utilities.Contract;
import utilities.RenewContractStrategy;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class RenewContractListener extends Publisher implements ActionListener {

    private ObserverInputInterface inputPage;
    private Contract contractUtil;

    public RenewContractListener() {
        super();
        this.contractUtil = new Contract();
        contractUtil.setStrategy(new RenewContractStrategy());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton thisBtn = (JButton) e.getSource();
        this.inputPage = (ObserverInputInterface) thisBtn.getParent();

        if (thisBtn.getText().equals("Submit Contract")){
            contractUtil.postContract(inputPage.retrieveInputs());

        } else {
            JSONObject contractId = new JSONObject().put("id", inputPage.retrieveInputs().getString("contractId"));
            boolean isTutor = inputPage.retrieveInputs().optBoolean("isTutor");
            contractUtil.signContract(contractId, isTutor);
        }

    }

}
