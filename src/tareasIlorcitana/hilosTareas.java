/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tareasIlorcitana;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import satation.Propiedades;

/**
 *
 * @author Diseño
 */
public class hilosTareas extends Thread {

    // boolean que pondremos a false cuando queramos parar el hilo
    private boolean continuar = true;
    private boolean cont = true;
    Tareas_Principal p;
    boolean actualiza;
    

    public hilosTareas() throws SQLException {
        this.p = new Tareas_Principal();
    }

    // metodo para poner el boolean a false.
    public void detenElHilo() {
        continuar = false;
        //cont=false;
    }

    // metodo para poner el boolean a false.
    public void encenderElHilo() {
        continuar = true;
    }

    /**
     * Lee el archivo fioltrado para saber a que usuario va destinado el panel.
     *
     * @return
     */
    public boolean leeActual() {
        return Propiedades.getPropiedad("modificado").equals("true");
    }

    //metodo para recoger la feecha.
    public String CogerFecha() {
        Date fechaActual = new Date();
        String strDateFormat = "hh: mm: ss dd-MMM"; // El formato de fecha está especificado  
        SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);
        String fecha = objSDF.format(fechaActual);
        return fecha;
    }

    private void modificaTxt() throws IOException {
        Propiedades.setPropiedad("modificado", "false");
    }

    // Metodo del hilo
    @Override
    public void run() {
        boolean primer0 = true;
        p.setVisible(true);
        p.leeFiltrado();
        p.llenarComboMaquinas();
        while (cont) {
            Calendar fecha = Calendar.getInstance();
            
            if (leeActual() == true ) {
                try {
                    modificaTxt();
                    p.mostrarTabla();
                } catch (IOException ex) {
                    Logger.getLogger(hilosTareas.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if(p.isVisible()==false){
                cont=false;
            }
            
        }
    }

}
