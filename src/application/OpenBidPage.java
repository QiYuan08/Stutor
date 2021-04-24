package application;

import controller.ObserverInputInterface;
import controller.ObserverOutputInterface;
import org.json.JSONObject;
import utils.OpenBidUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;

// TODO: decide how to get subjectID consistently
public class OpenBidPage extends JPanel implements ObserverInputInterface, ObserverOutputInterface {

    private JLabel activityTitle, subjectField, qualificationField, lessonField, dayField, startTimeField, endTimeField, rateField, typeField;
    private JTextField qualificationInput, lessonInput, dayInput, rateInput;
    private JButton submitButton;
    private JComboBox<String> startMeridiem, endMeridiem,rateCombo, typeCombo, subjectInput;
    private JSpinner startTime, endTime;
    private String userId;
    private HashMap<String, String> subjectMapping;
    private OpenBidUtil util = new OpenBidUtil();

    public OpenBidPage(){
        String[] meridiem = {"AM", "PM"};

        this.setBorder(new EmptyBorder(15, 15, 15, 15));
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
//        c.weighty = 1;
        c.insets = new Insets(5, 5, 0, 5);

        activityTitle = new JLabel("Bidding");
        activityTitle.setHorizontalAlignment(JLabel.CENTER);
        activityTitle.setVerticalAlignment(JLabel.TOP);
        activityTitle.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 4;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(activityTitle, c);

        // subject
        subjectField = new JLabel("Subject Name: ");
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        this.add(subjectField, c);

        // retrieve all the subject name from the key mapping
        ArrayList<String> subjectsName = new ArrayList<String>();
        subjectMapping = util.getAllSubject();
        for (String key: subjectMapping.keySet()){
            subjectsName.add(key);
        }
        // convert arraylist into array for combobox
        String[] subjectsNameArr = new String[subjectsName.size()];
        subjectsName.toArray(subjectsNameArr);
        subjectInput = new JComboBox<String>(subjectsNameArr);
        c.gridx = 1;
        c.gridwidth = 4;
        this.add(subjectInput, c);

        // Tutor Qualification
        qualificationField = new JLabel("Minimum Qualification: ");
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        this.add(qualificationField, c);

        qualificationInput = new JTextField();
        c.gridx = 1;
        c.gridwidth = 4;
        this.add(qualificationInput, c);

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

        // checkbox to input bid type
        typeField = new JLabel("Bid Type");
        c.gridx = 0;
        c.gridy = 9;
        this.add(typeField, c);

        typeCombo = new JComboBox<>(new String[]{"open", "close"});
        c.weightx = 0.1;
        c.gridx = 1;
        this.add(typeCombo, c);

        //submitBtn
        submitButton = new JButton("Submit Request");
        c.weightx = 0.1;
        c.gridx = 0;
        c.gridy = 10;
        c.gridwidth = 4;
        this.add(submitButton, c);

        // actionListener
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String noOfLesson = lessonInput.getText();
                String day = dayInput.getText();
                String time = startTime.getValue().toString() + startMeridiem.getSelectedItem().toString() + " to " + endTime.getValue().toString() + endMeridiem.getSelectedItem().toString();
                String rate = rateInput.getText() + rateCombo.getSelectedItem().toString();

                JSONObject additionalInfo = new JSONObject();
                additionalInfo.put("noOfoLesson", noOfLesson);
                additionalInfo.put("day", day);
                additionalInfo.put("rate", rate);

//                createBid("Open",,"", additionalInfo);
            }
        });
    }

    @Override
    public JSONObject retrieveInputs() {

        String noOfLesson = lessonInput.getText();
        String day = dayInput.getText();
        String time = startTime.getValue().toString() + startMeridiem.getSelectedItem().toString() + " to " + endTime.getValue().toString() + endMeridiem.getSelectedItem().toString();
        String rate = rateInput.getText() + rateCombo.getSelectedItem().toString();
        String competency = qualificationInput.getText();

        JSONObject additionalInfo = new JSONObject();
        additionalInfo.put("noOfoLesson", noOfLesson);
        additionalInfo.put("time", time);
        additionalInfo.put("day", day);
        additionalInfo.put("rate", rate);
        additionalInfo.put("noOfLesson", noOfLesson);

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("type", typeCombo.getSelectedItem().toString());
        jsonObj.put("initiatorId", this.userId);
        jsonObj.put("dateCreated", Instant.now());
        jsonObj.put("additionalInfo", additionalInfo);
        jsonObj.put("subjectId", subjectMapping.get(subjectField.getText()));
        return new JSONObject();
    }

    @Override
    public void addActionListener(ActionListener actionListener) {
        submitButton.addActionListener(actionListener);
    }

    /***
     * Get update of the current user id when user login
     * @param data any data that is crucial to the pages for them to request the information that they need from the database
     */
    @Override
    public void update(String data) {
        this.userId = data;
    }
}
