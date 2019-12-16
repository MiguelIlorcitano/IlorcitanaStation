/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soporteMaquinas;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.File;
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
import tareasIlorcitana.Tareas_Principal;

/**
 *
 * @author Diseño
 */
public class PanelMaquinas extends javax.swing.JFrame {
    
    //Variables para la conexión.
    private Connection conexion = null;
    private Statement st;
    private ResultSet rs;
    String descripcion;
    String numero;
    
    int id_maquina;
    
    /**
     * Creates new form PanelMaquinas
     */
    public PanelMaquinas() {
        initComponents();
        this.setLocationRelativeTo(null);
        conectarMy();
        boton_reemplazar.setEnabled(false);
        //Cierra la ventana pulsando escape
    }
    
    /**
     * Creates new form PanelMaquinas
     * @param h
     */
    public PanelMaquinas(String h) {
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
        muestraMaquina(h);
        boton_registrar.setEnabled(false);
    }
    
    /**
     * Conectar con la base de datos.
     */
    private void conectarMy() {
        if (conexion == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                conexion = DriverManager.getConnection("jdbc:mysql://192.168.0.132:3307/ilorcitana", "irobotica", "1233");
            } catch (ClassNotFoundException | SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al realizar la conexion " + ex);
                Logger.getLogger(Tareas_Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void modificaTxt() throws IOException{ 
        Propiedades.setPropiedad("modificado", "true");
    }

    private void muestraMaquina(String des) {
        try {
            st = conexion.createStatement();
            rs = st.executeQuery("SELECT * FROM maquinas WHERE descripcion=\""+des+"\"");
            while (rs.next()) {
                id_maquina=Integer.parseInt(rs.getString("id_maquina"));
                txt_modelo.setText(rs.getString("modelo"));
                txt_año.setText(rs.getString("ano_fabricacion"));
                txt_referencia.setText(rs.getString("numero_referencia"));
                txt_descripcion.setText(rs.getString("descripcion"));
                descripcion=rs.getString("descripcion");
                txt_numero_maquina.setText(rs.getString("numero_maquina"));
                numero=rs.getString("numero_maquina");
                txt_fabricante.setText(rs.getString("fabricante"));
                txt_telefono.setText(rs.getString("telefono"));
                txt_movil.setText(rs.getString("movil"));
                txt_email.setText(rs.getString("email"));
                jC_Empresa.setSelectedItem(rs.getString("Empresa"));
                jC_Ubicacion.setSelectedItem(rs.getString("ubicacion"));
                txt_man_semanal.setText(rs.getString("M_Semanal"));
                txt_man_trimestral.setText(rs.getString("M_Trimestral"));
                txt_man_anual.setText(rs.getString("M_Anual"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PanelMaquinas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void reemplazaMaquina() {
        try {
            String query = "UPDATE maquinas SET modelo=\""+txt_modelo.getText()+"\",ano_fabricacion=\""+txt_año.getText()+"\",numero_referencia=\""+txt_referencia.getText()+"\",descripcion=\""+txt_descripcion.getText()+"\",numero_maquina="+Integer.parseInt(txt_numero_maquina.getText())+",fabricante=\""+txt_fabricante.getText()+"\",telefono=\""+txt_telefono.getText()+"\",movil=\""+txt_movil.getText()+"\",email=\""+txt_email.getText()+"\",Empresa=\""+jC_Empresa.getSelectedItem()+"\",ubicacion=\""+jC_Ubicacion.getSelectedItem()+"\",M_Semanal=\""+txt_man_semanal.getText()+"\",M_Trimestral=\""+txt_man_trimestral.getText()+"\",M_Anual=\""+txt_man_anual.getText()+"\" WHERE id_maquina ="+id_maquina;
            st = conexion.createStatement();
            st.executeUpdate(query);
            modificaTxt();
            JOptionPane.showMessageDialog(null,"Máquina MODIFICADA correctamente.");   
            dispose();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(PanelMaquinas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void insertarMaquina() {
        try {
            String query ="INSERT INTO `maquinas`(`modelo`, `ano_fabricacion`, `numero_referencia`, `descripcion`, `numero_maquina`, `fabricante`, `telefono`, `movil`, `email`, `Empresa`,`ubicacion`,`M_Semanal`, `M_Trimestral`, `M_Anual`) VALUES (\""+txt_modelo.getText()+"\",\""+txt_año.getText()+"\",\""+txt_referencia.getText()+"\",\""+txt_descripcion.getText()+"\","+Integer.parseInt(txt_numero_maquina.getText())+",\""+txt_fabricante.getText()+"\",\""+txt_telefono.getText()+"\",\""+txt_movil.getText()+"\",\""+txt_email.getText()+"\",\""+jC_Empresa.getSelectedItem()+"\",\""+jC_Ubicacion.getSelectedItem()+"\",\""+txt_man_semanal.getText()+"\",\""+txt_man_trimestral.getText()+"\",\""+txt_man_anual.getText()+"\")";
            st = conexion.createStatement();
            st.executeUpdate(query);
            modificaTxt();
            JOptionPane.showMessageDialog(null,"Máquina MODIFICADA correctamente.");   
            dispose();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(PanelMaquinas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void crearArchivo(){
        File directorio;
        File directorio1;
        File directorio2;
        if (txt_numero_maquina.getText().length() == 1) {
            directorio = new File("\\\\server\\datos\\GESCIM\\Gescim\\MODFACTUSOL\\DOCUMENTOS\\INFO_MAQUINARIA\\00" + txt_numero_maquina.getText() + " - " + txt_descripcion.getText());
            directorio.mkdirs();
            directorio1 = new File("\\\\server\\datos\\GESCIM\\Gescim\\MODFACTUSOL\\DOCUMENTOS\\INFO_MAQUINARIA\\00" + txt_numero_maquina.getText() + " - " + txt_descripcion.getText() + "\\DOCUMENTACION");
            directorio1.mkdirs();
            directorio2 = new File("\\\\server\\datos\\GESCIM\\Gescim\\MODFACTUSOL\\DOCUMENTOS\\INFO_MAQUINARIA\\00" + txt_numero_maquina.getText() + " - " + txt_descripcion.getText() + "\\MANTENIMIENTO");
            directorio2.mkdirs();
        }
        if (txt_numero_maquina.getText().length() == 2) {
            directorio = new File("\\\\server\\datos\\GESCIM\\Gescim\\MODFACTUSOL\\DOCUMENTOS\\INFO_MAQUINARIA\\0" + txt_numero_maquina.getText() + " - " + txt_descripcion.getText());
            directorio.mkdirs();
            directorio1 = new File("\\\\server\\datos\\GESCIM\\Gescim\\MODFACTUSOL\\DOCUMENTOS\\INFO_MAQUINARIA\\00" + txt_numero_maquina.getText() + " - " + txt_descripcion.getText() + "\\DOCUMENTACION");
            directorio1.mkdirs();
            directorio2 = new File("\\\\server\\datos\\GESCIM\\Gescim\\MODFACTUSOL\\DOCUMENTOS\\INFO_MAQUINARIA\\00" + txt_numero_maquina.getText() + " - " + txt_descripcion.getText() + "\\MANTENIMIENTO");
            directorio2.mkdirs();
        }
        if (txt_numero_maquina.getText().length() == 3) {
            directorio = new File("\\\\server\\datos\\GESCIM\\Gescim\\MODFACTUSOL\\DOCUMENTOS\\INFO_MAQUINARIA\\" + txt_numero_maquina.getText() + " - " + txt_descripcion.getText());
            directorio.mkdirs();
            directorio1 = new File("\\\\server\\datos\\GESCIM\\Gescim\\MODFACTUSOL\\DOCUMENTOS\\INFO_MAQUINARIA\\00" + txt_numero_maquina.getText() + " - " + txt_descripcion.getText() + "\\DOCUMENTACION");
            directorio1.mkdirs();
            directorio2 = new File("\\\\server\\datos\\GESCIM\\Gescim\\MODFACTUSOL\\DOCUMENTOS\\INFO_MAQUINARIA\\00" + txt_numero_maquina.getText() + " - " + txt_descripcion.getText() + "\\MANTENIMIENTO");
            directorio2.mkdirs();
        }
    }
    
    private boolean cambiaNombreArchivo(File f){
        File directorio = null;
        if (txt_numero_maquina.getText().length() == 1) {
            directorio = new File("\\\\server\\datos\\GESCIM\\Gescim\\MODFACTUSOL\\DOCUMENTOS\\INFO_MAQUINARIA\\00" + txt_numero_maquina.getText() + " - " + txt_descripcion.getText());
        }
        if (txt_numero_maquina.getText().length() == 2) {
            directorio = new File("\\\\server\\datos\\GESCIM\\Gescim\\MODFACTUSOL\\DOCUMENTOS\\INFO_MAQUINARIA\\0" + txt_numero_maquina.getText() + " - " + txt_descripcion.getText());
        }
        if (txt_numero_maquina.getText().length() == 3) {
            directorio = new File("\\\\server\\datos\\GESCIM\\Gescim\\MODFACTUSOL\\DOCUMENTOS\\INFO_MAQUINARIA\\" + txt_numero_maquina.getText() + " - " + txt_descripcion.getText());
        }
        return f.renameTo(directorio);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox1 = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        boton_registrar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txt_fabricante = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txt_telefono = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_movil = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txt_email = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txt_modelo = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txt_año = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txt_descripcion = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txt_referencia = new javax.swing.JTextField();
        boton_reemplazar = new javax.swing.JButton();
        txt_man_semanal = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txt_man_trimestral = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txt_man_anual = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jC_Empresa = new javax.swing.JComboBox<>();
        jC_Ubicacion = new javax.swing.JComboBox<>();
        txt_numero_maquina = new javax.swing.JTextField();
        boton_reemplazar1 = new javax.swing.JButton();
        Boton_Añadir = new javax.swing.JButton();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gestión de máquinas");
        setIconImage(new ImageIcon(getClass().getResource("/imagenes/editor.png")).getImage());

        jPanel1.setBackground(new java.awt.Color(238, 246, 255));
        jPanel1.setForeground(new java.awt.Color(238, 246, 255));

        boton_registrar.setBackground(new java.awt.Color(102, 102, 102));
        boton_registrar.setFont(new java.awt.Font("3ds Light", 1, 18)); // NOI18N
        boton_registrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/domain.png"))); // NOI18N
        boton_registrar.setText("Registrar");
        boton_registrar.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        boton_registrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_registrarActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("3ds Light", 1, 16)); // NOI18N
        jLabel1.setText(" Fabricante:");
        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(33, 135, 186)));

        txt_fabricante.setFont(new java.awt.Font("3ds Light", 0, 16)); // NOI18N
        txt_fabricante.setToolTipText("");
        txt_fabricante.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(33, 135, 186)));

        jLabel2.setFont(new java.awt.Font("3ds Light", 1, 16)); // NOI18N
        jLabel2.setText(" Teléfono:");
        jLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(33, 135, 186)));

        txt_telefono.setFont(new java.awt.Font("3ds Light", 0, 16)); // NOI18N
        txt_telefono.setToolTipText("");
        txt_telefono.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(33, 135, 186)));

        jLabel3.setFont(new java.awt.Font("3ds Light", 1, 16)); // NOI18N
        jLabel3.setText(" Movil:");
        jLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(33, 135, 186)));

        txt_movil.setFont(new java.awt.Font("3ds Light", 0, 16)); // NOI18N
        txt_movil.setToolTipText("");
        txt_movil.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(33, 135, 186)));

        jLabel4.setFont(new java.awt.Font("3ds Light", 1, 16)); // NOI18N
        jLabel4.setText(" Email:");
        jLabel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(33, 135, 186)));

