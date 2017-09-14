/*
 * 
 *  Showcase.java  jchart2d
 *  Copyright (C) Achim Westermann, created on 10.12.2004, 13:48:55  
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

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import aw.gui.chart.AbstractDataCollector;
import aw.gui.chart.Chart2D;
import aw.gui.chart.RandomDataCollector;
import aw.gui.chart.Trace2DLtd;
import aw.util.Range;

/**
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 *
 */
public class Showcase extends JFrame{
  
  

  Chart2D chart;
  AbstractDataCollector collector;
  Trace2DLtd trace; 
  
  private boolean running = false;
  
  public Showcase() {
    super();
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
    Container content = this.getContentPane();
    content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
    content.add(chart);
    content.add(new ControlPanel());
    this.collector = new RandomDataCollector(trace,50);
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
    JButton snapshot;
    JFileChooser filechooser;
    
    ControlPanel(){
      this.setBackground(Color.WHITE);
      // amountPointsSlider
      this.amountPointsSlider = new JSlider(10,410);
      this.amountPointsSlider.setBackground(Color.WHITE);
      // find the value of max points:
      int maxPoints = Showcase.this.trace.getMaxSize();
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
              Showcase.this.trace.setMaxSize(value);
            }
          }
        }
      );
      
      // Latency slider:
      this.latencyTimeSlider = new JSlider(10,210);
      this.latencyTimeSlider.setBackground(Color.WHITE);
      this.latencyTimeSlider.setValue((int)Showcase.this.collector.getLatency());
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
              Showcase.this.collector.setLatency(value);
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
            if(Showcase.this.running){
              Showcase.this.stopData();
              source.setText("start");
            }
            else{
              Showcase.this.startData();
              source.setText("stop");              
            }
            source.invalidate();
            source.repaint();
          }
        }
      );
      
      // the JFileChooser for saving snapshots:
      this.filechooser = new JFileChooser();
      this.filechooser.setAcceptAllFileFilterUsed(false);
      // the button for snapshot:
      this.snapshot = new JButton("snapshot");
      this.snapshot.setBackground(Color.WHITE);
      this.snapshot.addActionListener( 
          new ActionListener(){
            public void actionPerformed(ActionEvent e){
              // Immediately get the image:
              BufferedImage img = Showcase.this.chart.snapShot();
             // clear file filters (uncool API)
              FileFilter[] farr = filechooser.getChoosableFileFilters();
              for(int i=0;i<farr.length;i++){
                filechooser.removeChoosableFileFilter(farr[i]);
              }
              // collect capable writers by format name (API even gets worse!)
              String[] encodings = ImageIO.getWriterFormatNames();
              Set writers = new TreeSet();
              ImageTypeSpecifier spec = ImageTypeSpecifier.createFromRenderedImage(img);
              Iterator itWriters;
              for(int i=0;i<encodings.length;i++){
                itWriters = ImageIO.getImageWriters(spec,encodings[i]);
                if(itWriters.hasNext()){
                  writers.add(encodings[i].toLowerCase());
                }
              }
              // add the file filters:
              itWriters = writers.iterator();
              String encoding;
              while(itWriters.hasNext()){
                encoding = (String)itWriters.next();
                filechooser.addChoosableFileFilter(new FileFilterExtensions(new String[]{encoding}));
              }
              
              
              int ret = ControlPanel.this.filechooser.showSaveDialog(Showcase.this);
              if (ret == JFileChooser.APPROVE_OPTION) {
                File file = ControlPanel.this.filechooser.getSelectedFile();
                // get the encoding
                encoding = ControlPanel.this.filechooser.getFileFilter().getDescription().substring(2);
                try {
                  ImageIO.write(img,encoding,new File(file.getAbsolutePath()+"."+encoding));
                } catch (IOException e1) {
                  e1.printStackTrace();
                }
              } 
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
      stretch.add(this.snapshot);
      stretch.add(Box.createHorizontalGlue());
      this.add(stretch);
    }
  }
  public static void main(String[]args){
    JFrame frame = new Showcase();
    // add the chart to the frame:
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
  }
}
