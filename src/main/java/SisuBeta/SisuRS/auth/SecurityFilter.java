package SisuBeta.SisuRS.auth;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;

import javax.annotation.Priority;
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
import javax.ws.rs.Priorities;

import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;

import SisuBeta.SisuRS.classes.User;
import SisuBeta.SisuRS.db.DbHandler;
import SisuBeta.SisuRS.errors.ErrorMessage;
import SisuBeta.SisuRS.errors.ErrorMessageToken;
import SisuBeta.SisuRS.token.JWTService;

@Priority(Priorities.AUTHENTICATION)
@Provider
public class SecurityFilter implements ContainerRequestFilter{
    DbHandler dbHandler = new DbHandler();
    
    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final String AUTHORIZATION_HEADER_PREFIX_BASIC = "Basic ";
    private static final String AUTHORIZATION_HEADER_PREFIX_JWT = "Bearer";
    
    private static final ErrorMessage FORBIDDEN_MSG = new ErrorMessage("Access blocked for all users !!!", 403, "http://myDocs.org");
    private static final ErrorMessage UNAUTHORIZED_MSG = new ErrorMessage("User cannot access the resource.", 401, "http://myDocs.org");
    
    private static final String TOKEN_URL = "token";
    
    @Context private ResourceInfo resourceInfo;
    
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        System.out.println("DEBUG: filter" );
        User user = null;
        
        Method resMethod = resourceInfo.getResourceMethod();
        Class<?> resClass = resourceInfo.getResourceClass();
        
        if (resMethod.isAnnotationPresent(PermitAll.class)) {return;}
        if (resMethod.isAnnotationPresent(DenyAll.class)) {
            Response response = Response.status(Status.FORBIDDEN)
                    .entity(FORBIDDEN_MSG).build();
            requestContext.abortWith(response);
            return;
        }
       
        // BASIC AUTH for token resource
        if ((requestContext.getUriInfo().getPath().equals(TOKEN_URL))) {
            List<String> authHeader = requestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);
          
