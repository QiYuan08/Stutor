package controller;

import application.Application;
import interfaces.InputInterface;
import org.json.JSONObject;
import utils.ResponseOpenBidUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/***
 * Controller class that control the action of ResponseOpenBid GUI view class
 */
public class ResponseBidController extends Controller{

    public ResponseBidController(InputInterface inputPage) {
        super(inputPage);
        utils = new ResponseOpenBidUtil();
    }

    public void initListener(){
            this.inputPage.setListener(new ResponseBidListener());
    }

    // action listener when tutor submit a bid
    // update the existing bid additionalInformation part
    class ResponseBidListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            JSONObject bid = inputPage.retrieveInputs(); // get input data from GUI
            JButton btn = (JButton) e.getSource();; // get the button that is clicked to get the bidId
            String bidId = btn.getName(); // get the bidId that this bid is replying to

            utils.updateBid(bidId, bid);
            Application.loadPage(Application.DASHBOARD_PAGE);
        }
    }
}
