package SisuBeta.SisuRS.other;

import SisuBeta.SisuRS.classes.Building;

public class BuildingMapper {
    public static String buildingToString(Building building) {
        switch (building) {
            case A: return "A";
            case B: return "B";
            case C: return "C";
            case D: return "D";
            case E: return "E";
            case F: return "F";
            case G: return "G";
            case H: return "H";
            case I: return "I";
            case J: return "J";
            case K: return "K";
            case L: return "L";
            case M: return "M";
            case N: return "N";
            case O: return "O";
            case P: return "P";
            case Q: return "Q";
            case R: return "R";
            case S: return "S";
            case T: return "T";
            case U: return "U";
            case V: return "V";
            case W: return "W";
            case X: return "X";
            default: return "unknown";
        }
        
    }
    
    public static Building stringToBuilding(String buildingStr) {
        switch (buildingStr) {
            case "A": return Building.A;
            case "B": return Building.B;
            case "C": return Building.C;
            case "D": return Building.D;
            case "E": return Building.E;
            case "F": return Building.F;
            case "G": return Building.G;
            case "H": return Building.H;
            case "I": return Building.I;
            case "J": return Building.J;
            case "K": return Building.K;
            case "L": return Building.L;
            case "M": return Building.M;
            case "N": return Building.N;
            case "O": return Building.O;
            case "P": return Building.P;
            case "Q": return Building.Q;
            case "R": return Building.R;
            case "S": return Building.S;
            case "T": return Building.T;
            case "U": return Building.U;
            case "V": return Building.V;
            case "W": return Building.W;
            case "X": return Building.X;
            default: return Building.UNKNOWN;
        }
        
    }
}
