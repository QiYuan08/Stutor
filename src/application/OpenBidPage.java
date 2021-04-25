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
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;

// TODO: decide how to get subjectID consistently
public class OpenBidPage extends JPanel implements ObserverInputInterface, ObserverOutputInterface {

    private JLabel activityTitle, subjectField, qualificationField, lessonField, dayField, startTimeField, endTimeField, rateField, sessionLabel, typeField, durationLabel, rateLabel, sessionField;
    private JTextField lessonInput, dayInput, rateInput, sessionInput;
    private JButton submitButton;
    private JComboBox<String> startMeridiem, typeCombo, subjectCombo, competencyCombo;
    private JSpinner startTime, duration;
    private String userId = "ecc52cc1-a3e4-4037-a80f-62d3799645f4";   // TODO: remove the hardcoded userId
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

        activityTitle = new JLabel("Request Tutor");
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
        subjectCombo = new JComboBox<String>(subjectsNameArr);
        c.gridx = 1;
        c.gridwidth = 4;
        this.add(subjectCombo, c);

        // Tutor Qualification
        qualificationField = new JLabel("Minimum Qualification: ");
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        this.add(qualificationField, c);


        competencyCombo = new JComboBox<>(new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"});
        c.gridx = 1;
        c.gridwidth = 4;
        this.add(competencyCombo, c);

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

        // Duration per session
        endTimeField = new JLabel("Duration: ");
        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 1;
        c.gridheight = 1;
        this.add(endTimeField, c);

        c.gridx = 1;
        duration = new JSpinner(new SpinnerNumberModel(1, 1, 14, 1));
        this.add(duration, c);

        durationLabel = new JLabel(" hours per lesson");
        c.gridx = 2;
        c.gridy = 6;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0.2;
        this.add(durationLabel, c);

        // Session per week
        sessionField = new JLabel("Preferred Session: ");
        c.gridx = 0;
        c.gridy = 7;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        this.add(sessionField, c);

        sessionInput = new JTextField();
        c.gridx = 1;
        c.gridy = 7;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        this.add(sessionInput, c);

        sessionLabel = new JLabel("per week");
        c.gridx = 2;
        c.gridy = 7;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0.2;
        this.add(sessionLabel, c);

        // Preferred Rate
        rateField = new JLabel("Rate: ");
        c.gridx = 0;
        c.gridy = 8;
        c.gridwidth = 1;
        c.gridheight = 1;
        this.add(rateField, c);

        rateInput = new JTextField();
        c.gridx = 1;
        c.gridwidth = 1;
        this.add(rateInput, c);

        rateLabel = new JLabel("per hour");
        c.gridx = 2;
        c.weightx = 0.2;
        c.gridwidth = 1;
        c.gridheight = 1;
        this.add(rateLabel, c);

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
    }

    @Override
    public JSONObject retrieveInputs() {

        String noOfLesson = lessonInput.getText();
        String day = dayInput.getText();
        String time = startTime.getValue().toString() + startMeridiem.getSelectedItem().toString();
        String rate = rateInput.getText();
        Integer competency = Integer.valueOf(competencyCombo.getSelectedItem().toString());
        String subjectId = subjectMapping.get(subjectCombo.getSelectedItem());

        JSONObject additionalInfo = new JSONObject();
        additionalInfo.put("minCompetency", competency);
        additionalInfo.put("noOfoLesson", noOfLesson);
        additionalInfo.put("day", day);
        additionalInfo.put("startTime", time);
        additionalInfo.put("duration", duration.getValue().toString());
        additionalInfo.put("preferredSession", Integer.valueOf(sessionInput.getText()));
        additionalInfo.put("rate", rate);

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("subjectId", subjectId);
        jsonObj.put("type", typeCombo.getSelectedItem().toString());
        jsonObj.put("initiatorId", this.userId);
        jsonObj.put("dateCreated", Instant.now());
        jsonObj.put("additionalInfo", additionalInfo);

        return jsonObj;
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
