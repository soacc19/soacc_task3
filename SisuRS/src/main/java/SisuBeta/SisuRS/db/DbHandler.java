
package SisuBeta.SisuRS.db;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import java.util.ArrayList;

import SisuBeta.SisuRS.classes.Course;
import SisuBeta.SisuRS.classes.Person;
import SisuBeta.SisuRS.classes.Reservation;
import SisuBeta.SisuRS.classes.Room;
import SisuBeta.SisuRS.classes.User;
import SisuBeta.SisuRS.exceptions.DatabaseQueryException;

public class DbHandler {
    private static final String DB_FILENAME = "sisudb.sqlite";
    private static final String JDBC_PREFIX = "jdbc:sqlite:";
    
    private Connection conn;
    private boolean connected = false;
    
    public DbHandler() {
    }
    
    
    public Connection connect() {
        try {
            Class.forName("org.sqlite.JDBC");
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        
        Connection conn = null;
        try {
            String path;
            // DriverManager.getConnection not working well (doesn't raise exception even if db file is not found)
            if ((path = getDbAbsolutePath()) == null) {
                throw new DatabaseQueryException("Database file not found!", "", "");
            }
            System.out.println("DEBUG: DB file found. path=" + path);
            
            // create a connection to the database
            conn = DriverManager.getConnection(JDBC_PREFIX + path);
            System.out.println("DEBUG: Connection to SQLite has been established.");
            
        } 
        catch (SQLException e) {
            throw new DatabaseQueryException("Failed to connect to database!", e.getMessage(), "");
        } 
        
        this.connected = true;
        return conn;
    }
    
    public ResultSet execQuery(String sql) {
        if (this.conn == null) {
            throw new DatabaseQueryException("Database is not connected!","", "");
        }
        
        ResultSet rs = null;
        try {
            Statement stmt  = this.conn.createStatement();
            rs = stmt.executeQuery(sql);
        }
        catch (SQLException e) {
            throw new DatabaseQueryException("Requested SQL query failed!", e.getMessage(), sql);
        }
        
        return rs;
    }
    
    public List<Person> selectAllPersons() {
        if (this.connected == false) {
            this.conn = connect();
        }
        
        String sql = "SELECT id, name, faculty, email, role FROM Person";
        List<Person> returner = new ArrayList<Person>();
        ResultSet rs = execQuery(sql);
        
        if (rs == null) {
            throw new DatabaseQueryException("Database query returned null!", "", sql);
        }
        
        // loop through the result set
        try {
            while (rs.next()) {
                Person newPerson = new Person(rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("faculty"),
                    rs.getString("email"),
                    rs.getString("role"));
                
                returner.add(newPerson);
            }
        }
        catch (SQLException e) {
            throw new DatabaseQueryException("Reading result of SQL query failed!", e.getMessage(), sql);
        }
        return returner;
    }
    
    public void insertOrDeletePerson(String operation, Person person, long personId) {
        if (this.connected == false) {
            this.conn = connect();
        }
        
        String sql = null;
        
        if (operation.equals("delete")) {
            sql = "DELETE FROM Person WHERE id = ?";
        }
        else if (operation.equals("insert")) {
            sql = "INSERT OR REPLACE INTO Person VALUES (?,?,?,?,?)";
        }
        else {
            return;
        }
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, personId);
            if (operation.equals("insert")) {
                pstmt.setString(2, person.getName());
                pstmt.setString(3, person.getFaculty());
                pstmt.setString(4, person.getEmail());
                pstmt.setString(5, person.getRole());
            }

            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new DatabaseQueryException("Executing SQL update query failed!", e.getMessage(), sql);
        }
    }
    
    public List<Course> selectAllCourses() {
        if (this.connected == false) {
            this.conn = connect();
        }
        
        String sql = "SELECT id, name, code, description, year, period, capacity FROM Course";
        List<Course> returner = new ArrayList<Course>();
        ResultSet rs = execQuery(sql);
        
        if (rs == null) {
            throw new DatabaseQueryException("Database query returned null!", "", sql);
        }
        
        // loop through the result set
        try {
            while (rs.next()) {
                Course newCourse = new Course(rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("code"),
                    rs.getString("description"),
                    rs.getInt("year"),
                    rs.getInt("period"),
                    rs.getInt("capacity"));
                
                returner.add(newCourse);
            }
        }
        catch (SQLException e) {
            throw new DatabaseQueryException("Reading result of SQL query failed!", e.getMessage(), sql);
        }
        return returner;
    }
    
    public void insertOrDeleteCourse(String operation, Course course, long courseId) {
        if (this.connected == false) {
            this.conn = connect();
        }
        
        String sql = null;
        
        if (operation.equals("delete")) {
            sql = "DELETE FROM Course WHERE id = ?";
        }
        else if (operation.equals("insert")) {
            sql = "INSERT OR REPLACE INTO Course VALUES (?,?,?,?,?,?,?)";
        }
        else {
            return;
        }
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, courseId);
            if (operation.equals("insert")) {
                pstmt.setString(2, course.getName());
                pstmt.setString(3, course.getCode());
                pstmt.setString(4, course.getDescription());
                pstmt.setInt(5, course.getYear());
                pstmt.setInt(6, course.getPeriod());
                pstmt.setInt(7, course.getCapacity());
            }

            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new DatabaseQueryException("Executing SQL update query failed!", e.getMessage(), sql);
        }
    }
    
    public void createOrDropCourseStudentsTeachersTable(String operation, long courseId, String role) {
        if (this.connected == false) {
            this.conn = connect();
        }
        
        String sql = null;
        if (operation.equals("create")) {
            sql = "CREATE TABLE IF NOT EXISTS Course_" + courseId + "_" + role + "s (\n"
                    + " id INTEGER NOT NULL UNIQUE PRIMARY KEY,\n"
                    + " FOREIGN KEY(id) REFERENCES Person(id)"
                    + " );";
        }
        else if (operation.equals("drop")) {
            sql = "DROP TABLE IF EXISTS Course_" + courseId + "_" + role + "s";
        }
        else {
            return;
        }

        
        try (Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new DatabaseQueryException("Creation/dropping of table  of course students/teachers failed!", e.getMessage(), sql);
        }
    }
    
    public List<Long> selectAllCourseStudentsTeachers(long courseId, String role) {
        if (this.connected == false) {
            this.conn = connect();
        }
        
        String sql = "SELECT id FROM Course_" + courseId + "_" + role + "s";
        List<Long> returner = new ArrayList<Long>();
        ResultSet rs = execQuery(sql);
        
        // loop through the result set
        try {
            while (rs.next()) {
                returner.add((long)rs.getInt("id"));
            }
        }
        catch (SQLException e) {
            throw new DatabaseQueryException("Reading result of SQL query failed!", e.getMessage(), sql);
        }
        
        return returner;
    }
    
    public void insertOrDeleteCourseStudentTeacher(String operation, String role,
            long courseId, long studentTeacherId, boolean deleteAllRows) {
        if (this.connected == false) {
            this.conn = connect();
        }
        
        String sql = null;
        
        if (operation.equals("delete")) {
            sql = "DELETE FROM Course_" + courseId + "_" + role + "s";
            if (!deleteAllRows) {
                sql += " WHERE id = ?";
            }
        }
        else if (operation.equals("insert")) {
            sql = "INSERT OR REPLACE INTO Course_" + courseId + "_" + role + "s VALUES (?)";
        }
        else {
            return;
        }
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (!deleteAllRows) {
                pstmt.setLong(1, studentTeacherId);
            }
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new DatabaseQueryException("Executing SQL update query failed!", e.getMessage(), sql);
        }
    }
    
    // This time is more comfortable query by username, not by ID
    public List<User> selectUsers(String username, boolean allUsers) {
        if (this.connected == false) {
            this.conn = connect();
        }
        
        String sql = "SELECT id, username, password, role FROM User";
        if (!allUsers) {
            sql += " WHERE username='" + username + "'";
        }
        
        List<User> returner = new ArrayList<User>();
        ResultSet rs = execQuery(sql);
        
        if (rs == null) {
            throw new DatabaseQueryException("Database query returned null!", "", sql);
        }
        
        // loop through the result set
        try {
            while (rs.next()) {
                User newUser = new User(rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role"));
                
                returner.add(newUser);
            }
        }
        catch (SQLException e) {
            throw new DatabaseQueryException("Reading result of SQL query failed!", e.getMessage(), sql);
        }
        return returner;
    }
    
    public void insertOrDeleteUser(String operation, User user, long userId) {
        if (this.connected == false) {
            this.conn = connect();
        }
        
        String sql = null;
        
        if (operation.equals("delete")) {
            sql = "DELETE FROM User WHERE id = ?";
        }
        else if (operation.equals("insert")) {
            sql = "INSERT OR REPLACE INTO User VALUES (?,?,?,?)";
        }
        else {
            return;
        }
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            if (operation.equals("insert")) {
                pstmt.setString(2, user.getUsername());
                pstmt.setString(3, user.getPassword());
                pstmt.setString(4, user.getRole());
            }

            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new DatabaseQueryException("Executing SQL update query failed!", e.getMessage(), sql);
        }
    }
    
    public List<Room> selectAllRooms() {
        if (this.connected == false) {
            this.conn = connect();
        }
        
        String sql = "SELECT id, number, building, capacity, description FROM Room";
        List<Room> returner = new ArrayList<Room>();
        ResultSet rs = execQuery(sql);
        
        if (rs == null) {
            throw new DatabaseQueryException("Database query returned null!", "", sql);
        }
        
        // loop through the result set
        try {
            while (rs.next()) {
                Room newRoom = new Room((long)rs.getInt("id"),
                    rs.getInt("number"),
                    rs.getString("building"),
                    rs.getInt("capacity"),
                    rs.getString("description"));
                
                returner.add(newRoom);
            }
        }
        catch (SQLException e) {
            throw new DatabaseQueryException("Reading result of SQL query failed!", e.getMessage(), sql);
        }
        return returner;
    }
    
    public void insertOrDeleteRoom(String operation, Room room, long roomId) {
        if (this.connected == false) {
            this.conn = connect();
        }
        
        String sql = null;
        
        if (operation.equals("delete")) {
            sql = "DELETE FROM Room WHERE id = ?";
        }
        else if (operation.equals("insert")) {
            sql = "INSERT OR REPLACE INTO Room VALUES (?,?,?,?,?,?,?)";
        }
        else {
            return;
        }
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, roomId);
            if (operation.equals("insert")) {
                pstmt.setInt(2, room.getNumber());
                pstmt.setString(3, room.getBuilding());
                pstmt.setInt(4, room.getCapacity());
                pstmt.setString(5, room.getDescription());
            }

            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new DatabaseQueryException("Executing SQL update query failed!", e.getMessage(), sql);
        }
    }
    
    public void createOrDropRoomReservationTable(String operation, long roomId) {
        if (this.connected == false) {
            this.conn = connect();
        }
        
        String sql = null;
        if (operation.equals("create")) {
            sql = "CREATE TABLE IF NOT EXISTS Room_" + roomId + "_reservations (\n"
                    + " id INTEGER NOT NULL UNIQUE PRIMARY KEY,\n"
                    + " courseId INTEGER,\n"
                    + " startTime INTEGER,\n"
                    + " endTime INTEGER,\n"
                    + " FOREIGN KEY(courseId) REFERENCES Course(id)"
                    + " );";
        }
        else if (operation.equals("drop")) {
            sql = "DROP TABLE IF EXISTS Room_" + roomId + "reservations";
        }
        else {
            return;
        }

        try (Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new DatabaseQueryException("Creation/dropping of table  of room reservations failed!", e.getMessage(), sql);
        }
    }
    
    public List<Reservation> selectAllRoomReservations(long roomId) {
        if (this.connected == false) {
            this.conn = connect();
        }
        
        String sql = "SELECT id, courseId, startTime, endTime FROM Room_" + roomId + "_reservations";
        List<Reservation> returner = new ArrayList<Reservation>();
        ResultSet rs = execQuery(sql);
        
        // loop through the result set
        try {
            while (rs.next()) {
                Reservation newReservation = new Reservation((long)rs.getInt("id"),
                        (long)rs.getInt("courseId"),
                        LocalDateTime.ofInstant(Instant.ofEpochSecond((long)rs.getInt("startTime")), ZoneId.ofOffset("UTC", ZoneOffset.UTC)),
                        LocalDateTime.ofInstant(Instant.ofEpochSecond((long)rs.getInt("endTime")), ZoneId.ofOffset("UTC", ZoneOffset.UTC)));
                
                 returner.add(newReservation);
            }
        }
        catch (SQLException e) {
            throw new DatabaseQueryException("Reading result of SQL query failed!", e.getMessage(), sql);
        }
        
        return returner;
    }
    
    public void insertOrDeleteRoomReservation(String operation, long roomId,
            Reservation reservation, long reservationId, boolean deleteAllRows) {
        if (this.connected == false) {
            this.conn = connect();
        }
        
        String sql = null;
        
        if (operation.equals("delete")) {
            sql = "DELETE FROM Room_" + roomId + "_reservations";
            if (!deleteAllRows) {
                sql += " WHERE id = ?";
            }
        }
        else if (operation.equals("insert")) {
            sql = "INSERT OR REPLACE INTO Room_" + roomId + "_reservations VALUES (?,?,?,?)";
        }
        else {
            return;
        }
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (!deleteAllRows) {
                pstmt.setLong(1, reservationId);
                if (operation.equals("insert")) {
                    pstmt.setLong(2, reservation.getCourseId());
                    pstmt.setLong(3, reservation.getStartTime().toEpochSecond(ZoneOffset.UTC));
                    pstmt.setLong(4, reservation.getEndTime().toEpochSecond(ZoneOffset.UTC));
                }
            }
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new DatabaseQueryException("Executing SQL update query failed!", e.getMessage(), sql);
        }
    }
    
    public long selectHighestIdFromTable(String table) {
        if (this.connected == false) {
            this.conn = connect();
        }
        
        String sql = "SELECT MAX(id) FROM " + table;
        long returner = 0;
        ResultSet rs = execQuery(sql);
        
        try {
         // result set is not empty
            if (rs.isBeforeFirst()) { 
                returner = (long)rs.getLong(1);
            }
        }
        catch (SQLException e) {
            throw new DatabaseQueryException("Reading result of SQL query failed!", e.getMessage(), sql);
        }
        
        return returner;
    }
    
    public long getDbLastModifiedDate() {
        File db = new File(DB_FILENAME);
        
        return db.lastModified();
    }
    
    // FIXME not really portable solution, but working for eclipse + tomcat
    private String getDbAbsolutePath() {
        URL resource = getClass().getResource("/");
        String path = resource.getPath();
        path = path.replace("WEB-INF/classes/", "");
        path += DB_FILENAME;
        
        File f = new File(path);
        if(f.exists() && !f.isDirectory()) { 
            return path;
        }
        
        return null;
    }
    
    /**
     * Closes connection to DB.
     */
    public void close() {
        try {
            if (this.conn != null) {
                this.conn.close();
            }
      } catch (SQLException e) {
          throw new DatabaseQueryException("Failed to close database!", e.getMessage(), "");
      }
    }

}
