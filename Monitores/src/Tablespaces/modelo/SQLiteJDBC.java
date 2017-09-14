/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author adan-
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
/**/


public class SQLiteJDBC {
static Connection c = null;
static String dir = "org.sqlite.JDBC";
static String db = "test.db";
static Statement stmt = null;
/**/
 
   
  // query("drop table TB_SPACES");
   //query("CREATE TABLE TB_SPACES " + "(id INT PRIMARY KEY NOT NULL, fecha TEXT not null,nombre TEXT NOT NULL, registros INT not null, size INT NOT NULL,TasaTrans INT not null )");
  // query("INSERT INTO TB_SPACES (id,fecha,nombre,registros,size,tasatrans)VALUES (1,'12-10-17', 'SYSTEM', 32,15,0);");
   //query("INSERT INTO TB_SPACES (id,nombre,size)VALUES (2, 'USERS', 41);");
   //query("INSERT INTO TB_SPACES (id,nombre,size)VALUES (3, 'TBPS01', 68);");
   //select("select * from TB_SPACES;");
//   Scanner leer = new Scanner(System.in);  // Reading from System.in
//   System.out.println("Indique la tabla: ");
//   String tabla = leer.next();
//    System.out.println("Indique tamaño: ");
//   String tam = leer.next();
   
//   query("UPDATE TB_SPACES SET NOMBRE = '"+tabla+"' WHERE ID = 1;");
//   select("select * from TB_SPACES;"); 
 
   public  void conectar(){
     
      try {
         Class.forName(dir);
         c = DriverManager.getConnection("jdbc:sqlite:"+db);
      } catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         System.exit(0);
      }
      System.out.println("Conexion Correcta");
   }  
  public  void query (String sql) throws SQLException{
    try {   stmt = c.createStatement();
          //sql = ""; 
         stmt.executeUpdate(sql);
         System.out.println("Ejecutada");  
   }catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         System.exit(0);
      }
   }
   
   public  ArrayList<TableSpace> select(String sql) throws SQLException{
       ArrayList<TableSpace> regs= new ArrayList<>();
   try {
    TableSpace tab=null;
      stmt = c.createStatement();
      ResultSet rs = stmt.executeQuery("select * from TB_SPACES where nombre= '"+sql+"';");
      
      while ( rs.next() ) {
          rs.getMetaData();
        tab= new TableSpace(rs.getString("fecha"),rs.getString("NOMBRE"),rs.getFloat("usado"),rs.getFloat("MB_TABLAS"),rs.getFloat("tasatrans"),rs.getInt("registros"));
         
        regs.add(tab);
      }
      rs.close();
      stmt.close();
      c.close();
   
   }catch ( Exception e ) {
      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      System.exit(0);
   }
   return regs;
   }
   
    public  ArrayList<TableSpace> selectHist(String sql) throws SQLException{
       ArrayList<TableSpace> regs= new ArrayList<>();
   try {
    TableSpace tab=null;
      stmt = c.createStatement();
      ResultSet rs = stmt.executeQuery("select * from Hist where nombre='"+sql+"';");
      
      while ( rs.next() ) {
          rs.getMetaData();
        tab= new TableSpace(rs.getString("fecha"),rs.getString("NOMBRE"),rs.getInt("uso"),rs.getInt("porcentaje"));
         
        regs.add(tab);
      }
      rs.close();
      stmt.close();
      c.close();
   
   }catch ( Exception e ) {
      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      System.exit(0);
   }
   return regs;
   }
    
    
     public  ArrayList<TableSpace> selectHWM(ArrayList<TableSpace> sql) throws SQLException{
     
   try {
       boolean ban=false;
       for(int i=0;i<sql.size();i++)
       {
       
      stmt = c.createStatement();
      ResultSet rs = stmt.executeQuery("select * from HWM where nombre= '"+sql.get(i).getNombre()+"';");
      
     
     
      while ( rs.next() ) {
          rs.getMetaData();
          sql.get(i).setTam_total(rs.getInt("HWM"));
        ban=true;
       
      }
        if(ban== false)
      {
          sql.get(i).setTam_total(80);
      }
     
      rs.close();
       }
        
       stmt.close();
      c.close();
   }catch ( Exception e ) {
      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
//      System.exit(0);
   }
   return sql;
   }
}