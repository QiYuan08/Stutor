package application.bid_pages;

import controller.ObserverOutputInterface;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ConfirmBid extends JPanel implements ObserverOutputInterface {
    private JLabel name, rate, competency, session, message;
    private JTextField messageField;
    private JPanel details, response;
    private JButton confirmBtn, replyBtn;

    public ConfirmBid(){
        String[] meridiem = {"AM", "PM"};

        this.setBorder(new EmptyBorder(15, 15, 15, 15));
        this.setLayout(new GridLayout(2,1));
        GridBagConstraints c = new GridBagConstraints();

        // innner panel for detail
        details = new JPanel();
        details.setBackground(Color.cyan);
        details.setLayout(new GridBagLayout());
        GridBagConstraints detailConst = new GridBagConstraints();
        detailConst.insets = new Insets(5,5,5,5);
        detailConst.fill = GridBagConstraints.HORIZONTAL;
        detailConst.weightx = 0.5;
        c.weighty = 1;

        name = new JLabel("Name: " + "Teh Qi Yuan");
        detailConst.gridx = 0;
        detailConst.gridwidth = 3;
        detailConst.gridy = 0;
        detailConst.anchor = GridBagConstraints.PAGE_START;
        name.setBackground(Color.BLUE);
        details.add(name, detailConst);

        rate = new JLabel("Rate: " + "5$ per minute");
        detailConst.gridy = 1;
        details.add(rate, detailConst);

        competency = new JLabel("Competency: " + 5);
        detailConst.gridy = 2;
        details.add(competency, detailConst);

        session = new JLabel("Session: " + 4);
        detailConst.gridy = 3;
        details.add(session, detailConst);

        confirmBtn = new JButton("Confirm");
        detailConst.weightx = 0.1;
        detailConst.gridwidth = 1;
        detailConst.gridx = 4;
        detailConst.gridy = 4;
        detailConst.anchor = GridBagConstraints.PAGE_END; //bottom of space
        details.add(confirmBtn, detailConst);

        this.add(details);

        response = new JPanel();
        response.setBackground(Color.lightGray);
        response.setLayout(new GridBagLayout());
        GridBagConstraints responseConst = new GridBagConstraints();
        responseConst.insets = new Insets(5,5,5,5);
        responseConst.fill = GridBagConstraints.HORIZONTAL;
        responseConst.anchor = GridBagConstraints.PAGE_START;
        responseConst.weightx = 0.2;

        message = new JLabel("Messages: ");
        responseConst.gridx = 0;
        responseConst.gridy = 0;
        responseConst.gridwidth = 1;
        response.add(message, responseConst);

        messageField = new JTextField();
        responseConst.weighty = 1;
        responseConst.weightx = 1;
        responseConst.fill = GridBagConstraints.BOTH;
        responseConst.gridx = 1;
        responseConst.gridwidth = 3;
        responseConst.gridheight = 3;
        responseConst.gridy = 0;
        response.add(messageField, responseConst);

        replyBtn = new JButton("Bid");
        responseConst.weighty = 0;
        responseConst.weightx = 0.2;
        responseConst.gridwidth = 1;
        responseConst.gridx = 4;
        responseConst.gridy = 4;
        responseConst.anchor = GridBagConstraints.PAGE_END; //bottom of space
        response.add(replyBtn, responseConst);

        this.add(response);

    }

    @Override
    public void update(String data) {

    }
}
