package SisuBeta.SisuRS.exceptions;

public class InternalException extends RuntimeException{

    /**
     * 
     */
    private static final long serialVersionUID = 5243975691124376146L;
    private String additionalMessage;
    
    public InternalException(String message, String additionalMessage) {
        super(message);
        this.additionalMessage = additionalMessage;
    }

    public String getAdditionalMessage() {
        return additionalMessage;
    }

    public void setAdditionalMessage(String additionalMessage) {
        this.additionalMessage = additionalMessage;
    }

}
