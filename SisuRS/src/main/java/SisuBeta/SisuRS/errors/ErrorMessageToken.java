package SisuBeta.SisuRS.errors;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorMessageToken extends ErrorMessage{
    private String jwtErrorMessage;
    
    public ErrorMessageToken() {}
    public ErrorMessageToken(String errorMessage, int errorCode, String documentation, String jwtErrorMessage) {
        super(errorMessage, errorCode, documentation);
        this.jwtErrorMessage = jwtErrorMessage;
    }
    
    public String getJwtErrorMessage() {
        return jwtErrorMessage;
    }
    public void setJwtErrorMessage(String jwtErrorMessage) {
        this.jwtErrorMessage = jwtErrorMessage;
    }
}
