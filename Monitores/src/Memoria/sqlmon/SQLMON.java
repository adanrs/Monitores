package Memoria.sqlmon;

import Ventanas.Graf2;
import sqlmon.conexion.Conexion;

/**
 *
 * @author adan-
 */
public class SQLMON {

    public static void main(String[] args) throws InterruptedException {
        float[] aux = new float[15];
        boolean bandera;
        //si quiere ver un consumo hagalo con true...
        Graf2 vent = new Graf2(); // constructructor ventana 
        // vent.init();// inicio ventana 
        Conexion c = new Conexion(); // crea la conexion con la base de datos 
        c.conectar();// conecta a la base 
        while (true) // mantiene corriendo el programa 
        {
            // Thread.sleep(100);  
            //aqui hay que modificar el query ya que ocupamos informacion adicional revise el queri de monitor_memory es el que deberia ir aqui.
            aux = c.executeQuery("select POOL, Round(bytes/1024/1024,0) MEMORIA_MB From V$sgastat Where Name Like '%free memory%'");
            //estos valores son los que hay que pasar de 0 a 100% y de ahi pasarlos al grafico.
            System.out.println("----------------------------------------");
            bandera = vent.go(aux);// se le envia porcentaje a la ventana 
            aux = null;
            if (bandera) {
                vent = null;
                System.gc();
                return;
            }
        }
    }
}
