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
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.TableSpace;

/**
 *
 * @author carmc_000
 */
public class Tabla extends JFrame implements ActionListener {
    private Grafico graf;

    public Tabla(ArrayList<TableSpace> ts,TableSpace tab,Grafico graf,double x, double y) throws SQLException {
        //headers for the table
        this.graf=graf;
        String[] columns = new String[]{
            //"Id", "N", "Hourly Rate", "Part Time" 
            "Fecha","Nombre","Mb index", "Mb usado", "Tasa Consumo","registros"
        };
        
        Object[][] data = new Object[ts.size()+1][];
        for (int i = 0; i < ts.size(); i++) {
            data[i]
                    = new Object[]{
                        ts.get(i).getFecha(),
                        ts.get(i).getNombre(),
                        ts.get(i).getUso(),
                        ts.get(i).getTam_total(),
                        ts.get(i).getTasatrans(),
                        ts.get(i).getFree()
                    };
          
        }
           data[ts.size()]
                    = new Object[]{
                        tab.getFecha(),
                        tab.getNombre(),
                        tab.getUso(),
                        tab.getTam_total(),
                        tab.getTasatrans(),
                        tab.getFree()
                    };
           

        //create table with data
        JTable table = new JTable(data, columns);

        JButton atras= new JButton("atras");
        JPanel p_atras= new JPanel();
        atras.setActionCommand("atras");
        atras.addActionListener(this);
        p_atras.add(atras,BorderLayout.CENTER);
        add(p_atras,BorderLayout.SOUTH);
        //add the table to the frame
        this.add(new JScrollPane(table));

        this.setTitle("Tabla");
        this.setSize(600, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    public String fecha() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String sDate = sdf.format(date);
        return sDate;
    }

        @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if(e.getActionCommand().equals("atras"))
            {
            graf.atras('v');
            }
        } catch (Exception ex) {
            Logger.getLogger(Tabla.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   
}
