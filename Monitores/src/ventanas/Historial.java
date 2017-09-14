/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventanas;

import control.Control;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import modelo.TableSpace;

/**
 *
 * @author cesar
 */
public class Historial extends JFrame implements ActionListener {
    private Control gestor;
    private JTextField porce;
    private JPanel central;
    private ArrayList<TableSpace> ts;
    private ModeloTabla1 tabla;
    JTable table;
    public  Historial(Control ges)
    {
        JPanel arri=new JPanel();
        JLabel leg= new JLabel("inserte el nombre de la tabla a consultar su registro de hwm: ");
         porce= new JTextField(10);
         JButton aceptar= new JButton("aceptar");
        aceptar.setActionCommand("aceptar");
        aceptar.addActionListener(this);
        
        arri.add(leg,BorderLayout.CENTER);
        arri.add(porce,BorderLayout.CENTER);
        arri.add(aceptar,BorderLayout.CENTER);
        gestor=ges;
        central= new JPanel();
        tabla= new ModeloTabla1();
        central.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        central.setLayout(new BorderLayout());
        JScrollPane desplazamientoTabla = new JScrollPane(
                  ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                  ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
         table = new JTable();        
        table.setModel(tabla);
        desplazamientoTabla.setViewportView(table);
       central.add(BorderLayout.CENTER,desplazamientoTabla);

        JButton atras= new JButton("atras");
        JPanel p_atras= new JPanel();
        atras.setActionCommand("atras");
        atras.addActionListener(this);
        p_atras.add(atras,BorderLayout.CENTER);
        add(p_atras,BorderLayout.SOUTH);
        add(central,BorderLayout.CENTER);
        add(arri,BorderLayout.NORTH);
        
        //add the table to the frame
//        

        this.setTitle("Historial HWM");
        this.setSize(600, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }

    private void dibujar()
    {
        
        int aux=tabla.getRowCount();
        for (int i = 0; i < aux; i++) {
           
          tabla.removeRow(0);
          
        }
        
            
        for (int i = 0; i < ts.size(); i++) {
           
            tabla.addRow(
                     new Object[]{
                        ts.get(i).getFecha(),
                        ts.get(i).getNombre(),
                        ts.get(i).getUso(),
                        ts.get(i).getTam_total()                     
                    });
          
        }

           

        
       
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("atras"))
        {
            this.dispose();
            try {
                gestor.atras();
            } catch (InterruptedException ex) {
                Logger.getLogger(Historial.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Historial.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        if(e.getActionCommand().equals("aceptar"))
        {
            
            ts=null;
            
        
            try {
                ts=gestor.cargarHist(porce.getText().toUpperCase());//.toUpperCase()
            } catch (SQLException ex) {
                Logger.getLogger(Historial.class.getName()).log(Level.SEVERE, null, ex);
            }
            dibujar();
            porce.setText("");
            
        }
    }
    
}
 class ModeloTabla1 extends DefaultTableModel {

        public ModeloTabla1() {
            super(new Object[][]{},
                    new String[]{            
            "Fecha","Nombre","cantidad MB", "porcentaje"});
            
            }
        
        @Override
        public boolean isCellEditable(int filas, int columnas)
        {
            return false;
        }
    }
