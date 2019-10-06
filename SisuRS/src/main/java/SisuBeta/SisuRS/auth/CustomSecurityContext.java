package SisuBeta.SisuRS.auth;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import SisuBeta.SisuRS.classes.User;

public class CustomSecurityContext implements SecurityContext {
    private User user;
    private String scheme;

    public CustomSecurityContext(User user, String scheme) {
        super();
        this.user = user;
        this.scheme = scheme;
    }


    @Override
    public Principal getUserPrincipal() {
        return this.user;
    }


    @Override
    public boolean isUserInRole(String role) {
        return this.user.getRoles().contains(role);
    }


    @Override
    public boolean isSecure() {
        return "https".equals(this.scheme);
    }


    @Override
    public String getAuthenticationScheme() {
        return SecurityContext.BASIC_AUTH;
    }

}
