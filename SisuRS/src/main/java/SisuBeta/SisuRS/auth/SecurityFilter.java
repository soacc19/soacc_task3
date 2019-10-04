package SisuBeta.SisuRS.auth;

import java.io.IOException;

import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;


import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import SisuBeta.SisuRS.errors.ErrorMessage;

/**
 * Filter for handling authentication and authorization
 */
public class SecurityFilter implements ContainerRequestFilter {
    private static final String AUTH_HEADER_KEY = "Authorization";
    private static final String AUTH_HEADER_PREFIX = "Basic ";
    private static final String SECURED_URL_PREFIX = "secured";


    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
 
       
          if ((requestContext.getUriInfo().getPath().contains(SECURED_URL_PREFIX))
                || (requestContext.getMethod().equals("DELETE"))) {
            
           List<String> authHeader = requestContext.getHeaders().get(AUTH_HEADER_KEY);
            if (authHeader != null && authHeader.size() > 0) {
                String authToken = authHeader.get(0);
                authToken = authToken.replaceFirst(AUTH_HEADER_PREFIX, "");
                String decodedString = new String(Base64.getUrlDecoder().decode(authToken.getBytes()));
                StringTokenizer tokenizer = new StringTokenizer(decodedString, ":");
                String username = tokenizer.nextToken();
                String password = tokenizer.nextToken();
                if ("user".equals(username) && "password".equals(password)) {
                    return;
                }
            }
                

                ErrorMessage errorMsg = new ErrorMessage("Authentication failed", 401, "http://myDocs.org");
                Response unauthorized = Response.status(Status.UNAUTHORIZED)
                        .entity(errorMsg)
                        .build();
                
                requestContext.abortWith(unauthorized);

           }
        

    }

}
