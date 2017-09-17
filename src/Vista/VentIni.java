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
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author cesar
 */
public class VentIni extends JFrame implements ActionListener {
    private JButton ini,BHWM,CHWM;
    private Control gestor;
    private JPanel panel;

    public VentIni(Control c) {
        
         super("tablespace");
        gestor=c;
        panel= new JPanel();   
    }
    
    public void ini()
    {
         GridBagConstraints gc = new GridBagConstraints();

        gc.insets=new Insets(10,10,0,50);
        ini= new JButton("iniciar monitores");
        ini.setActionCommand("iniciar");
        ini.addActionListener(this);
        BHWM= new JButton("Bitácora HWM monitor SGA ");
        BHWM.setActionCommand("bita");
        BHWM.addActionListener(this);
        CHWM= new JButton("Cambiar HWM monitor SGA ");
        CHWM.setActionCommand("change");
        CHWM.addActionListener(this);
       
        panel.add(ini,BorderLayout.CENTER);
        panel.add(BHWM,BorderLayout.CENTER);
        panel.add(CHWM,BorderLayout.CENTER);
        this.add(panel,BorderLayout.CENTER);
        setSize(500,300);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    
        if(e.getActionCommand().equals("iniciar"))
        {
            try {
                this.dispose();
                gestor.inimon();
             
                
            } catch (SQLException ex) {
                Logger.getLogger(VentIni.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(VentIni.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(VentIni.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
            if(e.getActionCommand().equals("change"))
            {
                this.dispose();
                HWMSGA ven= new HWMSGA(gestor);
                ven.ini();
              
            }
    }
    
}
