package SisuBeta.SisuRS.classes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

public class User implements Principal {

    private String username;
    private String password;
    private List<String> roles = new ArrayList<String>();


    User() {
    }


    public User(String username, String password, List<String> roles) {
        super();
        this.username = username;
        this.password = password;
        this.roles = roles;
    }


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public List<String> getRoles() {
        return roles;
    }


    public void setRoles(List<String> roles) {
        this.roles = roles;
    }


    @Override
    public String getName() {
        return this.username;
    }

}