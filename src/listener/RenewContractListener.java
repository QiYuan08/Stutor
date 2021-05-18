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
            Boolean isTutor = inputPage.retrieveInputs().optBoolean("isTutor");
            signContract(contractId, isTutor);
        }

    }

    private void signContract(String contractId, Boolean isTutor) {

        JSONObject dateSigned = new JSONObject();
        HttpResponse<String> response = ApiRequest.get("/contract/" + contractId);
        String msg;

        if (response.statusCode() == 200) {

            // change is signed by for tutor to true
            JSONObject contract = new JSONObject(response.body());
            JSONObject additionalInfo = contract.getJSONObject("additionalInfo");

            // if tutor signing
            if (isTutor){
                additionalInfo.remove("tutorSigned");
                additionalInfo.put("tutorSigned", true);


            } else {
                additionalInfo.remove("studentSigned");
                additionalInfo.put("studentSigned", true);

            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("firstPartyId", contract.getJSONObject("firstParty").getString("id"));
            jsonObject.put("secondPartyId", contract.getJSONObject("secondParty").getString("id"));
            jsonObject.put("subjectId", contract.getJSONObject("subject").getString("id"));
            jsonObject.put("dateCreated", contract.getString("dateCreated"));
            jsonObject.put("expiryDate", contract.getString("expiryDate"));
            jsonObject.put("paymentInfo", new JSONObject());
            jsonObject.put("lessonInfo", contract.getJSONObject("lessonInfo"));
            jsonObject.put("additionalInfo", additionalInfo);

            response = ApiRequest.patch("/contract/" + contractId, jsonObject.toString());
            if (response.statusCode() == 200) {
                msg = "You signed the contract successfully";
                JOptionPane.showMessageDialog(new JFrame(), msg, "Contract Signed Successfully", JOptionPane.INFORMATION_MESSAGE);

                // remove the contract from additionalInfo for tutor after signing
                if (isTutor){
                    patchTutor(contract.getJSONObject("secondParty").getString("id"), contract.getString("id"));

                }
            }



            // if both student and tutor signed, sign the the contract
            if (additionalInfo.getBoolean("studentSigned") && additionalInfo.getBoolean("tutorSigned")) {
                Timestamp ts = Timestamp.from(ZonedDateTime.now().toInstant());
                Instant now = ts.toInstant();
                dateSigned.put("dateSigned", now);

                ApiRequest.post("/contract/" + contractId + "/sign", dateSigned.toString());

                msg = "Contract signed at " + now;
                JOptionPane.showMessageDialog(new JFrame(), msg, "Contract Signed Successfully", JOptionPane.INFORMATION_MESSAGE);

                patchUser(contract.getJSONObject("secondParty").getString("id"), contract.getString("id"), true); // patch the student
            }


        } else {
            msg = "Contract not signed: Error " + response.statusCode();
            JOptionPane.showMessageDialog(new JFrame(), msg, "Bad request", JOptionPane.ERROR_MESSAGE);
        }

        ViewManagerService.loadPage(ViewManagerService.DASHBOARD_PAGE);
    }

    /**
     * Method to removed signed contract from additional Info for tutor
     * @param tutorId tutorId
     * @param contractId the contract Id
     */
    private void patchTutor(String tutorId, String contractId) {

        JSONObject tutor = new JSONObject(ApiRequest.get("/user/" + tutorId).body());

        // loop through active contract of tutor and removed the one that is signed
        for (int i=0; i < tutor.getJSONObject("additionalInfo").getJSONArray("activeContract").length(); i++){
            if (tutor.getJSONObject("additionalInfo").getJSONArray("activeContract").get(i).equals(contractId)){
                tutor.getJSONObject("additionalInfo").getJSONArray("activeContract").remove(i);
                break;
            }
        }
        ApiRequest.put("/user/" + tutorId, tutor.toString());

    }

    private void postContract(JSONObject contract) {

        contract.put("firstPartyId", contract.getString("firstPartyId"));
        contract.put("secondPartyId", contract.getString("secondPartyId"));
        contract.put("lessonInfo", contract.getJSONObject("lessonInfo"));
        contract.put("additionalInfo", contract.getJSONObject("additionalInfo"));

        Timestamp ts = Timestamp.from(ZonedDateTime.now().toInstant());
        Instant now = ts.toInstant();
        contract.put("dateCreated", now);
        LocalDateTime time = LocalDateTime.ofInstant(ts.toInstant(), ZoneOffset.ofHours(0));
        time = time.plus(contract.getJSONObject("lessonInfo").getInt("contractLength"), ChronoUnit.MONTHS); // contract expires after a year
        Instant output = time.atZone(ZoneOffset.ofHours(0)).toInstant();
        Timestamp expiryDate = Timestamp.from(output);
        contract.put("subjectId", contract.getString("subjectId"));
        contract.put("expiryDate", expiryDate);
        contract.put("paymentInfo", new JSONObject());
        HttpResponse<String> contractResponse = ApiRequest.post("/contract", contract.toString());

        if (contractResponse.statusCode() == 201) {

            // after contract signed add them into additionalInfo for tutor so that he can sign later
            // if not cannot get the contractId for this contract
            contract = new JSONObject(contractResponse.body());
            patchUser(contract.getJSONObject("firstParty").getString("id"), contract.getString("id"), false); // patch the tutor
            JOptionPane.showMessageDialog(new JFrame(), "Contract Posted", "Success", JOptionPane.INFORMATION_MESSAGE);

        } else {
            String msg = "Contract not posted: Error " + contractResponse.statusCode();
            JOptionPane.showMessageDialog(new JFrame(), msg, "Bad request", JOptionPane.ERROR_MESSAGE);

        }
    }

    /**
     * Method to add pending contract into tutor additionalInfo to sign later on
     * @param userId id of the tutor
     */
    private void patchUser(String userId, String contractId, boolean isStudent){
        JSONObject user = new JSONObject(ApiRequest.get("/user/" + userId).body());
        JSONObject additionalInfo = user.getJSONObject("additionalInfo");

        // if user has previously pending contract
        if (additionalInfo.has("activeContract")){

            JSONArray activeContract = additionalInfo.getJSONArray("activeContract");

            if (isStudent) {
                if (activeContract.length() == 5){ // only save latest 5 signed contract
                    activeContract.remove(0); // remove the oldest contract
                    activeContract.put(contractId); // add latest contract
                }
            } else { //tutor save as many unsigned contract
                activeContract.put(contractId);

            }
            // update additionalInfo with new active Contract
            additionalInfo.remove("activeContract");
            additionalInfo.put("activeContract", activeContract);

        } else {
            JSONArray activeContract = new JSONArray();
            activeContract.put(contractId);

            // update additionalInfo with new active Contract
            additionalInfo.put("activeContract", activeContract);
        }
        ApiRequest.put("/user/" + userId, user.toString());

    }

}
