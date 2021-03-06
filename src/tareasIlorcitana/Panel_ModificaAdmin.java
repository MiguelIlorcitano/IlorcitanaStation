/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tareasIlorcitana;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import satation.Main;
import satation.Propiedades;

/**
 *
 * @author Usuario
 */
public class Panel_ModificaAdmin extends javax.swing.JFrame {
    
    int id_t;
    String id_m;
    int contadorP = 0;
    ArrayList <JCheckBox> array_checkbox;

    /**
     * Creates new form Panel_Nueva
     * @param id
     */
    public Panel_ModificaAdmin(String id) {
        initComponents();
        this.setLocationRelativeTo(null);  
        muestraTarea(id_t=Integer.parseInt(id));  
        jCMaquina.setEnabled(false);
    }
    
    public Panel_ModificaAdmin() {
        initComponents();
    }
    
    /**
     * LLena los combobox.
     */
    public void llenarComboMaquinas(){ 
            String descripcion=null;
            try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT id_maquina,descripcion FROM maquinas")) {
                jCMaquina.setEnabled(true);
                jCMaquina.removeAllItems();
                while(rs.next()){
                    jCMaquina.addItem(rs.getString("descripcion"));
                }
                if(descripcion!=null){
                jCMaquina.setSelectedItem(descripcion);
                }
            }catch(SQLException e){
                JOptionPane.showMessageDialog(rootPane, e);
            }
    }
    
    private void modificaTxt() throws IOException{ 
        Propiedades.setPropiedad("modificado", "true");
    }
    
    private void extraeObservaciones(String ob){
        array_checkbox = new ArrayList<>();
        String aux [];
        
        if (ob.contains("#")){
            aux = ob.split("#");
            for (int i = 0; i < (aux.length - 1); i++) {
                if (!aux[i].contains("#")) {
                    jPanel_tareas.setLayout(new BoxLayout(jPanel_tareas, BoxLayout.Y_AXIS));
                    array_checkbox.add(new JCheckBox(aux[i]));
                    array_checkbox.get(contadorP).setBackground(Color.white);
                    array_checkbox.get(contadorP).setFont(new Font("Tahoma", 0, 14));
                    jPanel_tareas.add(array_checkbox.get(contadorP));
                    jPanel_tareas.revalidate();
                    jPanel_tareas.repaint();
                    contadorP++;
                }
            }
        }else if(ob.contains("-")){
            aux = ob.split("-");
            for (int i = 0; i < (aux.length - 1); i++) {
                if (!aux[i].contains("-")) {
                    jPanel_tareas.setLayout(new BoxLayout(jPanel_tareas, BoxLayout.Y_AXIS));
                    array_checkbox.add(new JCheckBox(aux[i]));
                    array_checkbox.get(contadorP).setBackground(Color.white);
                    array_checkbox.get(contadorP).setFont(new Font("Tahoma", 0, 14));
                    jPanel_tareas.add(array_checkbox.get(contadorP));
                    jPanel_tareas.revalidate();
                    jPanel_tareas.repaint();
                    contadorP++;
                }
            }
        }else{
            jPanel_tareas.setLayout(new BoxLayout(jPanel_tareas, BoxLayout.Y_AXIS));
            array_checkbox.add(new JCheckBox(ob));
            array_checkbox.get(contadorP).setBackground(Color.white);
            array_checkbox.get(contadorP).setFont(new Font("Tahoma", 0, 14));
            jPanel_tareas.add(array_checkbox.get(contadorP));
            jPanel_tareas.revalidate();
            jPanel_tareas.repaint();
            contadorP++;
        }
        
    }
    
    private void muestraTarea(int i){
        try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
            Statement stmt = conn.createStatement();
            ResultSet r = stmt.executeQuery("SELECT * FROM tareas INNER JOIN maquinas ON (tareas.id_maquina=maquinas.id_maquina) WHERE  Id_tarea="+i)) {
            String obser;
            while (r.next()){ 
                tATarea.append(r.getString("tarea"));
                jlUsuario.setText(r.getString("usuario"));
                jCNivelPreferencia.setSelectedItem(r.getString("nivel_preferencia"));
                jCTipoTarea.setSelectedItem(r.getString("tipo_tarea"));
                jCEstado.setSelectedItem(r.getString("estado"));
                jlTipoProblema.setText(r.getString("tipo_problema"));
                jlFechaTarea.setText(r.getString("fecha_tarea"));
                jlFechaInicio.setText(r.getString("fecha_inicio"));
                jlFechaFin.setText(r.getString("fecha_fin"));
                extraeObservaciones(r.getString("operaciones"));
                tAObservaciones.setText(r.getString("observaciones")); 
                jlMaquina.setText(r.getString("descripcion"));
                id_m=r.getString("id_maquina");
                String as = r.getString("estado");
               
            }
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,"Error al cargar los datos de la tabla Mantenimiento ");
        }
    }
    
    private void modificaMaquina(){
        String com = jlMaquina.getText();
        String sss=null;
        try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM maquinas WHERE descripcion=\""+com+"\"")) {
            while(rs.next()){
                 sss=rs.getString("id_maquina");
            }
            if(!com.equals(id_m)){
                id_m=sss;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Panel_ModificaAdmin.class.getName()).log(Level.SEVERE, null, ex);
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

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tATarea = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jCNivelPreferencia = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jlMaquina = new javax.swing.JLabel();
        jCMaquina = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jCTipoTarea = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jlTipoProblema = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jlFechaTarea = new javax.swing.JLabel();
        jlFechaInicio = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jlFechaFin = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jCEstado = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        jlUsuario = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tAObservaciones = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel_tareas = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Tarea");
        setBackground(new java.awt.Color(204, 204, 204));
        setIconImage(new ImageIcon(getClass().getResource("/imagenes/satelite_p.png")).getImage());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 0, 0));
        jLabel1.setText("Tarea:");

        tATarea.setColumns(20);
        tATarea.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tATarea.setLineWrap(true);
        tATarea.setRows(5);
        tATarea.setWrapStyleWord(true);
        jScrollPane1.setViewportView(tATarea);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 0, 0));
        jLabel2.setText("Operaciones:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 0, 0));
        jLabel4.setText("Nivel de preferencia:");

        jCNivelPreferencia.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jCNivelPreferencia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "urgente", "prioritaria", "normal" }));

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/z_tarea.png"))); // NOI18N
        jButton1.setText("Modifica tarea");
        jButton1.setToolTipText("Modifica tarea");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 0, 0));
        jLabel5.setText("Maquina:");

        jlMaquina.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jlMaquina.setText("jLabel3");
        jlMaquina.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jlMaquinaMousePressed(evt);
            }
        });

        jCMaquina.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jCMaquina.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Null" }));
        jCMaquina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCMaquinaActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 0, 0));
        jLabel6.setText("Tipo de tarea:");

        jCTipoTarea.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jCTipoTarea.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "programación", "mecánica", "mantenimiento operario" }));
        jCTipoTarea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCTipoTareaActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 0, 0));
        jLabel7.setText("Tipo de problema:");

        jlTipoProblema.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jlTipoProblema.setText("jLabel3");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 0, 0));
        jLabel8.setText("Fecha de tarea:");

        jlFechaTarea.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jlFechaTarea.setText("jLabel3");

        jlFechaInicio.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jlFechaInicio.setText("jLabel3");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(51, 0, 0));
        jLabel10.setText("Fecha de inicio:");

        jlFechaFin.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jlFechaFin.setText("jLabel3");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(51, 0, 0));
        jLabel12.setText("Fecha de fin:");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(51, 0, 0));
        jLabel13.setText("Estado");

        jCEstado.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jCEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "en espera", "en proceso", "finalizado" }));
        jCEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCEstadoActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(51, 0, 0));
        jLabel14.setText("Usuario:");

        jlUsuario.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jlUsuario.setText("jLabel3");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 0, 0));
        jLabel3.setText("Observaciones:");

        tAObservaciones.setColumns(20);
        tAObservaciones.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tAObservaciones.setLineWrap(true);
        tAObservaciones.setRows(5);
        tAObservaciones.setWrapStyleWord(true);
        jScrollPane3.setViewportView(tAObservaciones);

        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jPanel_tareas.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel_tareasLayout = new javax.swing.GroupLayout(jPanel_tareas);
        jPanel_tareas.setLayout(jPanel_tareasLayout);
        jPanel_tareasLayout.setHorizontalGroup(
            jPanel_tareasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 697, Short.MAX_VALUE)
        );
        jPanel_tareasLayout.setVerticalGroup(
            jPanel_tareasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 127, Short.MAX_VALUE)
        );

        jScrollPane4.setViewportView(jPanel_tareas);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jCTipoTarea, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCNivelPreferencia, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jlFechaTarea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jlFechaInicio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jlFechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel5)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jlMaquina, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jCMaquina, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jScrollPane4)
                    .addComponent(jScrollPane3)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jlTipoProblema, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jlUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCNivelPreferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCTipoTarea, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlTipoProblema))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlMaquina)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCMaquina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jlFechaTarea))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jlFechaInicio))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(jlFechaFin))))
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel14)
                        .addComponent(jlUsuario)))
                .addGap(19, 19, 19))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        modificaMaquina();
        try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
            Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("UPDATE tareas SET tarea=\""+tATarea.getText()+"\",nivel_preferencia=\""+jCNivelPreferencia.getSelectedItem().toString()+"\",estado=\""+jCEstado.getSelectedItem().toString()+"\",id_maquina=\""+id_m+"\",observaciones=\""+tAObservaciones.getText()+"\" WHERE Id_tarea="+id_t);
            JOptionPane.showMessageDialog(null, "Tarea modificada correctamente.");
            modificaTxt();
        } catch (SQLException ex) {
            Logger.getLogger(Panel_ModificaAdmin.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "No se ha podido crear la tarea."); 
        } catch (IOException ex) {
            Logger.getLogger(Panel_ModificaAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jlMaquinaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlMaquinaMousePressed
        // TODO add your handling code here:
       llenarComboMaquinas();
    }//GEN-LAST:event_jlMaquinaMousePressed

    private void jCMaquinaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCMaquinaActionPerformed
        // TODO add your handling code here:
        jlMaquina.setText((String) jCMaquina.getSelectedItem());
//        try {
//            st= conexion.createStatement();
//            rs=st.executeQuery("SELECT id_maquina FROM maquinas WHERE descripcion="+jCMaquina.getSelectedItem());
//            while(rs.next()){
//                 id_m=rs.getString("id_maquina");
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(Panel_Modifica.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
        
    }//GEN-LAST:event_jCMaquinaActionPerformed

    private void jCTipoTareaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCTipoTareaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCTipoTareaActionPerformed

    private void jCEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCEstadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCEstadoActionPerformed

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
            java.util.logging.Logger.getLogger(Panel_ModificaAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>
        //</editor-fold>
        
        //</editor-fold>
        

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Panel_ModificaAdmin().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jCEstado;
    private javax.swing.JComboBox<String> jCMaquina;
    private javax.swing.JComboBox<String> jCNivelPreferencia;
    private javax.swing.JComboBox<String> jCTipoTarea;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel_tareas;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel jlFechaFin;
    private javax.swing.JLabel jlFechaInicio;
    private javax.swing.JLabel jlFechaTarea;
    private javax.swing.JLabel jlMaquina;
    private javax.swing.JLabel jlTipoProblema;
    private javax.swing.JLabel jlUsuario;
    private javax.swing.JTextArea tAObservaciones;
    private javax.swing.JTextArea tATarea;
    // End of variables declaration//GEN-END:variables
}
