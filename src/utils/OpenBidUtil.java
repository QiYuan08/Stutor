package utils;

import api.ApiRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;

public class OpenBidUtil {

    /**
     * Method to get all the subject name in database
     * @return An hashmap of all the subject in database using key:data of subjectName:subjectId
     */
    public HashMap<String, String> getAllSubject(){
        HttpResponse<String> response = ApiRequest.get("/subject");
        HashMap<String, String> subjects = new HashMap<String, String>();

        if (response.statusCode() == 200){
            JSONArray responseBody = new JSONArray(response.body());
            for (int i=0; i < responseBody.length(); i++){
                JSONObject subject = responseBody.getJSONObject(i);
                subjects.put(subject.get("name").toString(), subject.get("id").toString());
            }

        } else {
            String msg = "Error: " + new JSONObject(response.body()).get("message");
            JOptionPane.showMessageDialog(new JFrame(), msg, "Bad request", JOptionPane.ERROR_MESSAGE);
        }

        return subjects;
    }
}
