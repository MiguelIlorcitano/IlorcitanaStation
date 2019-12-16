/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tareasIlorcitana;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import satation.Propiedades;

/**
 *
 * @author Usuario
 */
public class Panel_Nueva_Tarea extends javax.swing.JFrame {
    
    private Connection conexion = null;
    private Statement st;
    private ResultSet rs;
    boolean pulsado;
    int contador = 0;

    /**
     * Creates new form Panel_Nueva
     */
    public Panel_Nueva_Tarea() {
        initComponents();
        this.setLocationRelativeTo(null);
        conectarMy();
        rbMecanico.setVisible(false);
        rbHidraulico.setVisible(false);
        rbNeumatico.setVisible(false);
        rbElectrico.setVisible(false);
        jCMaquina.setVisible(false);
        etiMaquina.setVisible(false);
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
    
    /**
     * LLena los combobox.
     */
    private void llenarComboMaquinas(){ 
        try {
            jCMaquina.setEnabled(true);
            jCMaquina.removeAllItems();
            st= conexion.createStatement();
            rs=st.executeQuery("SELECT descripcion FROM maquinas");
            while(rs.next()){
                jCMaquina.addItem(rs.getString("descripcion"));
            }
            jCMaquina.setSelectedItem("Sin descripción");
        }catch(SQLException e){
            JOptionPane.showMessageDialog(rootPane, e);
        }
    }
    
     private void modificaTxt() throws IOException{ 
         Propiedades.setPropiedad("modificado", "true");
    }
    
    /**
     * Metodo para recoger el tipo de problema.
     */
    private String cogeProblema(){
        String cadena="";
        
         if (rbMecanico.isSelected()==true){
            cadena="Mecánico\t";
        }
        if (rbHidraulico.isSelected()==true){
            cadena=cadena+"Hidráulico\t";
        }
        if (rbNeumatico.isSelected()==true){
            cadena=cadena+="Neumático\t";
        }      
        if (rbElectrico.isSelected()==true){
            cadena=cadena+"Eléctrico";
        }
        return cadena;
    }
    
    private static void enviarConGMail(String destinatario, String asunto, String cuerpo) {
        // Esto es lo que va delante de @gmail.com en tu cuenta de correo. Es el remitente también.
        String remitente = "sugerencias.cim@gmail.com";  //Para la dirección nomcuenta@gmail.com
        String clave = "968694183";
        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com");  //El servidor SMTP de Google
        props.put("mail.smtp.user", remitente);
        props.put("mail.smtp.clave", "miClaveDeGMail");    //La clave de la cuenta
        props.put("mail.smtp.auth", "true");    //Usar autenticación mediante usuario y clave
        props.put("mail.smtp.starttls.enable", "true"); //Para conectar de manera segura al servidor SMTP
        props.put("mail.smtp.port", "587"); //El puerto SMTP seguro de Google

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(remitente));
            //message.addRecipient(Message.RecipientType.TO, Address.class.cast(destinatario));   //Se podrían añadir varios de la misma manera
            message.addRecipients(Message.RecipientType.TO, destinatario);
            message.setSubject(asunto);
            message.setText(cuerpo);
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", remitente, clave);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException me) {
            //Si se produce un error

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
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tAObservaciones = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jCTipoTarea = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jCNivelPreferencia = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        rbMecanico = new javax.swing.JRadioButton();
        rbHidraulico = new javax.swing.JRadioButton();
        rbNeumatico = new javax.swing.JRadioButton();
        rbElectrico = new javax.swing.JRadioButton();
        jCMaquina = new javax.swing.JComboBox<>();
        etiMaquina = new javax.swing.JLabel();
        jCTarea = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nueva Tarea");
        setBackground(new java.awt.Color(204, 204, 204));
        setIconImage(new ImageIcon(getClass().getResource("/imagenes/editor.png")).getImage());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 0, 0));
        jLabel1.setText("Tarea:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 0, 0));
        jLabel2.setText("Observaciones:");

        tAObservaciones.setColumns(20);
        tAObservaciones.setFont(new java.awt.Font("Monospaced", 0, 16)); // NOI18N
        tAObservaciones.setLineWrap(true);
        tAObservaciones.setRows(5);
        jScrollPane2.setViewportView(tAObservaciones);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 0, 0));
        jLabel3.setText("Tipo de tarea:");

        jCTipoTarea.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jCTipoTarea.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sin descripción", "programación", "mecánica", "mantenimiento operario" }));
        jCTipoTarea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCTipoTareaActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 0, 0));
        jLabel4.setText("Nivel de preferencia:");

        jCNivelPreferencia.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jCNivelPreferencia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "urgente", "prioritaria", "normal" }));

        jButton1.setBackground(new java.awt.Color(0, 204, 51));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton1.setText("Crear nueva tarea");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        rbMecanico.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        rbMecanico.setText("Mecánico");

        rbHidraulico.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        rbHidraulico.setText("Hidáulico");

        rbNeumatico.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        rbNeumatico.setText("Neumático");

        rbElectrico.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        rbElectrico.setText("Eléctrico");

        jCMaquina.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jCMaquina.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "todas" }));
        jCMaquina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCMaquinaActionPerformed(evt);
            }
        });

        etiMaquina.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        etiMaquina.setForeground(new java.awt.Color(51, 0, 0));
        etiMaquina.setText("Máquina");

        jCTarea.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jCTarea.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Rotura", "Revisión", "Revisión Semestral", "Revisión Trimestral", "Revisión Anual", "Elaboración", "Sugerencia" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(477, 477, 477)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(24, 24, 24)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jCMaquina, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, Short.MAX_VALUE))
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel3)
                                                .addComponent(etiMaquina)
                                                .addComponent(rbMecanico)
                                                .addComponent(rbHidraulico)
                                                .addComponent(rbNeumatico)
                                                .addComponent(rbElectrico)
                                                .addComponent(jLabel1))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jCTarea, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jCTipoTarea, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addGap(215, 215, 215)))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4)
                                        .addComponent(jCNivelPreferencia, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel2)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 686, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(36, 36, 36))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCTarea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCTipoTarea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCNivelPreferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(etiMaquina)
                        .addGap(39, 39, 39))
                    .addComponent(jCMaquina, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(rbMecanico)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbHidraulico)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbNeumatico)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbElectrico)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        String operario;
        String usuario=null;
        String tipoproblema;
        String idmaquina=null;
        
        //Se comprueba el tipo de problema.
        switch (jCTipoTarea.getSelectedItem().toString()) {
            case "programación":
                operario = "técnico programación";
                tipoproblema = "programación";
                break;
            case "seguridad y mantenimiento":
                operario = "técnico mantenimiento";
                tipoproblema = "mantenimiento";
                break;
            case "mecánica":
                operario = "mecánico";
                tipoproblema = cogeProblema();
                break;
            default:
                operario = "operario";
                tipoproblema = "mantenimiento";
                break;
        }
        
        //Si el problema es una reparación se recoge qué máquina es la averiada.
        if(jCMaquina.isVisible()==true){
            try {
                st= conexion.createStatement();
                rs=st.executeQuery("SELECT id_maquina FROM maquinas WHERE descripcion=\""+jCMaquina.getSelectedItem().toString()+"\"");
                while(rs.next()){
                    idmaquina=rs.getString("id_maquina");
                }
            } catch (SQLException ex) {
                Logger.getLogger(Panel_Nueva_Tarea.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
        
        try {
            st = conexion.createStatement();
            rs = st.executeQuery("SELECT IdUsuario FROM UsuariosP WHERE Clave="+ Integer.parseInt(Propiedades.getPropiedad("clave")+""));
            while (rs.next()) {
                usuario = rs.getString("IdUsuario");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Panel_Nueva_Tarea.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(jCMaquina.isVisible()==true&&pulsado==false){
            JOptionPane.showMessageDialog(null,"Debes de elegir una máqina");
        }else if (jCTarea.getSelectedItem().toString().equals("Sugerencia")) {
            String destinatario =  "irobotica@canelaspring.com"; //A quien le quieres escribir.
            String asunto = "Sugerencias CIM";
            String cuerpo ="TAREA: "+jCTipoTarea.getSelectedItem().toString()+"\nMAQUINA: "+jCMaquina.getSelectedItem().toString()+"\nSUGERENCIA: "+tAObservaciones.getText();
            enviarConGMail(destinatario, asunto, cuerpo);
            destinatario =  "jcontrol@canelaspring.com"; //A quien le quieres escribir.
            enviarConGMail(destinatario, asunto, cuerpo);
            JOptionPane.showMessageDialog(rootPane, "Mensaje enviado correctamente");
        } else {
            //Se pasa a la base de datos la nueva tarea
            try {
                // TODO add your handling code here:
                st = conexion.createStatement();
                st.executeUpdate("INSERT INTO tareas(usuario,tarea, tipo_tarea,tipo_problema, id_maquina, nivel_preferencia, estado, observaciones, tipo_operario) VALUES (\"" + usuario + "\",\"" + jCTarea.getSelectedItem().toString() + "\",\"" + jCTipoTarea.getSelectedItem().toString() + "\",\"" + tipoproblema + "\",\"" + idmaquina + "\",\"" + jCNivelPreferencia.getSelectedItem().toString() + "\",\"en espera\",\"" + tAObservaciones.getText() + "\",\"" + operario + "\")");
                JOptionPane.showMessageDialog(null, "Tarea creada correctamente.");
                modificaTxt();
                dispose();
            } catch (SQLException ex) {
                Logger.getLogger(Panel_Nueva_Tarea.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "No se ha podido crear la tarea.");
            } catch (IOException ex) {
                Logger.getLogger(Panel_Nueva_Tarea.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jCTipoTareaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCTipoTareaActionPerformed
        if(jCTipoTarea.getSelectedItem().equals("Sin descripción")){
            
        }else{
            rbMecanico.setVisible(true);
            rbHidraulico.setVisible(true);
            rbNeumatico.setVisible(true);
            rbElectrico.setVisible(true);
            jCMaquina.setVisible(true);
            etiMaquina.setVisible(true);
            llenarComboMaquinas();
        }
    }//GEN-LAST:event_jCTipoTareaActionPerformed

    private void jCMaquinaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCMaquinaActionPerformed
        if(contador==0||contador==1||contador==2){
            contador++;
        }else{
            pulsado=true;
        }
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
            java.util.logging.Logger.getLogger(Panel_Nueva_Tarea.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Panel_Nueva_Tarea().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel etiMaquina;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jCMaquina;
    private javax.swing.JComboBox<String> jCNivelPreferencia;
    private javax.swing.JComboBox<String> jCTarea;
    private javax.swing.JComboBox<String> jCTipoTarea;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JRadioButton rbElectrico;
    private javax.swing.JRadioButton rbHidraulico;
    private javax.swing.JRadioButton rbMecanico;
    private javax.swing.JRadioButton rbNeumatico;
    private javax.swing.JTextArea tAObservaciones;
    // End of variables declaration//GEN-END:variables
}