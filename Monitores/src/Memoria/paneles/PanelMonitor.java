/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paneles;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
/**
 *
 * @author adan-
 */


public class PanelMonitor extends JPanel implements Runnable {

    public PanelMonitor() {
        hiloMonitor = new Thread(this);
        hiloMonitor.start();
    }

    @Override
    //aqui debe hacer la conexion con la query y mandarla al panel.
    public void run() {
        while (hiloMonitor == Thread.currentThread()) {
            try {
                repaint();
                Thread.sleep(1000);
                if(incrementador <= 420 && incrementador >= 20){
                incrementador -= randIncremento();
                } else {
                    incrementador +=20;
                }
                contador +=5;
            } catch (InterruptedException ex) {
                System.out.println("ERROR!");
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        update(g);
    }
    
    @Override
     public void update(Graphics g){
        bibujarMonitor(g);
     }

    public void bibujarMonitor(Graphics g) {
       g.setColor(random());
       g.fillRect(contador,incrementador,5,420-incrementador);
    }
    
    public Color random(){ 
        if(incrementador <= 420 && incrementador >200){
            return new Color(0,255,0);
        }
        if(incrementador <= 200 && incrementador >150){
            return new Color(255,255,0);
        }
        if(incrementador <= 150 && incrementador >50){
            return new Color(255,128,0);
        } else{
            return new Color(255,0,0);
        }
        
    }
    
    public double randIncremento(){
        double valorEntero = Math.floor(Math.random()*(-40-90+1)+90);  // Valor entre M y N, ambos incluido
        System.out.println(incrementador);
        return valorEntero;
    }
    
    private Thread hiloMonitor;
     
    private int incrementador = 420 ;
    private int contador = 70;
     int  numero = (int) (Math.random() * 10) + 1;
}