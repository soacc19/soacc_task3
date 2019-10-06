package SisuBeta.SisuRS.auth;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import SisuBeta.SisuRS.errors.ErrorMessage;

/**
 * Filter for handling authentication and authorization
 */
@Provider
public class SecurityFilter implements ContainerRequestFilter {
    private static final String AUTH_HEADER_KEY = "Authorization";
    private static final String AUTH_HEADER_PREFIX = "Basic ";
    private static final String SECURED_URL_PREFIX = "secured";

    private static final ErrorMessage FORBIDDEN_MSG = new ErrorMessage("Access blocked for all users !!!", 403, "http://myDocs.org");
    private static final ErrorMessage UNAUTHORIZED_MSG = new ErrorMessage("User cannot access the resource.", 401, "http://myDocs.org");
   
    @Context private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
 
        Method resMethod = resourceInfo.getResourceMethod();
        Class<?> resClass = resourceInfo.getResourceClass();
        
        if (resMethod.isAnnotationPresent(PermitAll.class)) {return;}
        if (resMethod.isAnnotationPresent(DenyAll.class)) {
            Response response = Response.status(Status.FORBIDDEN)
                    .entity(FORBIDDEN_MSG).build();
            requestContext.abortWith(response);
        }
        
       
        //TODO: Check user's role, figure out if class-level auth checks are needed
        
           List<String> authHeader = requestContext.getHeaders().get(AUTH_HEADER_KEY);
            if (authHeader != null && authHeader.size() > 0) {
                String authToken = authHeader.get(0);
                authToken = authToken.replaceFirst(AUTH_HEADER_PREFIX, "");
                String decodedString = new String(Base64.getUrlDecoder().decode(authToken.getBytes()));
                StringTokenizer tokenizer = new StringTokenizer(decodedString, ":");
                String username = tokenizer.nextToken();
                String password = tokenizer.nextToken();
                
                if (resMethod.isAnnotationPresent(RolesAllowed.class)) {
                    
                    // Get the allowed roles from the annotation
                    List<String> rolesAllowed = Arrays.asList(resMethod.getAnnotation(RolesAllowed.class).value()); 

                    // Check if the user's rights are ok
                    if (rolesMatched(username, password, rolesAllowed)) {return;}
                    Response response = Response.status(Status.UNAUTHORIZED)
                                        .entity(UNAUTHORIZED_MSG).build();
                    requestContext.abortWith(response);
                }
                
                // Class level annotation checks 
                
                if (resClass.isAnnotationPresent(PermitAll.class)) {return;}
                if (resClass.isAnnotationPresent(DenyAll.class)) {
                    Response response = Response.status(Status.FORBIDDEN)
                            .entity(FORBIDDEN_MSG).build();
                    requestContext.abortWith(response);
                }
                
                
                if (resClass.isAnnotationPresent(RolesAllowed.class)) {
                    
                    // Get the allowed roles from the annotation
                    List<String> rolesAllowed = Arrays.asList(resClass.getAnnotation(RolesAllowed.class).value()); 

                    // Check if the user's rights are ok
                    if (rolesMatched(username, password, rolesAllowed)) {return;}
                    Response response = Response.status(Status.UNAUTHORIZED)
                                        .entity(UNAUTHORIZED_MSG).build();
                    requestContext.abortWith(response);
                }
                
                
            }
                    
        
          ErrorMessage errorMsg = new ErrorMessage("Authentication failed", 401,"http://myDocs.org"); 
          Response unauthorized = Response.status(Status.UNAUTHORIZED) 
                                          .entity(errorMsg) 
                                          .build();
          
          requestContext.abortWith(unauthorized);
         
           
        

    }

    
    
    private boolean rolesMatched(String username, String password, List<String> roles) {
        
        boolean roleMatches = false;
        //TODO: here get the usrnm and psswd from the db
        //String username = "";
        //String password = "";
        
        if ("user".equals(username) && "password".equals(password)) {
        
            //TODO: here get the actual role from db
            String userRole = "admin";
            
            if (roles.contains(userRole)) {
                roleMatches = true;
            }
            
        }
        
        return roleMatches;
    }
    
    
}
