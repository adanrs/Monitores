/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package revisar;

/**
 *
 * @author adan-
 */
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import aw.gui.chart.AbstractDataCollector;
import aw.gui.chart.Chart2D;
import aw.gui.chart.ITrace2D;
import aw.gui.chart.RandomDataCollector;
import aw.gui.chart.Trace2DLtd;

public class MinimalDynamicChart {
  private MinimalDynamicChart() {
    super();
  }
  
  public static void main(String[]args){
    // Create a chart:  
    Chart2D chart = new Chart2D();
    // Create an ITrace: 
    // Note that dynamic charts need limited amount of values!!! 
    ITrace2D trace = new Trace2DLtd(60); 
    trace.setColor(Color.RED);
 
    // Add the trace to the chart: 
    chart.addTrace(trace);
    
    // Make it visible:
    // Create a frame. 
    JFrame frame = new JFrame("Consumo de memoria");
    // add the chart to the frame: 
    frame.getContentPane().add(chart);
    frame.setSize(400,300);
    // Enable the termination button [cross on the upper right edge]: 
    frame.addWindowListener(
        new WindowAdapter(){
          public void windowClosing(WindowEvent e){
              System.exit(0);
          }
        }
      );
    frame.show(); 
    // Every 50 milliseconds a new value is collected. 
    AbstractDataCollector collector = new RandomDataCollector(trace,1000);
    // Start a Thread that adds the values: 
    new Thread(collector).start();
  }
}