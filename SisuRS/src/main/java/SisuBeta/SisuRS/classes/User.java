package SisuBeta.SisuRS.classes;

import javax.xml.bind.annotation.XmlRootElement;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class User implements Principal {
    private long id;
    private String username;
    private String password;
    private String role;
    private List<Link> links= new ArrayList<>(); // HATEOAS
    
    public User() {}
    
    public User(long id, String username, String password, String role) {
        super();
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String getName() {
        return this.username;
    }
    
    public List<Link> getLinks() {
        return links;
    }
    
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public void addLink(String url, String rel) {
        Link link = new Link();
        link.setHref(url);
        link.setRel(rel);
        links.add(link);
    }

}
