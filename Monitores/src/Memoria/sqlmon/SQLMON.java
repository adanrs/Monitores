/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sqlmon;

import Ventanas.Graf2;



import javax.swing.JOptionPane;
import sqlmon.conexion.Conexion;
import paneles.PanelMonitor;
/**
 *
 * @author adan-
 */
public class SQLMON {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException  {
        // TODO code application logic here
       
        float[] aux=new float[15];
        boolean bandera;
        //si quiere ver un consumo hagalo con true...
       Graf2 vent= new Graf2(); // constructructor ventana 
        
       // vent.init();// inicio ventana 
        Conexion c = new Conexion(); // crea la conexion con la base de datos 
        c.conectar();// conecta a la base 
        while(true) // mantiene corriendo el programa 
        {
       // Thread.sleep(100);  
         //aqui hay que modificar el query ya que ocupamos informacion adicional revise el queri de monitor_memory es el que deberia ir aqui.
       aux= c.executeQuery("select POOL, Round(bytes/1024/1024,0) MEMORIA_MB From V$sgastat Where Name Like '%free memory%'");
        //estos valores son los que hay que pasar de 0 a 100% y de ahi pasarlos al grafico.
        System.out.println("----------------------------------------");
        bandera=vent.go(aux);// se le envia porcentaje a la ventana 
        aux=null;
       if(bandera)
       {
           vent=null;
           System.gc();
           return;
           
       }
         
       
        }        
    }
    
}
