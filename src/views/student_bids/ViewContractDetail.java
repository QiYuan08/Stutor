package views.student_bids;

import abstractions.ObserverInputInterface;
import abstractions.ObserverOutputInterface;
import org.json.JSONObject;
import services.ViewManagerService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.HashMap;

public class ViewContractDetail extends JPanel implements ObserverOutputInterface, ObserverInputInterface {

    private JLabel activityTitle, tutorField, qualificationField, lessonField, dayField, startTimeField, endTimeField, rateField, sessionLabel, typeField, durationLabel, rateLabel, sessionField;
    private JTextField lessonInput, dayInput, tutorInput, rateInput, sessionInput, competencyInput;
    private JButton submitButton;
    private JButton backBtn;
    private JComboBox<String> startMeridiem;
    private JLabel typeName;
    private JComboBox<String> subjectCombo;
    private JComboBox<String> competencyCombo;
    private JSpinner startTime, duration;
    private HashMap<String, String> subjectMapping;
    private String userId;

    public ViewContractDetail() {
        submitButton = new JButton("Submit Request");

        this.setBorder(new EmptyBorder(15, 15, 15, 15));
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.insets = new Insets(5, 5, 0, 5);

        backBtn = new JButton("Back");
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.weightx = 0.2;
        this.add(backBtn, c);

        activityTitle = new JLabel("Contract Detail");
        activityTitle.setHorizontalAlignment(JLabel.CENTER);
        activityTitle.setVerticalAlignment(JLabel.TOP);
        activityTitle.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        c.gridx = 0;
        c.weightx = 1;
        c.gridy = 0;
        c.gridwidth = 4;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(activityTitle, c);

        // Tutor Qualification
        qualificationField = new JLabel("Minimum Qualification: ");
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        this.add(qualificationField, c);

        competencyInput = new JTextField();
        competencyInput.setText("3");
        competencyInput.setEditable(false);
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
        lessonInput.setEditable(false);
        c.gridx = 1;
        c.gridwidth = 4;
        this.add(lessonInput, c);

        // Preferred Day
        dayField = new JLabel("Preferred Day(s): ");
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 1;
        c.gridheight = 1;
        this.add(dayField, c);

        dayInput = new JTextField();
        dayInput.setEditable(false);
        c.gridx = 1;
        c.gridwidth = 4;
        this.add(dayInput, c);

        // Start time
        startTimeField = new JLabel("Preferred Time: ");
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 1;
        c.gridheight = 1;
        this.add(startTimeField, c);

        c.gridx = 1;
        startTime = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        startTime.setEnabled(false);
        this.add(startTime, c);

        c.gridx = 2;
        String[] meridiem = {"AM", "PM"};
        startMeridiem = new JComboBox<>(meridiem);
        startMeridiem.setEnabled(false);
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
        duration.setEnabled(false);
        this.add(duration, c);

        durationLabel = new JLabel(" hours per lesson");
        c.gridx = 2;
        c.gridy = 6;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0.2;
        this.add(durationLabel, c);

        // Session per week
        sessionField = new JLabel("Preferred No of Lesson(s): ");
        c.gridx = 0;
        c.gridy = 7;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        this.add(sessionField, c);

        sessionInput = new JTextField();
        sessionInput.setEditable(false);
        c.gridx = 1;
        c.gridy = 7;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        this.add(sessionInput, c);

        sessionLabel = new JLabel("sessions per week");
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
        rateInput.setEditable(false);
        c.gridx = 1;
        c.gridwidth = 1;
        this.add(rateInput, c);

        rateLabel = new JLabel("dollar per hour");
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

        typeName = new JLabel("open");
        c.weightx = 0.1;
        c.gridx = 1;
        this.add(typeName, c);

        //submitBtn
        c.weightx = 0.1;
        c.gridx = 0;
        c.gridy = 10;
        c.gridwidth = 4;
        this.add(submitButton, c);

        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewManagerService.loadPage(ViewManagerService.DASHBOARD_PAGE);
            }
        });

    }

    private void buildPage(){

    }

    @Override
    public JSONObject retrieveInputs() {

        String noOfLesson = lessonInput.getText();
        String day = dayInput.getText();
        String time = startTime.getValue().toString() + startMeridiem.getSelectedItem().toString();
        String rate = rateInput.getText();
        Integer competency = Integer.valueOf(competencyCombo.getSelectedItem().toString());
        String subjectId = subjectMapping.get(subjectCombo.getSelectedItem());

        // getting local timestamp
        Timestamp ts = Timestamp.from(ZonedDateTime.now().toInstant());
        Instant now = ts.toInstant();

        JSONObject additionalInfo = new JSONObject();
        additionalInfo.put("minCompetency", competency);
        additionalInfo.put("noOfLesson", noOfLesson);
        additionalInfo.put("day", day);
        additionalInfo.put("startTime", time);
        additionalInfo.put("duration", duration.getValue().toString());
        additionalInfo.put("preferredSession", Integer.valueOf(sessionInput.getText()));
        additionalInfo.put("rate", rate);

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("subjectId", subjectId);
//        jsonObj.put("type", typeName.getSelectedItem().toString());
        jsonObj.put("initiatorId", this.userId);
        jsonObj.put("dateCreated", now);
        jsonObj.put("additionalInfo", additionalInfo);

        return jsonObj;
    }

    @Override
    public void addActionListener(ActionListener actionListener) {
        this.submitButton.addActionListener(actionListener);
    }

    @Override
    public void update(String data) {

    }
}
