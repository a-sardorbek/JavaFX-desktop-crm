
package databaseConnect;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DbConnect {
    
//     static Connection con;
//    
//    public static Connection getConnection(){
//        
//        try{
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            
//            con = DriverManager.getConnection("jdbc:mysql://localhost/textilecrm?serverTimezone=UTC","root","rootpas");
//            return con;
//        }catch(ClassNotFoundException | SQLException e){
//            System.out.println("Error in connection");
//            return null;
//            
//        } 
//    }
    
    
    static Connection con;
    
    public static Connection getConnection(boolean connect){
     
        try{
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            if(connect==true){
            con = DriverManager.getConnection("jdbc:derby:data;create=true");
            }else{
                con = DriverManager.getConnection("jdbc:derby:data;shutdown=true");
                System.out.println("stopped derby");
            }
            return con;
        }catch(ClassNotFoundException | SQLException e){
            System.out.println("Error in connection");
            return null;
        }
        
    }
    
    public static void setPosition(){
        String table_name = "workposition";
        Statement st;
        try{
             st =con.createStatement();
            DatabaseMetaData dbm = con.getMetaData();
            ResultSet rs = dbm.getTables(null, null, table_name.toUpperCase(), null);
            if(rs.next()){
                System.out.println("Table position already exists");
            }else{
                st.execute("CREATE TABLE workposition (\n" +
                            "  id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY(Start with 1, Increment by 1),\n" +
                            "  positionname VARCHAR(45))");

                   
       
           }
        }catch(SQLException e){
            System.out.println("Error in create table workposition");
        }finally{
            
        }
    }
   
   public static void setEmployee(){
        String table_name = "employee";
        Statement st;
        try{
             st =con.createStatement();
            DatabaseMetaData dbm = con.getMetaData();
            ResultSet rs = dbm.getTables(null, null, table_name.toUpperCase(), null);
            if(rs.next()){
                System.out.println("Table employee already exists");
            }else{

                   st.execute("CREATE TABLE employee (\n" +
                            "  id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY(Start with 1, Increment by 1),\n" +
                            "  fio VARCHAR(45),\n" +
                            "  dob VARCHAR(45),\n" +
                            "  position VARCHAR(45),\n"+
                            "  phone VARCHAR(45))");
       
           }
        }catch(SQLException e){
            System.out.println("Error in create table employee");
        }finally{
            
        }
    }
   
   
   public static void setChildren(){
        String table_name = "children";
        Statement st;
        try{
             st =con.createStatement();
            DatabaseMetaData dbm = con.getMetaData();
            ResultSet rs = dbm.getTables(null, null, table_name.toUpperCase(), null);
            if(rs.next()){
                System.out.println("Table children already exists");
            }else{

                   st.execute("CREATE TABLE children (\n" +
                            "  id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY(Start with 1, Increment by 1),\n" +
                            "  fio VARCHAR(45),\n" +
                            "  dob VARCHAR(45),\n" +
                            "  idemployee INTEGER)");
       
           }
        }catch(SQLException e){
            System.out.println("Error in create table children");
        }finally{
            
        }
    }
    
}