        txt_email.setFont(new java.awt.Font("3ds Light", 0, 16)); // NOI18N
        txt_email.setToolTipText("");
        txt_email.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(33, 135, 186)));

        jLabel5.setFont(new java.awt.Font("3ds Light", 1, 16)); // NOI18N
        jLabel5.setText(" Modelo:");
        jLabel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(33, 135, 186)));

        txt_modelo.setFont(new java.awt.Font("3ds Light", 0, 16)); // NOI18N
        txt_modelo.setToolTipText("");
        txt_modelo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(33, 135, 186)));

        jLabel6.setFont(new java.awt.Font("3ds Light", 1, 16)); // NOI18N
        jLabel6.setText(" Año de fabricación:");
        jLabel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(33, 135, 186)));

        txt_año.setFont(new java.awt.Font("3ds Light", 0, 16)); // NOI18N
        txt_año.setToolTipText("");
        txt_año.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(33, 135, 186)));

        jLabel7.setFont(new java.awt.Font("3ds Light", 1, 16)); // NOI18N
        jLabel7.setText(" Descripción:");
        jLabel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(33, 135, 186)));

        jLabel8.setFont(new java.awt.Font("3ds Light", 1, 16)); // NOI18N
        jLabel8.setText(" Número de máquina:");
        jLabel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(33, 135, 186)));

        txt_descripcion.setFont(new java.awt.Font("3ds Light", 0, 16)); // NOI18N
        txt_descripcion.setToolTipText("");
        txt_descripcion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(33, 135, 186)));

        jLabel9.setFont(new java.awt.Font("3ds Light", 1, 16)); // NOI18N
        jLabel9.setText(" Número de referencia:");
        jLabel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(33, 135, 186)));

        txt_referencia.setFont(new java.awt.Font("3ds Light", 0, 16)); // NOI18N
        txt_referencia.setToolTipText("");
        txt_referencia.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(33, 135, 186)));

        boton_reemplazar.setBackground(new java.awt.Color(102, 102, 102));
        boton_reemplazar.setFont(new java.awt.Font("3ds Light", 1, 18)); // NOI18N
        boton_reemplazar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/replace.png"))); // NOI18N
        boton_reemplazar.setText("Reemplazar");
        boton_reemplazar.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        boton_reemplazar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_reemplazarActionPerformed(evt);
            }
        });

        txt_man_semanal.setFont(new java.awt.Font("3ds Light", 0, 16)); // NOI18N
        txt_man_semanal.setToolTipText("");
        txt_man_semanal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(33, 135, 186)));

        jLabel10.setFont(new java.awt.Font("3ds Light", 1, 16)); // NOI18N
        jLabel10.setText(" Mantenimiento semanal: ");
        jLabel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(33, 135, 186)));

        jLabel11.setFont(new java.awt.Font("3ds Light", 1, 16)); // NOI18N
        jLabel11.setText(" Mantenimiento trimestral: ");
        jLabel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(33, 135, 186)));

        txt_man_trimestral.setFont(new java.awt.Font("3ds Light", 0, 16)); // NOI18N
        txt_man_trimestral.setToolTipText("");
        txt_man_trimestral.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(33, 135, 186)));

        jLabel12.setFont(new java.awt.Font("3ds Light", 1, 16)); // NOI18N
        jLabel12.setText(" Mantenimiento anual: ");
        jLabel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(33, 135, 186)));

        txt_man_anual.setFont(new java.awt.Font("3ds Light", 0, 16)); // NOI18N
        txt_man_anual.setToolTipText("");
        txt_man_anual.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(33, 135, 186)));

        jLabel13.setFont(new java.awt.Font("3ds Light", 1, 16)); // NOI18N
        jLabel13.setText("Empresa:");
        jLabel13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(33, 135, 186)));

        jLabel14.setFont(new java.awt.Font("3ds Light", 1, 16)); // NOI18N
        jLabel14.setText("Ubicación:");
        jLabel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(33, 135, 186)));

        jC_Empresa.setFont(new java.awt.Font("3ds Light", 0, 16)); // NOI18N
        jC_Empresa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Comercial Ilorcitana", "Canela Spring" }));
        jC_Empresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jC_EmpresaActionPerformed(evt);
            }
        });

        jC_Ubicacion.setFont(new java.awt.Font("3ds Light", 0, 16)); // NOI18N
        jC_Ubicacion.setEnabled(false);

        txt_numero_maquina.setFont(new java.awt.Font("3ds Light", 0, 16)); // NOI18N
        txt_numero_maquina.setToolTipText("");
        txt_numero_maquina.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(33, 135, 186)));

        boton_reemplazar1.setBackground(new java.awt.Color(102, 102, 102));
        boton_reemplazar1.setFont(new java.awt.Font("3ds Light", 1, 18)); // NOI18N
        boton_reemplazar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/folder.png"))); // NOI18N
        boton_reemplazar1.setText("Ver Documentos");
        boton_reemplazar1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        boton_reemplazar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_reemplazar1ActionPerformed(evt);
            }
        });

        Boton_Añadir.setBackground(new java.awt.Color(102, 102, 102));
        Boton_Añadir.setFont(new java.awt.Font("3ds Light", 1, 18)); // NOI18N
        Boton_Añadir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/folder (2).png"))); // NOI18N
        Boton_Añadir.setText("Añadir Documentos");
        Boton_Añadir.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        Boton_Añadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Boton_AñadirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jC_Empresa, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_telefono)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_movil))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_referencia, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_año))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_modelo)
                            .addComponent(txt_email)
                            .addComponent(txt_fabricante)
                            .addComponent(txt_descripcion)
                            .addComponent(txt_man_semanal, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txt_man_trimestral)
                            .addComponent(txt_man_anual, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jC_Ubicacion, 0, 425, Short.MAX_VALUE)
                            .addComponent(txt_numero_maquina)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(boton_reemplazar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(boton_reemplazar1, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Boton_Añadir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(boton_registrar, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_fabricante, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_telefono, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_movil, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_modelo, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_referencia, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_año, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jC_Empresa, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jC_Ubicacion, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_numero_maquina, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_man_semanal, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_man_trimestral, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_man_anual, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boton_registrar)
                    .addComponent(boton_reemplazar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boton_reemplazar1)
                    .addComponent(Boton_Añadir))
                .addGap(19, 19, 19))
        );

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

    private void boton_reemplazarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_reemplazarActionPerformed
        
        if(numero.length()==1){
            numero="00"+numero;
        }
        if(numero.length()==2){
            numero="0"+numero;
        }
        if(numero.equals(txt_numero_maquina.getText())&&descripcion.equals(txt_descripcion.getText())){
            reemplazaMaquina();
        }else{
            if(cambiaNombreArchivo(new File("\\\\server\\datos\\GESCIM\\Gescim\\MODFACTUSOL\\DOCUMENTOS\\INFO_MAQUINARIA\\" + numero + " - " + descripcion))){
                reemplazaMaquina();
            }else{
                JOptionPane.showMessageDialog(null, "El fichero no se ha podido modificar. Comprueba que esten todos los documentos esten cerrados.");
            }
        }
    }//GEN-LAST:event_boton_reemplazarActionPerformed

    private void boton_registrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_registrarActionPerformed
        insertarMaquina();
        crearArchivo();
    }//GEN-LAST:event_boton_registrarActionPerformed

    private void jC_EmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jC_EmpresaActionPerformed
        jC_Ubicacion.setEnabled(true);
        if(jC_Empresa.getSelectedItem().equals("Comercial Ilorcitana")){
            jC_Ubicacion.addItem("Nave 1");
            jC_Ubicacion.addItem("Nave 2");
            jC_Ubicacion.addItem("Nave 3");
            jC_Ubicacion.addItem("Nave 4");
            jC_Ubicacion.addItem("Pasillo 1");
            jC_Ubicacion.addItem("Pasillo 2");
            jC_Ubicacion.addItem("Pasillo 3");
            jC_Ubicacion.addItem("Altillo 1");
            jC_Ubicacion.addItem("Altillo 2");
            jC_Ubicacion.addItem("Altillo 3");
            jC_Ubicacion.addItem("Exterior 1");
            jC_Ubicacion.addItem("Exterior 2");
        }else if(jC_Empresa.getSelectedItem().equals("Canela Spring")){
            jC_Ubicacion.addItem("Nave 1");
            jC_Ubicacion.addItem("Nave 2");
            jC_Ubicacion.addItem("Nave 3");
            jC_Ubicacion.addItem("Nave 4");
            jC_Ubicacion.addItem("Nave 5");
            jC_Ubicacion.addItem("Pasillo");
            jC_Ubicacion.addItem("Exterior 1");
        }
    }//GEN-LAST:event_jC_EmpresaActionPerformed

    private void boton_reemplazar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_reemplazar1ActionPerformed
        if(numero.length()==1){
            numero="00"+numero;
        }
        if(numero.length()==2){
            numero="0"+numero;
        }
