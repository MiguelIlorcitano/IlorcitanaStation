/**
 * En esta clase de tipo frame es donde se configura la interfaz gráfica principal y desde donde se ralizan las 
 * llamadas a las distintas clases.
 */

package soporteActas;

import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.KeyEventDispatcher;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import tareasIlorcitana.Tareas_Principal;
import reporte.Proyecto_Report;
import satation.Main;
import satation.Propiedades;

/**
 *
 * @author Miguel Angel Carrillo Garcia
 */
public final class panelActas extends javax.swing.JFrame {
    
    DefaultTableModel n;
    
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
    
    Proyecto_Report reporte;
    Muestra_Acta mod;

    /**
     * Constructo
     * @throws java.sql.SQLException
     */
    public panelActas() throws SQLException {
        initComponents();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().
                addKeyEventDispatcher(new KeyEventDispatcher() {
                    @Override
                    public boolean dispatchKeyEvent(KeyEvent e) {
                        if (e.getID() == java.awt.event.KeyEvent.KEY_RELEASED
                                && e.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
                            
                            dispose();
                        }
                        return false;
                    }
                });
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);
        llenarComboMaquinas();
        mostrarTabla();
    }
   
    /**
     * LLena los combobox.
     */
    public void llenarComboMaquinas() {
        String descripcion = null;
       try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
            Statement stmt = conn.createStatement();
            ResultSet r = stmt.executeQuery("SELECT id_maquina,descripcion FROM maquinas")) {
            selectorMaquina.setEnabled(true);
            selectorMaquina.removeAllItems();
            while (r.next()) {
                selectorMaquina.addItem(r.getString("descripcion"));
            }
            if (descripcion != null) {
                selectorMaquina.setSelectedItem(descripcion);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }
    }
    
    /**
    * Metodo para saber si los datos pasados son numéricos o no.
     * @param x pasa el string que queremos comprobar.
     * @return debuelve verdadero si es numérico y falso si no lo es.
    */
    public static boolean esNumero(String x){
        return (x.matches("[+-]?\\d*(\\.\\d+)?") && x.equals("")==false);
    }
    
    public void actualiza(){
        repaint();
    }
    
    /**
     * Extrae el contenido de la base de datos y lo inserta en una jtable.
     */
    public void mostrarTabla(){
        //Extrae el contenido de la tabla y lo almacena en el resultset.
        try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
            Statement stmt = conn.createStatement();
            ResultSet r = stmt.executeQuery(generaConsulta(Integer.parseInt(txtNActas.getText())))) {
            //crear el modelo de la tabla.
            String titulos [] = {"Acta","Maquina","Fecha","Operaio","Tipo de Mantenimiento","Tipo de probelma","Operaciones","Piezas Sustituidas","Observaciones","Horas"};
            n = new DefaultTableModel(null,titulos);
            //Pasa el resultado de la consulta a la trabla.
            String fila [] = new String[10];
            while (r.next()){
                fila[0]=r.getString("NumeroActa");
                fila[1]=r.getString("Maquina");                                               
                fila[2]=r.getString("Fecha");
                fila[3]=r.getString("Operario");
                fila[4]=r.getString("TipoMantenimiento");
                fila[5]=r.getString("TipoProblema");
                fila[6]=r.getString("Operaciones");
                fila[7]=r.getString("PiezasSustituidas");
                fila[8]=r.getString("Observaciones");
                fila[9]=r.getString("Horas");                                                              
                n.addRow(fila);
            }
            Tabla.setModel(n);
            Tabla.setDefaultRenderer(Object.class, new RenderActas());
            Tabla.getColumnModel().getColumn(0).setPreferredWidth(6);
            Tabla.getColumnModel().getColumn(1).setPreferredWidth(150);
            Tabla.getColumnModel().getColumn(2).setPreferredWidth(30);
            Tabla.getColumnModel().getColumn(3).setPreferredWidth(200);
            Tabla.getColumnModel().getColumn(4).setPreferredWidth(30);
            Tabla.getColumnModel().getColumn(5).setPreferredWidth(30);
            Tabla.getColumnModel().getColumn(6).setPreferredWidth(200);
            Tabla.getColumnModel().getColumn(7).setPreferredWidth(200);
            Tabla.getColumnModel().getColumn(8).setPreferredWidth(200);
            Tabla.getColumnModel().getColumn(9).setPreferredWidth(30);
            JTableHeader th;
            th = Tabla.getTableHeader();
            Font fuente = new Font("3ds Light", Font.BOLD, 14);
            Color cl = new Color(0,102,102);
            th.setForeground(cl);
            th.setFont(fuente);
            Tabla.setShowHorizontalLines(true);
            Tabla.setShowVerticalLines(true);
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,"Error al cargar los datos de la tabla Mantenimiento 2 ");
        }
    }
    
    private String generaConsulta(int num){
        String parte[]= new String [3];
        if (rbTodas.isSelected()){
                if (rbInicio.isSelected()){
                        if(rbPiezasS.isSelected()){
                                return "SELECT * FROM mantenimiento WHERE PiezasSustituidas !=\"Sin operación\" ORDER BY Fecha DESC";
                        }else{
                                return "SELECT * FROM mantenimiento ORDER BY Fecha DESC";
                        }
                }
                if (rbEntreFechas.isSelected()){
                        Calendar inicio =jcInicioRango.getCalendar();
                        Calendar fin =jcFinRango.getCalendar();
                        Date ini = Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(inicio.getTime()));
                        Date fn = Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(fin.getTime()));
                        if(rbPiezasS.isSelected()){
                                return "SELECT * FROM mantenimiento WHERE Fecha BETWEEN \""+ini+"\" AND \""+fn+"\" AND PiezasSustituidas !=\"Sin operación\" ORDER BY Fecha DESC";
                        }else{
                                return "SELECT * FROM mantenimiento WHERE Fecha BETWEEN \""+ini+"\" AND \""+fn+"\" ORDER BY Fecha DESC";
                        }
                }
                if (rbNumeroActas.isSelected()){
                        parte[2]="ORDER BY NumeroActa DESC LIMIT="+num;
                        //prepara consulta para saber si vamos a mostrar piezas sustituidas o no
                        if(rbPiezasS.isSelected()){
                                return "SELECT * FROM mantenimiento WHERE PiezasSustituidas !=\"Sin operación.\" ORDER BY Fecha DESC LIMIT="+num;
                        }else{
                                return "SELECT * FROM mantenimiento ORDER BY Fecha DESC LIMIT="+num;
                        }
                }
        }
        if (rbIndividual.isSelected()){
                if (rbInicio.isSelected()==false&&rbEntreFechas.isSelected()==false&&rbNumeroActas.isSelected()==false){
                        if(rbPiezasS.isSelected()){
                                return "SELECT * FROM mantenimiento WHERE Maquina=\""+selectorMaquina.getSelectedItem()+"\" AND PiezasSustituidas !=\"Sin operación.\" ORDER BY Fecha DESC";
                        }else{
                                return "SELECT * FROM mantenimiento WHERE Maquina=\""+selectorMaquina.getSelectedItem()+"\" ORDER BY Fecha DESC";
                        }
                }
                if (rbInicio.isSelected()){
                        if(rbPiezasS.isSelected()){
                                return "SELECT * FROM mantenimiento WHERE Maquina=\""+selectorMaquina.getSelectedItem()+"\" AND PiezasSustituidas !=\"Sin operación.\" ORDER BY Fecha DESC";
                        }else{
                                return "SELECT * FROM mantenimiento WHERE Maquina=\""+selectorMaquina.getSelectedItem()+"\" ORDER BY Fecha DESC";
                        }
                }
                if (rbEntreFechas.isSelected()){
                        Calendar inicio =jcInicioRango.getCalendar();
                        Calendar fin =jcFinRango.getCalendar();
                        Date ini = Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(inicio.getTime()));
                        Date fn = Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(fin.getTime()));
                        if(rbPiezasS.isSelected()){
                                return "SELECT * FROM mantenimiento WHERE Maquina=\""+selectorMaquina.getSelectedItem()+"\" AND Fecha BETWEEN \""+ini+"\" AND \""+fn+"\" AND PiezasSustituidas !=\"Sin operación.\"ORDER BY Fecha DESC";
                        }else{
                                return "SELECT * FROM mantenimiento WHERE Maquina=\""+selectorMaquina.getSelectedItem()+"\" AND Fecha BETWEEN \""+ini+"\" AND \""+fn+"\"ORDER BY Fecha DESC";
                        }
                }
                if (rbNumeroActas.isSelected()){
                        if(rbPiezasS.isSelected()){
                                return "SELECT * FROM mantenimiento WHERE Maquina=\""+selectorMaquina.getSelectedItem()+"\" AND PiezasSustituidas !=\"Sin operación.\"ORDER BY Fecha DESC LIMIT "+num;
                        }else{
                                return "SELECT * FROM mantenimiento WHERE Maquina=\""+selectorMaquina.getSelectedItem()+"\" ORDER BY Fecha DESC LIMIT "+num;
                        }
                }
        }
        if (rbInicio.isSelected()) {
            if (rbPiezasS.isSelected()) {
                return "SELECT * FROM mantenimiento WHERE PiezasSustituidas !=\"Sin operación\" ORDER BY Fecha DESC";
            } else {
                return "SELECT * FROM mantenimiento ORDER BY Fecha DESC";
            }
        }
        if (rbEntreFechas.isSelected()) {
            Calendar inicio = jcInicioRango.getCalendar();
            Calendar fin = jcFinRango.getCalendar();
            Date ini = Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(inicio.getTime()));
            Date fn = Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(fin.getTime()));
            if (rbPiezasS.isSelected()) {
                return "SELECT * FROM mantenimiento WHERE Fecha BETWEEN \"" + ini + "\" AND \"" + fn + "\" AND PiezasSustituidas !=\"Sin operación\" ORDER BY Fecha DESC";
            } else {
                return "SELECT * FROM mantenimiento WHERE Fecha BETWEEN \"" + ini + "\" AND \"" + fn + "\" ORDER BY Fecha DESC";
            }
        }
        if (rbNumeroActas.isSelected()) {
            //prepara consulta para saber si vamos a mostrar piezas sustituidas o no
            if (rbPiezasS.isSelected()) {
                return "SELECT * FROM mantenimiento WHERE PiezasSustituidas NOT LIKE \"Sin operación\" ORDER BY Fecha DESC LIMIT " + num;
            } else {
                return "SELECT * FROM mantenimiento ORDER BY Fecha DESC LIMIT " + num;
            }
        }
        return "SELECT * FROM mantenimiento ORDER BY Fecha DESC";
    }
    
    private void modificaTxt() throws IOException{ 
        Propiedades.setPropiedad("modificado", "true");
    }
    
    private void preparaReporte(){
        try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
            Statement stmt = conn.createStatement();
            ResultSet r = stmt.executeQuery(generaConsulta(10))) {
            Proyecto_Report reporte1 = new Proyecto_Report ();
            String historial[]= new String [9];
            while (r.next()){
                historial[0]=r.getString("Maquina");                                               
                historial[1]=r.getString("Fecha");
                historial[2]=r.getString("Operario");
                historial[3]=r.getString("TipoMantenimiento");
                historial[4]=r.getString("TipoProblema");
                historial[5]=r.getString("Operaciones");
                historial[6]=r.getString("PiezasSustituidas");
                historial[7]=r.getString("Observaciones");
                historial[8]=r.getString("Horas");                                                              
                reporte1.addMantenimiento(historial);
            }
            reporte1.creaReporte(); 
        } catch (SQLException ex) {
            Logger.getLogger(panelActas.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }
    
    /**
     * Pasa las actas del arraylist ya ordenadas a la base de datos y de esta al Jtable.
     */
    private void ordenaTabla(char ord){ 
        String query = null;
        if (ord == 'A') {
            query = "SELECT * FROM mantenimiento ORDER BY Fecha ASC";
        } else {
            query = "SELECT * FROM mantenimiento ORDER BY Fecha DESC";
        }
        try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
                Statement stmt = conn.createStatement();
                ResultSet r = stmt.executeQuery(query)) {

            String titulos[] = {"Maquina", "Fecha", "Operaio", "Tipo de Mantenimiento", "Tipo de probelma", "Operaciones", "Piezas Sustituidas", "Observaciones", "Horas"};
            n = new DefaultTableModel(null, titulos);
            String fila[] = new String[9];
            while (r.next()) {
                fila[0] = r.getString("Maquina");
                fila[1] = r.getString("Fecha");
                fila[2] = r.getString("Operario");
                fila[3] = r.getString("TipoMantenimiento");
                fila[4] = r.getString("TipoProblema");
                fila[5] = r.getString("Operaciones");
                fila[6] = r.getString("PiezasSustituidas");
                fila[7] = r.getString("Observaciones");
                fila[8] = r.getString("Horas");
                n.addRow(fila);
            }
            Tabla.setModel(n);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los datos de la tabla Mantenimiento 1 ");
        }
    
    }        
    
    /**
     * Metodo para abrir un archivo con el programa predeterminado que tiene el usuario en su pc.
     */
    private void abrirPrograma(File a){
        try {              
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+a);
            System.out.println("Final");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }       
    }
    
    private void eliminarBorrarActa(){
        int ax = JOptionPane.showConfirmDialog(null, "Vas a ELIMINAR/MODIFICAR una acta.¿Estas seguro?");
        if (ax == JOptionPane.YES_OPTION) {
            n = (DefaultTableModel) Tabla.getModel();
            n.removeRow(Tabla.getSelectedRow());

            try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
                PreparedStatement pst = conn.prepareStatement("DELETE from MANTENIMIENTO")) {
                pst.executeUpdate();
            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
            try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
                PreparedStatement pst = conn.prepareStatement("INSERT INTO MANTENIMIENTO(M, F, O, TM, TP, Op, Ps, Ob, H)VALUES(?,?,?,?,?,?,?,?,?)")) {
                for (int i = 0; i < Tabla.getRowCount(); i++) {
                    pst.setString(1, Tabla.getValueAt(i, 0).toString());
                    pst.setString(2, Tabla.getValueAt(i, 1).toString());
                    pst.setString(3, Tabla.getValueAt(i, 2).toString());
                    pst.setString(4, Tabla.getValueAt(i, 3).toString());
                    pst.setString(5, Tabla.getValueAt(i, 4).toString());
                    pst.setString(6, Tabla.getValueAt(i, 5).toString());
                    pst.setString(7, Tabla.getValueAt(i, 6).toString());
                    pst.setString(8, Tabla.getValueAt(i, 7).toString());
                    pst.setString(9, Tabla.getValueAt(i, 8).toString());
                    pst.executeUpdate();
                }
                JTableHeader th;
                th = Tabla.getTableHeader();
                Font fuente = new Font("Arial Narrow", Font.BOLD, 14);
                th.setFont(fuente);
                JOptionPane.showMessageDialog(null, "Acta eliminada.");
            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        } else if (ax == JOptionPane.NO_OPTION) {
            JOptionPane.showMessageDialog(null, "Has cancelado, NO SE HAN REALIZADO CAMBIOS.");
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
        jPanel1 = new javax.swing.JPanel();
        rbTodas = new javax.swing.JRadioButton();
        rbIndividual = new javax.swing.JRadioButton();
        selectorMaquina = new javax.swing.JComboBox<>();
        rbInicio = new javax.swing.JRadioButton();
        rbNumeroActas = new javax.swing.JRadioButton();
        rbPiezasS = new javax.swing.JRadioButton();
        jLabel9 = new javax.swing.JLabel();
        txtNActas = new javax.swing.JTextField();
        rbEntreFechas = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jcInicioRango = new org.freixas.jcalendar.JCalendarCombo();
        jcFinRango = new org.freixas.jcalendar.JCalendarCombo();
        jbExportaPdf1 = new javax.swing.JButton();
        jbExportaPdf2 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        Tabla = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuAscendente = new javax.swing.JMenuItem();
        jMenuDescendente = new javax.swing.JMenuItem();
        Opciones = new javax.swing.JMenu();
        jRadioButtonElimina = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonModifica = new javax.swing.JRadioButtonMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Informe de tareas");
        setFont(new java.awt.Font("Century", 0, 14)); // NOI18N
        setIconImage(new ImageIcon(getClass().getResource("/imagenes/editor.png")).getImage());
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        rbTodas.setBackground(new java.awt.Color(0, 102, 102));
        buttonGroup1.add(rbTodas);
        rbTodas.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        rbTodas.setForeground(new java.awt.Color(255, 255, 255));
        rbTodas.setText("Todas las máquinas.");
        rbTodas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbTodasActionPerformed(evt);
            }
        });

        rbIndividual.setBackground(new java.awt.Color(0, 102, 102));
        buttonGroup1.add(rbIndividual);
        rbIndividual.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        rbIndividual.setForeground(new java.awt.Color(255, 255, 255));
        rbIndividual.setText("Elige la máquina a gestionar.");
        rbIndividual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbIndividualActionPerformed(evt);
            }
        });

        selectorMaquina.setFont(new java.awt.Font("Century Gothic", 2, 14)); // NOI18N
        selectorMaquina.setForeground(new java.awt.Color(0, 51, 51));
        selectorMaquina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectorMaquinaActionPerformed(evt);
            }
        });

        rbInicio.setBackground(new java.awt.Color(0, 102, 102));
        buttonGroup2.add(rbInicio);
        rbInicio.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        rbInicio.setForeground(new java.awt.Color(255, 255, 255));
        rbInicio.setText("Desde el inicio.");
        rbInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbInicioActionPerformed(evt);
            }
        });

        rbNumeroActas.setBackground(new java.awt.Color(0, 102, 102));
        buttonGroup2.add(rbNumeroActas);
        rbNumeroActas.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        rbNumeroActas.setForeground(new java.awt.Color(255, 255, 255));
        rbNumeroActas.setText("Número de actas.");
        rbNumeroActas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbNumeroActasActionPerformed(evt);
            }
        });

        rbPiezasS.setBackground(new java.awt.Color(0, 102, 102));
        rbPiezasS.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        rbPiezasS.setForeground(new java.awt.Color(255, 255, 255));
        rbPiezasS.setText("Solo piezas sustituidas");
        rbPiezasS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbPiezasSActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 51, 51));
        jLabel9.setText("Nº de actas a imprimir:");

        txtNActas.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        txtNActas.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtNActas.setText("10");
        txtNActas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNActasActionPerformed(evt);
            }
        });

        rbEntreFechas.setBackground(new java.awt.Color(0, 102, 102));
        buttonGroup2.add(rbEntreFechas);
        rbEntreFechas.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        rbEntreFechas.setForeground(new java.awt.Color(255, 255, 255));
        rbEntreFechas.setText("Entre Fechas.");
        rbEntreFechas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbEntreFechasActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 51, 51));
        jLabel1.setText("Fecha de inicio:");

        jLabel14.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 51, 51));
        jLabel14.setText("Fecha final:");

        jLabel3.setBackground(new java.awt.Color(204, 204, 204));
        jLabel3.setFont(new java.awt.Font("Century Gothic", 1, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(102, 0, 0));
        jLabel3.setText("Elije máquina a mostrar:");

        jLabel4.setBackground(new java.awt.Color(204, 204, 204));
        jLabel4.setFont(new java.awt.Font("Century Gothic", 1, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 0, 0));
        jLabel4.setText("Elije que actas quieres mostrar :");

        jLabel5.setBackground(new java.awt.Color(204, 204, 204));
        jLabel5.setFont(new java.awt.Font("Century Gothic", 1, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 0, 0));
        jLabel5.setText("Solo muestra piezas sustituidas:");

        jcInicioRango.setForeground(new java.awt.Color(0, 51, 51));
        jcInicioRango.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jcInicioRango.addDateListener(new org.freixas.jcalendar.DateListener() {
            public void dateChanged(org.freixas.jcalendar.DateEvent evt) {
                jcInicioRangoDateChanged(evt);
            }
        });

        jcFinRango.setForeground(new java.awt.Color(0, 51, 51));
        jcFinRango.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jcFinRango.addDateListener(new org.freixas.jcalendar.DateListener() {
            public void dateChanged(org.freixas.jcalendar.DateEvent evt) {
                jcFinRangoDateChanged(evt);
            }
        });

        jbExportaPdf1.setBackground(new java.awt.Color(0, 102, 102));
        jbExportaPdf1.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        jbExportaPdf1.setForeground(new java.awt.Color(255, 255, 255));
        jbExportaPdf1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/editor_boton.png"))); // NOI18N
        jbExportaPdf1.setText("Panel Principal");
        jbExportaPdf1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jbExportaPdf1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbExportaPdf1ActionPerformed(evt);
            }
        });

        jbExportaPdf2.setBackground(new java.awt.Color(0, 102, 102));
        jbExportaPdf2.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        jbExportaPdf2.setForeground(new java.awt.Color(255, 255, 255));
        jbExportaPdf2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/muestra.png"))); // NOI18N
        jbExportaPdf2.setText("Mostrar Actas");
        jbExportaPdf2.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jbExportaPdf2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbExportaPdf2ActionPerformed(evt);
            }
        });

        Tabla.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        Tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        Tabla.setGridColor(new java.awt.Color(0, 0, 0));
        Tabla.setRowHeight(25);
        Tabla.setRowMargin(5);
        Tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                clicK_acta(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                TablaMousePressed(evt);
            }
        });
        jScrollPane3.setViewportView(Tabla);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jcFinRango, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcInicioRango, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rbPiezasS, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel14)
                    .addComponent(jbExportaPdf1, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(rbInicio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(rbEntreFechas, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rbTodas, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectorMaquina, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rbIndividual, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbExportaPdf2, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel9)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtNActas))
                        .addComponent(rbNumeroActas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 783, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rbTodas)
                        .addGap(18, 18, 18)
                        .addComponent(rbIndividual)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(selectorMaquina, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rbInicio)
                        .addGap(18, 18, 18)
                        .addComponent(rbEntreFechas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcInicioRango, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcFinRango, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(rbNumeroActas)
                        .addGap(2, 2, 2)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txtNActas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rbPiezasS)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 119, Short.MAX_VALUE)
                        .addComponent(jbExportaPdf2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbExportaPdf1))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );

        rbIndividual.getAccessibleContext().setAccessibleName("Elige la máquina a gestionar:");

        jMenuBar1.setBackground(new java.awt.Color(204, 204, 204));
        jMenuBar1.setForeground(new java.awt.Color(51, 51, 51));
        jMenuBar1.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jMenuBar1AncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        jMenu1.setBackground(new java.awt.Color(0, 102, 102));
        jMenu1.setText("Organizar actas");
        jMenu1.setFont(new java.awt.Font("3ds Light", 1, 14)); // NOI18N

        jMenuAscendente.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        jMenuAscendente.setText("en orden ascendente");
        jMenuAscendente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuAscendenteActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuAscendente);

        jMenuDescendente.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        jMenuDescendente.setText("en roden descendente");
        jMenuDescendente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuDescendenteActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuDescendente);

        jMenuBar1.add(jMenu1);

        Opciones.setBackground(new java.awt.Color(0, 51, 51));
        Opciones.setText("Opciones");
        Opciones.setFont(new java.awt.Font("3ds Light", 1, 14)); // NOI18N

        jRadioButtonElimina.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        jRadioButtonElimina.setSelected(true);
        jRadioButtonElimina.setText("Elimina tarea");
        jRadioButtonElimina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonEliminaActionPerformed(evt);
            }
        });
        Opciones.add(jRadioButtonElimina);

        jRadioButtonModifica.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_MASK));
        jRadioButtonModifica.setSelected(true);
        jRadioButtonModifica.setText("Modifica tarea");
        jRadioButtonModifica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonModificaActionPerformed(evt);
            }
        });
        Opciones.add(jRadioButtonModifica);

        jMenuBar1.add(Opciones);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel1.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rbInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbInicioActionPerformed
        generaConsulta(Integer.parseInt(txtNActas.getText()));
        try {
            modificaTxt();
        } catch (IOException ex) {
            Logger.getLogger(panelActas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_rbInicioActionPerformed

    private void selectorMaquinaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectorMaquinaActionPerformed
        generaConsulta(Integer.parseInt(txtNActas.getText()));
        try {
            modificaTxt();
        } catch (IOException ex) {
            Logger.getLogger(panelActas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_selectorMaquinaActionPerformed

    private void jMenuDescendenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuDescendenteActionPerformed
        ordenaTabla('D');
        repaint();
        JOptionPane.showMessageDialog(null,"Se han ordenado las actas en orden DESCENDENTE.");
    }//GEN-LAST:event_jMenuDescendenteActionPerformed

    private void jMenuAscendenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuAscendenteActionPerformed
        ordenaTabla('A');
        repaint();
        JOptionPane.showMessageDialog(null,"Se han ordenado las actas en orden ASCENDENTE.");
    }//GEN-LAST:event_jMenuAscendenteActionPerformed

    private void jMenuBar1AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jMenuBar1AncestorAdded
            
    }//GEN-LAST:event_jMenuBar1AncestorAdded

    private void jRadioButtonEliminaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonEliminaActionPerformed
        eliminarBorrarActa();
    }//GEN-LAST:event_jRadioButtonEliminaActionPerformed

    private void jbExportaPdf1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbExportaPdf1ActionPerformed
        dispose();
        mod.dispose();
    }//GEN-LAST:event_jbExportaPdf1ActionPerformed

    private void jRadioButtonModificaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonModificaActionPerformed
         eliminarBorrarActa();
    }//GEN-LAST:event_jRadioButtonModificaActionPerformed

    private void rbTodasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbTodasActionPerformed
        generaConsulta(Integer.parseInt(txtNActas.getText()));
        try {
            modificaTxt();
        } catch (IOException ex) {
            Logger.getLogger(panelActas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_rbTodasActionPerformed

    private void rbIndividualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbIndividualActionPerformed
        generaConsulta(Integer.parseInt(txtNActas.getText()));
        try {
            modificaTxt();
        } catch (IOException ex) {
            Logger.getLogger(panelActas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_rbIndividualActionPerformed

    private void rbEntreFechasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbEntreFechasActionPerformed
        generaConsulta(Integer.parseInt(txtNActas.getText()));
        try {
            modificaTxt();
        } catch (IOException ex) {
            Logger.getLogger(panelActas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_rbEntreFechasActionPerformed

    private void rbNumeroActasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbNumeroActasActionPerformed
        generaConsulta(Integer.parseInt(txtNActas.getText()));
        try {
            modificaTxt();
        } catch (IOException ex) {
            Logger.getLogger(panelActas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_rbNumeroActasActionPerformed

    private void rbPiezasSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbPiezasSActionPerformed
        generaConsulta(Integer.parseInt(txtNActas.getText()));
        try {
            modificaTxt();
        } catch (IOException ex) {
            Logger.getLogger(panelActas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_rbPiezasSActionPerformed

    private void txtNActasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNActasActionPerformed
        generaConsulta(Integer.parseInt(txtNActas.getText()));
        try {
            modificaTxt();
        } catch (IOException ex) {
            Logger.getLogger(panelActas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtNActasActionPerformed

    private void jcInicioRangoDateChanged(org.freixas.jcalendar.DateEvent evt) {//GEN-FIRST:event_jcInicioRangoDateChanged
       generaConsulta(Integer.parseInt(txtNActas.getText()));
        try {
            modificaTxt();
        } catch (IOException ex) {
            Logger.getLogger(panelActas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jcInicioRangoDateChanged

    private void jcFinRangoDateChanged(org.freixas.jcalendar.DateEvent evt) {//GEN-FIRST:event_jcFinRangoDateChanged
        generaConsulta(Integer.parseInt(txtNActas.getText()));
        try {
            modificaTxt();
        } catch (IOException ex) {
            Logger.getLogger(panelActas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jcFinRangoDateChanged

    private void jbExportaPdf2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbExportaPdf2ActionPerformed
        preparaReporte();
        boolean cuantificador = rbInicio.isSelected();  //indica verdadero si se van a imprimir cuantificador las actas y falso si no.        
        boolean piezasSustiruidas = rbPiezasS.isSelected();
        boolean todas = rbTodas.isSelected();
        String nombreMaquina = (String) selectorMaquina.getSelectedItem();

        String cadena = this.txtNActas.getText();
        if (esNumero(cadena) == false) {
            JOptionPane.showMessageDialog(null, "Los datos introducidos no son numéricos. Inseta solo numeros");
        } else {
            numeroActas = Integer.parseInt(cadena);
        }
    }//GEN-LAST:event_jbExportaPdf2ActionPerformed

    private void clicK_acta(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clicK_acta
        
    }//GEN-LAST:event_clicK_acta

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dispose(); //Método que tienes que crearte
        }
    }//GEN-LAST:event_formKeyPressed

    private void TablaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaMousePressed
        try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
            Statement stmt = conn.createStatement();
            ResultSet r = stmt.executeQuery("SELECT * FROM mantenimiento WHERE NumeroActa=\"" + Tabla.getValueAt(Tabla.getSelectedRow(), 0) + "\"")) {
            while (r.next()) {
                String h = r.getString("NumeroActa");
                mod = new Muestra_Acta(h);
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

        } catch (SQLException ex) {
            Logger.getLogger(Tareas_Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        mostrarTabla();
    }//GEN-LAST:event_TablaMousePressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(panelActas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new panelActas().setVisible(true);
            } catch (SQLException ex) {
                Logger.getLogger(panelActas.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu Opciones;
    private javax.swing.JTable Tabla;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuAscendente;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuDescendente;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButtonMenuItem jRadioButtonElimina;
    private javax.swing.JRadioButtonMenuItem jRadioButtonModifica;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton jbExportaPdf1;
    private javax.swing.JButton jbExportaPdf2;
    private org.freixas.jcalendar.JCalendarCombo jcFinRango;
    private org.freixas.jcalendar.JCalendarCombo jcInicioRango;
    private javax.swing.JRadioButton rbEntreFechas;
    private javax.swing.JRadioButton rbIndividual;
    private javax.swing.JRadioButton rbInicio;
    private javax.swing.JRadioButton rbNumeroActas;
    private javax.swing.JRadioButton rbPiezasS;
    private javax.swing.JRadioButton rbTodas;
    private javax.swing.JComboBox<String> selectorMaquina;
    private javax.swing.JTextField txtNActas;
    // End of variables declaration//GEN-END:variables
}
