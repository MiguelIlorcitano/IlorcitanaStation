
package tareasIlorcitana;

import java.awt.Font;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import satation.Main;
import satation.Propiedades;

/**
 *
 * @author Miguel Angel Carrillo Garcia
 */
public class Panel_Mantenimiento extends javax.swing.JFrame {
    
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
    String tipoProblemaFinal; //Recoge la suma de todos los problemas.
    String operaciones;         //Recoge las operaciones realizadas.
    String piezasSustituidas;   //Recoge las piezas sustituidas.
    String observaciones;       //Recoge las observaciones del usuario.
    String obperaciones;       //Recoge las observaciones del usuario.
    String auxOperario;
    String auxMaquina;
    String dia;
    String mes;
    String anno;
    String id_maquina;
    String id_tarea;
    boolean es_tarea=false;
    private String ob;
    
    /**
     * Creates new form Mantenimiento
     */
    public Panel_Mantenimiento() {
        initComponents();
        this.setLocationRelativeTo(null);
        //this.setExtendedState(MAXIMIZED_BOTH);
        llenarComboMaquinas();
        id_tarea="0";
        txtOperaciones.setEditable(false);
    }
    
    /**
     * Creates new form Mantenimiento
     * @param id_tarea
     * @param tr variable para tarea
     * @param t_tr variable para tipo de tarea
     * @param t_pr variable para tipo de problema
     * @param id_maquina
     * @param op
     * @param ob variable para observaciones
     */
    public Panel_Mantenimiento(String id_tarea, String tr, String t_tr, String t_pr, String id_maquina, String op, String ob) {
        initComponents();
        this.setLocationRelativeTo(null);
        //this.setExtendedState(MAXIMIZED_BOTH);
        this.id_maquina=id_maquina;
        this.id_tarea=id_tarea;
        llenarComboMaquinas();
        jCTarea.setSelectedIndex(muestraTarea(tr));
        jCTarea.setEditable(false);
        //txtIndicaciones.setFont( new Font("Century Gothic",1, 14) );
        //txtIndicaciones.append(tr+":");
        //txtIndicaciones.append("\n");
        txtIndicaciones.setFont( new Font("Century Gothic",0, 14) );
        txtIndicaciones.setText(tr);
        txtIndicaciones.setEditable(false);
        txtOperaciones.setText(op);
        tipoProblemaMantenimiento(t_pr);
        this.ob = ob;
        es_tarea=true;
    }
    
    private int muestraTarea(String tipo){
        int res=0;
        if("Rotura".equals(tipo)){
            res=1;
        }else if("Revisión".equals(tipo)){
            res=2;
        }else if("Revisión Semanal".equals(tipo)){
            res=3;
        }else if("Revisión Mensual".equals(tipo)){
            res=4;
        }else if("Revisión Semestral".equals(tipo)){
            res=5;
        }else if("Modificación".equals(tipo)){
            res=6;
        }
        return res;
    }
    
    /**
     * Metodo para ingresar el tipo de mantenimiento y el tipo de problema cuando se hace un parte a través de una tarea.
     */
    private void tipoProblemaMantenimiento(String tip){
        if("mantenimiento".equals(tip)){
            rbPreventivo.setSelected(true);
        }else if (tip.contains("null")||tip.contains("NULL")){
            String a = tip;
        }else{
            rbCorrectivo.setSelected(true);
            String esp [] = new String[4];
            esp= tip.split("\t");
            for (String esp1 : esp) {
                if (esp1.equals("Mecánico")) {
                    rbMecanico.setSelected(true);
                }else if (esp1.equals("Hidráulico")) {
                    rbHidraulico.setSelected(true);
                }else if (esp1.equals("Neumático")) {
                    rbNeumatico.setSelected(true);
                }else if (esp1.equals("Electrico")) {
                    rbElectrico.setSelected(true);
                }
            }
        }
    }
    
    /**
     * LLena los combobox.
     */
    private void llenarComboMaquinas(){ 
        String descripcion=null;
            
        try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id_maquina,descripcion FROM maquinas")) {
            jCMaquina.setEnabled(true);
            jCMaquina.removeAllItems();
            while (rs.next()) {
                jCMaquina.addItem(rs.getString("descripcion"));
                if (rs.getString("id_maquina").equals(id_maquina)) {
                    jCMaquina.addItem(rs.getString("descripcion"));
                    descripcion = rs.getString("descripcion");
                }
            }
            if (descripcion != null) {
                jCMaquina.setSelectedItem(descripcion);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }
            
        try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Nombre,Clave FROM UsuariosP ORDER BY nombre ASC;")) {
            boxOperario.setEnabled(true);
            boxOperario.removeAllItems();
            while (rs.next()) {
                boxOperario.addItem(rs.getString("Nombre"));
                if (rs.getString("Clave").equals(Propiedades.getPropiedad("clave"))) {
                    boxOperario.setSelectedItem(rs.getString("Nombre"));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }
    }
    
    /**
    * Metodo para añadir, modificar o borrar datos en la base de datos.
    * @param sql
    * @return 
    */
    private int añadirModElim(String sql){
        try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
            Statement stmt = conn.createStatement();) {
            JOptionPane.showMessageDialog(null,"Acta almacenada correctamente.");
            return stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Error al crear el objeto sentencia "+ex);
            Logger.getLogger(Panel_Mantenimiento.class.getName()).log(Level.SEVERE, null, ex);
        }return 0;
    }
    
