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

public class CloseBidStrategy implements ContractStrategy {

    @Override
    public void postContract(JSONObject contractDetail) {
        String tutorId = contractDetail.getString("tutorId");
        String userId = contractDetail.getString("userId");
        String messageId = contractDetail.getString("messageId");

        JSONObject contract = new JSONObject();
        if (tutorId.equals("")) { // buyout action (there could be no responses for the bid when the tutor buys it out)
            contract.put("firstPartyId", userId);
            contract.put("secondPartyId", contractDetail.getJSONObject("initiator").getString("id"));
            contract.put("lessonInfo", contractDetail.getJSONObject("additionalInfo"));
        } else { // a confirm bid action from the user or ExpireBidService chooses the last tutor as the winner (has response)
            contract.put("firstPartyId", tutorId);
            contract.put("secondPartyId", contractDetail.getJSONObject("initiator").getString("id"));
            JSONObject message = new JSONObject(ApiRequest.get("/message/" + messageId).body());
            contract.put("lessonInfo", message.getJSONObject("additionalInfo"));
        }
        Timestamp ts = Timestamp.from(ZonedDateTime.now().toInstant());
        Instant now = ts.toInstant();
        contract.put("dateCreated", now);
        LocalDateTime time = LocalDateTime.ofInstant(ts.toInstant(), ZoneOffset.ofHours(0));
        time = time.plus(1, ChronoUnit.YEARS); // contract expires after a year
        Instant output = time.atZone(ZoneOffset.ofHours(0)).toInstant();
        Timestamp expiryDate = Timestamp.from(output);
        contract.put("subjectId", contractDetail.getJSONObject("subject").getString("id"));
        contract.put("expiryDate", expiryDate);
        contract.put("paymentInfo", new JSONObject());
        contract.put("additionalInfo", new JSONObject());
        HttpResponse<String> contractResponse = ApiRequest.post("/contract", contract.toString());

        if (contractResponse.statusCode() == 201) {
            contract = new JSONObject(contractResponse.body());
            patchUser(contract.getJSONObject("secondParty").getString("id"), contract.getString("id"), true);
            contract.put("tutorId", tutorId);
            signContract(contract, false);
        } else {
            String msg = "Contract not posted: Error " + contractResponse.statusCode();
            JOptionPane.showMessageDialog(new JFrame(), msg, "Bad request", JOptionPane.ERROR_MESSAGE);
        }
    }

    // isTutor is not used in this strategy but I need it in renew contract to determine who is signing the contract
    // having extra parameter is better than duplicated code
    @Override
    public void signContract(JSONObject contractDetail, boolean isTutor) {
        JSONObject dateSigned = new JSONObject();
        Timestamp ts = Timestamp.from(ZonedDateTime.now().toInstant());
        Instant now = ts.toInstant();
        dateSigned.put("dateSigned", now);
        HttpResponse<String> contractSignResponse = ApiRequest.post("/contract/" + contractDetail.getString("id") + "/sign", dateSigned.toString());
        String msg;
        String tutorId = contractDetail.getString("tutorId");

        if (contractSignResponse.statusCode() == 200) {
            msg = "Bid closed successfully and contract created at " + now;
            JOptionPane.showMessageDialog(new JFrame(), msg, "Bid Closed Successfully", JOptionPane.INFORMATION_MESSAGE);
            if (tutorId.equals("")) { // what is this ah?
                ViewManagerService.loadPage(ViewManagerService.DASHBOARD_PAGE);
            }
        } else {
            msg = "Contract not signed: Error " + contractSignResponse.statusCode();
            JOptionPane.showMessageDialog(new JFrame(), msg, "Bad request", JOptionPane.ERROR_MESSAGE);
        }
    }
}
