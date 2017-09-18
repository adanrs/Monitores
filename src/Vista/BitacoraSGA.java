/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Control.Control;
import Modelo.Bitacora;
import Modelo.TableSpace;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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

/**
 *
 * @author cesar
 */
public class BitacoraSGA extends JFrame implements ActionListener{
    private Control gestor;
    private JTextField porce;
    private JPanel central;
    private ModeloTabla3 tabla;
    JTable table;

    public BitacoraSGA(Control ges, ArrayList<Bitacora> bit) {
       
        gestor=ges;
        central= new JPanel();
        tabla= new ModeloTabla3();
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
    
        
        //add the table to the frame
//        
        dibujar(bit);
        this.setTitle("Ventana de Bitacoras de sobrepaso de HWM monitor SGA");
        this.setSize(600, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }
    
    private void dibujar(ArrayList<Bitacora> bit)
    {
        
       
        for (int i = 0; i < bit.size(); i++) {
           
            tabla.addRow(
                     new Object[]{
                        bit.get(i).getFecha(),
                        bit.get(i).getHora(),
                        bit.get(i).getSql(),
                        bit.get(i).getUsuario(),
                        bit.get(i).getMaquina()
                    });
          
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("atras"))
        {
            this.dispose();
            gestor.iniciar();
        }
    }
    
    class ModeloTabla3 extends DefaultTableModel {

        public ModeloTabla3() {
            super(new Object[][]{},
                    new String[]{            
            "Fecha evento","hora evento","query utilizado", "usuario que lo utilizo","maquina que lo ejecuto"});
            
            }
        
        @Override
        public boolean isCellEditable(int filas, int columnas)
        {
            return false;
        }
    }
    
}
