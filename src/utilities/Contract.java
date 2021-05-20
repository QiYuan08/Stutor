package utilities;

import abstractions.ContractStrategy;
import org.json.JSONObject;

// TODO: talk about contract class in design rationale (extract methods into utility class)
public class Contract {

    private ContractStrategy strategy;

    public void setStrategy(ContractStrategy strategy) {
        this.strategy = strategy;
    }

    public void postContract(JSONObject contractDetail){
        strategy.postContract(contractDetail);
    }

    public void signContract(JSONObject contractDetail, boolean isTutor) {
        strategy.signContract(contractDetail, isTutor);
    }


}
