package SisuBeta.SisuRS.other;

import SisuBeta.SisuRS.classes.UserRole;

public class UserRoleMapper {
    public static String userRoleToString(UserRole userRole) {
        switch (userRole) {
            case ADMIN: return "admin";
            case FACULTY: return "faculty";
            case STUDENT: return "student";
            default: return "unknown";
        }
        
    }
    
    public static UserRole stringToUserRole(String userRoleStr) {
        switch (userRoleStr) {
            case "admin": return UserRole.ADMIN;
            case "faculty": return UserRole.FACULTY;
            case "student": return UserRole.STUDENT;
            default: return UserRole.UNKNOWN;
        }
        
    }

}
