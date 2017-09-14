/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import modelo.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import java.util.GregorianCalendar;


import ventanas.*;


/**
 *
 * @author cesar
 */
public class Control {
    private Conexion model;
    private Vent1 ventIni;
    private Grafico graf;
    private ArrayList<String> tabSpa;
    private ArrayList<TableSpace> ta;
    private TableSpace tab_graf;
    private Tabla tabla;
   private SQLiteJDBC sqlite;
   private Calendar fecha;
    public Control() throws SQLException 
    {
        model= new Conexion();
        model.conectar();
        ventIni= new Vent1(this);
        tabSpa= new ArrayList<>();
        ta = new ArrayList<>();
         tab_graf= new TableSpace();
         sqlite= new SQLiteJDBC();
//
//        sqlite.conectar();
//         sqlite.query("drop table TB_SPACES");
//         sqlite.conectar();
//         sqlite.query("drop table Hist");
//         sqlite.conectar();
//         sqlite.query("CREATE TABLE TB_SPACES " + "(fecha TEXT not null,nombre TEXT NOT NULL, MB_TABLAS float not null, usado float NOT NULL,TasaTrans float not null,registros INT NOT NULL)");
//         sqlite.conectar();
//           sqlite.query("CREATE TABLE Hist " + "(fecha TEXT not null,nombre TEXT NOT NULL, uso INT not null, porcentaje INT NOT NULL)");
//          sqlite.conectar();
//         sqlite.query("drop table HWM");
//         sqlite.conectar();
//         sqlite.query("CREATE TABLE HWM " + "(nombre TEXT NOT NULL, HWM INT not null)");

         fecha=  new GregorianCalendar(); 
 }
    
    public void iniciar() throws  SQLException, InterruptedException
    {        
        ta= model.getSegmentos();
        ta=cargarHWM(ta);
        ventIni.init(ta);
        
    }
    
    public void iniciarVent2(String select,int h) throws InterruptedException, SQLException, IOException 
    {
        TableSpace aux=null;
        String date="";
        ta=null;
        tab_graf=null;
          sqlite.conectar();
        ta=sqlite.select(select);        
        aux=model.getTable(select);
        tab_graf=model.getGrafica(select);
       date=fecha.get(Calendar.DATE)+"-"+fecha.get(Calendar.MONTH)+"-"+fecha.get(Calendar.YEAR);
       aux.setFecha(date);
       aux.setTam_total(tab_graf.getUso());
       float hwm=h;
       float D_HWM=-1;
       float D_tot=-1;
       if(!ta.isEmpty())
       {
           System.out.println(aux.getTam_total()-ta.get(ta.size()-1).getTam_total());
           aux.setTasatrans(aux.getTam_total()-ta.get(ta.size()-1).getTam_total());            
           
               
           System.out.println(D_HWM);
           
           if(aux.getTasatrans()!=0){
               D_HWM =(((hwm/100)*tab_graf.getTam_total())-tab_graf.getUso())/(aux.getTasatrans()+aux.getUso());//hwm en bites/ libre en bites
               D_tot=(tab_graf.getTam_total()-tab_graf.getUso())/(aux.getTasatrans()+aux.getUso());
              }
       }
       
       sqlite.conectar();
       guardar(aux);

     
        graf= new Grafico(ventIni,ta,aux,this);
        graf.init(tab_graf,(int) hwm,(int) D_HWM,(int) D_tot);
      
        // revisar lo de los dias
        
        
    }
    
    private void guardar(TableSpace tab) throws SQLException
    {
        sqlite.query("INSERT INTO TB_SPACES (fecha,nombre,MB_TABLAS,usado,tasatrans,registros)VALUES ('"+tab.getFecha()
                +"','"+tab.getNombre()+"',"+tab.getUso()+","+tab.getTam_total()+","+tab.getTasatrans()+","+tab.getFree()+");");
    }
    
  public ArrayList<TableSpace> cargarHWM(ArrayList<TableSpace> ts) throws SQLException
  {
      sqlite.conectar();
      return ts= sqlite.selectHWM(ts);
    
  }
  
  public void guardarHWM(ArrayList<TableSpace> porc) throws  SQLException
  {
            sqlite.conectar();
         sqlite.query("drop table HWM");
         sqlite.conectar();
         sqlite.query("CREATE TABLE HWM " + "(nombre TEXT NOT NULL, HWM INT not null)");
     for(int i=0;i<porc.size();i++)
     {
//         sqlite.conectar();
          sqlite.query("INSERT INTO HWM (nombre,HWM)VALUES ('"+porc.get(i).getNombre()+"',"+porc.get(i).getTam_total()+");");
     }
     
     
  }
  
  public void atras () throws InterruptedException, SQLException
  {
      
            
      tabSpa=null;
      ta=null;
      tab_graf=null;
      
      ta= model.getSegmentos();
      ta=cargarHWM(ta);
      ventIni= new Vent1(this);
        ventIni.init(ta);
  }
  public ArrayList<TableSpace> cargarHist(String tab) throws SQLException
  {
      ta=null;     
      sqlite.conectar();
      ta=sqlite.selectHist(tab);
      return ta;
      
  }
  public void GuardarHist(String nom,float tam_to,float porc) throws SQLException
  {
      String date="";
      date=fecha.get(Calendar.DATE)+"-"+fecha.get(Calendar.MONTH)+"-"+fecha.get(Calendar.YEAR);
        sqlite.conectar();
         sqlite.query("INSERT INTO Hist (fecha,nombre,uso,porcentaje)VALUES ('"+date+"','"+nom+"',"+tam_to+","+porc+");");
  }
  
}
