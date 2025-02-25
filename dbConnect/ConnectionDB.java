package dbConnect;

import java.sql.*;


import javax.swing.JOptionPane;
public class ConnectionDB {
	
	static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static String DB_URL = "jdbc:mysql://localhost:3306/attendancedb";
	static  String USER = "root";
	static String PASSWORD = "";
       
       
       public static Connection connection(){
    	      try{
    	          Class.forName(JDBC_DRIVER);
    	                  Connection conn = DriverManager.getConnection(DB_URL ,USER,PASSWORD);
    	                  return conn;
    	      } catch(Exception e){
    	           JOptionPane.showMessageDialog(null, e);
    	           return null;
    	      }
    	      
    	   }

}
