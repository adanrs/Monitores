package Vista;

import Modelo.Conexion;
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
    private boolean first;
    @Override
    public void start(Stage stage) {
        first = true;
        cont = 1;
        final NumberAxis xAxis = new NumberAxis(0, 10, 1);
        final NumberAxis yAxis = new NumberAxis(0, 100, 10);
        Conexion c = new Conexion();
        c.conectar();
        xAxis.setLabel("Segundos");
        yAxis.setLabel("Porciento");
        stage.setTitle("Consumo de memoria");
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        final AreaChart<Number, Number> ac = new AreaChart<>(xAxis, yAxis);
        ac.setAnimated(false);
        ac.getData().add(series);
        Scene scene = new Scene(ac, 800, 600);
        stage.setScene(scene);
        stage.show();
        Thread updateThread = new Thread(() -> {
            while (true) {
                try {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if(first){
                                series.getData().add(new XYChart.Data<>(0, 0));
                                first = false;
                            }
                            try {
                                float value = c.executeQuery();
                                series.getData().add(new XYChart.Data<>(cont, value));
                                series.setName("Porcentaje de uso: "+value+"%");
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Graf2.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if (cont > 10) {
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

    public static void main(String[] args) {
        launch(args);
    }
}

