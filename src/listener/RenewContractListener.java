package listener;

import abstractions.ObserverInputInterface;
import org.json.JSONArray;
import org.json.JSONObject;
import services.ApiRequest;
import services.ViewManagerService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class RenewContractListener implements ActionListener {

    private ObserverInputInterface inputPage;

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton thisBtn = (JButton) e.getSource();
        this.inputPage = (ObserverInputInterface) thisBtn.getParent();

        if (thisBtn.getText().equals("Submit Contract")){
            postContract(inputPage.retrieveInputs());

        } else {
            String contractId = inputPage.retrieveInputs().getString("contractId");
            signContract(contractId);
        }

    }

    private void signContract(String contractId) {

        JSONObject dateSigned = new JSONObject();
        Timestamp ts = Timestamp.from(ZonedDateTime.now().toInstant());
        Instant now = ts.toInstant();
        dateSigned.put("dateSigned", now);
        HttpResponse<String> contractSignResponse = ApiRequest.post("/contract/" + contractId + "/sign", dateSigned.toString());
        String msg;

        if (contractSignResponse.statusCode() == 200) {

            // change is signed by for tutor to true
            JSONObject contract = new JSONObject(ApiRequest.get("/contract/" + contractId).body());
            JSONObject lessonInfo = contract.getJSONObject("lessonInfo");
            lessonInfo.remove("tutorSigned");
            lessonInfo.put("tutorSigned", true);

            ApiRequest.patch("/contract/" + contractId, contract.toString());

            msg = "Contract signed at " + now;
            JOptionPane.showMessageDialog(new JFrame(), msg, "Contract Signed Successfully", JOptionPane.INFORMATION_MESSAGE);
        } else {
            msg = "Contract not signed: Error " + contractSignResponse.statusCode();
            JOptionPane.showMessageDialog(new JFrame(), msg, "Bad request", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void postContract(JSONObject contract) {

        contract.put("firstPartyId", contract.getString("firstPartyId"));
        contract.put("secondPartyId", contract.getString("secondPartyId"));
        contract.put("lessonInfo", contract.getJSONObject("additionalInfo"));

        Timestamp ts = Timestamp.from(ZonedDateTime.now().toInstant());
        Instant now = ts.toInstant();
        contract.put("dateCreated", now);
        LocalDateTime time = LocalDateTime.ofInstant(ts.toInstant(), ZoneOffset.ofHours(0));
        time = time.plus(contract.getJSONObject("additionalInfo").getInt("contractLength"), ChronoUnit.MONTHS); // contract expires after a year
        Instant output = time.atZone(ZoneOffset.ofHours(0)).toInstant();
        Timestamp expiryDate = Timestamp.from(output);
        contract.put("subjectId", contract.getString("subjectId"));
        contract.put("expiryDate", expiryDate);
        contract.put("paymentInfo", new JSONObject());
        contract.put("additionalInfo", new JSONObject());
        HttpResponse<String> contractResponse = ApiRequest.post("/contract", contract.toString());

        if (contractResponse.statusCode() == 201) {

            // after contract signed add them into additionalInfo for tutor so that he can sign later
            // if not cannot get the contractId for this contract
            contract = new JSONObject(contractResponse.body());
            patchTutor(contract.getJSONObject("firstParty").getString("id"), contract.getString("id"));
            JOptionPane.showMessageDialog(new JFrame(), "Contract Posted", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            String msg = "Contract not posted: Error " + contractResponse.statusCode();
            JOptionPane.showMessageDialog(new JFrame(), msg, "Bad request", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Method to add pending contract into tutor additionalInfo to sign later on
     * @param tutorId id of the tutor
     */
    private void patchTutor(String tutorId, String contractId){
        JSONObject tutor = new JSONObject(ApiRequest.get("/user/" + tutorId).body());
        JSONObject additionalInfo = tutor.getJSONObject("additionalInfo");

        // if tutor has previously pending contract
        if (additionalInfo.has("activeContract")){
            JSONArray activeContract = additionalInfo.getJSONArray("activeContract");
            activeContract.put(contractId);

            additionalInfo.remove("activeContract");
            additionalInfo.put("activeContract", activeContract);

        } else {
            JSONArray activeContract = new JSONArray();
            activeContract.put(contractId);

            additionalInfo.put("activeContract", activeContract);
        }
        ApiRequest.put("/user/" + tutorId, tutor.toString());

    }


}
