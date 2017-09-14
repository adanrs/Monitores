/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sqlmon.conexion;




import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
/**
 *
 * @author adan-
 */
public class Conexion {
    private Connection conexion;
    static String url = "jdbc:oracle:thin:@localhost:1521:XE"; //Descargar ojdbc6.jar e incluirlo en la libreria
    static String user = "system";
    static String password = "root";
    private boolean exito;
   
    private ArrayList<String> resultados = new ArrayList<String>();
    
    
    /*Metodos*/
     public String getUser() {
        return user;
    }

    public void setUser(String user) {
        Conexion.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        Conexion.password = password;
    }
    
    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }


    
    public void conectar(){
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            exito = true;
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            conexion = DriverManager.getConnection(url, user, password);
            System.out.println("Conectado");
            exito = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
           

                   System.out.println(e.getMessage());
            exito = false;
        }
    }

    public int executeUpdate(String sql) { 
        try {
            Statement sentencia = conexion.createStatement();
            sentencia.executeQuery(sql);
            return sentencia.getUpdateCount();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    /*Ejecutar Querys*/
    //aqui va el cod del grafico 
        public float [] executeQuery(String statement) throws InterruptedException {
            int i=0;
            float [] vec = new float[15];
            while(i<15)
            {
                 float valor=0;
                 
        try {
            Statement stm = conexion.createStatement();
            ResultSet rs = stm.executeQuery(statement);
           // System.out.println("Ejecutando");
             getColumnNames(rs);
            while (rs.next()) {
              
               String a = rs.getString("POOL");//Aqui deberia jalar el nombre de la columna
                String b = rs.getString("MEMORIA_MB");// valor columnas 
               
              valor+= Float.parseFloat(b); // parsear valor 
                System.out.println(a+" "+" "+b);   
                
               
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
       int total_memoria=176;
        valor=total_memoria- valor;// diferencia a la memoria libre 
        valor=(valor/total_memoria)*100;// porcentaje de memoria usada 
            System.out.println("%"+valor);
            vec[i]=valor;
            i++;
          Thread.sleep(995);
        }
        return vec;  
    }
        
        /*Devuelve columna*/
   public static void getColumnNames(ResultSet rs) throws SQLException {
    if (rs == null) {
      return;
    }
    // get result set meta data
    ResultSetMetaData rsMetaData = rs.getMetaData();
    int numberOfColumns = rsMetaData.getColumnCount();
    
    // get the column names; column indexes start from 1
    for (int i = 1; i < numberOfColumns + 1; i++) {
      String columnName = rsMetaData.getColumnName(i);
      // Get the name of the column's table name
      String tableName = rsMetaData.getTableName(i);
    
      //System.out.println("column name=" + columnName + " table=" + tableName + "");
      System.out.println(columnName);
    }
  }
}
