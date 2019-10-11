package SisuBeta.SisuRS.exceptions;


/*
 * Example of usage:
 *      if (sampleJson.getInteger() > 100) {
            throw new BadInputException("Value of parameter 'integer' cannot be higher than 100!",
                                        "integer",
                                        Integer.toString(sampleJson.getInteger()));
        }
        
        Input JSON:
        {
            "str1": "String number one",
            "str2": "String number two",
            "integer": "110",
            "dbl": 11.5
        }

        Output error JSON:
        {
            "documentation": "http://myDocs.org",
            "errorCode": 400,
            "errorMessage": "Value of parameter 'integer' cannot be higher than 100!",
            "badInputName": "integer",
            "badInputValue": "110"
        }
 */
public class BadInputException extends RuntimeException {

    private static final long serialVersionUID = -6847412882540642075L;
    private String badInputName;
    private String badInputValue;
    
    public BadInputException(String message, String badInputName, String badInputValue) {
        super(message);
        this.badInputName = badInputName;
        this.badInputValue = badInputValue;
    }

    public String getBadInputName() {
        return badInputName;
    }
    
    public void setBadInputName(String badInputName) {
        this.badInputName = badInputName;
    }
    
    public String getBadInputValue() {
        return badInputValue;
    }
    
    public void setBadInputValue(String badInputValue) {
        this.badInputValue = badInputValue;
    }

}
