package timeline.timelineLab;

import java.sql.*;
import java.util.*;

public class DatabaseUtils {
	
	//setter defaultverdier for tilkobling til database
	private  String DEFAULT_DRIVER = "org.h2.Driver";
    private  String DEFAULT_URL = "jdbc:h2:~/test";
    private  String DEFAULT_USERNAME = "lifeline";
    private  String DEFAULT_PASSWORD = "nsep";
    
    private Connection c;
    
    public static void main(String[] args) {
    	
    	
    }
    
    public void setDatabaseParameters(String[] args)
    {
    	if(args.length == 0)
    	{
	    	if(args.length > 0){ DEFAULT_DRIVER=args[0];}
	    	if(args.length > 1){ DEFAULT_URL=args[1];}
	    	if(args.length > 2){ DEFAULT_USERNAME=args[0];}
	    	if(args.length > 3){ DEFAULT_PASSWORD=args[0];}
    	}
    }
    
    public Connection getConnection(){
    	
    	if(c==null){
    		try{
    			createConnection();
    		}catch (ClassNotFoundException|SQLException e) {
                e.printStackTrace();
            }
    	}
    	
    	return c;
    }
    
    public void setConnection(Connection con){
    	c=con;
    }
    
    public void createConnection() throws ClassNotFoundException, SQLException {
        
    	String driver= DEFAULT_DRIVER;
        String url = DEFAULT_URL;
        String username = DEFAULT_USERNAME;
        String password = DEFAULT_PASSWORD;
    	
    	Class.forName(driver); //henter inn driver for database
        if ((username == null) || (password == null) || (username.trim().length() == 0) || (password.trim().length() == 0)) {
            c=DriverManager.getConnection(url); //hvis det ikke er registrert noe brukernavn eller passord
        } else {
            c=DriverManager.getConnection(url, username, password); //kobler til med brukernavn og pasords
        }
    }

    public void closeConnection() {
        try {
            if (c != null) {
                c.close(); //lukker connection
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void close(Statement st) {
        try {
            if (st != null) {
                st.close(); //lukker statement. Statement er som en forbindelse over connection?
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void close(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close(); //resultset holder resultater for et statement
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void rollback() {
        try {
            if (c != null) {
                c.rollback(); //rollback er en angrefunksjon. Tilbakestiller alt som er gjort på gitt connection
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    //map lager mysql svar om til en array
    
    private static List<Map<String, Object>> map(ResultSet rs) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        try {
            if (rs != null) {
                ResultSetMetaData meta = rs.getMetaData(); //informasjon om sql svaret
                int numColumns = meta.getColumnCount(); //antall rader i svar
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<String, Object>(); //lager enkel hashmap for hver rad
                    for (int i = 1; i <= numColumns; ++i) {
                        String name = meta.getColumnName(i); //henter column navnet
                        Object value = rs.getObject(i); //henter kolonnesvaret
                        row.put(name, value); //legger svaret med key og value i hashmap
                    }
                    results.add(row);
                }
            }
        } finally {
            close(rs); //lukker svaret
        }
        return results;
    }
    
    //query henter svar fra database
    
    public List<Map<String, Object>> query(String sql, List<Object> parameters) throws SQLException {
        
    	List<Map<String, Object>> results = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
        	
        	ps = c.prepareStatement(sql);
            
            //kan lage fancy prepare statement her.
            
            int i = 0;
            for (Object parameter : parameters) {
                ps.setObject(++i, parameter);
            }
            rs = ps.executeQuery();
            results = map(rs);
        }catch(Exception e) {
            
        	rollback();
            e.printStackTrace();
        	
        }finally {
            close(rs);
            close(ps);
        }
        return results;
    }
    
    //overloader query til å ikke trenge parameters
    
    public List<Map<String, Object>> query(String sql) throws SQLException {
        List<Map<String, Object>> results = null;
        Statement s = null;
        ResultSet rs = null;

        try {
            s = c.createStatement();
            rs = s.executeQuery(sql);
            results = map(rs);
        }catch(Exception e) {
        	rollback();
            e.printStackTrace();	
        } finally {
            close(rs);
            close(s);
        }
        return results;
    }
    
    //update returnerer noe annet enn query, er ellers nokså lik
    
    public int update(String sql, List<Object> parameters) throws SQLException {
        int numRowsUpdated = 0;
        PreparedStatement ps = null;
     
        try {	
            ps = c.prepareStatement(sql);

            int i = 0;
            for (Object parameter : parameters) {
                ps.setObject(++i, parameter);
            }
            numRowsUpdated = ps.executeUpdate();
        }catch(Exception e) {  
        	rollback();
            e.printStackTrace(); 	
        }  finally {	
            close(ps);
        }
        return numRowsUpdated;
    }
    
    //overlaoder update til å ikke trenge parameters
    
    public int update(String sql) throws SQLException {
        int numRowsUpdated = 0;
        Statement s = null;
        
        try {
        	s = c.createStatement();
            numRowsUpdated = s.executeUpdate(sql);
            
        }catch(Exception e) {
            
        	rollback();
            e.printStackTrace();
        	
        }finally {
        	
            close(s);
        }
        
        return numRowsUpdated;
    }
}
