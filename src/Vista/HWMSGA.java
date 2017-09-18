/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Control.Control;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author cesar
 */
public class HWMSGA extends JFrame implements ActionListener {
    private Control gestor;
JTextField BHWM;
    public HWMSGA(Control g) {
         super("HWM monitor SGA");
        gestor=g;
    }
    
    
      public void ini()
    {
       
         GridBagConstraints gc = new GridBagConstraints();

        gc.insets=new Insets(10,10,0,50);
        JLabel ini= new JLabel("inserte el valor de HWM para el monitor de SGA");
        
       BHWM= new JTextField(10);
      
        JButton CHWM= new JButton("Guardar HWM monitor SGA ");
        CHWM.setActionCommand("guardar");
        CHWM.addActionListener(this);
       JPanel panel= new JPanel();
        panel.add(ini,BorderLayout.CENTER);
        panel.add(BHWM,BorderLayout.CENTER);
        panel.add(CHWM,BorderLayout.CENTER);
        this.add(panel,BorderLayout.CENTER);
        setSize(600,300);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("guardar"))
        {
                boolean bandera=true;
                try  
               {  
                  int d = Integer.parseInt(BHWM.getText());  
               }  
                     catch(NumberFormatException nfe)  
                     {  
                         bandera=false;
                      }  
                            try {
                           if(!bandera)
                           {
                                   JOptionPane.showMessageDialog(null, "Dato ingresado no es un numero", "confirmacion", JOptionPane.ERROR_MESSAGE);
                                   BHWM.setText("");
                            }
                           else
                           {
                                this.dispose();
                                gestor.guardarHWMSGA(BHWM.getText());
                                VentIni ven= new VentIni(gestor);
                                ven.ini();
                           }
               
                  
            } catch (IOException ex) {
                Logger.getLogger(HWMSGA.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    
    }

    
}
