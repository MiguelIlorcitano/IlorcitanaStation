/**
 * En esta clase de tipo frame es donde se configura la interfaz gráfica principal y desde donde se ralizan las 
 * llamadas a las distintas clases.
 */

package soporteMaquinas;

import java.awt.Color;
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;



/**
 *
 * @author Miguel Angel Carrillo Garcia
 */
public final class Maquinas_Principal extends javax.swing.JFrame {
    
    private static ServerSocket SERVER_SOCKET;
    
    //Variables para la conexión.
    private Connection conexion = null;
    private Statement st;
    private ResultSet rs;
    DefaultTableModel n;
    PanelMaquinas mod;
    
    /**
     * Constructo
     * @throws java.sql.SQLException
     */
    public Maquinas_Principal() throws SQLException {
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
        conectarMy();
        mostrarTabla();
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
    }
    
    /**
     * Conectar con la base de datos.
     */
    public final void conectarMy(){
        if (conexion == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                conexion = DriverManager.getConnection("jdbc:mysql://192.168.0.132:3307/ilorcitana", "irobotica", "1233");
            } catch (ClassNotFoundException | SQLException ex) {
                JOptionPane.showMessageDialog(null,"Error al realizar la conexion "+ex);
                Logger.getLogger(Maquinas_Principal.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }
    
    private String generaConsulta(){
        if(jR_numeroMaquina.isSelected()){
            return "SELECT * FROM maquinas WHERE numero_maquina="+Integer.parseInt(txt_numeroMaquina.getText());
        }else if(jR_Descripcion.isSelected()){
            return "SELECT * FROM maquinas WHERE descripcion LIKE \"%"+txt_descripcion.getText()+"%\"";
        }else if(jR_Fabricante.isSelected()){
            return "SELECT * FROM maquinas WHERE fabricante LIKE \"%"+txt_fabricante.getText()+"%\"";
        }else if(jR_Empresa.isSelected()&&jR_Ubicacion.isSelected()==false){
            return "SELECT * FROM maquinas WHERE Empresa=\""+jC_Empresa.getSelectedItem().toString()+"\"";
        }else if(jR_Empresa.isSelected()&&jR_Ubicacion.isSelected()==true){
            return "SELECT * FROM maquinas WHERE Empresa=\""+jC_Empresa.getSelectedItem().toString()+"\" AND ubicacion=\""+jC_Ubicacion.getSelectedItem().toString()+"\"";
        }else{
            return "SELECT * FROM maquinas";
        }
    }
    
   
    /**
     * Extrae el contenido de la base de datos y lo inserta en una jtable.
     */
    public void mostrarTabla() {
        try {
            String consulta = generaConsulta();
            st = conexion.createStatement();
            ResultSet r = st.executeQuery(consulta);
            String titulos[] = {"Modelo", "Descrición", "Número de máquina", "Fabricante", "Empresa", "Ubicación", "M_semanal", "M_mensual", "M_trimestral", "M_anual"};
            n = new DefaultTableModel(null, titulos);
            String fila[] = new String[10];
            while (r.next()) {
                fila[0] = r.getString("modelo");
                fila[1] = r.getString("descripcion");
                fila[2] = r.getString("numero_maquina");
                fila[3] = r.getString("fabricante");
                fila[4] = r.getString("Empresa");
                fila[5] = r.getString("ubicacion");
                fila[6] = r.getString("M_Semanal");
                fila[7] = r.getString("M_Mensual");
                fila[8] = r.getString("M_Trimestral");
                fila[9] = r.getString("M_Anual");
                n.addRow(fila);
            }
            Tabla.setModel(n);
            Tabla.setDefaultRenderer(Object.class, new RenderM());
            Tabla.getColumnModel().getColumn(0).setPreferredWidth(150);
            Tabla.getColumnModel().getColumn(1).setPreferredWidth(200);
            Tabla.getColumnModel().getColumn(2).setPreferredWidth(100);
            Tabla.getColumnModel().getColumn(3).setPreferredWidth(30);
            Tabla.getColumnModel().getColumn(4).setPreferredWidth(30);
            Tabla.getColumnModel().getColumn(5).setPreferredWidth(30);
            Tabla.getColumnModel().getColumn(6).setPreferredWidth(30);
            Tabla.getColumnModel().getColumn(7).setPreferredWidth(30);
            Tabla.getColumnModel().getColumn(8).setPreferredWidth(30);
            Tabla.getColumnModel().getColumn(9).setPreferredWidth(30);
            JTableHeader th;
            th = Tabla.getTableHeader();
            Font fuente = new Font("3ds Light", Font.BOLD, 14);
            Color cl = new Color(14, 70, 126);
            th.setForeground(cl);
            th.setFont(fuente);
            Tabla.setShowHorizontalLines(true);
            Tabla.setShowVerticalLines(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los datos de la tabla Maquinas");
        }
    }
    
    public void utilJTablePrint(JTable jTable, String header, String footer, boolean showPrintDialog){        
        boolean fitWidth = true;        
        boolean interactive = true;
        // We define the print mode (Definimos el modo de impresión)
        JTable.PrintMode mode = fitWidth ? JTable.PrintMode.FIT_WIDTH : JTable.PrintMode.NORMAL;
        try {
            // Print the table (Imprimo la tabla)             
            boolean complete = jTable.print(mode,
                    new MessageFormat(header),
                    new MessageFormat(footer),
                    showPrintDialog,
                    null,
                    interactive);                 
            if (complete) {
                // Mostramos el mensaje de impresión existosa
                JOptionPane.showMessageDialog(jTable,
                        "Print complete (Impresión completa)",
                        "Print result (Resultado de la impresión)",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Mostramos un mensaje indicando que la impresión fue cancelada                 
                JOptionPane.showMessageDialog(jTable,
                        "Print canceled (Impresión cancelada)",
                        "Print result (Resultado de la impresión)",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (PrinterException pe) {
            JOptionPane.showMessageDialog(jTable, 
                    "Print fail (Fallo de impresión): " + pe.getMessage(), 
                    "Print result (Resultado de la impresión)", 
                    JOptionPane.ERROR_MESSAGE);
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
        botonPrincipal = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jlReloj = new javax.swing.JLabel();
        jbCreaTarea1 = new javax.swing.JButton();
        txt_numeroMaquina = new javax.swing.JTextField();
        jR_numeroMaquina = new javax.swing.JRadioButton();
        jR_Descripcion = new javax.swing.JRadioButton();
        txt_descripcion = new javax.swing.JTextField();
        jR_Fabricante = new javax.swing.JRadioButton();
        txt_fabricante = new javax.swing.JTextField();
        jR_Empresa = new javax.swing.JRadioButton();
        jC_Empresa = new javax.swing.JComboBox<>();
        jC_Ubicacion = new javax.swing.JComboBox<>();
        jR_Ubicacion = new javax.swing.JRadioButton();
        boton_reemplazar1 = new javax.swing.JButton();
        Boton_Añadir = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gestión de máquinas");
        setFont(new java.awt.Font("Century", 0, 14)); // NOI18N
        setIconImage(new ImageIcon(getClass().getResource("/imagenes/editor.png")).getImage());
        setPreferredSize(new java.awt.Dimension(1024, 768));

        Panel1.setBackground(new java.awt.Color(227, 224, 224));

        jScrollPane3.setAutoscrolls(true);

        Tabla.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
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
        Tabla.setGridColor(new java.awt.Color(51, 51, 51));
        Tabla.setIntercellSpacing(new java.awt.Dimension(10, 10));
        Tabla.setRowHeight(25);
        Tabla.setSelectionBackground(new java.awt.Color(204, 0, 0));
        Tabla.setSelectionForeground(new java.awt.Color(255, 153, 153));
        Tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                click_tarea(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                TablaMousePressed(evt);
            }
        });
        jScrollPane3.setViewportView(Tabla);

        botonPrincipal.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.light"));
        botonPrincipal.setFont(new java.awt.Font("3ds Light", 1, 18)); // NOI18N
        botonPrincipal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/homepage.png"))); // NOI18N
        botonPrincipal.setText("Panel principal");
        botonPrincipal.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        botonPrincipal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonPrincipalActionPerformed(evt);
            }
        });

        jLabel2.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.light"));
        jLabel2.setFont(new java.awt.Font("3ds Light", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(14, 70, 126));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/muestra.png"))); // NOI18N
        jLabel2.setText("Gestión de máquinas");
        jLabel2.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel2.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        jlReloj.setFont(new java.awt.Font("3ds Light", 1, 18)); // NOI18N
        jlReloj.setForeground(new java.awt.Color(51, 51, 51));
        jlReloj.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlReloj.setText("jLabel4");

        jbCreaTarea1.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.light"));
        jbCreaTarea1.setFont(new java.awt.Font("3ds Light", 1, 18)); // NOI18N
        jbCreaTarea1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/domain.png"))); // NOI18N
        jbCreaTarea1.setText("Registrar Máquina");
        jbCreaTarea1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jbCreaTarea1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCreaTarea1ActionPerformed(evt);
            }
        });

        txt_numeroMaquina.setFont(new java.awt.Font("3ds Light", 0, 16)); // NOI18N
        txt_numeroMaquina.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_numeroMaquina.setText("0");
        txt_numeroMaquina.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
                txt_numeroMaquinaAncestorRemoved(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        txt_numeroMaquina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_numeroMaquinaActionPerformed(evt);
            }
        });

