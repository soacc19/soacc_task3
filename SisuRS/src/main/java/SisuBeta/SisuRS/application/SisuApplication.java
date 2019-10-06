package SisuBeta.SisuRS.application;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import SisuBeta.SisuRS.auth.SecurityFilter;

@ApplicationPath("webapi")
public class SisuApplication extends ResourceConfig {

    public SisuApplication() {
        register(RolesAllowedDynamicFeature.class);
        register(SecurityFilter.class);
    }
    
    
    
}