    /**
     * Metodo para recoger el tipo de problema.
     */
    private void cogeProblema(){
         if (rbMecanico.isSelected()==true){
            tipoProblemaM="Mecánico. ";
        }else {
            tipoProblemaM="";
        }
        
        if (rbHidraulico.isSelected()==true){
            tipoProblemaH="Hidráulico. ";
        }else {
            tipoProblemaH="";
        }
        
        
        if (rbNeumatico.isSelected()==true){
            tipoProblemaN="Neumático. ";
        }else {
            tipoProblemaN="";
        }
                
        if (rbElectrico.isSelected()==true){
            tipoProblemaE="Electrico. ";
        }else {
            tipoProblemaE="";
        }
               
        if((rbMecanico.isSelected()==false)&&(rbHidraulico.isSelected()==false)&&(rbNeumatico.isSelected()==false)&&(rbElectrico.isSelected()==false)){
            tipoProblema="Mantenimiento.";    
        }
        
        //Creamos un array para pasar todas la variable de tipoProblema.
        String []t= {tipoProblemaM,tipoProblemaH,tipoProblemaN,tipoProblemaE};
        
        //El array lo pasamos a un stringBuffer para después pasarlo a string.
        StringBuffer cadena = new StringBuffer();
        for (int x=0;x<t.length;x++){
            cadena =cadena.append(t[x]);
        }
        
        //Pasamos el stringBuffer a string.
        tipoProblemaFinal=cadena.toString();
        if(tipoProblemaFinal.equals("")){
            tipoProblemaFinal="mantenimiento";
        }
    }
    
