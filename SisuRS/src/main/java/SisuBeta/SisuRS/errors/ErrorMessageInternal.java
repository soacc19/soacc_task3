package SisuBeta.SisuRS.errors;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorMessageInternal extends ErrorMessage {
    private String additionalMessage;
    
    public ErrorMessageInternal() {}
    public ErrorMessageInternal(String errorMessage, int errorCode, String documentation, String additionalMessage) {
        super(errorMessage, errorCode, documentation);
        this.additionalMessage = additionalMessage;
    }
    
    public String getAdditionalMessage() {
        return additionalMessage;
    }
    public void setAdditionalMessage(String additionalMessage) {
        this.additionalMessage = additionalMessage;
    }
}
