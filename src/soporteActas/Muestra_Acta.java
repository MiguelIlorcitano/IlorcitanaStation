/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soporteActas;

import tareasIlorcitana.*;
import java.awt.Color;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import satation.Propiedades;

/**
 *
 * @author Usuario
 */
public class Muestra_Acta extends javax.swing.JFrame {
    
    private Connection conexion = null;
    private Statement st;
    private ResultSet rs;
    int id_t;
    String id_m;

    /**
     * Creates new form Panel_Nueva
     * @param id
     */
    public Muestra_Acta(String id) {
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
        conectarMy();        
        muestraTarea(id_t=Integer.parseInt(id));  
        //jCMaquina.setEnabled(false);
    }
    
    public Muestra_Acta() {
        initComponents();
    }
    
     /**
     * Conectar con la base de datos.
     */
    public final void conectarMy(){
        if (conexion == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                conexion = DriverManager.getConnection("jdbc:mysql://192.168.0.132:3307/ilorcitana", "irobotica", "1233");
                //conexion = DriverManager.getConnection("jdbc:mysql://nas:3307/ilorcitana", "local", "1233");
            } catch (ClassNotFoundException | SQLException ex) {
                JOptionPane.showMessageDialog(null,"Error al realizar la conexion "+ex);
            } 
        }
    }
    
//    /**
//     * LLena los combobox.
//     */
//    public void llenarComboMaquinas(){ 
//            String descripcion=null;
//            try {
//                jCMaquina.setEnabled(true);
//                jCMaquina.removeAllItems();
//                st= conexion.createStatement();
//                rs=st.executeQuery("SELECT id_maquina,descripcion FROM maquinas");
//                while(rs.next()){
//                    jCMaquina.addItem(rs.getString("descripcion"));
//                }
//                if(descripcion!=null){
//                jCMaquina.setSelectedItem(descripcion);
//                }
//            }catch(SQLException e){
//                JOptionPane.showMessageDialog(rootPane, e);
//            }
//    }
    
    private void modificaTxt() throws IOException{ 
        Propiedades.setPropiedad("modificado", "true");
    }
    
    private void muestraTarea(int i){
        try{ 
            st= conexion.createStatement();
            ResultSet r = st.executeQuery("SELECT * FROM mantenimiento WHERE  NumeroActa="+i); 
            while (r.next()){ 
                lActa.setText(r.getString("NumeroActa"));
                lTarea.setText(r.getString("Id_Tarea"));
                lMaquina.setText(r.getString("Maquina"));
                lOperario.setText(r.getString("Operario"));
                lFecha.setText(r.getString("Fecha"));
                lHoras.setText(r.getString("Horas"));
                lMantenimiento.setText(r.getString("TipoMantenimiento"));
                lProblema.setText(r.getString("TipoProblema"));
                taObservaciones.setText(r.getString("Observaciones"));
                taIndicaciones.setText(r.getString("Indicaciones"));
                taOperaciones.setText(r.getString("Operaciones"));
                taSustituidas.setText(r.getString("PiezasSustituidas"));
                taObservaciones.setEditable(false);
                taOperaciones.setEditable(false);
                taSustituidas.setEditable(false);
            }
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,"Error al cargar los datos de la tabla Mantenimiento ");
        }
    }
    
