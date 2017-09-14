/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventanas;

import control.Control;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import modelo.Table;
import modelo.TableSpace;

/**
 *
 * @author cesar
 */
public class Grafico extends JFrame implements ActionListener {
     private JPanel pan_prc, pArriba,pt;
     private Control gestor;
     private Tabla tabla1;
     private JLabel porcent,ch,warning;
     private String[] numb;
     private JButton boton;
     private int hwm;
     TableSpace ts,ta2;
     private ArrayList<TableSpace> ta1;
     Color[] colors;
      int[] param;
    public Grafico(Vent1 ven,ArrayList<TableSpace> tabl1,TableSpace aux2,Control c)
    {
        super("tablespace");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
     ven.dispose();   
     
     ta1=tabl1;
     ta2 = aux2;
     pan_prc= new JPanel(); 
     pArriba = new JPanel();
     pt = new JPanel();
     boton= new JButton();
     gestor=c;
      numb= new String[]{"0%","10%","20%","30%","40%","50%","60%","70%","80%","90%","100%"};
       setSize(600,300);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
       
    }
    
    public void init(TableSpace tab_graf,int hw,int d_hwm,int d_tot)
    {
        ts=tab_graf;
        hwm = hw;
        int j=0;
        if(d_hwm<0)
        {
            d_hwm=0;
        }
        if(d_tot<0)
        {
            d_tot=0;
        }
        GridBagLayout tb= new GridBagLayout();
        pan_prc.setLayout(tb);
        pt.setLayout(tb);
        pArriba.setLayout(tb);     
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets=new Insets(0,0,10,10);
        for(int i=0;i<11;i++)// no tocar 
        {
            gc.gridx=i;
            gc.gridy=0;
            
           ch=new JLabel(numb[j]);

            pan_prc.add(ch,gc);     
            j++;
        }
      
           
        
      // aqui van los paneles de los datos y la tabla 
         //PANEL DE ARRIBA 
        gc.insets = new Insets(2, 4, 2, 4);
        gc.gridx = 0;
        gc.gridy = 0;
      
        pArriba.add(new JLabel("INFORMACION:"), gc);
        gc.gridx = 0;
        gc.gridy = 1;
      
        pArriba.add(new JLabel("Dias para alcanzar el HWM :"+d_hwm), gc);
        gc.gridx = 0;
        gc.gridy = 2;
      
        pArriba.add(new JLabel("dias para alcanzar el total: "+d_tot), gc);
        
        gc.gridx = 1;
        gc.gridy = 0;
        pArriba.add(new JLabel("TableSpace: "+ts.getNombre() ), gc);
        
            
        gc.gridx = 1;
        gc.gridy = 1;
        warning=new JLabel("memoria en uso: "+Float.toString(ts.getUso())+" mb");
        pArriba.add(warning, gc);
        
         gc.gridx = 2;
        gc.gridy = 1;
        warning=new JLabel("porc. memoria en uso: "+Float.toString(ts.porcent_use())+"%");
        pArriba.add(warning, gc);
        
        gc.gridx = 1;
        gc.gridy = 2;       
        pArriba.add(new JLabel("memoria libre: "+Float.toString(ts.getFree())+" mb"), gc);
        gc.gridx = 2;
        gc.gridy = 2;       
        pArriba.add(new JLabel("porc. memoria libre: "+Float.toString(ts.porcent_free())+"%"), gc);
       
        gc.gridx = 2;
        gc.gridy = 0;
        pArriba.add(new JLabel("memoria total: "+Float.toString(ts.getTam_total())+" mb"), gc);
        
        gc.gridx=2;
        gc.gridy=4;
        boton= new JButton("Atras");
        boton.setActionCommand("Atras");
        boton.addActionListener(this);
        pArriba.add(boton,gc);
        
        gc.gridx=1;
        gc.gridy=4;
        boton= new JButton("Mas información");
        boton.setActionCommand("Mas información");
        boton.addActionListener(this);
        pArriba.add(boton,gc);
        
        add(pArriba,BorderLayout.NORTH);
        
        add(pan_prc,BorderLayout.SOUTH);
       
    }
    public void paint( Graphics g )
{ 
    super.paint( g );  // llamar al método paint de la superclase
 
   param=new int[]{125,150,185,218,250,283,315,350,380,415,450};
    g.setColor(Color.LIGHT_GRAY);
    for(int i=0;i<11;i++)
    {
        g.drawLine(param[i],150, param[i], 300);
    }
         if(hwm <= ts.porcent_use())
    {
        try {
            gestor.GuardarHist(ts.getNombre(),ts.getUso(), ts.porcent_use());
            warning.setForeground(Color.red);
         } catch (SQLException ex) {
            Logger.getLogger(Grafico.class.getName()).log(Level.SEVERE, null, ex);
        }
     
    }
    hwm=posHWM(hwm);
    g.setColor(Color.RED);
    System.out.println(hwm);
    g.drawLine(hwm,150, hwm, 300);// variable cambiable
    
    int aux;   
    g.setColor(Color.GREEN);
    g.drawRect(125,200,325, 20);// ver eje x
    aux=locuse((int)ts.porcent_use());
    g.fillRect(125,200, aux, 20);
    
  
   
     
} 
 
    private int locuse(int porc)
    {
        int aux=((322*porc)/100)+1;
       
        return aux;
    }
    
   int posHWM(int por)
   {
       int rang=param[(por/10)+1]-param[por/10];
       int dif=(por%10)*10;
       return param[por/10]+(dif*rang/100);
       
   }

    @Override
    public void actionPerformed(ActionEvent e) {
          try {
            if(e.getActionCommand().equals("Mas información"))
            {
            tabla1 = new Tabla(ta1,ta2,this,this.getLocation().getX(),this.getLocation().getY()+200);
            
            
            }
            else
                if(e.getActionCommand().equals("Atras"))
            {
                this.dispose();
            gestor.atras();
            
            }
        } catch (Exception ex) {
            Logger.getLogger(Tabla.class.getName()).log(Level.SEVERE, null, ex);
        } 
}

    public void atras (char ban) throws InterruptedException
  {
      if(ban=='v')
      {
        tabla1.dispose();  
      }
  }

    
}
 

