/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitores;

import Control.Control;
import java.sql.SQLException;

/**
 *
 * @author cesar
 */
public class Monitores {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) throws InterruptedException, SQLException{
         Control gestor = new Control();
        gestor.iniciar();
    }
    
}
