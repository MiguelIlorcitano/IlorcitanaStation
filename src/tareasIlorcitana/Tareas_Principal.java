/**
 * En esta clase de tipo frame es donde se configura la interfaz gráfica principal y desde donde se ralizan las 
 * llamadas a las distintas clases.
 */

package tareasIlorcitana;

import java.awt.Color;
import java.awt.Font;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import satation.Main;
import satation.Propiedades;



/**
 *
 * @author Miguel Angel Carrillo Garcia
 */
public final class Tareas_Principal extends javax.swing.JFrame {
    
    private static ServerSocket SERVER_SOCKET;
    
    DefaultTableModel n;
    Panel_Mantenimiento pm=null;
    Panel_ModificaAdmin modA=null;
    Panel_Modifica mod=null;
    Panel_Nueva_Tarea nueva=null;
    
    String operario;            //Recoge el nombre del operario que realiza el mantenimiento.
    String maquina;             //Regoge la máquina donde se realiza dicho mantenimiento.
    String fecha;               //Recoge la fecha en la cual se realiza el mantenimiento.
    String horas;               //Recoge las horas de duración del mantenimiento.
    String tipoMantenimiento;   //Recoge el tipo de mantenimiento (preventivo o correctivo).
    String tipoProblemaM;       //Recoge si el problema ha sido mecácnico.
    String tipoProblemaH;       //Recoge si el problema ha sido hidráulico.
    String tipoProblemaN;       //Recoge si el problema ha sido neumático.
    String tipoProblemaE;       //Recoge si el problema ha sido eléctrico.
    String tipoProblema;        //Recoge si no tiene ningún problema solo mantenimiento.
    String tipoProblemaFinal;   //Recoge la suma de todos los problemas.
    String operaciones;         //Recoge las operaciones realizadas.
    String piezasSustituidas;   //Recoge las piezas sustituidas.
    String observaciones;       //Recoge las observaciones del usuario.
    int numeroActas;            //Recoge el número de actas a exportar qu epor defecto está en 10
    String filtrado= null;
    String consulta = null;
    int contador=0;
    
