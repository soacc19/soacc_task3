package SisuBeta.SisuRS.errors;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorMessageDbQuery extends ErrorMessage {
    private String query;
    private String sqlErrorMessage;
    
    public ErrorMessageDbQuery() {}
    public ErrorMessageDbQuery(String errorMessage, int errorCode, String documentation, String sqlErrorMessage, String query) {
        super(errorMessage, errorCode, documentation);
        this.query = query;
        this.sqlErrorMessage = sqlErrorMessage;
    }
    
    public String getQuery() {
        return query;
    }
    public void setQuery(String query) {
        this.query = query;
    }
    
    public String getSqlErrorMessage() {
        return sqlErrorMessage;
    }
    
    public void setSqlErrorMessage(String sqlErrorMessage) {
        this.sqlErrorMessage = sqlErrorMessage;
    }
}