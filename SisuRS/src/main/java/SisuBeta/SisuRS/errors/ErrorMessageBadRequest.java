package SisuBeta.SisuRS.errors;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorMessageBadRequest extends ErrorMessage {
    private String badInputName;
    private String badInputValue;
    
    public ErrorMessageBadRequest() {}
    public ErrorMessageBadRequest(String errorMessage, int errorCode, String documentation, String badInputName, String badInputValue) {
        super(errorMessage, errorCode, documentation);
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
