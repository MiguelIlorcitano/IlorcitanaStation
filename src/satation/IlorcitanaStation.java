/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package satation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JOptionPane;
import tareasIlorcitana.principalTareas;

/**
 *
 * @author Diseño
 */
public class IlorcitanaStation {
    
    static String filtrado[]= new String[2];
    static String usuario;

    public IlorcitanaStation() {
    }
    
    /**
     * Lee el archivo fioltrado para saber a que usuario va destinado el panel.
     */
   public static void leeFiltrado() {
        String linea = null;
        int numF=0;
        try {
            try (BufferedReader leerfichero = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\IlorcitanaStation\\Tareas\\filtrado.txt"), "utf-8"));) {
                while ((linea = leerfichero.readLine())!=null) {
                    if(numF==1 && linea.contains("progra")){
                            filtrado[numF]="programación";
                    }else  if(numF==1 && linea.contains("mec")){
                            filtrado[numF]="mecánica";
                    }else{
                        filtrado[numF] = linea;                     
                    }
                    numF++;
                }  
            }
        } catch (IOException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        leeFiltrado();
        if(filtrado[1].equals("administrador")){
            PanelStation panel = new PanelStation();
            panel.setVisible(true);
        }else{
            principalTareas prin = new principalTareas();
        }
        
    }
    
}