//    private void modificaMaquina(){
//        String com = jlMaquina.getText();
//        String sss=null;
//        try {
//            st=conexion.createStatement();
//            rs=st.executeQuery("SELECT * FROM maquinas WHERE descripcion=\""+com+"\"");
//            while(rs.next()){
//                 sss=rs.getString("id_maquina");
//            }
//            if(!com.equals(id_m)){
//                id_m=sss;
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(Panel_Modifica1.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    
//    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        lActa = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lTarea = new javax.swing.JLabel();
        lMaquina = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lFecha = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lOperario = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lMantenimiento = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lProblema = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        lHoras = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        taOperaciones = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        taObservaciones = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        taSustituidas = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        taIndicaciones = new javax.swing.JTextArea();
        jLabel15 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Tarea");
        setBackground(new java.awt.Color(255, 255, 255));
        setIconImage(new ImageIcon(getClass().getResource("/imagenes/editor.png")).getImage());

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 51, 51));
        jLabel1.setText("Número de acta:");

        jButton1.setBackground(new java.awt.Color(0, 102, 102));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Modifica tarea");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        lActa.setBackground(new java.awt.Color(255, 255, 255));
        lActa.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lActa.setForeground(new java.awt.Color(51, 51, 51));
        lActa.setText("jLabel3");
        lActa.setOpaque(true);
        lActa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lActaMousePressed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 51, 51));
        jLabel3.setText("Id de tarea:");

        lTarea.setBackground(new java.awt.Color(255, 255, 255));
        lTarea.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lTarea.setForeground(new java.awt.Color(51, 51, 51));
        lTarea.setText("jLabel3");
        lTarea.setOpaque(true);
        lTarea.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lTareaMousePressed(evt);
            }
        });

        lMaquina.setBackground(new java.awt.Color(255, 255, 255));
        lMaquina.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lMaquina.setForeground(new java.awt.Color(51, 51, 51));
        lMaquina.setText("jLabel3");
        lMaquina.setOpaque(true);
        lMaquina.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lMaquinaMousePressed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 51, 51));
        jLabel6.setText("Maquina:");

        lFecha.setBackground(new java.awt.Color(255, 255, 255));
        lFecha.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lFecha.setForeground(new java.awt.Color(51, 51, 51));
        lFecha.setText("jLabel3");
        lFecha.setOpaque(true);
        lFecha.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lFechaMousePressed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 51, 51));
        jLabel7.setText("Fecha:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 51, 51));
        jLabel8.setText("Operario:");

        lOperario.setBackground(new java.awt.Color(255, 255, 255));
        lOperario.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lOperario.setForeground(new java.awt.Color(51, 51, 51));
        lOperario.setText("jLabel3");
        lOperario.setOpaque(true);
        lOperario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lOperarioMousePressed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 51, 51));
        jLabel9.setText("Tipo de mantenimiento:");

        lMantenimiento.setBackground(new java.awt.Color(255, 255, 255));
        lMantenimiento.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lMantenimiento.setForeground(new java.awt.Color(51, 51, 51));
        lMantenimiento.setText("jLabel3");
        lMantenimiento.setOpaque(true);
        lMantenimiento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lMantenimientoMousePressed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 51, 51));
        jLabel10.setText("Tipo de problema:");

        lProblema.setBackground(new java.awt.Color(255, 255, 255));
        lProblema.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lProblema.setForeground(new java.awt.Color(51, 51, 51));
        lProblema.setText("jLabel3");
        lProblema.setOpaque(true);
        lProblema.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lProblemaMousePressed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 51, 51));
        jLabel11.setText("Observaciones:");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 51, 51));
        jLabel12.setText("Operaciones:");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 51, 51));
        jLabel13.setText("Piezas sustituidas:");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 51, 51));
        jLabel14.setText("Horas:");

        lHoras.setBackground(new java.awt.Color(255, 255, 255));
        lHoras.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lHoras.setForeground(new java.awt.Color(51, 51, 51));
        lHoras.setText("jLabel3");
        lHoras.setOpaque(true);
        lHoras.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lHorasMousePressed(evt);
            }
        });

        taOperaciones.setColumns(20);
        taOperaciones.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        taOperaciones.setForeground(new java.awt.Color(51, 51, 51));
        taOperaciones.setRows(5);
        taOperaciones.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)));
        jScrollPane1.setViewportView(taOperaciones);

        taObservaciones.setColumns(20);
        taObservaciones.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        taObservaciones.setForeground(new java.awt.Color(51, 51, 51));
        taObservaciones.setRows(5);
        taObservaciones.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)));
        taObservaciones.setPreferredSize(new java.awt.Dimension(242, 90));
        jScrollPane2.setViewportView(taObservaciones);

        taSustituidas.setColumns(20);
        taSustituidas.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        taSustituidas.setForeground(new java.awt.Color(51, 51, 51));
        taSustituidas.setRows(5);
        taSustituidas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)));
        jScrollPane3.setViewportView(taSustituidas);

        taIndicaciones.setColumns(20);
        taIndicaciones.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        taIndicaciones.setForeground(new java.awt.Color(51, 51, 51));
        taIndicaciones.setRows(5);
        taIndicaciones.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)));
        jScrollPane4.setViewportView(taIndicaciones);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 51, 51));
        jLabel15.setText("Indicaciones:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lProblema, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lMantenimiento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane4)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane3)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lOperario, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lHoras, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lActa, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lTarea, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lMaquina, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel11)
                            .addComponent(jLabel15)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(lMaquina)
                        .addComponent(jLabel3)
                        .addComponent(lTarea))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(lActa)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lHoras)
                        .addComponent(jLabel14))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(lOperario)
                        .addComponent(jLabel7)
                        .addComponent(lFecha)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(lMantenimiento))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(lProblema))
                .addGap(18, 18, 18)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
