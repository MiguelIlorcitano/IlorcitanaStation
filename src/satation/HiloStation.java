/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package satation;

import soporteActas.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Diseño
 */
public class HiloStation extends Thread {

    // boolean que pondremos a false cuando queramos parar el hilo
    private boolean continuar = true;
    private boolean cont = true;
    private boolean primero = true;
    
    panelActas p;
    boolean actualiza;

//    private Connection con;
//    private ResultSet rs;
//    private Statement st;

    ArrayList<String> anual;
    ArrayList<String> trimestral;
    ArrayList<String> mensual;
    ArrayList<String> semanal;
    ArrayList<String> descripcion_m;
    ArrayList<String> n_maquina;
    ArrayList<String> id_maquina;

    int hora, minutos, segundos;
    Calendar calendar;

    /**
     * Constructor de la clase.
     */
    public HiloStation() {
        anual = new ArrayList<>();
        trimestral = new ArrayList<>();
        mensual = new ArrayList<>();
        semanal = new ArrayList<>();
        descripcion_m = new ArrayList<>();
        n_maquina = new ArrayList<>();
        id_maquina = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HiloStation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método para detener el hilo.
     */
    public void detenElHilo() {
        continuar = false;
        //cont=false;
    }

    /**
     * Método para encender el hilo.
     */
    public void encenderElHilo() {
        continuar = true;
    }

    /**
     * Recoge la información de todas las máquinas en la variables globales tipo ArrayList.
     */
    private void extraeMaquinas() {
        try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id_maquina,M_Semanal,M_Mensual,M_Trimestral,M_Anual,descripcion,numero_maquina FROM maquinas")) {
            while (rs.next()) {
                id_maquina.add(rs.getString("id_maquina"));
                n_maquina.add(rs.getString("numero_maquina"));
                descripcion_m.add(rs.getString("descripcion"));
                anual.add(rs.getString("M_Anual"));
                trimestral.add(rs.getString("M_Trimestral"));
                mensual.add(rs.getString("M_Mensual"));
                semanal.add(rs.getString("M_Semanal"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los valores de la tabla maquinas extraeMaquinas kkkkkkkkkkkk. "+e);
        }
    }

    /**
     * Metodo mediante el cual comparamos la fecha que pasamos por parámetro con la fecha actual.
     * El formato de fecha a de ser "dd,mm,yyyy".
     * @param fc fecha a comparar.
     * @return 
     */
    private int compruebaFecha(String fc) {
        Date fecha_db = new Date();
        Date fecha_actual = new Date();

        String strDateFormat = "dd-MM-yyyy"; // El formato de fecha está especificado  
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

        try {
            fecha_actual = sdf.parse(sdf.format(new Date()));
            fecha_db = sdf.parse(fc);
        } catch (ParseException ex) {
            Logger.getLogger(HiloStation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fecha_actual.compareTo(fecha_db);
    }

    /**
     * Metodo mediante el cual comparamos dos fechas que pasamos por parámetro.
     * El formato de fecha a de ser "dd,mm,yyyy".
     * @param f1 fecha 1
     * @param f2 fecha 2
     * @return 
     */
    private int compruebaFecha(String f1, String f2) {
        String año = f1.split("-")[0];
        String mes = f1.split("-")[1];
        String dia = f1.split("-")[2].split(" ")[0];
        f1 = dia + "-" + mes + "-" + año;

        Date fecha_1 = new Date();
        Date fecha_2 = new Date();

        String strDateFormat = "dd-MM-yyyy"; // El formato de fecha está especificado  
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

        try {
            fecha_1 = sdf.parse(f1);
            fecha_2 = sdf.parse(f2);
        } catch (ParseException ex) {
            Logger.getLogger(HiloStation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fecha_1.compareTo(fecha_2);
    }

    /**
     * Metodo mediante el cual se crea una tarea según el tipo de mantenimento a realizar.
     * @param tarea se pasa un número para saber a que tipo de manteniento pertenece. 1=anual, 2=trimestral, 3=mensual y 4=semanal
     * @param id_m se pasa el id_maquina para saber en que máquina se crea la tarea.
     */
    private void creaTarea(int tarea, String id_m) {
        String mantenimiento = "";
        String observaciones = "";
        
        // Se recoden las ordenes de mantenimiento para crear la tarea
        switch (tarea) {
            case 1:
                try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT Codigo,Descripcion,Especificacion FROM Operaciones WHERE Periodicidad=\"anual\" AND id_maquina=" + Integer.parseInt(id_m))) {
                    while (rs.next()) {
                        observaciones = observaciones + rs.getString("Codigo") + "__" + rs.getString("Descripcion") + "__" + rs.getString("Especificacion") + " # ";
                        mantenimiento = "Revisión Anual";
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error al cargar los valores de la tabla Operaciones anual. " + e);
                }
                break;
            case 2:
                try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT Codigo,Descripcion,Especificacion FROM Operaciones WHERE Periodicidad=\"trimestral\" AND id_maquina=" + Integer.parseInt(id_m))) {
                    while (rs.next()) {
                        observaciones = observaciones + rs.getString("Codigo") + "__" + rs.getString("Descripcion") + "__" + rs.getString("Especificacion") + " # ";
                        mantenimiento = "Revisión Trimestral";
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error al cargar los valores de la tabla Operaciones trimestral. " + e);
                }
                break;
            case 3:
                try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT Codigo,Descripcion,Especificacion FROM Operaciones WHERE Periodicidad=\"mensual\" AND id_maquina=" + Integer.parseInt(id_m))) {
                    while (rs.next()) {
                        observaciones = observaciones + rs.getString("Codigo") + "__" + rs.getString("Descripcion") + "__" + rs.getString("Especificacion") + " # ";
                        mantenimiento = "Revisión Mensual";
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error al cargar los valores de la tabla Operaciones mensual. " + e);
                }
                break;
            case 4:
                try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT Codigo,Descripcion,Especificacion FROM Operaciones WHERE Periodicidad=\"semanal\" AND id_maquina=" + Integer.parseInt(id_m))) {
                    while (rs.next()) {
                        observaciones = observaciones + rs.getString("Codigo") + "__" + rs.getString("Descripcion") + "__" + rs.getString("Especificacion") + " # ";
                        mantenimiento = "Revisión Semanal";
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error al cargar los valores de la tabla Operaciones semanal. " + e);
                }
                break;
            default:
                break;
        }
         // Se crea la tarea.
        try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
            Statement stmt = conn.createStatement();) {
            stmt.executeUpdate("INSERT INTO tareas(usuario,tarea, tipo_tarea,tipo_problema, id_maquina, nivel_preferencia, estado, operaciones, tarea, tipo_operario) VALUES (\"0\",\"" + mantenimiento + "\",\"mantenimiento operario\",\"mantenimiento\",\"" + id_m + "\",\"prioritaria\",\"en espera\",\"\",\"" + observaciones + "\",\"operario\")");
            JOptionPane.showMessageDialog(null, "Tarea creada correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al intentar crear una tarea. " + e);
        }
    }

    /**
     * Metodo para comprobar si la tarea ha sido ya creada.
     * @param f fecha que se quiere comprobar
     * @param id_m id_maquina que se quiere comprobar.
     * @return
     */
    private boolean compruebaTarea(String f, String id_m , String tm) {
        boolean com = false;
        try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT estado FROM tareas WHERE id_maquina=\"" + id_m + "\" AND tarea=\"" + tm + "\"")) {
            while (rs.next()) {
                if ("en espera".equals(rs.getString("estado")) || "en proceso".equals(rs.getString("estado"))) {
                    com = true;
                }
            }
            return com;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los valores de la tabla Tareas-compruebaTareas. " + e);
            return com;
        }
    }
    
