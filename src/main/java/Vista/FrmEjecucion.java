/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

/**
 *
 * @author HP
 */
public class FrmEjecucion extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmEjecucion.class.getName());
    private DefaultTableModel modeloActividades;
    private DefaultTableModel modeloConsumos;
    private DefaultTableModel modeloControles;
    /**
     * Creates new form FrmEjecucion
     */
    public FrmEjecucion() {
        initComponents();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        inicializarTablas();
        cargarOrdenes();
        cargarMecanicos();
        cargarServicios();
        cargarRepuestos();
    }

    private void inicializarTablas() {
        modeloActividades = new DefaultTableModel(
            new Object [][] {},
            new String [] { "IdActividad", "Servicio", "Descripcion", "Mecanico", "Estado", "Horas" }
        ) {
            boolean[] canEdit = new boolean [] { false, false, false, false, false, false };
            public boolean isCellEditable(int rowIndex, int columnIndex) { return canEdit[columnIndex]; }
        };
        tblActividades.setModel(modeloActividades);

        modeloConsumos = new DefaultTableModel(
            new Object [][] {},
            new String [] { "IdConsumo", "Codigo", "Repuesto", "Cantidad", "Precio", "Estado" }
        ) {
            boolean[] canEdit = new boolean [] { false, false, false, false, false, false };
            public boolean isCellEditable(int rowIndex, int columnIndex) { return canEdit[columnIndex]; }
        };
        tblConsumos.setModel(modeloConsumos);

        modeloControles = new DefaultTableModel(
            new Object [][] {},
            new String [] { "IdControl", "Criterio", "Obligatorio", "Resultado" }
        ) {
            boolean[] canEdit = new boolean [] { false, false, false, false };
            public boolean isCellEditable(int rowIndex, int columnIndex) { return canEdit[columnIndex]; }
        };
        tblControles.setModel(modeloControles);
    }

    private void cargarOrdenes() {
        cboOrden.removeAllItems();
        cboOrden.addItem("-- Seleccione una Orden --");
        String sql = "SELECT IdOrden FROM dbo.OrdenesTrabajo";
        try (Connection conn = config.Conexion.obtener();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                cboOrden.addItem(rs.getInt("IdOrden") + "");
            }
        } catch (SQLException ex) {
            cboOrden.addItem("1");
            cboOrden.addItem("2");
        }
    }

    private void cargarMecanicos() {
        cboMecanico.removeAllItems();
        cboMecanico.addItem("-- Seleccione Mecánico --");
        String sql = "SELECT IdMecanico, Nombres, Apellidos FROM dbo.Mecanicos";
        try (Connection conn = config.Conexion.obtener();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                cboMecanico.addItem(rs.getInt("IdMecanico") + " - " + rs.getString("Nombres") + " " + rs.getString("Apellidos"));
            }
        } catch (SQLException ex) {
            cboMecanico.addItem("1 - Carlos Martínez");
            cboMecanico.addItem("2 - Juan Pérez");
        }
    }

    private void cargarServicios() {
        cboServicioOrden.removeAllItems();
        cboServicioOrden.addItem("-- Seleccione Servicio --");
        cboServicioOrden.addItem("Mantenimiento General");
        cboServicioOrden.addItem("Reparación de Sistema Eléctrico");
        cboServicioOrden.addItem("Cambio de Frenos");
    }

    private void cargarRepuestos() {
        cboRepuesto.removeAllItems();
        cboRepuesto.addItem("-- Seleccione Repuesto --");
        String sql = "SELECT IdRepuesto, NombreRepuesto, Codigo FROM dbo.Repuestos";
        try (Connection conn = config.Conexion.obtener();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                cboRepuesto.addItem(rs.getInt("IdRepuesto") + " - " + rs.getString("NombreRepuesto") + " (" + rs.getString("Codigo") + ")");
            }
        } catch (SQLException ex) {
            cboRepuesto.addItem("1 - Filtro de Aceite (REP-001)");
            cboRepuesto.addItem("2 - Pastillas de Freno (REP-002)");
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

        pnlFondo = new javax.swing.JPanel();
        pnlSuperior = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblServicio = new javax.swing.JLabel();
        lblMecanico = new javax.swing.JLabel();
        txtApellido = new javax.swing.JTextField();
        lblActividad = new javax.swing.JLabel();
        btnRegistrar = new javax.swing.JButton();
        btnCompletar = new javax.swing.JButton();
        cboServicioOrden = new javax.swing.JComboBox<>();
        cboMecanico = new javax.swing.JComboBox<>();
        btnIniciar = new javax.swing.JButton();
        lblTitulo = new javax.swing.JLabel();
        pnlSuperior2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        lblObservacion = new javax.swing.JLabel();
        txtObservacion = new javax.swing.JTextField();
        btnEnviarCalidad = new javax.swing.JButton();
        btnAprobarControl = new javax.swing.JButton();
        btnRechazarControl = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblControles = new javax.swing.JTable();
        lblOrden = new javax.swing.JLabel();
        cboOrden = new javax.swing.JComboBox<>();
        btnFinalizar = new javax.swing.JButton();
        pnlSuperior3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lblRepuesto = new javax.swing.JLabel();
        lblCantidad = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        btnAgregar = new javax.swing.JButton();
        btnDescontar = new javax.swing.JButton();
        cboRepuesto = new javax.swing.JComboBox<>();
        btnQuitarConsumo = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblActividades = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblConsumos = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlFondo.setBackground(new java.awt.Color(153, 153, 255));

        pnlSuperior.setBackground(new java.awt.Color(204, 204, 255));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 2, 24)); // NOI18N
        jLabel1.setText("----- Actividades------------------------------------");

        lblServicio.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblServicio.setText("Servicio");

        lblMecanico.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblMecanico.setText("Mecanico");

        txtApellido.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        txtApellido.addActionListener(this::txtApellidoActionPerformed);

        lblActividad.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblActividad.setText("Actividad");

        btnRegistrar.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnRegistrar.setText("Registrar");
        btnRegistrar.addActionListener(this::btnRegistrarActionPerformed);

        btnCompletar.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnCompletar.setText("Completar");
        btnCompletar.addActionListener(this::btnCompletarActionPerformed);

        cboServicioOrden.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboServicioOrden.addActionListener(this::cboServicioOrdenActionPerformed);

        cboMecanico.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnIniciar.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnIniciar.setText("Iniciar");
        btnIniciar.addActionListener(this::btnIniciarActionPerformed);

        javax.swing.GroupLayout pnlSuperiorLayout = new javax.swing.GroupLayout(pnlSuperior);
        pnlSuperior.setLayout(pnlSuperiorLayout);
        pnlSuperiorLayout.setHorizontalGroup(
            pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSuperiorLayout.createSequentialGroup()
                .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(pnlSuperiorLayout.createSequentialGroup()
                            .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(pnlSuperiorLayout.createSequentialGroup()
                                    .addGap(8, 8, 8)
                                    .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(lblMecanico, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(pnlSuperiorLayout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(lblActividad, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(cboMecanico, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cboServicioOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtApellido, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(pnlSuperiorLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(btnRegistrar)
                            .addGap(47, 47, 47)
                            .addComponent(btnIniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnCompletar)))
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlSuperiorLayout.setVerticalGroup(
            pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSuperiorLayout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlSuperiorLayout.createSequentialGroup()
                        .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlSuperiorLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboServicioOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cboMecanico, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblMecanico)))
                            .addGroup(pnlSuperiorLayout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(lblServicio)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlSuperiorLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblActividad)))
                .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlSuperiorLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlSuperiorLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnIniciar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnCompletar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(16, 16, 16))
        );

        lblTitulo.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        lblTitulo.setText("Ejecución de Ordenes");

        pnlSuperior2.setBackground(new java.awt.Color(204, 204, 255));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 2, 24)); // NOI18N
        jLabel2.setText("----- Control de Calidad -------------------------------------------------------------------------------");

        lblObservacion.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblObservacion.setText("Observacion");

        txtObservacion.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        btnEnviarCalidad.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnEnviarCalidad.setText("Enviar");
        btnEnviarCalidad.addActionListener(this::btnEnviarCalidadActionPerformed);

        btnAprobarControl.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnAprobarControl.setText("Aprobar Control");
        btnAprobarControl.addActionListener(this::btnAprobarControlActionPerformed);

        btnRechazarControl.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnRechazarControl.setText("Rechazar");
        btnRechazarControl.addActionListener(this::btnRechazarControlActionPerformed);

        tblControles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "IdControl", "Criterio", "Obligatorio", "Resultado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tblControles);

        javax.swing.GroupLayout pnlSuperior2Layout = new javax.swing.GroupLayout(pnlSuperior2);
        pnlSuperior2.setLayout(pnlSuperior2Layout);
        pnlSuperior2Layout.setHorizontalGroup(
            pnlSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSuperior2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSuperior2Layout.createSequentialGroup()
                        .addGroup(pnlSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2)
                            .addGroup(pnlSuperior2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(lblObservacion)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtObservacion, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnEnviarCalidad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(btnAprobarControl)
                                .addGap(18, 18, 18)
                                .addComponent(btnRechazarControl, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlSuperior2Layout.setVerticalGroup(
            pnlSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSuperior2Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEnviarCalidad, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAprobarControl, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRechazarControl, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtObservacion)
                    .addComponent(lblObservacion))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        lblOrden.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblOrden.setText("Orden:");

        cboOrden.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnFinalizar.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        btnFinalizar.setText("Finalizar y Entregar");
        btnFinalizar.addActionListener(this::btnFinalizarActionPerformed);

        pnlSuperior3.setBackground(new java.awt.Color(204, 204, 255));

        jLabel3.setFont(new java.awt.Font("Times New Roman", 2, 24)); // NOI18N
        jLabel3.setText("----- Consumo------------------------------------");

        lblRepuesto.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblRepuesto.setText("Repuesto");

        lblCantidad.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblCantidad.setText("Cantidad");

        txtCantidad.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        txtCantidad.addActionListener(this::txtCantidadActionPerformed);

        btnAgregar.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(this::btnAgregarActionPerformed);

        btnDescontar.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnDescontar.setText("Descontar");
        btnDescontar.addActionListener(this::btnDescontarActionPerformed);

        cboRepuesto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnQuitarConsumo.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnQuitarConsumo.setText("Quitar");
        btnQuitarConsumo.addActionListener(this::btnQuitarConsumoActionPerformed);

        javax.swing.GroupLayout pnlSuperior3Layout = new javax.swing.GroupLayout(pnlSuperior3);
        pnlSuperior3.setLayout(pnlSuperior3Layout);
        pnlSuperior3Layout.setHorizontalGroup(
            pnlSuperior3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSuperior3Layout.createSequentialGroup()
                .addGroup(pnlSuperior3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSuperior3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(pnlSuperior3Layout.createSequentialGroup()
                            .addGap(8, 8, 8)
                            .addGroup(pnlSuperior3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(lblCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblRepuesto, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(pnlSuperior3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(cboRepuesto, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtCantidad, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSuperior3Layout.createSequentialGroup()
                            .addGap(18, 18, 18)
                            .addComponent(btnAgregar)
                            .addGap(35, 35, 35)
                            .addComponent(btnQuitarConsumo, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnDescontar, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel3))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        pnlSuperior3Layout.setVerticalGroup(
            pnlSuperior3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSuperior3Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addGroup(pnlSuperior3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSuperior3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboRepuesto, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlSuperior3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCantidad)
                            .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlSuperior3Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(lblRepuesto)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlSuperior3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlSuperior3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnQuitarConsumo, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                        .addComponent(btnAgregar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnDescontar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(16, 16, 16))
        );

        tblActividades.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "IdActividad", "Servicio", "Descripcion", "Mecanico", "Estado", "Horas"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblActividades);
        if (tblActividades.getColumnModel().getColumnCount() > 0) {
            tblActividades.getColumnModel().getColumn(4).setHeaderValue("Estado");
            tblActividades.getColumnModel().getColumn(5).setHeaderValue("Horas");
        }

        tblConsumos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "IdConsumo", "Codigo", "Repuesto", "Cantidad", "Precio", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblConsumos);

        javax.swing.GroupLayout pnlFondoLayout = new javax.swing.GroupLayout(pnlFondo);
        pnlFondo.setLayout(pnlFondoLayout);
        pnlFondoLayout.setHorizontalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFondoLayout.createSequentialGroup()
                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlFondoLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnlFondoLayout.createSequentialGroup()
                                .addComponent(lblOrden)
                                .addGap(33, 33, 33)
                                .addComponent(cboOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnFinalizar, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlFondoLayout.createSequentialGroup()
                                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(pnlSuperior2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(pnlFondoLayout.createSequentialGroup()
                                        .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                            .addComponent(pnlSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(pnlSuperior3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(pnlFondoLayout.createSequentialGroup()
                        .addGap(300, 300, 300)
                        .addComponent(lblTitulo)))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        pnlFondoLayout.setVerticalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFondoLayout.createSequentialGroup()
                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlFondoLayout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(btnFinalizar))
                    .addGroup(pnlFondoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblTitulo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlSuperior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlSuperior3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlSuperior2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 950, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlFondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 766, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlFondo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFinalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinalizarActionPerformed
        FrmPagos frmPagos = new FrmPagos();
        frmPagos.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnFinalizarActionPerformed

    private void txtApellidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtApellidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApellidoActionPerformed

    private void txtCantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantidadActionPerformed

    private void btnQuitarConsumoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarConsumoActionPerformed
        int fila = tblConsumos.getSelectedRow();
        if (fila >= 0) {
            modeloConsumos.removeRow(fila);
            JOptionPane.showMessageDialog(this, "Consumo retirado de la lista.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un consumo de la tabla para quitar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnQuitarConsumoActionPerformed

    private void btnDescontarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescontarActionPerformed
        JOptionPane.showMessageDialog(this, "Stock descontado del inventario exitosamente.", "Inventario Actualizado", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnDescontarActionPerformed

    private void btnAprobarControlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAprobarControlActionPerformed
       int fila = tblControles.getSelectedRow();
        if (fila >= 0) {
            tblControles.setValueAt("Aprobado", fila, 3);
            JOptionPane.showMessageDialog(this, "Control de calidad aprobado.", "Aprobado", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un control de la tabla para aprobar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnAprobarControlActionPerformed

    private void btnEnviarCalidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarCalidadActionPerformed
        if (txtObservacion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una observación para enviar a control de calidad.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        modeloControles.addRow(new Object[]{
            modeloControles.getRowCount() + 1,
            txtObservacion.getText().trim(),
            "Sí",
            "Pendiente"
        });
        JOptionPane.showMessageDialog(this, "Enviado a control de calidad con éxito.", "Calidad", JOptionPane.INFORMATION_MESSAGE);
        txtObservacion.setText("");
    }//GEN-LAST:event_btnEnviarCalidadActionPerformed

    private void cboServicioOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboServicioOrdenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboServicioOrdenActionPerformed

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        if (cboServicioOrden.getSelectedIndex() <= 0 || cboMecanico.getSelectedIndex() <= 0 || txtApellido.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos de la actividad.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        modeloActividades.addRow(new Object[]{
            modeloActividades.getRowCount() + 1,
            cboServicioOrden.getSelectedItem().toString(),
            txtApellido.getText().trim(),
            cboMecanico.getSelectedItem().toString(),
            "Registrada",
            "0 hrs"
        });
        JOptionPane.showMessageDialog(this, "Actividad registrada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        txtApellido.setText("");
    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void btnIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciarActionPerformed
       int fila = tblActividades.getSelectedRow();
        if (fila >= 0) {
            tblActividades.setValueAt("En Proceso", fila, 4);
            JOptionPane.showMessageDialog(this, "Actividad iniciada.", "Estado Actualizado", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una actividad de la tabla para iniciar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnIniciarActionPerformed

    private void btnCompletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCompletarActionPerformed
        int fila = tblActividades.getSelectedRow();
        if (fila >= 0) {
            tblActividades.setValueAt("Completada", fila, 4);
            JOptionPane.showMessageDialog(this, "Actividad marcada como completada.", "Estado Actualizado", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una actividad de la tabla para completar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnCompletarActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        if (cboRepuesto.getSelectedIndex() <= 0 || txtCantidad.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un repuesto e indique la cantidad.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        modeloConsumos.addRow(new Object[]{
            modeloConsumos.getRowCount() + 1,
            "REP-00" + (modeloConsumos.getRowCount() + 1),
            cboRepuesto.getSelectedItem().toString(),
            txtCantidad.getText().trim(),
            "$50.00",
            "Agregado"
        });
        JOptionPane.showMessageDialog(this, "Repuesto agregado al consumo.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        txtCantidad.setText("");
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnRechazarControlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRechazarControlActionPerformed
       int fila = tblControles.getSelectedRow();
        if (fila >= 0) {
            tblControles.setValueAt("Rechazado", fila, 3);
            JOptionPane.showMessageDialog(this, "Control de calidad rechazado.", "Rechazado", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un control de la tabla para rechazar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnRechazarControlActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FrmEjecucion().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnAprobarControl;
    private javax.swing.JButton btnCompletar;
    private javax.swing.JButton btnDescontar;
    private javax.swing.JButton btnEnviarCalidad;
    private javax.swing.JButton btnFinalizar;
    private javax.swing.JButton btnIniciar;
    private javax.swing.JButton btnQuitarConsumo;
    private javax.swing.JButton btnRechazarControl;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JComboBox<String> cboMecanico;
    private javax.swing.JComboBox<String> cboOrden;
    private javax.swing.JComboBox<String> cboRepuesto;
    private javax.swing.JComboBox<String> cboServicioOrden;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblActividad;
    private javax.swing.JLabel lblCantidad;
    private javax.swing.JLabel lblMecanico;
    private javax.swing.JLabel lblObservacion;
    private javax.swing.JLabel lblOrden;
    private javax.swing.JLabel lblRepuesto;
    private javax.swing.JLabel lblServicio;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel pnlFondo;
    private javax.swing.JPanel pnlSuperior;
    private javax.swing.JPanel pnlSuperior2;
    private javax.swing.JPanel pnlSuperior3;
    private javax.swing.JTable tblActividades;
    private javax.swing.JTable tblConsumos;
    private javax.swing.JTable tblControles;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtObservacion;
    // End of variables declaration//GEN-END:variables
}
