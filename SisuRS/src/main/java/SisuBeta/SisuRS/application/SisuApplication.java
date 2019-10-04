package SisuBeta.SisuRS.application;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import SisuBeta.SisuRS.auth.SecurityFilter;

@ApplicationPath("webapi")
public class SisuApplication extends ResourceConfig {

    public SisuApplication() {
       
        register(SecurityFilter.class);
    }
    
    
    
}