    /**
     * Metodo mediante el cual se llama a la clase que muestra el panel de mensajes para mantenimientos.
     * @param id_m este parámetro es para en número de máquina.
     * @param descip este parámetro es para las descripción de máquina.
     */
    private void compruebaRealizada(String id_m, String descip) {
        
        try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT tarea,estado FROM tareas WHERE id_maquina="+Integer.parseInt(id_m))) {
            while (rs.next()) {
                if (rs.getString("tarea").contains("Revisión Anual") && rs.getString("estado").equals("en espera")) {
                    panel_muestra panel = new panel_muestra(id_m,"anual",descip);
                    panel.setVisible(true);
                } else if (rs.getString("tarea").contains("Revisión Trimestral") && rs.getString("estado").equals("en espera")) {
                    panel_muestra panel = new panel_muestra(id_m,"trimestral",descip);
                    panel.setVisible(true);
                } else if (rs.getString("tarea").contains("Revisión Mensual") && rs.getString("estado").equals("en espera")) {
                    panel_muestra panel = new panel_muestra(id_m,"mensual",descip);
                    panel.setVisible(true);
                } else if (rs.getString("tarea").contains("Revisión Semanal") && rs.getString("estado").equals("en espera")) {
                    panel_muestra panel = new panel_muestra(id_m,"semanal",descip);
                    panel.setVisible(true);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los valores de la tabla Tareas-compruebaRealizada. " + e);
        }
    }
    
    /**
     * Método para comprobar que la máquina que se pasa como parámetro es igual que la que hay en 
     * el archivo de propiedades.
     * @param m número de máquina que se quiere comprar
     * @return 
     */
    private boolean compruebaMaquinas(String m){
        String aux = Propiedades.getPropiedad("numero_maquina");
        String aux2[]=aux.split(",");
        for (String aux21 : aux2) {
            if (aux21.equals(m)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Método mediante el cual el sistema busca si hay que crear una tarea o mostrar mensaje.
     * Desde este método se llaman a todos los demás
     */
    private void main() {
        
        //------------------------------------------------------------------------------------------------------------------------------------------------//
        //---------------------------- revisar esto para el tema de las fechas ---------------------------------------------------------------------------//
        //------------------------------------------------------------------------------------------------------------------------------------------------//
        
        extraeMaquinas();
        
        for (int i = 0; i < n_maquina.size(); i++) {
            // Comprueba que la máquina está en el archivo de configuarción
            if (compruebaMaquinas(n_maquina.get(i))) {
                if (compruebaFecha(anual.get(i)) == 0 || compruebaFecha(anual.get(i)) == 1) { //Comprueba que la fecha del mantenimiento es igual o menor a la fecha actual
                    if (compruebaTarea(anual.get(i), id_maquina.get(i),"Revisión Anual") == false ) { //comprueba si la tarea ya está creada sino la crea
                        creaTarea(1, id_maquina.get(i));
                    }
                }
                if (compruebaFecha(trimestral.get(i)) == 0 || compruebaFecha(trimestral.get(i)) == 1) {
                    if (compruebaTarea(trimestral.get(i), id_maquina.get(i),"Revisión Trimestral") == false) {
                        creaTarea(2, id_maquina.get(i));
                    }
                }
                if (compruebaFecha(mensual.get(i)) == 0 || compruebaFecha(mensual.get(i)) == 1) {
                    if (compruebaTarea(mensual.get(i), id_maquina.get(i),"Revisión Mensual") == false) {
                        creaTarea(3, id_maquina.get(i));
                    }
                }
                if (compruebaFecha(semanal.get(i)) == 0 || compruebaFecha(semanal.get(i)) == 1) {
                    if (compruebaTarea(semanal.get(i), id_maquina.get(i),"Revisión Semanal") == false) {
                        creaTarea(4, id_maquina.get(i));
                    }
                } 
                //Comprueba que las tareas enviadas con anterioridad se han realizado
                compruebaRealizada(id_maquina.get(i),descripcion_m.get(i));
            }
        }
    }

    // Metodo del hilo
    @Override
    public void run() {
        Calendar calendario;
        while (continuar) {
            calendario = Calendar.getInstance();
            hora = calendario.get(Calendar.HOUR_OF_DAY);
            if (hora == 7 && cont == true || hora == 15 && cont == true || primero==true) {
                primero=false;
                cont = false;
                main();
            } else if (hora != 7 && hora != 15) {
                cont = true;
            }
        }
    }
}
