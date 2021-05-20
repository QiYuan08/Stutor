package abstractions;

import org.json.JSONArray;
import org.json.JSONObject;
import services.ApiRequest;

public interface ContractStrategy {

    /**
     * Method to post a contract between student and tutor
     * @param contractDetail JSONObject containing all the detail of the contract
     */
    void postContract(JSONObject contractDetail);

    /**
     * Method to sign a contract between student and tutor
     * @param contractDetail Id of the contract
     * @param isTutor true if the one signing is tutor and false otherwise
     */
    void signContract(JSONObject contractDetail, boolean isTutor);

    /**
     * Method to add pending contract into tutor/student additionalInfo to sign later on
     * @param userId id of the tutor/student
     * @param contractId id of the contract
     * @param isStudent true if user is student and false otherwise
     */
    default void patchUser(String userId, String contractId, boolean isStudent) {
        JSONObject user = new JSONObject(ApiRequest.get("/user/" + userId).body());
        JSONObject additionalInfo = user.getJSONObject("additionalInfo");

        // if user has previously pending/signed contract
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
