/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventanas;

import control.Control;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import modelo.TableSpace;

/**
 *
 * @author cesar
 */
public class Vent1 extends JFrame implements ActionListener  {
    
    private JPanel panel;
     private Button aceptar;
    private Control gestor;
   JTable tabla;
   private ModeloTabla2 table;
  private  ArrayList<TableSpace> TaSpa;
    public Vent1(Control c)
    {
        super("tablespace");
        gestor=c;
        panel= new JPanel();   
        tabla=new JTable();
        table= new ModeloTabla2();
        
        
    }
    
    public void init(ArrayList<TableSpace> TaSpa)
    {
        tabla.setModel(table);
        this.TaSpa=TaSpa;
        tabla.addMouseListener(new MouseAdapter() 
        {
            
            
            public void mouseClicked(MouseEvent e)
            {
                if(e.getClickCount()==1)
                {
                   int row= tabla.getSelectedRow();
                    int colum=tabla.getSelectedColumn();
                    System.out.println(tabla.getValueAt(2, 1).toString());
                  if(colum ==0)
                {
                   
                       try {
                           gestor.iniciarVent2(tabla.getValueAt(row, 0).toString(),(int)Float.parseFloat(tabla.getValueAt(row, 1).toString()));
                       } catch (InterruptedException ex) {
                           Logger.getLogger(Vent1.class.getName()).log(Level.SEVERE, null, ex);
                       } catch (SQLException ex) {
                           Logger.getLogger(Vent1.class.getName()).log(Level.SEVERE, null, ex);
                       } catch (IOException ex) {
                           Logger.getLogger(Vent1.class.getName()).log(Level.SEVERE, null, ex);
                       }
                }
                }
                  
                
                
            }
        });
        JScrollPane desplazamientoTabla = new JScrollPane(
                  ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                  ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        desplazamientoTabla.setViewportView(tabla);
          this.gestor=gestor;
        String[] columns = new String[]{
            //"Id", "N", "Hourly Rate", "Part Time" 
            "Nombre","HWM"
        };
        
        Object[][] data = new Object[TaSpa.size()+1][];
        for (int i = 0; i < TaSpa.size(); i++) {
            
            table.addRow(
                     new Object[]{                      
                        TaSpa.get(i).getNombre(),
                        TaSpa.get(i).getTam_total()
                    });
            
        }
           
         
       panel.add(BorderLayout.CENTER,desplazamientoTabla);

       
        
        
       
        
        GridBagConstraints gc = new GridBagConstraints();

        gc.insets=new Insets(10,10,0,50);
        
      
        JPanel p_opc=new JPanel();
        JButton b_config= new JButton("guardar HWM");
        b_config.setActionCommand("guardar");
        b_config.addActionListener(this);
        p_opc.add(b_config,BorderLayout.CENTER);
        b_config= new JButton("Hist. HWM");
        b_config.setActionCommand("hist");
        b_config.addActionListener(this);
        p_opc.add(b_config,BorderLayout.CENTER);
        add(panel,BorderLayout.CENTER);
        add(p_opc,BorderLayout.SOUTH);
       setSize(500,300);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            if(e.getActionCommand().endsWith("guardar"))
            {
                 for (int i = 0; i < TaSpa.size(); i++) {
                    
            TaSpa.get(i).setTam_total(Float.parseFloat( tabla.getValueAt(i, 1).toString()));
            
        }
                gestor.guardarHWM(TaSpa);
                JOptionPane.showMessageDialog(null, "Configuracion guardada con exito", "confirmacion", JOptionPane.INFORMATION_MESSAGE);

            }
            else
                if(e.getActionCommand().endsWith("hist"))
                {
                    this.dispose();
                    Historial his= new Historial(gestor);
                }
           
        } catch (Exception ex) {
            Logger.getLogger(Vent1.class.getName()).log(Level.SEVERE, null, ex);
        }
				
    }
    
class ModeloTabla2 extends DefaultTableModel {

        public ModeloTabla2() {
            super(new Object[][]{},
                    new String[]{            
            "Nombre","HWM"});
            
            }
        
      
    }

  
    
    
}
