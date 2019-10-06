package SisuBeta.SisuRS.exceptions;

public class UnauthorizedException extends RuntimeException {

    private static final long serialVersionUID = 6923754743342657233L;
    
    public UnauthorizedException(String message) {
        super(message);
    }

}
