package SisuBeta.SisuRS.other;

import SisuBeta.SisuRS.classes.UserRole;

public class UserRoleMapper {
    public static String userRoleToString(UserRole userRole) {
        switch (userRole) {
            case ADMIN: return "admin";
            case USER: return "user";
            default: return "unknown";
        }
        
    }
    
    public static UserRole stringToUserRole(String userRoleStr) {
        switch (userRoleStr) {
            case "admin": return UserRole.ADMIN;
            case "user": return UserRole.USER;
            default: return UserRole.UNKNOWN;
        }
        
    }

}