//        modificaMaquina();
//        try {
//            st= conexion.createStatement();
//            st.executeUpdate("UPDATE tareas SET tarea=\""+tATarea.getText()+"\",nivel_preferencia=\""+jCNivelPreferencia.getSelectedItem().toString()+"\",id_maquina=\""+id_m+"\",observaciones=\""+tAObservaciones.getText()+"\" WHERE Id_tarea="+id_t);
//            JOptionPane.showMessageDialog(null, "Tarea modificada correctamente.");
//            modificaTxt();
//        } catch (SQLException ex) {
//            Logger.getLogger(Panel_Modifica1.class.getName()).log(Level.SEVERE, null, ex);
//            JOptionPane.showMessageDialog(null, "No se ha podido crear la tarea."); 
//        } catch (IOException ex) {
//            Logger.getLogger(Panel_Modifica1.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void lActaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lActaMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_lActaMousePressed

    private void lTareaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lTareaMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_lTareaMousePressed

    private void lMaquinaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lMaquinaMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_lMaquinaMousePressed

    private void lFechaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lFechaMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_lFechaMousePressed

    private void lOperarioMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lOperarioMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_lOperarioMousePressed

    private void lMantenimientoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lMantenimientoMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_lMantenimientoMousePressed

    private void lProblemaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lProblemaMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_lProblemaMousePressed

    private void lHorasMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lHorasMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_lHorasMousePressed

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
            java.util.logging.Logger.getLogger(Muestra_Acta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>
        //</editor-fold>
        
        //</editor-fold>
        //</editor-fold>
        
        //</editor-fold>
        //</editor-fold>
        
        //</editor-fold>
        //</editor-fold>
        
        //</editor-fold>
        //</editor-fold>
        
        //</editor-fold>
        //</editor-fold>
        
        //</editor-fold>
        //</editor-fold>
        
        //</editor-fold>
        

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Muestra_Acta().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lActa;
    private javax.swing.JLabel lFecha;
    private javax.swing.JLabel lHoras;
    private javax.swing.JLabel lMantenimiento;
    private javax.swing.JLabel lMaquina;
    private javax.swing.JLabel lOperario;
    private javax.swing.JLabel lProblema;
    private javax.swing.JLabel lTarea;
    private javax.swing.JTextArea taIndicaciones;
    private javax.swing.JTextArea taObservaciones;
    private javax.swing.JTextArea taOperaciones;
    private javax.swing.JTextArea taSustituidas;
    // End of variables declaration//GEN-END:variables
}
