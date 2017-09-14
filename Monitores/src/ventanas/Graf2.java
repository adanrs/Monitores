/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventanas;

import java.sql.Date;
import java.text.DecimalFormat;


import javax.swing.JOptionPane;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

/**
 *
 * @author cesar
 */
public class Graf2 {
    double phase = 0;
    double[][] initdata= new double[10][10];
    final XYChart chart;
    final SwingWrapper<XYChart> sw;
    double cont;
     DecimalFormat format;
    public  Graf2()
    {
      chart = QuickChart.getChart("Monitor memoria Bases de Datos", "segundos", "porcentaje de memoria", "uso", initdata[0], initdata[1]);
 
    // Show it
     sw= new SwingWrapper<XYChart>(chart);
   
    sw.displayChart("monitor");
    cont=0;
  format = new DecimalFormat("00.0");
    }
    
    public boolean go(float[] vec) throws InterruptedException
    {
        
            
 
      final double[][] data = getSineData(vec);
 
      chart.updateXYSeries("uso", data[0], data[1], null);
      sw.repaintChart();
    

     
      
      
    
         return false;
    }
    
    private  double[][] getSineData(float[] phase) throws InterruptedException {
 
        
    double[] xData = new double[15];
    double[] yData = new double[15];
    for (int i = 0; i < xData.length; i++) {
            
           
           
      xData[i] = cont;
      yData[i] =Double.valueOf(format.format(phase[i]).replaceAll(",", "."));
      cont++;
        if(phase[i]>85)
        {
            JOptionPane.showMessageDialog(null, "porcentaje de memoria al"+phase[i], "alert", JOptionPane.WARNING_MESSAGE);
       if(phase[i]==100)
       {
            JOptionPane.showMessageDialog(null, "porcentaje de memoria al 100% por favor cierre la ventana de grafico", "alert", JOptionPane.WARNING_MESSAGE);
       }
        }
      
    }
    return new double[][] { xData, yData };
  }
    

}

