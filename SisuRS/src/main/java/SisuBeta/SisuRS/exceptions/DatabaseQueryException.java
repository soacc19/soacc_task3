package SisuBeta.SisuRS.exceptions;

public class DatabaseQueryException extends RuntimeException {

    private static final long serialVersionUID = -5628927865589351644L;
    private String sqlErrorMessage;
    private String query;
    
    
    public DatabaseQueryException(String message, String sqlErrorMessage, String query) {
        super(message);
        this.sqlErrorMessage = sqlErrorMessage;
        this.query = query;
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
