package interfaces;

import org.json.JSONObject;

import java.awt.event.ActionListener;

public interface InputInterface {

    public JSONObject retrieveInputs();

    public void setListener(ActionListener listener);
}
