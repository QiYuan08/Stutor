package application.bid_pages;

import api.ApiRequest;
import application.ApplicationManager;
import controller.ObserverInputInterface;
import controller.ObserverOutputInterface;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZonedDateTime;

public class ResponseCloseBid extends  JPanel implements ObserverOutputInterface, ObserverInputInterface {

    private JLabel activityTitle, lessonField, dayField,sessionLabel,startTimeField, sessionField, durationLabel, rateLabel, endTimeField, rateField, freeLessonField, messageField;
    private JTextField lessonInput, dayInput, rateInput, sessionInput;
    private JTextArea messageInput;
    private JButton submitButton, backBtn;
    private JComboBox<String> startMeridiem;
    private JSpinner duration, endTime, freeLesson, startTime;
    private String bidId, userId;

    public ResponseCloseBid() {
        String[] meridiem = {"AM", "PM"};

        this.setBorder(new EmptyBorder(15, 15, 15, 15));
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
//        c.weighty = 1;
        c.insets = new Insets(5, 5, 0, 5);

        backBtn = new JButton("Back");
        c.weightx = 0.2;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        this.add(backBtn, c);

        activityTitle = new JLabel("Bid for request: " + this.bidId);
        activityTitle.setHorizontalAlignment(JLabel.CENTER);
        activityTitle.setVerticalAlignment(JLabel.TOP);
        activityTitle.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        c.gridx = 1;
        c.weightx = 0.5;
        c.gridy = 0;
        c.gridwidth = 4;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(activityTitle, c);

        // Lesson
        lessonField = new JLabel("No of Lesson: ");
        c.gridx = 0;
        c.gridy = 1;
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
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        this.add(dayField, c);

        dayInput = new JTextField();
        c.gridx = 1;
        c.gridwidth = 4;
        this.add(dayInput, c);

        // Duration per session
        endTimeField = new JLabel("Duration: ");
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        c.gridheight = 1;
        this.add(endTimeField, c);

        c.gridx = 1;
        duration = new JSpinner(new SpinnerNumberModel(1, 1, 14, 1));
        this.add(duration, c);

        durationLabel = new JLabel(" hours per lesson");
        c.gridx = 2;
        c.gridy = 3;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0.2;
        this.add(durationLabel, c);

        // Session per week
        sessionField = new JLabel("Preferred Session: ");
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        this.add(sessionField, c);

        sessionInput = new JTextField();
        c.gridx = 1;
        c.gridy = 4;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        this.add(sessionInput, c);

        sessionLabel = new JLabel("sessions per week");
        c.gridx = 2;
        c.gridy = 4;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0.2;
        this.add(sessionLabel, c);

        c.gridx = 1;
        endTime = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        this.add(endTime, c);

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

        // Preferred Rate
        rateField = new JLabel("Rate: ");
        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 1;
        c.gridheight = 1;
        this.add(rateField, c);

        rateInput = new JTextField();
        c.gridx = 1;
        c.gridwidth = 1;
        this.add(rateInput, c);

        rateLabel = new JLabel("dollar per hour");
        c.gridx = 2;
        c.weightx = 0.2;
        c.gridwidth = 1;
        c.gridheight = 1;
        this.add(rateLabel, c);

        // option to provide free lesson
        freeLessonField = new JLabel("Free Lesson: ");
        c.gridx = 0;
        c.gridy = 7;
        c.gridwidth = 3;
        this.add(freeLessonField, c);

        freeLesson = new JSpinner(new SpinnerNumberModel(0,0,5,1));
        c.gridx = 1;
        c.gridy = 7;
        c.weightx = 0;
        c.gridwidth = 2;
        this.add(freeLesson, c);

        // messages
        messageField = new JLabel("Message: ");
        c.gridx = 0;
        c.gridy = 8;
        c.gridwidth = 1;
        this.add(messageField, c);

        messageInput = new JTextArea(5, 20);
        messageInput.setLineWrap(true);
        messageInput.setWrapStyleWord(true);
        c.gridx = 1;
        c.gridy = 8;
        c.gridwidth = 3;
        c.gridheight = 2;
        c.weighty = 0;
        this.add(messageInput, c);

        //submitBtn
        submitButton = new JButton("Submit Close Bid");
        c.gridx = 0;
        c.weightx = 1;
        c.gridy = 11;
        c.gridwidth = 4;
        this.add(submitButton, c);

        // create a new bid
        // add the bid into additionalInfo of current bid

        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ApplicationManager.loadPage(ApplicationManager.FIND_BID_DETAIL);
            }
        });
    }

    @Override
    public JSONObject retrieveInputs() {

        // getting local timestamp
        Timestamp ts = Timestamp.from(ZonedDateTime.now().toInstant());
        Instant now = ts.toInstant();

        String noOfLesson = lessonInput.getText();
        String day = dayInput.getText();
        String time = startTime.getValue().toString() + startMeridiem.getSelectedItem().toString();
        String rate = rateInput.getText();

        // creating the json body to pass to response bid listener
        JSONObject additionalInfo = new JSONObject();
        additionalInfo.put("noOfoLesson", noOfLesson);
        additionalInfo.put("day", day);
        additionalInfo.put("startTime", time);
        additionalInfo.put("duration", duration.getValue().toString());
        additionalInfo.put("preferredSession", Integer.valueOf(sessionInput.getText()));
        additionalInfo.put("rate", rate);
        additionalInfo.put("freeLesson", freeLesson.getValue());

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("bidId", this.bidId);
        jsonObj.put("posterId", this.userId);
        jsonObj.put("datePosted", now);
        jsonObj.put("content", (messageInput.getText().equals("")) ? "string" : messageInput.getText()); // if messageInput empty return string else get messageInput
        jsonObj.put("additionalInfo", additionalInfo);

        return jsonObj;
    }

    @Override
    public void addActionListener(ActionListener actionListener) {
        submitButton.addActionListener(actionListener);
    }

    @Override
    public void update(String data) {
        JSONObject jsonObject = new JSONObject(data);
        this.bidId = jsonObject.getString("bidId");
        this.userId = jsonObject.getString("userId");

        HttpResponse<String> response = ApiRequest.get("/bid/" + this.bidId);
        String subjectName = new JSONObject(response.body()).getJSONObject("subject").getString("name");
        activityTitle.setText(subjectName);

        submitButton.setName(bidId); // set the name of this button as bidId for quering with db
    }
}
