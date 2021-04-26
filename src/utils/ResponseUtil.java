package utils;
import org.json.JSONObject;

/***
 * Base class for tutor to create response/bid for a student request
 */
public interface ResponseUtil {

    /***
     * Function to update an bid with respones from a tutor
     * @param bidId the id of the bid to update
     * @param msgBody the information/data to be updated into the bid
     */
    void updateBid(String bidId, JSONObject msgBody);
}
