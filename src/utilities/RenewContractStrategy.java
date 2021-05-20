package utilities;

import abstractions.ContractStrategy;
import org.json.JSONObject;
import services.ApiRequest;
import services.ViewManagerService;

import javax.swing.*;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class RenewContractStrategy implements ContractStrategy {

    @Override
    public void postContract(JSONObject contractDetail) {
        contractDetail.put("firstPartyId", contractDetail.getString("firstPartyId"));
        contractDetail.put("secondPartyId", contractDetail.getString("secondPartyId"));
        contractDetail.put("lessonInfo", contractDetail.getJSONObject("lessonInfo"));
        contractDetail.put("additionalInfo", contractDetail.getJSONObject("additionalInfo"));

        Timestamp ts = Timestamp.from(ZonedDateTime.now().toInstant());
        Instant now = ts.toInstant();
        contractDetail.put("dateCreated", now);
        LocalDateTime time = LocalDateTime.ofInstant(ts.toInstant(), ZoneOffset.ofHours(0));
        time = time.plus(contractDetail.getJSONObject("lessonInfo").getInt("contractLength"), ChronoUnit.MONTHS); // contract expires after a year
        Instant output = time.atZone(ZoneOffset.ofHours(0)).toInstant();
        Timestamp expiryDate = Timestamp.from(output);
        contractDetail.put("subjectId", contractDetail.getString("subjectId"));
        contractDetail.put("expiryDate", expiryDate);
        contractDetail.put("paymentInfo", new JSONObject());
        HttpResponse<String> contractResponse = ApiRequest.post("/contract", contractDetail.toString());

        if (contractResponse.statusCode() == 201) {

            // after contract signed add them into additionalInfo for tutor so that he can sign later
            // if not cannot get the contractId for this contract
            contractDetail = new JSONObject(contractResponse.body());
            patchUser(contractDetail.getJSONObject("firstParty").getString("id"), contractDetail.getString("id"), false); // patch the tutor
            JOptionPane.showMessageDialog(new JFrame(), "Contract Posted", "Success", JOptionPane.INFORMATION_MESSAGE);

        } else {
            String msg = "Contract not posted: Error " + contractResponse.statusCode();
            JOptionPane.showMessageDialog(new JFrame(), msg, "Bad request", JOptionPane.ERROR_MESSAGE);

        }
    }

    @Override
    public void signContract(JSONObject contractDetail, boolean isTutor) {

        JSONObject dateSigned = new JSONObject();
        HttpResponse<String> response = ApiRequest.get("/contract/" + contractDetail.getString("id"));
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

            response = ApiRequest.patch("/contract/" + contract, jsonObject.toString());
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

                ApiRequest.post("/contract/" + contract + "/sign", dateSigned.toString());

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
}