    /**
     * Metodo para recoger el tipo de mantenimiento.
     */
    private void cogeMantenimiento(){
        if ((rbPreventivo.isSelected()==true)&&(rbCorrectivo.isSelected()==false)){
            tipoMantenimiento="Preventivo";
        }else if((rbPreventivo.isSelected()==true)&&(rbCorrectivo.isSelected()==true)){
            tipoMantenimiento="Preventivo y correctivo";
        }else if((rbPreventivo.isSelected()==false)&&(rbCorrectivo.isSelected()==true)){
            tipoMantenimiento="Correctivo";
        }else{
            tipoMantenimiento="ninguno";
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
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        rbPreventivo = new javax.swing.JRadioButton();
        rbCorrectivo = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        rbMecanico = new javax.swing.JRadioButton();
        rbHidraulico = new javax.swing.JRadioButton();
        rbNeumatico = new javax.swing.JRadioButton();
        rbElectrico = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        boxOperario = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jCMaquina = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        boxHoras = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        boxMinutos = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jCTarea = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        jCalendarCombo1 = new org.freixas.jcalendar.JCalendarCombo();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtOperaciones = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtObservaciones = new javax.swing.JTextArea();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtIndicaciones = new javax.swing.JTextArea();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtPiezasSustituidas = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Parte de Mantenimiento");
        setIconImage(new ImageIcon(getClass().getResource("/imagenes/satelite_p.png")).getImage());
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204), 2));
        jPanel1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        jLabel1.setBackground(new java.awt.Color(234, 245, 245));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 153, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/man.png"))); // NOI18N
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel3.setBackground(new java.awt.Color(51, 153, 255));
        jLabel3.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Tipo de mantenimiento:");
        jLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 153)));
        jLabel3.setOpaque(true);

        rbPreventivo.setBackground(new java.awt.Color(226, 236, 247));
        rbPreventivo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbPreventivo.setText("Preventivo ");

        rbCorrectivo.setBackground(new java.awt.Color(226, 236, 247));
        rbCorrectivo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbCorrectivo.setText("Correctivo");

        jLabel4.setBackground(new java.awt.Color(51, 153, 255));
        jLabel4.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Tipo de problema:");
        jLabel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 153)));
        jLabel4.setOpaque(true);

        rbMecanico.setBackground(new java.awt.Color(226, 236, 247));
        rbMecanico.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbMecanico.setText("Mecanico");

        rbHidraulico.setBackground(new java.awt.Color(226, 236, 247));
        rbHidraulico.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbHidraulico.setText("Hidráulico");

        rbNeumatico.setBackground(new java.awt.Color(226, 236, 247));
        rbNeumatico.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbNeumatico.setText("Neumático");

        rbElectrico.setBackground(new java.awt.Color(226, 236, 247));
        rbElectrico.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbElectrico.setText("Eléctrico");

        jLabel5.setBackground(new java.awt.Color(51, 153, 255));
        jLabel5.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Tipo de tarea:");
        jLabel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 153)));
        jLabel5.setOpaque(true);

        jLabel6.setBackground(new java.awt.Color(51, 153, 255));
        jLabel6.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Piezas sustituidas:");
        jLabel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 153)));
        jLabel6.setOpaque(true);

        jLabel7.setBackground(new java.awt.Color(51, 153, 255));
        jLabel7.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Horas:");
        jLabel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 153)));
        jLabel7.setOpaque(true);

        jLabel8.setBackground(new java.awt.Color(51, 153, 255));
        jLabel8.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Operario:");
        jLabel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 153)));
        jLabel8.setOpaque(true);

        boxOperario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        boxOperario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxOperarioActionPerformed(evt);
            }
        });

        jLabel9.setBackground(new java.awt.Color(51, 153, 255));
        jLabel9.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Máquina:");
        jLabel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 153)));
        jLabel9.setOpaque(true);

        jCMaquina.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jButton1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/archivar.png"))); // NOI18N
        jButton1.setText("Finalizar");
        jButton1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel10.setBackground(new java.awt.Color(51, 153, 255));
        jLabel10.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Operaciones:");
        jLabel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 153)));
        jLabel10.setOpaque(true);

        boxHoras.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        boxHoras.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30" }));
        boxHoras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxHorasActionPerformed(evt);
            }
        });

        jLabel11.setBackground(new java.awt.Color(51, 153, 255));
        jLabel11.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Minutos:");
        jLabel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 153)));
        jLabel11.setOpaque(true);

        boxMinutos.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        boxMinutos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "00", "15", "30", "45" }));

        jLabel12.setBackground(new java.awt.Color(51, 153, 255));
        jLabel12.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Fecha:");
        jLabel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 153)));
        jLabel12.setOpaque(true);

        jLabel13.setBackground(new java.awt.Color(51, 153, 255));
        jLabel13.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Especifica tarea:");
        jLabel13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 153)));
        jLabel13.setOpaque(true);

        jButton2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/adjuntar.png"))); // NOI18N
        jButton2.setText("Adjuntar documento");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jCTarea.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jCTarea.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sin tarea", "Reparación", "Revisión", "Revisión semanal", "Revisión mensual", "Revisión semestral", "Elaboración", "Modificación", "Cambio de herramienta", "Montaje de nueva herramienta" }));
        jCTarea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCTareaActionPerformed(evt);
            }
        });

        jLabel14.setBackground(new java.awt.Color(51, 153, 255));
        jLabel14.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Observaciones:");
        jLabel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 153)));
        jLabel14.setOpaque(true);

        jCalendarCombo1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        txtOperaciones.setColumns(20);
        txtOperaciones.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtOperaciones.setLineWrap(true);
        txtOperaciones.setRows(5);
        txtOperaciones.setWrapStyleWord(true);
        jScrollPane3.setViewportView(txtOperaciones);

        txtObservaciones.setColumns(20);
        txtObservaciones.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setRows(5);
        txtObservaciones.setWrapStyleWord(true);
        jScrollPane4.setViewportView(txtObservaciones);

        txtIndicaciones.setColumns(20);
        txtIndicaciones.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtIndicaciones.setLineWrap(true);
        txtIndicaciones.setRows(5);
        txtIndicaciones.setWrapStyleWord(true);
        jScrollPane5.setViewportView(txtIndicaciones);

        txtPiezasSustituidas.setColumns(20);
        txtPiezasSustituidas.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPiezasSustituidas.setLineWrap(true);
        txtPiezasSustituidas.setRows(5);
        txtPiezasSustituidas.setWrapStyleWord(true);
        jScrollPane6.setViewportView(txtPiezasSustituidas);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCalendarCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(boxOperario, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(rbPreventivo)
                                    .addGap(18, 18, 18)
                                    .addComponent(rbCorrectivo))))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jCMaquina, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(boxHoras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(boxMinutos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(rbMecanico)
                                        .addGap(18, 18, 18)
                                        .addComponent(rbHidraulico)
                                        .addGap(18, 18, 18)
                                        .addComponent(rbNeumatico)
                                        .addGap(18, 18, 18)
                                        .addComponent(rbElectrico)))
                                .addGap(0, 99, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE)
                            .addComponent(jScrollPane5)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3)
                            .addComponent(jScrollPane6)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton1))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCTarea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jCMaquina, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(boxOperario, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(boxHoras, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel11)
                        .addComponent(boxMinutos, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jCalendarCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel12)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rbPreventivo)
                            .addComponent(rbCorrectivo)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rbMecanico)
                            .addComponent(rbHidraulico)
                            .addComponent(rbNeumatico)
                            .addComponent(rbElectrico))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jCTarea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        boxOperario.getAccessibleContext().setAccessibleName("");
        jCMaquina.getAccessibleContext().setAccessibleName("");
        boxHoras.getAccessibleContext().setAccessibleName("");
        boxMinutos.getAccessibleContext().setAccessibleName("");

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

        pack();
    }// </editor-fold>//GEN-END:initComponents

    @SuppressWarnings("empty-statement")
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:                                
        operario = (String) boxOperario.getSelectedItem();  //Obtenemos el nombre del operario.
        maquina = (String) jCMaquina.getSelectedItem();    //Obtenemos el nombre de la máquina.
        
        fecha = new SimpleDateFormat("yyyy-MM-dd").format(jCalendarCombo1.getDate());
        
        String hor = (String) boxHoras.getSelectedItem();
        String min = (String) boxMinutos.getSelectedItem();
        horas = hor+":"+min+" horas.";
               
        cogeMantenimiento();    //Obtenemos el tipo de mantenimiento.
        cogeProblema();         //Obtenemos el tipo de problema.
        
        operaciones=this.txtIndicaciones.getText();             //Optenemos las operaciones realizadas.
        
        //Obtenemos las piezas sustituidas.
        if(this.txtPiezasSustituidas.getText().equals(""))
            piezasSustituidas="Sin operación";         
        else{
            piezasSustituidas=this.txtPiezasSustituidas.getText();   
        }

        //Obtenemos las observaciones.
        if(this.txtObservaciones.getText().equals(""))
            observaciones="Sin operación";         
        else{
            observaciones=this.txtOperaciones.getText();   
        }
        
        if ("".equals(fecha)){
            JOptionPane.showMessageDialog(null,"El Acta debe contener una fecha.");
        }else if("00".equals(hor) && "00".equals(min)){
            JOptionPane.showMessageDialog(null,"Se deben especificar las horas de trabajo.");
        }else if((rbPreventivo.isSelected()==false)&&(rbCorrectivo.isSelected()==false)){
            JOptionPane.showMessageDialog(null,"Deves seleccionar un tipo de mantenimiento.");
        }else if(jCTarea.getSelectedItem().equals("Sin tarea")&&es_tarea==false){
            JOptionPane.showMessageDialog(null,"Deves seleccionar una tarea.");
        }else if("".equals(operaciones)){
            JOptionPane.showMessageDialog(null,"Se deben especificar las operaciones realizadas.");
        }else{
            añadirModElim("INSERT INTO mantenimiento(Id_tarea,Maquina,Fecha,Operario,TipoMantenimiento,TipoProblema,Operaciones,PiezasSustituidas,CodigoRepuesto,Observaciones,Indicaciones,Horas)VALUES('"+id_tarea+"','"+maquina+"','"+fecha+"','"+operario+"','"+tipoMantenimiento+"','"+tipoProblemaFinal+"','"+operaciones+"','"+piezasSustituidas+"',NULL,'"+observaciones+"','"+txtObservaciones.getText()+"','"+horas+"')");            
            dispose();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void boxOperarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxOperarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_boxOperarioActionPerformed

    private void boxHorasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxHorasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_boxHorasActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        Adjunta_Archivos add = new Adjunta_Archivos((jCMaquina.getSelectedItem().toString()),id_tarea  );
        add.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jCTareaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCTareaActionPerformed
        txtOperaciones.setEditable(true);
        txtOperaciones.setText("");
        txtOperaciones.append(jCTarea.getSelectedItem().toString());
        txtOperaciones.setEditable(false);
    }//GEN-LAST:event_jCTareaActionPerformed

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Panel_Mantenimiento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Panel_Mantenimiento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Panel_Mantenimiento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Panel_Mantenimiento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Panel_Mantenimiento().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> boxHoras;
    private javax.swing.JComboBox<String> boxMinutos;
    private javax.swing.JComboBox<String> boxOperario;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jCMaquina;
    private javax.swing.JComboBox<String> jCTarea;
    private org.freixas.jcalendar.JCalendarCombo jCalendarCombo1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JRadioButton rbCorrectivo;
    private javax.swing.JRadioButton rbElectrico;
    private javax.swing.JRadioButton rbHidraulico;
    private javax.swing.JRadioButton rbMecanico;
    private javax.swing.JRadioButton rbNeumatico;
    private javax.swing.JRadioButton rbPreventivo;
    private javax.swing.JTextArea txtIndicaciones;
    private javax.swing.JTextArea txtObservaciones;
    private javax.swing.JTextArea txtOperaciones;
    private javax.swing.JTextArea txtPiezasSustituidas;
    // End of variables declaration//GEN-END:variables
}
