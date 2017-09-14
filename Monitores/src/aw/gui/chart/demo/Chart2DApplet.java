/*
 * 
 *  Chart2DApplet.java  jchart2d
 *  Copyright (C) Achim Westermann, created on 11.12.2004, 00:36:22  
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *  
 *  If you modify or optimize the code in a useful way please let me know.
 *  Achim.Westermann@gmx.de
 *	
 */
package aw.gui.chart.demo;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import aw.gui.chart.AbstractDataCollector;
import aw.gui.chart.Chart2D;
import aw.gui.chart.RandomDataCollector;
import aw.gui.chart.Trace2DLtd;
import aw.util.Range;

/**
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 *
 */
public class Chart2DApplet extends Applet {

  Chart2D chart;
  AbstractDataCollector collector;
  Trace2DLtd trace;
  
  private boolean running = false;
  /**
   * @throws java.awt.HeadlessException
   */
  public Chart2DApplet() throws HeadlessException {
    super();
  }

  /* (non-Javadoc)
   * @see java.applet.Applet#init()
   */
  public void init() {
    super.init();
    this.chart = new Chart2D();
    chart.setGridX(true);
    chart.setDecimalsX(0);
    chart.setGridY(true);
    chart.setForceYRange(new Range(-20,+20));
    chart.setGridColor(Color.LIGHT_GRAY);
    this.trace = new Trace2DLtd(100);
    trace.setName("random");
    trace.setPhysicalUnits("Milliseconds","random value");
    trace.setColor(Color.RED);
    chart.addTrace(trace);
    this.collector = new RandomDataCollector(trace,60);
    this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
    this.add(chart);
    this.add(new ControlPanel());
  }
  
  public synchronized void startData(){
    if(!this.running){
    new Thread(this.collector).start();    
    this.running = true;
    }
  }
  
  public synchronized void stopData(){
    if(this.running){
      this.collector.stop();
      this.running = false;
    }
  }
  
  class ControlPanel extends JPanel{
    JSlider amountPointsSlider;
    JSlider latencyTimeSlider;
    JButton startStop;
    
    ControlPanel(){
      this.setBackground(Color.WHITE);
      // amountPointsSlider
      this.amountPointsSlider = new JSlider(10,410);
      this.amountPointsSlider.setBackground(Color.WHITE);
      // find the value of max points:
      int maxPoints = Chart2DApplet.this.trace.getMaxSize();
      this.amountPointsSlider.setValue(maxPoints);
      this.amountPointsSlider.setMajorTickSpacing(40);
      this.amountPointsSlider.setMinorTickSpacing(20);
      this.amountPointsSlider.setSnapToTicks(true);
      this.amountPointsSlider.setPaintLabels(true);
      this.amountPointsSlider.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Amount of points.",TitledBorder.LEFT,TitledBorder.BELOW_TOP));
      this.amountPointsSlider.setPaintTicks(true);
      this.amountPointsSlider.addChangeListener(
        new ChangeListener(){
          public void stateChanged(ChangeEvent e){
            JSlider source = (JSlider)e.getSource();
            // Only if not currently dragged...
            if(!source.getValueIsAdjusting()){
              int value = source.getValue();
              Chart2DApplet.this.trace.setMaxSize(value);
            }
          }
        }
      );
      
      // Latency slider:
      this.latencyTimeSlider = new JSlider(10,210);
      this.latencyTimeSlider.setBackground(Color.WHITE);
      this.latencyTimeSlider.setValue((int)Chart2DApplet.this.collector.getLatency());
      this.latencyTimeSlider.setMajorTickSpacing(50);
      this.latencyTimeSlider.setMinorTickSpacing(10);
      this.latencyTimeSlider.setSnapToTicks(true);
      this.latencyTimeSlider.setPaintLabels(true);
      this.latencyTimeSlider.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Latency for adding points.",TitledBorder.LEFT,TitledBorder.BELOW_TOP));
      this.latencyTimeSlider.setPaintTicks(true);

      this.latencyTimeSlider.addChangeListener(
        new ChangeListener(){
          public void stateChanged(ChangeEvent e){
            JSlider source = (JSlider)e.getSource();
            // Only if not currently dragged...
            if(!source.getValueIsAdjusting()){
              int value = source.getValue();
              Chart2DApplet.this.collector.setLatency(value);
            }
          }
        }
      );
      
      // Start stop Button
      this.startStop = new JButton("start");
      this.startStop.setBackground(Color.WHITE);
      this.startStop.addActionListener(
        new ActionListener(){
          public void actionPerformed(ActionEvent e){     
          JButton source = (JButton)e.getSource(); 
            if(Chart2DApplet.this.running){
              Chart2DApplet.this.stopData();
              source.setText("start");
            }
            else{
              Chart2DApplet.this.startData();
              source.setText("stop");              
            }
            source.invalidate();
            source.repaint();
          }
        }
      );
      
      // Layouting: Vertical Grid Layout for putting the sliders...
      this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
      this.add(this.amountPointsSlider);
      this.add(this.latencyTimeSlider);
      // GridLayout stretches components (no respect for their preferred size): 
      // Small trick by inserting another component with different Layout. 
      JComponent stretch = new JPanel();
      stretch.setBackground(Color.WHITE);
      stretch.setLayout(new BoxLayout(stretch,BoxLayout.X_AXIS));
      stretch.add(Box.createHorizontalGlue());
      stretch.add(startStop);
      stretch.add(Box.createHorizontalGlue());
      this.add(stretch);
    }
  }
  
  // Test for development: run as app to save time moving files and resetting browser cache...
  
  public static void main(String[]args){
    JFrame frame = new JFrame();
    Container contentpane = frame.getContentPane();
    contentpane.setLayout(new GridLayout(1,1));
    Chart2DApplet applet = new Chart2DApplet();
    applet.init();
    contentpane.add(applet);
    frame.setSize(300,400);
    frame.addWindowListener(
        new WindowAdapter(){
          public void windowClosing(WindowEvent e){
              System.exit(0);
          }
        }
      );
    frame.show();
    frame.invalidate();
    frame.repaint();
    
  }
}
