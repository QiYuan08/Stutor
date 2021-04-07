import java.util.Date;

public class Bid {

//    private String id;
//    private String type; /////// not sure, maybe enum?
    private User initiator; // maybe private StudentInterface?
    private Date dateCreated; /////// to find suitable class for datetime formats
    private Date dateClosedDown; /////// to find suitable class for datetime formats
    private Subject subject;
    private String additionalInfo;
}
