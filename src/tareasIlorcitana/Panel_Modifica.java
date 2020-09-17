/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tareasIlorcitana;

import java.awt.Color;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import satation.Main;
import satation.Propiedades;

/**
 *
 * @author Usuario
 */
public class Panel_Modifica extends javax.swing.JFrame {
    
    int id_t;
    String id_m;
    Calendar c1;

    /**
     * Creates new form Panel_Nueva
     * @param id
     */
    public Panel_Modifica(String id) {
        initComponents();
        this.setLocationRelativeTo(null);   
        muestraTarea(id_t=Integer.parseInt(id));  
        jCMaquina.setEnabled(false);
    }
    
    public Panel_Modifica() {
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
        String aux [];
        aux = ob.split("#");
        for (int i = 0; i<aux.length;i++) {
            if (!aux[i].contains("#")) {
                tAObservaciones.append("- "+aux[i]+"\n\n");
            } 
        }
    }
    
    private void muestraTarea(int i){
        
        try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
            Statement stmt = conn.createStatement();
            ResultSet r = stmt.executeQuery("SELECT * FROM tareas INNER JOIN maquinas ON (tareas.id_maquina=maquinas.id_maquina) WHERE  Id_tarea="+i)) {
            String obser;
            while (r.next()){ 
                tATarea.append(r.getString("tarea"));
                jCNivelPreferencia.setSelectedItem(r.getString("nivel_preferencia"));
                //tAObservaciones.append(r.getString("observaciones")); 
                obser= r.getString("observaciones");
                extraeObservaciones(obser);
                jlMaquina.setText(r.getString("descripcion"));
                id_m=r.getString("id_maquina");
                String as = r.getString("estado");
                if(null != as)switch (as) {
                    case "en proceso":
                        //jButton3.setVisible(false);
                        jbIniciar.setBackground(Color.gray);
                        jbIniciar.setEnabled(false);
                        labelProceso.setVisible(true);
                        break;
                    case "en espera":
                        jbFinalizar.setBackground(Color.gray);
                        jbFinalizar.setEnabled(false);
                        labelProceso.setVisible(false);
                        break;
                    default:
                        jbIniciar.setBackground(Color.gray);
                        jbIniciar.setEnabled(false);
                        jbFinalizar.setBackground(Color.gray);
                        jbFinalizar.setEnabled(false);
                        labelProceso.setVisible(false);
                        break;
                }
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
            Logger.getLogger(Panel_Modifica.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void actualizarFecha(int dias, String mantenimiento){
        c1 = new GregorianCalendar();
        c1.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0
        //c1.roll(Calendar.DATE,dias);
        int año = c1.get(Calendar.YEAR);
        int mes = c1.get(Calendar.MONTH)+1;
        int dia = c1.get(Calendar.DAY_OF_MONTH);
        String fech_fin = String.valueOf(dia)+"-"+String.valueOf(mes)+"-"+String.valueOf(año);
        
        try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
                Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("UPDATE maquinas SET "+mantenimiento+"=\""+fech_fin+"\" WHERE id_maquina=" + id_m);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los valores de la tabla Operaciones en panelMestra. " + e);
        }
    }
    
    private void preparaSiguienteMantenimiento(){
        switch (tATarea.getText()) {
            case "Revisión Anual":
                actualizarFecha(365, "M_Anual");
                break;
            case "Revisión Trimestral":
                actualizarFecha(90, "M_Trimestral");
                break;
            case "Revisión Mensual":
                actualizarFecha(30, "M_Mensual");
                break;
            case "Revisión Semanal":
                actualizarFecha(8, "M_Semanal");
                break;
            default:
                break;
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tATarea = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tAObservaciones = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jCNivelPreferencia = new javax.swing.JComboBox<>();
        labelProceso = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jlMaquina = new javax.swing.JLabel();
        jCMaquina = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jbIniciar = new javax.swing.JButton();
        jbFinalizar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Tarea");
        setBackground(new java.awt.Color(204, 204, 204));
        setIconImage(new ImageIcon(getClass().getResource("/imagenes/satelite_p.png")).getImage());
        setPreferredSize(new java.awt.Dimension(816, 768));
        setResizable(false);

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
        jLabel2.setText("Observaciones:");

        tAObservaciones.setColumns(20);
        tAObservaciones.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tAObservaciones.setLineWrap(true);
        tAObservaciones.setRows(5);
        tAObservaciones.setWrapStyleWord(true);
        jScrollPane2.setViewportView(tAObservaciones);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 0, 0));
        jLabel4.setText("Nivel de preferencia:");

        jCNivelPreferencia.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jCNivelPreferencia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "urgente", "prioritaria", "normal" }));

        labelProceso.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        labelProceso.setForeground(new java.awt.Color(255, 0, 0));
        labelProceso.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelProceso.setText("En proceso ...");

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

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));

        jbIniciar.setBackground(new java.awt.Color(51, 255, 51));
        jbIniciar.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jbIniciar.setText("Iniciar");
        jbIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbIniciarActionPerformed(evt);
            }
        });

        jbFinalizar.setBackground(new java.awt.Color(204, 0, 0));
        jbFinalizar.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jbFinalizar.setText("Finalizar");
        jbFinalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbFinalizarActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(0, 153, 153));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton1.setText("Modifica tarea");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                    .addComponent(jbFinalizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbIniciar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbIniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jbFinalizar, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jbIniciar.getAccessibleContext().setAccessibleDescription("");
        jbFinalizar.getAccessibleContext().setAccessibleDescription("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jCNivelPreferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(228, 228, 228)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(253, 253, 253))
                            .addComponent(jlMaquina, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCMaquina, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelProceso)
                            .addComponent(jLabel2)
                            .addComponent(jScrollPane2))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCNivelPreferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlMaquina))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCMaquina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addGap(18, 18, 18)
                .addComponent(labelProceso)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        modificaMaquina();
        try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
            Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("UPDATE tareas SET tarea=\""+tATarea.getText()+"\",nivel_preferencia=\""+jCNivelPreferencia.getSelectedItem().toString()+"\",id_maquina=\""+id_m+"\",observaciones=\""+tAObservaciones.getText()+"\" WHERE Id_tarea="+id_t);
            JOptionPane.showMessageDialog(null, "Tarea modificada correctamente.");
            modificaTxt();
        } catch (SQLException ex) {
            Logger.getLogger(Panel_Modifica.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "No se ha podido crear la tarea."); 
        } catch (IOException ex) {
            Logger.getLogger(Panel_Modifica.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jbIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbIniciarActionPerformed
        try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
            Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("UPDATE tareas SET tarea=\""+tATarea.getText()+"\",nivel_preferencia=\""+jCNivelPreferencia.getSelectedItem().toString()+"\",estado=\"en proceso\",observaciones=\""+tAObservaciones.getText()+"\",fecha_inicio=CURRENT_TIMESTAMP WHERE Id_tarea="+id_t);
            JOptionPane.showMessageDialog(null, "Tarea modificada correctamente.");
            modificaTxt();
        } catch (SQLException ex) {
            Logger.getLogger(Panel_Modifica.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "No se ha podido modificar la tarea.");
        } catch (IOException ex) {
            Logger.getLogger(Panel_Modifica.class.getName()).log(Level.SEVERE, null, ex);
        }
        dispose();
    }//GEN-LAST:event_jbIniciarActionPerformed

    private void jbFinalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbFinalizarActionPerformed
        preparaSiguienteMantenimiento();
        try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
            Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("UPDATE tareas SET tarea=\""+tATarea.getText()+"\",nivel_preferencia=\""+jCNivelPreferencia.getSelectedItem().toString()+"\",estado=\"finalizado\",observaciones=\""+tAObservaciones.getText()+"\",fecha_fin=CURRENT_TIMESTAMP WHERE Id_tarea="+id_t);
            JOptionPane.showMessageDialog(null, "Tarea modificada correctamente.");
            modificaTxt();
        }catch (SQLException ex) {
            Logger.getLogger(Panel_Modifica.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "No se ha podido modificar la tarea.");
        } catch (IOException ex) { 
            Logger.getLogger(Panel_Modifica.class.getName()).log(Level.SEVERE, null, ex);
        }
        dispose();
        try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
            Statement stmt = conn.createStatement();
            ResultSet r = stmt.executeQuery("SELECT * FROM tareas WHERE Id_tarea="+id_t)) {
            while (r.next()){ 
                tATarea.append(r.getString("tarea"));
                jCNivelPreferencia.setSelectedItem(r.getString("nivel_preferencia")); //r.getString("nivel_preferencia");
                tAObservaciones.append(r.getString("observaciones")); //r.getString("observaciones");
                Panel_Mantenimiento panel = new Panel_Mantenimiento(r.getString("Id_tarea"),r.getString("tarea"),r.getString("tipo_tarea"),r.getString("tipo_problema"),r.getString("id_maquina"),r.getString("observaciones"));
                panel.setVisible(true);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,"Error al cargar los datos de la tabla Mantenimiento ");
        }
    }//GEN-LAST:event_jbFinalizarActionPerformed

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
            java.util.logging.Logger.getLogger(Panel_Modifica.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>
        

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Panel_Modifica().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jCMaquina;
    private javax.swing.JComboBox<String> jCNivelPreferencia;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton jbFinalizar;
    private javax.swing.JButton jbIniciar;
    private javax.swing.JLabel jlMaquina;
    private javax.swing.JLabel labelProceso;
    private javax.swing.JTextArea tAObservaciones;
    private javax.swing.JTextArea tATarea;
    // End of variables declaration//GEN-END:variables
}
