package application;

import controller.ObserverInputInterface;
import controller.ObserverOutputInterface;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class ResponseOpenBid extends JPanel implements ObserverInputInterface, ObserverOutputInterface {

    private JLabel activityTitle, nameField, competencyField, lessonField, dayField, startTimeField, endTimeField, rateField, freeLessonField;
    private JTextField nameInput, competencyInput, lessonInput, dayInput, rateInput;
    private JButton submitButton;
    private JComboBox<String> startMeridiem, endMeridiem, rateCombo;
    private JSpinner startTime, endTime, freeLesson;
    private String bidId;

    public ResponseOpenBid() {
        String[] meridiem = {"AM", "PM"};

        this.setBorder(new EmptyBorder(15, 15, 15, 15));
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
//        c.weighty = 1;
        c.insets = new Insets(5, 5, 0, 5);

        activityTitle = new JLabel("Bid for request:" + this.bidId);
        activityTitle.setHorizontalAlignment(JLabel.CENTER);
        activityTitle.setVerticalAlignment(JLabel.TOP);
        activityTitle.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 4;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(activityTitle, c);

        // subject
        nameField = new JLabel("Name: ");
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        this.add(nameField, c);

        nameInput = new JTextField();
        c.gridx = 1;
        c.gridwidth = 4;
        this.add(nameInput, c);

        // Tutor Qualification
        competencyField = new JLabel("Competency: ");
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        this.add(competencyField, c);

        competencyInput = new JTextField();
        c.gridx = 1;
        c.gridwidth = 4;
        this.add(competencyInput, c);

        // Lesson
        lessonField = new JLabel("No of Lesson: ");
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        c.gridheight = 1;
        this.add(lessonField, c);

        lessonInput = new JTextField();
        c.gridx = 1;
        c.gridwidth = 4;
        this.add(lessonInput, c);

        // Preferred Day
        dayField = new JLabel("Day: ");
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 1;
        c.gridheight = 1;
        this.add(dayField, c);

        dayInput = new JTextField();
        c.gridx = 1;
        c.gridwidth = 4;
        this.add(dayInput, c);

        // Start time
        startTimeField = new JLabel("Start Time: ");
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 1;
        c.gridheight = 1;
        this.add(startTimeField, c);

        c.gridx = 1;
        startTime = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        this.add(startTime, c);

        c.gridx = 2;
        startMeridiem = new JComboBox<>(meridiem);
        this.add(startMeridiem, c);

        // End time
        endTimeField = new JLabel("End Time: ");
        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 1;
        c.gridheight = 1;
        this.add(endTimeField, c);

        c.gridx = 1;
        endTime = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        this.add(endTime, c);

        c.gridx = 2;
        endMeridiem = new JComboBox<>(meridiem);
        this.add(endMeridiem, c);

        // Preferred Rate
        rateField = new JLabel("Rate: ");
        c.gridx = 0;
        c.gridy = 7;
        c.gridwidth = 1;
        c.gridheight = 1;
        this.add(rateField, c);

        rateInput = new JTextField();
        c.gridx = 1;
        c.gridwidth = 2;
        this.add(rateInput, c);

        // dropdown to choose rate per day or week
        String[] dayOrWeek = {"per day", "per week"};
        rateCombo = new JComboBox<String>(dayOrWeek);
        c.gridwidth = 1;
        c.gridx = 3;
        this.add(rateCombo, c);

        // option to provide free lesson
        freeLessonField = new JLabel("Free Lesson: ");
        c.gridx = 0;
        c.gridy = 8;
        c.gridwidth = 3;
        this.add(freeLessonField, c);

        freeLesson = new JSpinner(new SpinnerNumberModel(0,0,5,1));
        c.gridx = 1;
        c.gridy = 8;
        c.gridwidth = 2;
        this.add(freeLesson, c);

        //submitBtn
        submitButton = new JButton("Submit Bid");
        c.gridx = 0;
        c.gridy = 9;
        c.gridwidth = 4;
        this.add(submitButton, c);

        // create a new bid
        // add the bid into additionalInfo of current bid
    }

    @Override
    public JSONObject retrieveInputs() {

        String time = startTime.getValue().toString() + startMeridiem.getSelectedItem().toString() + " to " + endTime.getValue().toString() + endMeridiem.getSelectedItem().toString();
        String rate = rateInput.getText() + rateCombo.getSelectedItem().toString();

        JSONObject data = new JSONObject();
        data.put("name", nameInput.getText());
        data.put("competency", competencyInput.getText());
        data.put("noOfLesson", lessonInput.getText());
        data.put("day", dayInput.getText());
        data.put("time", time);
        data.put("rate", rate);
        data.put("freeLesson", freeLesson.getValue());

        return data;
    }

    @Override
    public void addActionListener(ActionListener actionListener) {
        submitButton.addActionListener(actionListener);
    }

    @Override
    public void update(String data) {
        this.bidId = bidId;
        activityTitle.setText("Bidding for: " + this.bidId);
        submitButton.setName(bidId); // set the name of this button as bidId for quering with db
    }
}