    public Tareas_Principal(String a) {
        initComponents();
        //Cierra la ventana pulsando escape
        KeyboardFocusManager kb = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        kb.addKeyEventPostProcessor(new KeyEventPostProcessor() {
            @Override
            public boolean postProcessKeyEvent(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE && this != null) {
                    dispose();
                    return false;
                }
                return true;
            }
        });
    }
    
    /**
     * Constructo
     * @throws java.sql.SQLException
     */
    public Tareas_Principal() throws SQLException {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);
        if(!Propiedades.getPropiedad("usuario").equals("administrador")){
            botonPrincipal.setVisible(false);
        }
        
        //Cierra la ventana pulsando escape
        KeyboardFocusManager kb = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        kb.addKeyEventPostProcessor(new KeyEventPostProcessor() {
            @Override
            public boolean postProcessKeyEvent(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE && this != null) {
                    dispose();
                    return false;
                }
                return true;
            }
        });
    }
    
    /**
    * Metodo para saber si los datos pasados son numéricos o no.
     * @param x pasa el string que queremos comprobar.
     * @return debuelve verdadero si es numérico y falso si no lo es.
    */
    public static boolean esNumero(String x){
        return (x.matches("[+-]?\\d*(\\.\\d+)?") && x.equals("")==false);
    }
    
    public void actualiza(String fech){
        jlReloj.setText(fech);
        repaint();
        if(fech.split(":")[2].split(" ")[1].equals("00")){
        }
    }
    
    /**
     * Lee el archivo filtrado para saber a que usuario va destinado el panel.
     */
    public void leeFiltrado() {
        if (Propiedades.getPropiedad("usuario").contains("progra")) {
            filtrado = "programación";
        } else if (Propiedades.getPropiedad("usuario").contains("mec")) {
            filtrado = "mecánica";
        } else if (Propiedades.getPropiedad("usuario").contains("ope")) {
            filtrado = "mantenimiento operario";
        } else if (Propiedades.getPropiedad("usuario").contains("admin")) {
            filtrado = "administrador";
        }
    }
   
    /**
     * LLena los combobox.
     */
    public void llenarComboMaquinas(){ 
        if(contador==0){
            String descripcion=null;
            try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT id_maquina,descripcion FROM maquinas")) {
                jCMaquina.setEnabled(true);
                jCMaquina.removeAllItems();
                while(rs.next()){
                    jCMaquina.addItem(rs.getString("descripcion"));
                }
                jCMaquina.setSelectedItem("Sin descripción");
                if(descripcion!=null){
                jCMaquina.setSelectedItem(descripcion);
                }
            }catch(SQLException e){
                JOptionPane.showMessageDialog(rootPane, e);
            }
        }
    }
   
    /**
     * Extrae el contenido de la base de datos y lo inserta en una jtable.
     */
    public void mostrarTabla() {
        preparaConsulta2();
        switch (filtrado) {
            case "mecánica":
                try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
                    Statement stmt = conn.createStatement();
                    ResultSet r = stmt.executeQuery(consulta)) {
                    String titulos[] = {"Id", "Tarea", "Máquina", "Tipo_Problema", "Preferencia", "Estado", "Fecha de tarea", "Observaciones"};
                    n = new DefaultTableModel(null, titulos);
                    String fila[] = new String[8];
                    while (r.next()) {
                        String tip = fila[0] = r.getString("tipo_tarea");
                        if (filtrado.equals(tip)) {
                            if (!"finalizado".equals(r.getString("estado"))) {
                                fila[0] = r.getString("Id_tarea");
                                fila[1] = r.getString("tarea");
                                fila[2] = r.getString("descripcion");
                                fila[3] = r.getString("tipo_problema");
                                fila[4] = r.getString("nivel_preferencia");
                                fila[5] = r.getString("estado");
                                fila[6] = r.getString("fecha_tarea");
                                fila[7] = r.getString("observaciones");
                                n.addRow(fila);
                            } else if ("finalizado".equals(r.getString("estado")) && "finalizado".equals(jCEstado.getSelectedItem().toString())) {
                                fila[0] = r.getString("Id_tarea");
                                fila[1] = r.getString("tarea");
                                fila[2] = r.getString("descripcion");
                                fila[3] = r.getString("tipo_problema");
                                fila[4] = r.getString("nivel_preferencia");
                                fila[5] = r.getString("estado");
                                fila[6] = r.getString("fecha_tarea");
                                fila[7] = r.getString("observaciones");
                                n.addRow(fila);
                            }
                        }
                    }
                    Tabla.setModel(n);
                    Tabla.setDefaultRenderer(Object.class, new Render());
                    Tabla.getColumnModel().getColumn(0).setPreferredWidth(50);
                    Tabla.getColumnModel().getColumn(1).setPreferredWidth(180);
                    Tabla.getColumnModel().getColumn(2).setPreferredWidth(180);
                    Tabla.getColumnModel().getColumn(3).setPreferredWidth(200);
                    Tabla.getColumnModel().getColumn(4).setPreferredWidth(150);
                    Tabla.getColumnModel().getColumn(5).setPreferredWidth(150);
                    Tabla.getColumnModel().getColumn(6).setPreferredWidth(170);
                    Tabla.getColumnModel().getColumn(7).setPreferredWidth(450);
                    JTableHeader th;
                    th = Tabla.getTableHeader();
                    Font fuente = new Font("Century Gothic", Font.BOLD, 14);
                    Color cl = new Color(0,102,102);
                    th.setForeground(cl);
                    th.setFont(fuente);
                    Tabla.setShowHorizontalLines(true);
                    Tabla.setShowVerticalLines(true);
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error al cargar los datos de la tabla Mantenimiento ");
                }
                break;
            case "programación":
                try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
                    Statement stmt = conn.createStatement();
                    ResultSet r = stmt.executeQuery(consulta)) {
                    String titulos[] = {"Id", "Tarea", "Máquina", "Tipo_Problema", "Preferencia", "Estado", "Fecha de tarea", "Observaciones"};
                    n = new DefaultTableModel(null, titulos);
                    String fila[] = new String[8];
                    while (r.next()) {
                        String tip = fila[0] = r.getString("tipo_tarea");
                        if (filtrado.equals(tip)) {
                            if (!"finalizado".equals(r.getString("estado"))) {
                                fila[0] = r.getString("Id_tarea");
                                fila[1] = r.getString("tarea");
                                fila[2] = r.getString("descripcion");
                                fila[3] = r.getString("tipo_problema");
                                fila[4] = r.getString("nivel_preferencia");
                                fila[5] = r.getString("estado");
                                fila[6] = r.getString("fecha_tarea");
                                fila[7] = r.getString("observaciones");
                                n.addRow(fila);
                            } else if ("finalizado".equals(r.getString("estado")) && "finalizado".equals(jCEstado.getSelectedItem().toString())) {
                                fila[0] = r.getString("Id_tarea");
                                fila[1] = r.getString("tarea");
                                fila[2] = r.getString("descripcion");
                                fila[3] = r.getString("tipo_problema");
                                fila[4] = r.getString("nivel_preferencia");
                                fila[5] = r.getString("estado");
                                fila[6] = r.getString("fecha_tarea");
                                fila[7] = r.getString("observaciones");
                                n.addRow(fila);
                            }
                        }
                    }
                    Tabla.setModel(n);
                    Tabla.setDefaultRenderer(Object.class, new Render());
                    Tabla.getColumnModel().getColumn(0).setPreferredWidth(50);
                    Tabla.getColumnModel().getColumn(1).setPreferredWidth(180);
                    Tabla.getColumnModel().getColumn(2).setPreferredWidth(180);
                    Tabla.getColumnModel().getColumn(3).setPreferredWidth(200);
                    Tabla.getColumnModel().getColumn(4).setPreferredWidth(150);
                    Tabla.getColumnModel().getColumn(5).setPreferredWidth(150);
                    Tabla.getColumnModel().getColumn(6).setPreferredWidth(170);
                    Tabla.getColumnModel().getColumn(7).setPreferredWidth(450);
                    JTableHeader th;
                    th = Tabla.getTableHeader();
                    Font fuente = new Font("Century Gothic", Font.BOLD, 14);
                    Color cl = new Color(0,102,102);
                    th.setForeground(cl);
                    th.setFont(fuente);
                    Tabla.setShowHorizontalLines(true);
                    Tabla.setShowVerticalLines(true);
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error al cargar los datos de la tabla Mantenimiento ");
                }
                break;
            case "administrador":
                try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
                    Statement stmt = conn.createStatement();
                    ResultSet r = stmt.executeQuery(consulta)) {
                    String titulos[] = {"I", "U", "Tarea", "Tipo de tarea", "Maquina", "Tipo de problema", "Preferencia", "Estado", "Observaciones", "Fecha de tarea", "Fecha de inicio", "Fecha de fin"};
                    n = new DefaultTableModel(null, titulos);
                    String fila[] = new String[12];
                    while (r.next()) {
                        fila[0] = r.getString("Id_tarea");
                        fila[1] = r.getString("usuario");
                        fila[2] = r.getString("tarea");
                        fila[3] = r.getString("tipo_tarea");
                        fila[4] = r.getString("descripcion");
                        fila[5] = r.getString("tipo_problema");
                        fila[6] = r.getString("nivel_preferencia");
                        fila[7] = r.getString("estado");
                        fila[8] = r.getString("observaciones");
                        fila[9] = r.getString("fecha_tarea");
                        fila[10] = r.getString("fecha_inicio");
                        fila[11] = r.getString("fecha_fin");
                        n.addRow(fila);
                    }
                    Tabla.setModel(n);
                    Tabla.setDefaultRenderer(Object.class, new Render());
                    Tabla.getColumnModel().getColumn(0).setPreferredWidth(0);
                    Tabla.getColumnModel().getColumn(1).setPreferredWidth(0);
                    Tabla.getColumnModel().getColumn(2).setPreferredWidth(180);
                    Tabla.getColumnModel().getColumn(3).setPreferredWidth(200);
                    Tabla.getColumnModel().getColumn(4).setPreferredWidth(200);
                    Tabla.getColumnModel().getColumn(5).setPreferredWidth(200);
                    Tabla.getColumnModel().getColumn(6).setPreferredWidth(170);
                    Tabla.getColumnModel().getColumn(7).setPreferredWidth(170);
                    Tabla.getColumnModel().getColumn(8).setPreferredWidth(550);
                    Tabla.getColumnModel().getColumn(9).setPreferredWidth(170);
                    Tabla.getColumnModel().getColumn(10).setPreferredWidth(170);
                    Tabla.getColumnModel().getColumn(11).setPreferredWidth(170);
                    JTableHeader th;
                    th = Tabla.getTableHeader();
                    Font fuente = new Font("Century Gothic", Font.BOLD, 14);
                    th.setFont(fuente);
                    Color cl = new Color(0,102,102);
                    th.setForeground(cl);
                    Tabla.setDefaultRenderer(Object.class, new Render());
                    Tabla.setShowHorizontalLines(true);
                    Tabla.setShowVerticalLines(true);
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error al cargar los datos de la tabla Mantenimiento ");
                }
                break;
            case "mantenimiento operario":
                try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
                    Statement stmt = conn.createStatement();
                    ResultSet r = stmt.executeQuery(consulta)) {
                    String titulos[] = {"Id", "Tarea", "Máquina", "Tipo_Problema", "Preferencia", "Estado", "Fecha de tarea", "Observaciones"};
                    n = new DefaultTableModel(null, titulos);
                    String fila[] = new String[8];
                    while (r.next()) {
                        String tip = fila[0] = r.getString("tipo_tarea");
                        if (filtrado.equals(tip)) {
                            if (!"finalizado".equals(r.getString("estado"))) {
                                fila[0] = r.getString("Id_tarea");
                                fila[1] = r.getString("tarea");
                                fila[2] = r.getString("descripcion");
                                fila[3] = r.getString("tipo_problema");
                                fila[4] = r.getString("nivel_preferencia");
                                fila[5] = r.getString("estado");
                                fila[6] = r.getString("fecha_tarea");
                                fila[7] = r.getString("observaciones");
                                n.addRow(fila);
                            } else if ("finalizado".equals(r.getString("estado")) && "finalizado".equals(jCEstado.getSelectedItem().toString())) {
                                fila[0] = r.getString("Id_tarea");
                                fila[1] = r.getString("tarea");
                                fila[2] = r.getString("descripcion");
                                fila[3] = r.getString("tipo_problema");
                                fila[4] = r.getString("nivel_preferencia");
                                fila[5] = r.getString("estado");
                                fila[6] = r.getString("fecha_tarea");
                                fila[7] = r.getString("observaciones");
                                n.addRow(fila);
                            }
                        }
                    }
                    Tabla.setModel(n);
                    Tabla.setDefaultRenderer(Object.class, new Render());
                    Tabla.getColumnModel().getColumn(0).setPreferredWidth(50);
                    Tabla.getColumnModel().getColumn(1).setPreferredWidth(180);
                    Tabla.getColumnModel().getColumn(2).setPreferredWidth(180);
                    Tabla.getColumnModel().getColumn(3).setPreferredWidth(200);
                    Tabla.getColumnModel().getColumn(4).setPreferredWidth(150);
                    Tabla.getColumnModel().getColumn(5).setPreferredWidth(150);
                    Tabla.getColumnModel().getColumn(6).setPreferredWidth(170);
                    Tabla.getColumnModel().getColumn(7).setPreferredWidth(450);
                    JTableHeader th;
                    th = Tabla.getTableHeader();
                    Font fuente = new Font("Century Gothic", Font.BOLD, 14);
                    th.setFont(fuente);
                    Tabla.setDefaultRenderer(Object.class, new Render());
                    Tabla.setShowHorizontalLines(true);
                    Tabla.setShowVerticalLines(true);
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error al cargar los datos de la tabla Mantenimiento ");
                }
                break;
            default:
                break;
        }
        
    }
    
    /**
     * Metodo para preparar las consultas. 
     */
    public void preparaConsulta(){
        if("todos".equals(jCNivelPreferencia.getSelectedItem().toString())&&"todos".equals(jCEstado.getSelectedItem().toString())&&"Sin descripción".equals(jCMaquina.getSelectedItem().toString())){   //1//
            consulta="SELECT * FROM tareas INNER JOIN maquinas ON (tareas.id_maquina=maquinas.id_maquina)";
            
        }else if(!"todos".equals(jCNivelPreferencia.getSelectedItem().toString())&&"todos".equals(jCEstado.getSelectedItem().toString())&&"Sin descripción".equals(jCMaquina.getSelectedItem().toString())){    //2//
            consulta="SELECT * FROM tareas INNER JOIN maquinas ON (tareas.id_maquina=maquinas.id_maquina) WHERE tareas.nivel_preferencia=\""+jCNivelPreferencia.getSelectedItem().toString()+"\"";
            
        }else if(!"todos".equals(jCNivelPreferencia.getSelectedItem().toString())&&!"todos".equals(jCEstado.getSelectedItem().toString())&&"Sin descripción".equals(jCMaquina.getSelectedItem().toString())){   //3//
            consulta="SELECT * FROM tareas INNER JOIN maquinas ON (tareas.id_maquina=maquinas.id_maquina) WHERE tareas.nivel_preferencia=\""+jCNivelPreferencia.getSelectedItem().toString()+"\" AND tareas.estado=\""+jCEstado.getSelectedItem().toString()+"\""; 
            
        }else if(!"todos".equals(jCNivelPreferencia.getSelectedItem().toString())&&"todos".equals(jCEstado.getSelectedItem().toString())&&!"Sin descripción".equals(jCMaquina.getSelectedItem().toString())){   //4//
            consulta="SELECT * FROM tareas INNER JOIN maquinas ON (tareas.id_maquina=maquinas.id_maquina) WHERE tareas.nivel_preferencia=\""+jCNivelPreferencia.getSelectedItem().toString()+"\" AND maquinas.descripcion=\""+jCMaquina.getSelectedItem().toString()+"\"";  
        
        }else if("todos".equals(jCNivelPreferencia.getSelectedItem().toString())&&!"todos".equals(jCEstado.getSelectedItem().toString())&&!"Sin descripción".equals(jCMaquina.getSelectedItem().toString())){   //5//
            consulta="SELECT * FROM tareas INNER JOIN maquinas ON (tareas.id_maquina=maquinas.id_maquina) WHERE tareas.estado=\""+jCEstado.getSelectedItem().toString()+"\" AND maquinas.descripcion=\""+jCMaquina.getSelectedItem().toString()+"\"";           
        
        }else if("todos".equals(jCNivelPreferencia.getSelectedItem().toString())&&"todos".equals(jCEstado.getSelectedItem().toString())&&!"Sin descripción".equals(jCMaquina.getSelectedItem().toString())){    //6//
            consulta="SELECT * FROM tareas INNER JOIN maquinas ON (tareas.id_maquina=maquinas.id_maquina) WHERE maquinas.descripcion=\""+jCMaquina.getSelectedItem().toString()+"\"";
        
        }else if("todos".equals(jCNivelPreferencia.getSelectedItem().toString())&&!"todos".equals(jCEstado.getSelectedItem().toString())&&"Sin descripción".equals(jCMaquina.getSelectedItem().toString())){    //7//
            consulta="SELECT * FROM tareas INNER JOIN maquinas ON (tareas.id_maquina=maquinas.id_maquina) WHERE tareas.estado=\""+jCEstado.getSelectedItem().toString()+"\"";
        
        }else{
            consulta="SELECT * FROM tareas INNER JOIN maquinas ON (tareas.id_maquina=maquinas.id_maquina) WHERE tareas.nivel_preferencia=\""+jCNivelPreferencia.getSelectedItem().toString()+"\" AND tareas.estado=\""+jCEstado.getSelectedItem().toString()+"\" AND maquinas.descripcion=\""+jCMaquina.getSelectedItem().toString()+"\"";    
        }
    }
    
    private void preparaConsulta2(){
        boolean primera = "todos".equals(jCNivelPreferencia.getSelectedItem().toString());
        boolean segunda = "todos".equals(jCEstado.getSelectedItem().toString());
        boolean tercera = "Sin descripción".equals(jCMaquina.getSelectedItem().toString());
        boolean cuarta = "Sin descripción".equals(jCTipoTarea.getSelectedItem().toString());
        
        if (primera==true && segunda==true && tercera==true && cuarta==false){
            consulta="SELECT * FROM tareas INNER JOIN maquinas ON (tareas.id_maquina=maquinas.id_maquina) WHERE tareas.tipo_tarea=\""+jCTipoTarea.getSelectedItem().toString()+"\"";
        }else if (primera==true && segunda==true && tercera==false && cuarta==false){
            consulta="SELECT * FROM tareas INNER JOIN maquinas ON (tareas.id_maquina=maquinas.id_maquina) WHERE maquinas.descripcion=\""+jCMaquina.getSelectedItem().toString()+"\" AND tareas.tipo_tarea=\""+jCTipoTarea.getSelectedItem().toString()+"\"";
        }else if (primera==true && segunda==false && tercera==false && cuarta==false){
            consulta="SELECT * FROM tareas INNER JOIN maquinas ON (tareas.id_maquina=maquinas.id_maquina) WHERE tareas.estado=\""+jCEstado.getSelectedItem().toString()+"\" AND maquinas.descripcion=\""+jCMaquina.getSelectedItem().toString()+"\" AND tareas.tipo_tarea=\""+jCTipoTarea.getSelectedItem().toString()+"\"";
        }else if (primera==false && segunda==false && tercera==false && cuarta==false){
            consulta="SELECT * FROM tareas INNER JOIN maquinas ON (tareas.id_maquina=maquinas.id_maquina) WHERE tareas.nivel_preferencia=\""+jCNivelPreferencia.getSelectedItem().toString()+"\" AND tareas.estado=\""+jCEstado.getSelectedItem().toString()+"\" AND maquinas.descripcion=\""+jCMaquina.getSelectedItem().toString()+"\" AND tareas.tipo_tarea=\""+jCTipoTarea.getSelectedItem().toString()+"\"";
        }else if (primera==false && segunda==false && tercera==false && cuarta==true){
            consulta="SELECT * FROM tareas INNER JOIN maquinas ON (tareas.id_maquina=maquinas.id_maquina) WHERE tareas.nivel_preferencia=\""+jCNivelPreferencia.getSelectedItem().toString()+"\" AND tareas.estado=\""+jCEstado.getSelectedItem().toString()+"\" AND maquinas.descripcion=\""+jCMaquina.getSelectedItem().toString()+"\"";
        }else if (primera==false && segunda==false && tercera==true && cuarta==true){
            consulta="SELECT * FROM tareas INNER JOIN maquinas ON (tareas.id_maquina=maquinas.id_maquina) WHERE tareas.nivel_preferencia=\""+jCNivelPreferencia.getSelectedItem().toString()+"\" AND tareas.estado=\""+jCEstado.getSelectedItem().toString()+"\"";
        }else if (primera==false && segunda==true && tercera==true && cuarta==true){
            consulta="SELECT * FROM tareas INNER JOIN maquinas ON (tareas.id_maquina=maquinas.id_maquina) WHERE tareas.nivel_preferencia=\""+jCNivelPreferencia.getSelectedItem().toString()+"\"";
        }else if (primera==true && segunda==false && tercera==false && cuarta==true){
            consulta="SELECT * FROM tareas INNER JOIN maquinas ON (tareas.id_maquina=maquinas.id_maquina) WHERE tareas.estado=\""+jCEstado.getSelectedItem().toString()+"\" AND maquinas.descripcion=\""+jCMaquina.getSelectedItem().toString()+"\"";
        }else if (primera==true && segunda==false && tercera==true && cuarta==true){
            consulta="SELECT * FROM tareas INNER JOIN maquinas ON (tareas.id_maquina=maquinas.id_maquina) WHERE tareas.estado=\""+jCEstado.getSelectedItem().toString()+"\"";
        }else if (primera==true && segunda==false && tercera==true && cuarta==false){
            consulta="SELECT * FROM tareas INNER JOIN maquinas ON (tareas.id_maquina=maquinas.id_maquina) WHERE tareas.estado=\""+jCEstado.getSelectedItem().toString()+"\" AND tareas.tipo_tarea=\""+jCTipoTarea.getSelectedItem().toString()+"\"";
        }else if (primera==false && segunda==true && tercera==true && cuarta==false){
            consulta="SELECT * FROM tareas INNER JOIN maquinas ON (tareas.id_maquina=maquinas.id_maquina) WHERE tareas.nivel_preferencia=\""+jCNivelPreferencia.getSelectedItem().toString()+"\" AND tareas.tipo_tarea=\""+jCTipoTarea.getSelectedItem().toString()+"\"";
        }else if (primera==false && segunda==true && tercera==false && cuarta==false){
            consulta="SELECT * FROM tareas INNER JOIN maquinas ON (tareas.id_maquina=maquinas.id_maquina) WHERE tareas.nivel_preferencia=\""+jCNivelPreferencia.getSelectedItem().toString()+"\" AND maquinas.descripcion=\""+jCMaquina.getSelectedItem().toString()+"\" AND tareas.tipo_tarea=\""+jCTipoTarea.getSelectedItem().toString()+"\"";
        }else if (primera==false && segunda==true && tercera==false && cuarta==true){
            consulta="SELECT * FROM tareas INNER JOIN maquinas ON (tareas.id_maquina=maquinas.id_maquina) WHERE tareas.nivel_preferencia=\""+jCNivelPreferencia.getSelectedItem().toString()+"\" AND maquinas.descripcion=\""+jCMaquina.getSelectedItem().toString()+"\"";
        }else if (primera==true && segunda==true && tercera==false && cuarta==true){
            consulta="SELECT * FROM tareas INNER JOIN maquinas ON (tareas.id_maquina=maquinas.id_maquina) WHERE maquinas.descripcion=\""+jCMaquina.getSelectedItem().toString()+"\"";
        }else{
            consulta="SELECT * FROM tareas INNER JOIN maquinas ON (tareas.id_maquina=maquinas.id_maquina)";
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        Panel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        Tabla = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jCNivelPreferencia = new javax.swing.JComboBox<>();
        jCEstado = new javax.swing.JComboBox<>();
        jbCreaTarea = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jCMaquina = new javax.swing.JComboBox<>();
        botonPrincipal = new javax.swing.JButton();
        botonMantenimiento = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jlReloj = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jCTipoTarea = new javax.swing.JComboBox<>();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Control de tareas");
        setFont(new java.awt.Font("Century", 0, 14)); // NOI18N
        setIconImage(new ImageIcon(getClass().getResource("/imagenes/editor.png")).getImage());

        Panel1.setBackground(new java.awt.Color(204, 204, 204));
        Panel1.setPreferredSize(new java.awt.Dimension(1024, 768));

        Tabla.setAutoCreateRowSorter(true);
        Tabla.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        Tabla.setForeground(new java.awt.Color(0, 51, 51));
        Tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Tares", "Preferencia", "Estado", "Fecha", "Observaciones"
            }
        ));
        Tabla.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        Tabla.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Tabla.setFillsViewportHeight(true);
        Tabla.setGridColor(new java.awt.Color(0, 0, 0));
        Tabla.setIntercellSpacing(new java.awt.Dimension(10, 10));
        Tabla.setRowHeight(25);
        Tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                click_tarea(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                TablaMousePressed(evt);
            }
        });
        jScrollPane3.setViewportView(Tabla);

        jLabel2.setFont(new java.awt.Font("Agency FB", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 153, 102));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Tareas a realizar");
        jLabel2.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel2.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        jCNivelPreferencia.setFont(new java.awt.Font("3ds Light", 1, 16)); // NOI18N
        jCNivelPreferencia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "todos", "urgente", "prioritaria", "normal" }));
        jCNivelPreferencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCNivelPreferenciaActionPerformed(evt);
            }
        });

        jCEstado.setFont(new java.awt.Font("3ds Light", 1, 16)); // NOI18N
        jCEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "todos", "en espera", "en proceso", "finalizado" }));
        jCEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCEstadoActionPerformed(evt);
            }
        });

        jbCreaTarea.setBackground(new java.awt.Color(0, 102, 102));
        jbCreaTarea.setFont(new java.awt.Font("3ds Light", 1, 18)); // NOI18N
        jbCreaTarea.setForeground(new java.awt.Color(255, 255, 255));
        jbCreaTarea.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/muestra.png"))); // NOI18N
        jbCreaTarea.setText("Crea Tarea");
        jbCreaTarea.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jbCreaTarea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCreaTareaActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("3ds Light", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 0, 0));
        jLabel1.setText("Nivel de preferencia:");

        jLabel3.setFont(new java.awt.Font("3ds Light", 1, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(102, 0, 0));
        jLabel3.setText("Estado:");

        jLabel4.setFont(new java.awt.Font("3ds Light", 1, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 0, 0));
        jLabel4.setText("Máquina");

        jCMaquina.setFont(new java.awt.Font("3ds Light", 1, 16)); // NOI18N
        jCMaquina.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sin descripcion" }));
        jCMaquina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCMaquinaActionPerformed(evt);
            }
        });

        botonPrincipal.setBackground(new java.awt.Color(0, 102, 102));
        botonPrincipal.setFont(new java.awt.Font("3ds Light", 1, 18)); // NOI18N
        botonPrincipal.setForeground(new java.awt.Color(255, 255, 255));
        botonPrincipal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/editor_boton.png"))); // NOI18N
        botonPrincipal.setText("Panel principal");
        botonPrincipal.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        botonPrincipal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonPrincipalActionPerformed(evt);
            }
        });

        botonMantenimiento.setBackground(new java.awt.Color(0, 102, 102));
        botonMantenimiento.setFont(new java.awt.Font("3ds Light", 1, 18)); // NOI18N
        botonMantenimiento.setForeground(new java.awt.Color(255, 255, 255));
        botonMantenimiento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/mantenimiento.png"))); // NOI18N
        botonMantenimiento.setText("Mantenimiento");
        botonMantenimiento.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        botonMantenimiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonMantenimientoActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/updated.png"))); // NOI18N
        jButton1.setText("Actualizar");
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jlReloj.setBackground(new java.awt.Color(180, 230, 230));
        jlReloj.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        jlReloj.setForeground(new java.awt.Color(0, 153, 102));
        jlReloj.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlReloj.setText("jLabel4");
        jlReloj.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 102), 2, true));

        jLabel5.setFont(new java.awt.Font("3ds Light", 1, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 0, 0));
        jLabel5.setText("Tipo de tarea:");

        jCTipoTarea.setFont(new java.awt.Font("3ds Light", 1, 16)); // NOI18N
        jCTipoTarea.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sin descripción", "mantenimiento operario", "mecánica", "programación" }));
        jCTipoTarea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCTipoTareaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Panel1Layout = new javax.swing.GroupLayout(Panel1);
        Panel1.setLayout(Panel1Layout);
        Panel1Layout.setHorizontalGroup(
            Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCMaquina, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCEstado, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(botonMantenimiento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbCreaTarea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(Panel1Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlReloj, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jCNivelPreferencia, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .addComponent(jLabel5)
                    .addComponent(jCTipoTarea, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 769, Short.MAX_VALUE))
        );
        Panel1Layout.setVerticalGroup(
            Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlReloj, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCNivelPreferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCMaquina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCTipoTarea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(botonPrincipal)
                .addGap(18, 18, 18)
                .addComponent(botonMantenimiento)
                .addGap(18, 18, 18)
                .addComponent(jbCreaTarea)
                .addContainerGap())
            .addComponent(jScrollPane3)
        );

        jMenu1.setText("Opciones");
        jMenu1.setFont(new java.awt.Font("3ds Light", 1, 14)); // NOI18N

        jMenuItem1.setText("Pendientes operario");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Pendientes mecánica");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Panel1, javax.swing.GroupLayout.DEFAULT_SIZE, 747, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbCreaTareaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCreaTareaActionPerformed
        nueva = new Panel_Nueva_Tarea();
        nueva.setVisible(true);
        //Cierra la ventana pulsando escape
        KeyboardFocusManager kb = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        kb.addKeyEventPostProcessor(new KeyEventPostProcessor() {
            @Override
            public boolean postProcessKeyEvent(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE && this != null) {
                    System.out.println("probando...");
                    nueva.dispose();
                    return false;
                }
                return true;
            }
        });
    }//GEN-LAST:event_jbCreaTareaActionPerformed

    private void TablaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaMousePressed

        try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
            Statement stmt = conn.createStatement();
            ResultSet r = stmt.executeQuery("SELECT * FROM tareas WHERE Id_tarea=\"" + Tabla.getValueAt(Tabla.getSelectedRow(), 0) + "\"")) {
            while (r.next()) {
                String h = r.getString("Id_tarea");
                if ("administrador".equals(filtrado)) {
                    modA = new Panel_ModificaAdmin(h);
                    modA.setVisible(true);
                    //Cierra la ventana pulsando escape
                    KeyboardFocusManager kb = KeyboardFocusManager.getCurrentKeyboardFocusManager();
                    kb.addKeyEventPostProcessor(new KeyEventPostProcessor() {
                        @Override
                        public boolean postProcessKeyEvent(KeyEvent e) {
                            if (e.getKeyCode() == KeyEvent.VK_ESCAPE && this != null) {
                                System.out.println("probando...");
                                modA.dispose();
                                return false;
                            }
                            return true;
                        }
                    });
                } else {
                    mod = new Panel_Modifica(h);
                    mod.setVisible(true);
                    //Cierra la ventana pulsando escape
                    KeyboardFocusManager kb = KeyboardFocusManager.getCurrentKeyboardFocusManager();
                    kb.addKeyEventPostProcessor(new KeyEventPostProcessor() {
                        @Override
                        public boolean postProcessKeyEvent(KeyEvent e) {
                            if (e.getKeyCode() == KeyEvent.VK_ESCAPE && this != null) {
                                System.out.println("probando...");
                                mod.dispose();
                                return false;
                            }
                            return true;
                        }
                    });
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(Tareas_Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        mostrarTabla();
    }//GEN-LAST:event_TablaMousePressed

    private void click_tarea(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_click_tarea
        
        
    }//GEN-LAST:event_click_tarea

    private void jCNivelPreferenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCNivelPreferenciaActionPerformed
        // TODO add your handling code here:
        llenarComboMaquinas();
        mostrarTabla();
        contador++;
    }//GEN-LAST:event_jCNivelPreferenciaActionPerformed

    private void jCEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCEstadoActionPerformed
         llenarComboMaquinas();
         mostrarTabla();
         contador++;
    }//GEN-LAST:event_jCEstadoActionPerformed

    private void jCMaquinaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCMaquinaActionPerformed
        llenarComboMaquinas(); 
        mostrarTabla();
        contador++;
    }//GEN-LAST:event_jCMaquinaActionPerformed

    private void botonPrincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonPrincipalActionPerformed
        if(pm==null){
        }else{
            pm.dispose();
        }
        if(modA==null){
        }else{
            modA.dispose();
        }
        if(mod==null){
        }else{
            mod.dispose();
        }
        if(nueva==null){
        }else{
            nueva.dispose();
        }
        this.dispose();
    }//GEN-LAST:event_botonPrincipalActionPerformed

    private void botonMantenimientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonMantenimientoActionPerformed
        pm = new Panel_Mantenimiento();
        pm.setVisible(true);
        //Cierra la ventana pulsando escape
        KeyboardFocusManager kb = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        kb.addKeyEventPostProcessor(new KeyEventPostProcessor() {
            @Override
            public boolean postProcessKeyEvent(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE && this != null) {
                    pm.dispose();
                    return false;
                }
                return true;
            }
        });
    }//GEN-LAST:event_botonMantenimientoActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Propiedades.setPropiedad("modificado", "true");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jCTipoTareaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCTipoTareaActionPerformed
        llenarComboMaquinas(); 
        mostrarTabla();
        contador++;
    }//GEN-LAST:event_jCTipoTareaActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        Proyecto_Report1 p = new Proyecto_Report1();
        p.creaReporte(2);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        Proyecto_Report1 p = new Proyecto_Report1();
        p.creaReporte(1);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tareas_Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                SERVER_SOCKET = new ServerSocket(1333);
                new Tareas_Principal().setVisible(true);
            } catch (SQLException | IOException ex) {
                Logger.getLogger(Tareas_Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JPanel Panel1;
    private javax.swing.JTable Tabla;
    private javax.swing.JButton botonMantenimiento;
    private javax.swing.JButton botonPrincipal;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jCEstado;
    public javax.swing.JComboBox<String> jCMaquina;
    private javax.swing.JComboBox<String> jCNivelPreferencia;
    public javax.swing.JComboBox<String> jCTipoTarea;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton jbCreaTarea;
    public javax.swing.JLabel jlReloj;
    // End of variables declaration//GEN-END:variables
}

