package SisuBeta.SisuRS.classes;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Person {
   private long id; 
   private String name; 
   private String faculty;
   private String email;
   private String role;
   private List<Link> links= new ArrayList<>(); // HATEOAS
   
   public Person(){} 
    
   public Person(long id, String name, String faculty, String email, String role) {
      this.id = id; 
      this.name = name; 
      this.faculty = faculty; 
      this.email = email;
      this.role = role;
   }
   
   public long getId() { 
      return id; 
   }  

   public void setId(long id) { 
      this.id = id; 
   } 
   public String getName() { 
      return name; 
   } 
   public void setName(String name) { 
      this.name = name; 
   } 
   public String getFaculty() { 
      return faculty; 
   } 
   public void setFaculty(String faculty) { 
      this.faculty = faculty; 
   }   
   public String getEmail() {
       return email;
   }
   public void setEmail(String email) {
       this.email = email;
   }
   public String getRole() {
       return role;
   }
   public void setRole(String role) {
       this.role = role;
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
