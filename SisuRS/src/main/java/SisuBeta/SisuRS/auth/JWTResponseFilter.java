package SisuBeta.SisuRS.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class JWTResponseFilter implements ContainerResponseFilter {
    private static final String AUTHORIZATION_HEADER_PREFIX_JWT = "Bearer";
    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        String authHeaderVal = requestContext.getHeaderString(AUTHORIZATION_HEADER_KEY);
        
        System.out.println("DEBUG: response filter invoked");
        if (requestContext.getProperty("auth-failed") != null) {
            Boolean failed = (Boolean) requestContext.getProperty("auth-failed");
            if (failed) {
                System.out.println("DEBUG: JWT auth failed. No need to return JWT token");
                return;
            }
        }

        if(authHeaderVal.startsWith(AUTHORIZATION_HEADER_PREFIX_JWT)) {
            List<Object> jwt = new ArrayList<Object>();
            jwt.add(authHeaderVal.split(" ")[1]);
            responseContext.getHeaders().put("jwt", jwt);
            System.out.println("DEBUG: Added JWT to response header 'jwt': " + jwt);
        }
    }
}