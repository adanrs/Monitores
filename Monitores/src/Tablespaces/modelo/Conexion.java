/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import control.Control;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author cesar
 */
public class Conexion {
///parametros de configuracion de usuario
    private Connection conexion;
    static String url = "jdbc:oracle:thin:@localhost:1521/XE"; //Descargar ojdbc6.jar e incluirlo en la libreria
    static String user = "system";
    static String password = "root";
    private boolean exito;
    private Control gestor;

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

    public void conectar() {
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

    // se obtienen los segmentos de la base de datos
    public ArrayList<TableSpace> getSegmentos() throws InterruptedException {
        ArrayList<TableSpace> vec = new ArrayList<>();

        try {
            Statement stm = conexion.createStatement();
            ResultSet rs = stm.executeQuery("select tablespace_name from dba_tables where tablespace_name is not null AND tablespace_name != 'SYSTEM' group by tablespace_name");

            getColumnNames(rs);
            while (rs.next()) {

                 //Aqui deberia jalar el nombre de la columna

                vec.add(new TableSpace(rs.getString("TABLESPACE_NAME"),0,0));

            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());

        }

        return vec;
    }
    // se obtienen las tablas de cada tablespace
    // se obtienen las tablas de la base de datos
// meter aqui el query de contar los indices de una tabla
    public TableSpace getTable(String tablespace) throws InterruptedException {
        ArrayList<Table> vec = new ArrayList<>();
        Statement stm;
        ResultSet rs, rs2,rs3;
        String a, b;
        Table table;
        TableSpace registro;
        int indices=0;
        int mb=0,regs=0,index=0,aux=0;
        try {
            stm = conexion.createStatement();
            rs = stm.executeQuery("select TABLE_NAME,OWNER from all_tables where tablespace_name = '" + tablespace + "'");
            getColumnNames(rs);
            while (rs.next()) {
                a = rs.getString("TABLE_NAME");//Aqui deberia jalar el nombre de la columna               
                b = rs.getString("OWNER");
                vec.add(new Table(a, b));
            }
            for (int i = 0; i < vec.size(); i++) {
                table = vec.get(i);
               try {
                    rs2 = stm.executeQuery("select a.bytes, b.count from\n"
                            + "(SELECT sum(data_length) bytes FROM all_tab_columns where table_name = '" + table.getName() + "' group by table_name) a,\n"
                            + "(select count(*) count from " + table.getOwner() + "." + table.getName() + ") b");
                    rs2.next();
                     aux=rs2.getInt("BYTES");
                     regs+=rs2.getInt("COUNT");
                     System.out.println(aux);
                    System.out.println(table.getName());
                      rs3 = stm.executeQuery("select count(index_name) indices from all_indexes where  table_name='"+table.getName()+"'");
                      rs3.next();
                      index=rs3.getInt("INDICES");                     
                      mb+=((aux*0.30)*index);
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
        return registro=new TableSpace("",tablespace,0,mb,0,regs);
    }
//obtener los byte

  
    public TableSpace getGrafica(String selec) throws InterruptedException {
        
        TableSpace table = null;

        try {
            Statement stm = conexion.createStatement();
            ResultSet rs = stm.executeQuery("select a.tablespace_name, sum(a.bytes)/1024/1024 total_space_MB, (b.free/1024/1024) Free_space_MB, round(b.free/(sum(a.bytes))* 100,2) percent_free from dba_data_files a, (select tablespace_name,sum(bytes) free  from dba_free_space group by tablespace_name) b where a.tablespace_name = b.tablespace_name(+) group by a.tablespace_name,b.free");

            getColumnNames(rs);
            while (rs.next()) {

                String a = rs.getString("TABLESPACE_NAME");//Aqui deberia jalar el nombre de la columna

                
                    if (selec.equals(a)) {
                        table = new TableSpace(a, Float.parseFloat(rs.getString("TOTAL_SPACE_MB")), Float.parseFloat(rs.getString("FREE_SPACE_MB")));
                        
                    }
                

            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());

        }

        return table;
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

        }
    }
}
