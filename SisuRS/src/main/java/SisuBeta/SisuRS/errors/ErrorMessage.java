package SisuBeta.SisuRS.errors;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorMessage {

	private String errorMessage;
	private int errorCode;          //own custom error code
	private String documentation;   //link to documentation regarding a error and it’s resolving
	
	public ErrorMessage() {}
	public ErrorMessage(String errorMessage, int errorCode, String documentation) {
	    //super(); // really needed? no ancestor
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
		this.documentation = documentation;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	
	public String getDocumentation() {
		return documentation;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}
}
