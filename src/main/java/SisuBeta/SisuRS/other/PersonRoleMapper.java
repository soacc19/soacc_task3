package SisuBeta.SisuRS.other;

import SisuBeta.SisuRS.classes.PersonRole;

public class PersonRoleMapper {
    public static String personRoleToString(PersonRole personRole) {
        switch (personRole) {
            case STUDENT: return "student";
            case TEACHER: return "teacher";
            default: return "unknown";
        }
        
    }
    
    public static PersonRole stringToPersonRole(String personRoleStr) {
        switch (personRoleStr) {
            case "student": return PersonRole.STUDENT;
            case "teacher": return PersonRole.TEACHER;
            default: return PersonRole.UNKNOWN;
        }
        
    }
}