        jR_numeroMaquina.setBackground(new java.awt.Color(86, 133, 181));
        jR_numeroMaquina.setFont(new java.awt.Font("3ds Light", 1, 16)); // NOI18N
        jR_numeroMaquina.setForeground(new java.awt.Color(51, 51, 51));
        jR_numeroMaquina.setText("Nº de máquina:");
        jR_numeroMaquina.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jR_numeroMaquina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jR_numeroMaquinaActionPerformed(evt);
            }
        });

        jR_Descripcion.setBackground(new java.awt.Color(86, 133, 181));
        jR_Descripcion.setFont(new java.awt.Font("3ds Light", 1, 16)); // NOI18N
        jR_Descripcion.setForeground(new java.awt.Color(51, 51, 51));
        jR_Descripcion.setText("Descripción:");
        jR_Descripcion.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jR_Descripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jR_DescripcionActionPerformed(evt);
            }
        });

        txt_descripcion.setFont(new java.awt.Font("3ds Light", 0, 16)); // NOI18N
        txt_descripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_descripcionActionPerformed(evt);
            }
        });

        jR_Fabricante.setBackground(new java.awt.Color(86, 133, 181));
        jR_Fabricante.setFont(new java.awt.Font("3ds Light", 1, 16)); // NOI18N
        jR_Fabricante.setForeground(new java.awt.Color(51, 51, 51));
        jR_Fabricante.setText("Fabricante:");
        jR_Fabricante.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jR_Fabricante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jR_FabricanteActionPerformed(evt);
            }
        });

        txt_fabricante.setFont(new java.awt.Font("3ds Light", 0, 16)); // NOI18N
        txt_fabricante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_fabricanteActionPerformed(evt);
            }
        });

        jR_Empresa.setBackground(new java.awt.Color(86, 133, 181));
        jR_Empresa.setFont(new java.awt.Font("3ds Light", 1, 16)); // NOI18N
        jR_Empresa.setForeground(new java.awt.Color(51, 51, 51));
        jR_Empresa.setText("Empresa:");
        jR_Empresa.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jR_Empresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jR_EmpresaActionPerformed(evt);
            }
        });

        jC_Empresa.setFont(new java.awt.Font("3ds Light", 0, 16)); // NOI18N
        jC_Empresa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Comercial Ilorcitana", "Canela Spring" }));
        jC_Empresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jC_EmpresaActionPerformed(evt);
            }
        });

        jC_Ubicacion.setFont(new java.awt.Font("3ds Light", 0, 16)); // NOI18N
        jC_Ubicacion.setEnabled(false);
        jC_Ubicacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jC_UbicacionActionPerformed(evt);
            }
        });

        jR_Ubicacion.setBackground(new java.awt.Color(86, 133, 181));
        jR_Ubicacion.setFont(new java.awt.Font("3ds Light", 1, 16)); // NOI18N
        jR_Ubicacion.setForeground(new java.awt.Color(51, 51, 51));
        jR_Ubicacion.setText("Ubicación:");
        jR_Ubicacion.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jR_Ubicacion.setEnabled(false);
        jR_Ubicacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jR_UbicacionActionPerformed(evt);
            }
        });

        boton_reemplazar1.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.light"));
        boton_reemplazar1.setFont(new java.awt.Font("3ds Light", 1, 18)); // NOI18N
        boton_reemplazar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/folder.png"))); // NOI18N
        boton_reemplazar1.setText("Documentos");
        boton_reemplazar1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        boton_reemplazar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_reemplazar1ActionPerformed(evt);
            }
        });

        Boton_Añadir.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.light"));
        Boton_Añadir.setFont(new java.awt.Font("3ds Light", 1, 18)); // NOI18N
        Boton_Añadir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/folder (2).png"))); // NOI18N
        Boton_Añadir.setText("Añadir Documentos");
        Boton_Añadir.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        Boton_Añadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Boton_AñadirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Panel1Layout = new javax.swing.GroupLayout(Panel1);
        Panel1.setLayout(Panel1Layout);
        Panel1Layout.setHorizontalGroup(
            Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jlReloj, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(Panel1Layout.createSequentialGroup()
                            .addComponent(jR_numeroMaquina)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(txt_numeroMaquina))
                        .addComponent(txt_descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jR_Fabricante, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jR_Descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_fabricante, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jR_Empresa, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jC_Empresa, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel1Layout.createSequentialGroup()
                            .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jC_Ubicacion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jR_Ubicacion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(24, 24, 24))
                        .addComponent(botonPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jbCreaTarea1, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(boton_reemplazar1, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Boton_Añadir, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 779, Short.MAX_VALUE))
        );
        Panel1Layout.setVerticalGroup(
            Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel1Layout.createSequentialGroup()
                .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel1Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jlReloj)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addGap(72, 72, 72)
                        .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_numeroMaquina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jR_numeroMaquina))
                        .addGap(18, 18, 18)
                        .addComponent(jR_Descripcion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jR_Fabricante)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_fabricante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jR_Empresa)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jC_Empresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jR_Ubicacion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jC_Ubicacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Boton_Añadir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(boton_reemplazar1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(botonPrincipal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbCreaTarea1))
                    .addComponent(jScrollPane3))
                .addContainerGap())
        );

        jMenu1.setBackground(new java.awt.Color(255, 255, 255));
        jMenu1.setForeground(new java.awt.Color(51, 51, 51));
        jMenu1.setText("Operaciones");
        jMenu1.setFont(new java.awt.Font("3ds Light", 1, 14)); // NOI18N
        jMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu1ActionPerformed(evt);
            }
        });

        jMenuItem1.setText("mantenimiento");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("imprimir");
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
            .addComponent(Panel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void click_tarea(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_click_tarea
        
        
    }//GEN-LAST:event_click_tarea

    private void jbCreaTarea1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCreaTarea1ActionPerformed
        PanelMaquinas mod = new PanelMaquinas();
        mod.setVisible(true);
    }//GEN-LAST:event_jbCreaTarea1ActionPerformed

    private void botonPrincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonPrincipalActionPerformed
        this.dispose();
        mod.dispose();
    }//GEN-LAST:event_botonPrincipalActionPerformed

    private void jR_numeroMaquinaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jR_numeroMaquinaActionPerformed
        if(jR_numeroMaquina.isSelected()){
            jR_Descripcion.setSelected(false);
            jR_Fabricante.setSelected(false);
            jR_Empresa.setSelected(false);
        }else{
            mostrarTabla();
            repaint();
        }
    }//GEN-LAST:event_jR_numeroMaquinaActionPerformed

    private void jR_DescripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jR_DescripcionActionPerformed
        if(jR_Descripcion.isSelected()){
            jR_numeroMaquina.setSelected(false);
            jR_Fabricante.setSelected(false);
            jR_Empresa.setSelected(false);
        }else{
            mostrarTabla();
            repaint();
        }
    }//GEN-LAST:event_jR_DescripcionActionPerformed

    private void jR_FabricanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jR_FabricanteActionPerformed
        if(jR_Fabricante.isSelected()){
            jR_Descripcion.setSelected(false);
            jR_numeroMaquina.setSelected(false);
            jR_Empresa.setSelected(false);
        }else{
            mostrarTabla();
            repaint();
        }
    }//GEN-LAST:event_jR_FabricanteActionPerformed

    private void jR_EmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jR_EmpresaActionPerformed
        if (jR_Empresa.isSelected()) {
            jR_Descripcion.setSelected(false);
            jR_Fabricante.setSelected(false);
            jR_numeroMaquina.setSelected(false);
            jR_Ubicacion.setEnabled(true);
            jC_Ubicacion.setEnabled(true);
            if (jC_Empresa.getSelectedItem().equals("Comercial Ilorcitana")) {
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
            } else if (jC_Empresa.getSelectedItem().equals("Canela Spring")) {
                jC_Ubicacion.addItem("Nave 1");
                jC_Ubicacion.addItem("Nave 2");
                jC_Ubicacion.addItem("Nave 3");
                jC_Ubicacion.addItem("Nave 4");
                jC_Ubicacion.addItem("Nave 5");
                jC_Ubicacion.addItem("Pasillo");
                jC_Ubicacion.addItem("Exterior 1");
            }
        }else{
            mostrarTabla();
            repaint();
        }
    }//GEN-LAST:event_jR_EmpresaActionPerformed

    private void txt_numeroMaquinaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_numeroMaquinaActionPerformed
        mostrarTabla();
        repaint();
    }//GEN-LAST:event_txt_numeroMaquinaActionPerformed

    private void jR_UbicacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jR_UbicacionActionPerformed
        
            mostrarTabla();
            repaint();
    }//GEN-LAST:event_jR_UbicacionActionPerformed

    private void txt_numeroMaquinaAncestorRemoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_txt_numeroMaquinaAncestorRemoved
       mostrarTabla();
       repaint();
    }//GEN-LAST:event_txt_numeroMaquinaAncestorRemoved

    private void txt_descripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_descripcionActionPerformed
        mostrarTabla();
        repaint();
    }//GEN-LAST:event_txt_descripcionActionPerformed

    private void txt_fabricanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_fabricanteActionPerformed
        mostrarTabla();
        repaint();
    }//GEN-LAST:event_txt_fabricanteActionPerformed

    private void jC_EmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jC_EmpresaActionPerformed
        mostrarTabla();
        repaint();
    }//GEN-LAST:event_jC_EmpresaActionPerformed

    private void jC_UbicacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jC_UbicacionActionPerformed
        mostrarTabla();
        repaint();
    }//GEN-LAST:event_jC_UbicacionActionPerformed

    private void boton_reemplazar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_reemplazar1ActionPerformed
        Muestra_Archivos_M m = new Muestra_Archivos_M();
        m.setVisible(true);
    }//GEN-LAST:event_boton_reemplazar1ActionPerformed

    private void Boton_AñadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Boton_AñadirActionPerformed
        Adjunta_Archivos_M m = new Adjunta_Archivos_M("\\\\server\\datos\\GESCIM\\Gescim\\MODFACTUSOL\\DOCUMENTOS\\INFO_MAQUINARIA\\");
        m.setVisible(true);
    }//GEN-LAST:event_Boton_AñadirActionPerformed

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu1ActionPerformed
        
    }//GEN-LAST:event_jMenu1ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        Panel_Operaciones panel = new Panel_Operaciones();
        panel.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        utilJTablePrint(Tabla, getTitle(), "Ilorcitana_Station", true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void TablaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaMousePressed
        try {
            st = conexion.createStatement();
            ResultSet r = st.executeQuery("SELECT * FROM maquinas WHERE descripcion=\"" + Tabla.getValueAt(Tabla.getSelectedRow(), 1) + "\"");
            while (r.next()) {
                String h = r.getString("descripcion");
                mod = new PanelMaquinas(h);
                mod.setVisible(true);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Maquinas_Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        mostrarTabla();
    }//GEN-LAST:event_TablaMousePressed

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
            java.util.logging.Logger.getLogger(Maquinas_Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                SERVER_SOCKET = new ServerSocket(1332);
                new Maquinas_Principal().setVisible(true);
            } catch (SQLException | IOException ex) {
                Logger.getLogger(Maquinas_Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Boton_Añadir;
    public javax.swing.JPanel Panel1;
    private javax.swing.JTable Tabla;
    private javax.swing.JButton botonPrincipal;
    private javax.swing.JButton boton_reemplazar1;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox<String> jC_Empresa;
    private javax.swing.JComboBox<String> jC_Ubicacion;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JRadioButton jR_Descripcion;
    private javax.swing.JRadioButton jR_Empresa;
    private javax.swing.JRadioButton jR_Fabricante;
    private javax.swing.JRadioButton jR_Ubicacion;
    private javax.swing.JRadioButton jR_numeroMaquina;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton jbCreaTarea1;
    public javax.swing.JLabel jlReloj;
    private javax.swing.JTextField txt_descripcion;
    private javax.swing.JTextField txt_fabricante;
    private javax.swing.JTextField txt_numeroMaquina;
    // End of variables declaration//GEN-END:variables
}