//         try {
//            Runtime.getRuntime().exec("explorer.exe /select," + "\\\\server\\datos\\GESCIM\\Gescim\\MODFACTUSOL\\DOCUMENTOS\\INFO_MAQUINARIA\\" + numero + " - " + descripcion+"\\");
//        } catch (IOException ex) {
//            Logger.getLogger(PanelMaquinas.class.getName()).log(Level.SEVERE, null, ex);
//        }
        Muestra_Archivos_M m = new Muestra_Archivos_M("\\\\server\\datos\\GESCIM\\Gescim\\MODFACTUSOL\\DOCUMENTOS\\INFO_MAQUINARIA\\" + numero + " - " + descripcion);
        m.setVisible(true);
    }//GEN-LAST:event_boton_reemplazar1ActionPerformed

    private void Boton_AñadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Boton_AñadirActionPerformed
        if(numero.length()==1){
            numero="00"+numero;
        }
        if(numero.length()==2){
            numero="0"+numero;
        }
        Adjunta_Archivos_M m = new Adjunta_Archivos_M("\\\\server\\datos\\GESCIM\\Gescim\\MODFACTUSOL\\DOCUMENTOS\\INFO_MAQUINARIA\\" + numero + " - " + descripcion+"\\");
        m.setVisible(true);
    }//GEN-LAST:event_Boton_AñadirActionPerformed

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
            java.util.logging.Logger.getLogger(PanelMaquinas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PanelMaquinas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PanelMaquinas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PanelMaquinas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new PanelMaquinas().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Boton_Añadir;
    private javax.swing.JButton boton_reemplazar;
    private javax.swing.JButton boton_reemplazar1;
    private javax.swing.JButton boton_registrar;
    private javax.swing.JComboBox<String> jC_Empresa;
    private javax.swing.JComboBox<String> jC_Ubicacion;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
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
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txt_año;
    private javax.swing.JTextField txt_descripcion;
    private javax.swing.JTextField txt_email;
    private javax.swing.JTextField txt_fabricante;
    private javax.swing.JTextField txt_man_anual;
    private javax.swing.JTextField txt_man_semanal;
    private javax.swing.JTextField txt_man_trimestral;
    private javax.swing.JTextField txt_modelo;
    private javax.swing.JTextField txt_movil;
    private javax.swing.JTextField txt_numero_maquina;
    private javax.swing.JTextField txt_referencia;
    private javax.swing.JTextField txt_telefono;
    // End of variables declaration//GEN-END:variables
}
