package unused;

import org.json.JSONArray;
import org.json.JSONObject;

public class FilterTutor {

//    public Boolean isQualified(JSONObject user) {
//
//        if (user.getBoolean("isTutor")){
//
//            // add every bid that is qualified to be teached by this user to bids
//            for (int i = returnedBids.length() - 1; i > -1; i--){
//
//                JSONObject bid = returnedBids.getJSONObject(i);
//
//                // if the bid still open
//                if (bid.isNull("dateClosedDown")) {
//                    // for some bids that doesn't have min competency TODO: don't all bids have competency?
//                    if (!bid.getJSONObject("additionalInfo").has("minCompetency")) {
//                        bids.put(bid);
//
//                    } else { // if that bid has competency
//                        // check this subject with every competency of this user
//                        JSONArray userCompetencies = user.getJSONArray("competencies");
//                        for (int j = 0; j < userCompetencies.length(); j++){
//
//                            // current competency
//                            JSONObject competency = userCompetencies.getJSONObject(j);
//
//                            // if user know this subject
//                            if (competency.getJSONObject("subject").get("id").equals(bid.getJSONObject("subject").get("id"))) {
//
//                                // compare the competency level
//                                if (competency.getInt("level") >= (bid.getJSONObject("additionalInfo").getInt("minCompetency"))) {
//                                    bids.put(bid);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

}