            if (authHeader != null && authHeader.size() > 0) {
                String authToken = authHeader.get(0);
                authToken = authToken.replaceFirst(AUTHORIZATION_HEADER_PREFIX_BASIC, "");
                String decodedString = new String(Base64.getDecoder().decode(authToken.getBytes()));
                StringTokenizer tokenizer = new StringTokenizer(decodedString, ":");
                String username = tokenizer.nextToken();
                String password = tokenizer.nextToken();
              
                List<User> usersWithMatchedUsername = new ArrayList<User>();
                usersWithMatchedUsername = dbHandler.selectUsers(username, false); // should return just 1 user, since username is also unique column
              
                // user not found
                if (usersWithMatchedUsername.isEmpty()) {
                    ErrorMessage errorMessage = new ErrorMessage("User with provided username not found!",
                            Status.UNAUTHORIZED.getStatusCode(), "http://myDocs.org");
                    Response unauthorizedStatus = Response.status(Response.Status.UNAUTHORIZED).entity(errorMessage).build();
                    requestContext.abortWith(unauthorizedStatus);
                    return;
                }
                user = usersWithMatchedUsername.get(0);
              
                // wrong password
                if (!user.getPassword().equals(password)) {
                    ErrorMessage errorMessage = new ErrorMessage("Wrong password provided!.",
                            Status.UNAUTHORIZED.getStatusCode(), "http://myDocs.org");
                    Response unauthorizedStatus = Response.status(Response.Status.UNAUTHORIZED).entity(errorMessage).build();
                    requestContext.abortWith(unauthorizedStatus);
                    return;
                }
                
                // OK - authorization verified
                requestContext.setProperty("username", username);
                //requestContext.setProperty("userId", (Long)user.getId());
                System.out.println("DEBUG: filter good basic auth" );
                return;
            }
            // not provided credentials
            else {
                ErrorMessage errorMessage = new ErrorMessage("Missing credentials in authorization header!",
                        Status.UNAUTHORIZED.getStatusCode(), "http://myDocs.org");
                Response unauthorizedStatus = Response.status(Response.Status.UNAUTHORIZED).entity(errorMessage).build();
                requestContext.abortWith(unauthorizedStatus);
                return;
            }
        }
        // PROTECTED RESOURCES
        else { 
            String authHeaderVal = requestContext.getHeaderString(AUTHORIZATION_HEADER_KEY);
            String usernameInToken = null;
            
            if (authHeaderVal == null || authHeaderVal.length() == 0) {
                ErrorMessage errorMessage = new ErrorMessage("Missing authorization header!",
                        Status.UNAUTHORIZED.getStatusCode(), "http://localhost:8080/SisuRS/webapi/token");
                Response unauthorizedStatus = Response.status(Response.Status.UNAUTHORIZED).entity(errorMessage).build();
                requestContext.setProperty("auth-failed", true);
                requestContext.abortWith(unauthorizedStatus);
                return;
            }
            
            //consume JWT i.e. execute signature validation
            if(authHeaderVal.startsWith(AUTHORIZATION_HEADER_PREFIX_JWT)) {
                String jwt = authHeaderVal.split(" ")[1];
                System.out.println("DEBUG: jwt: " + jwt);
                try {
                    usernameInToken = JWTService.verifyToken(jwt);
                }
                catch (InvalidJwtException | MalformedClaimException e) {
                    ErrorMessageToken errorMessage = new ErrorMessageToken("Token verification failed!.",
                            Status.UNAUTHORIZED.getStatusCode(), "http://localhost:8080/SisuRS/webapi/token", e.getMessage());
                    Response unauthorizedStatus = Response.status(Response.Status.UNAUTHORIZED).entity(errorMessage).build();
                    requestContext.setProperty("auth-failed", true);
                    requestContext.abortWith(unauthorizedStatus);
                    return;
                }
            }
            else {
                ErrorMessage errorMessage = new ErrorMessage("Missing token in authorization header!",
                        Status.UNAUTHORIZED.getStatusCode(), "http://localhost:8080/SisuRS/webapi/token");
                Response unauthorizedStatus = Response.status(Response.Status.UNAUTHORIZED).entity(errorMessage).build();
                requestContext.setProperty("auth-failed", true);
                requestContext.abortWith(unauthorizedStatus);
                return;
            }
            
            List<User> usersWithMatchedUsername = new ArrayList<User>();
            usersWithMatchedUsername = dbHandler.selectUsers(usernameInToken, false); // should return just 1 user, since username is also unique column
            System.out.println("DEBUG: usernameInToken: " + usernameInToken);
            
            // user was removed from db meanwhile token was exchanged
            if (usersWithMatchedUsername.isEmpty()) {
                ErrorMessage errorMessage = new ErrorMessage("Invalid token! Provided subject (user) is no more valid!",
                        Status.UNAUTHORIZED.getStatusCode(), "http://localhost:8080/SisuRS/webapi/token");
                Response unauthorizedStatus = Response.status(Response.Status.UNAUTHORIZED).entity(errorMessage).build();
                requestContext.setProperty("auth-failed", true);
                requestContext.abortWith(unauthorizedStatus);
                return;
            }
            
            user = usersWithMatchedUsername.get(0);
            String scheme = requestContext.getUriInfo().getRequestUri().getScheme();
            requestContext.setSecurityContext(new CustomSecurityContext(user, scheme));
            
            if (resMethod.isAnnotationPresent(RolesAllowed.class)) {
                
                // Get the allowed roles from the annotation
                List<String> rolesAllowed = Arrays.asList(resMethod.getAnnotation(RolesAllowed.class).value()); 

                // Check if the user's rights are ok
                if (rolesMatched(user, rolesAllowed)) {
                    return;
                }
                
                Response response = Response.status(Status.UNAUTHORIZED)
                                    .entity(UNAUTHORIZED_MSG).build();
                requestContext.abortWith(response);
                return;
            }
            
            
            // Class level annotation checks 
            if (resClass.isAnnotationPresent(PermitAll.class)) {
                return;
            }
            if (resClass.isAnnotationPresent(DenyAll.class)) {
                Response response = Response.status(Status.FORBIDDEN)
                        .entity(FORBIDDEN_MSG).build();
                requestContext.abortWith(response);
                return;
            }
            
            if (resClass.isAnnotationPresent(RolesAllowed.class)) {
                
                // Get the allowed roles from the annotation
                List<String> rolesAllowed = Arrays.asList(resClass.getAnnotation(RolesAllowed.class).value()); 

                // Check if the user's rights are ok
                if (rolesMatched(user, rolesAllowed)) {
                    return;
                }
                Response response = Response.status(Status.UNAUTHORIZED)
                                    .entity(UNAUTHORIZED_MSG).build();
                requestContext.abortWith(response);
                return;
            }
        }
    }
    
    private boolean rolesMatched(User user, List<String> rolesAllowed) {
        return rolesAllowed.contains(user.getRole()); 
    }
}