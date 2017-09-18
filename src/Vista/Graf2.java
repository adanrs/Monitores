package Vista;

import Control.Control;
import Modelo.Conexion;
import Modelo.SQLiteJDBC;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.scene.chart.AreaChart;

public class Graf2 extends Application {

    private int cont;
    private float total_memoria;
    private int hwm;
    private Control gestor;

    @Override
    public void start(Stage stage) {
        cont = 1;
        try {
            hwm = cargarHWMSGA();
        } catch (IOException ex) {
            Logger.getLogger(Graf2.class.getName()).log(Level.SEVERE, null, ex);
        }
        final NumberAxis xAxis = new NumberAxis(0, 10, 1);
        final NumberAxis yAxis = new NumberAxis(0, 100, 10);
        Conexion c = new Conexion();
        c.conectar();
        xAxis.setLabel("Segundos");
        yAxis.setLabel("Porcentaje");
        stage.setTitle("Consumo de memoria");
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        XYChart.Series hwm_series = new XYChart.Series();
        final AreaChart<Number, Number> ac = new AreaChart<>(xAxis, yAxis);
        hwm_series.setName("HWM: "+hwm+"%");
        hwm_series.getData().add(new XYChart.Data(0, hwm));
        hwm_series.getData().add(new XYChart.Data(10, hwm));
        ac.setAnimated(false);
        ac.getData().addAll(series, hwm_series);
        Scene scene = new Scene(ac, 600, 300);
        scene.getStylesheets().add("Vista/estilo.css");
        stage.setScene(scene);
        stage.show();
        try {
            total_memoria = c.total_memoria();
        } catch (SQLException ex) {
            Logger.getLogger(Graf2.class.getName()).log(Level.SEVERE, null, ex);
        }
        Thread updateThread = new Thread(() -> {
            while (true) {
                try {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            series.getData().add(new XYChart.Data<>(0, 0));

                            try {
                                float value = c.obtener_memoria_usada(total_memoria);
                                if(value >= hwm){
                                    //consulta para guardar usuario que se cago en todo
                                    BSGA(c.BitSGA());
                                }
                                series.getData().add(new XYChart.Data<>(cont, value));
                                series.setName("Porcentaje de uso: " + value + "%");
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Graf2.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (SQLException ex) {
                                Logger.getLogger(Graf2.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if (cont > 10) {
                                ((XYChart.Data) (hwm_series.getData().get(0))).setXValue(cont - 10);
                                ((XYChart.Data) (hwm_series.getData().get(1))).setXValue(cont);
                                xAxis.setUpperBound(cont);
                                xAxis.setLowerBound(cont - 10);
                                series.getData().remove(0);
                            }
                            cont++;
                        }
                    });
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        updateThread.setDaemon(true);
        updateThread.start();
    }

    public int cargarHWMSGA() throws IOException {
        String cadena;
        int hwm = 0;
        File archivo = new File("HWMSGA.txt");
        FileReader f = new FileReader(archivo);
        BufferedReader b = new BufferedReader(f);
        while ((cadena = b.readLine()) != null) {
            hwm = Integer.parseInt(cadena);
        }
        b.close();
        return hwm;
    }
     public void BSGA(ArrayList<String> bit) throws SQLException
    {
        SQLiteJDBC sqlite= new SQLiteJDBC();
       Calendar fecha=new GregorianCalendar();
       String date = "";
       String hour="";
        date = fecha.get(Calendar.DATE) + "-" + fecha.get(Calendar.MONTH) + "-" + fecha.get(Calendar.YEAR);
        hour= fecha.get(Calendar.HOUR)+":"+fecha.get(Calendar.MINUTE)+":"+fecha.get(Calendar.SECOND);
        sqlite.conectar();
         sqlite.query("INSERT INTO SGA (fecha,hora,sql,usuario,maquina)VALUES ('" + date
                + "','" + hour + "','" + bit.get(0)+"','"+bit.get(1)+"','"+bit.get(2)+"');");
         sqlite=null;
        
    }

    public static void main(String[] args) {
        launch(args);
        
    }
}
