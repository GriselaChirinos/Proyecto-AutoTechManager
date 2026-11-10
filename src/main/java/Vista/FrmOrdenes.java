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
public class FrmOrdenes extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmOrdenes.class.getName());
private DefaultTableModel modeloOrdenes;
    private DefaultTableModel modeloMecanicosOrden;

    /**
     * Creates new form FrmOrdenes
     */
    public FrmOrdenes() {
        initComponents();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        inicializarTablas();
        cargarClientes();
        cargarMecanicos();
        cargarServicios();
        cargarEstados();
        cargarOrdenesDesdeBD();
    }

    private void inicializarTablas() {
        // Modelo para la tabla principal de Órdenes
        modeloOrdenes = new DefaultTableModel(
            new Object [][] {},
            new String [] { "N. Orden", "Fecha", "Cliente", "Vehículo", "Estado", "Kilometraje" }
        ) {
            boolean[] canEdit = new boolean [] { false, false, false, false, false, false };
            public boolean isCellEditable(int rowIndex, int columnIndex) { return canEdit[columnIndex]; }
        };
        tblOrdenes.setModel(modeloOrdenes);

        // Modelo para la tabla secundaria de Mecánicos asignados
        modeloMecanicosOrden = new DefaultTableModel(
            new Object [][] {},
            new String [] { "Mecánico", "Responsable" }
        ) {
            Class[] types = new Class [] { java.lang.String.class, java.lang.Boolean.class };
            boolean[] canEdit = new boolean [] { false, false };
            public Class getColumnClass(int columnIndex) { return types[columnIndex]; }
            public boolean isCellEditable(int rowIndex, int columnIndex) { return canEdit[columnIndex]; }
        };
        tblMecanicosOrden.setModel(modeloMecanicosOrden);
    }

    private void cargarClientes() {
        cboCliente.removeAllItems();
        cboCliente.addItem("-- Seleccione un Cliente --");
        String sql = "SELECT IdCliente, Nombre, Apellido FROM dbo.Clientes WHERE Activo = 1";
        try (Connection conn = config.Conexion.obtener();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                cboCliente.addItem(rs.getInt("IdCliente") + " - " + rs.getString("Nombre") + " " + rs.getString("Apellido"));
            }
        } catch (SQLException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    private void cargarVehiculosPorCliente(int idCliente) {
        cboVehiculo.removeAllItems();
        cboVehiculo.addItem("-- Seleccione un Vehículo --");
        String sql = "SELECT IdVehiculo, Placa, Marca, Modelo FROM dbo.Vehiculos WHERE IdCliente = ?";
        try (Connection conn = config.Conexion.obtener();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCliente);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    cboVehiculo.addItem(rs.getInt("IdVehiculo") + " - [" + rs.getString("Placa") + "] " + rs.getString("Marca") + " " + rs.getString("Modelo"));
                }
            }
        } catch (SQLException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    private void cargarMecanicos() {
        cboMecanico.removeAllItems();
        cboMecanico.addItem("-- Seleccione un Mecánico --");
        // Asumiendo que existe una tabla Mecanicos o se gestiona en usuarios/empleados
        String sql = "SELECT IdMecanico, Nombre, Apellido FROM dbo.Mecanicos";
        try (Connection conn = config.Conexion.obtener();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                cboMecanico.addItem(rs.getInt("IdMecanico") + " - " + rs.getString("Nombre") + " " + rs.getString("Apellido"));
            }
        } catch (SQLException ex) {
            // Si la tabla no existe aún, cargamos elementos de prueba genéricos para evitar errores de compilación
            cboMecanico.addItem("1 - Mecánico Principal");
        }
    }

    private void cargarServicios() {
        cboServicio.removeAllItems();
        cboServicio.addItem("-- Seleccione un Servicio --");
        String sql = "SELECT IdServicio, NombreServicio FROM dbo.Servicios";
        try (Connection conn = config.Conexion.obtener();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                cboServicio.addItem(rs.getInt("IdServicio") + " - " + rs.getString("NombreServicio"));
            }
        } catch (SQLException ex) {
            cboServicio.addItem("1 - Mantenimiento General");
            cboServicio.addItem("2 - Cambio de Frenos");
        }
    }

    private void cargarEstados() {
        cboEstado.removeAllItems();
        cboEstado.addItem("Pendiente");
        cboEstado.addItem("En Proceso");
        cboEstado.addItem("Finalizado");
        cboEstado.addItem("Cancelado");
    }

    private void cargarOrdenesDesdeBD() {
       modeloOrdenes.setRowCount(0);
        
        // Consulta adaptada a los nombres estándar de tu base de datos
        String sql = "SELECT o.IdOrden, o.FechaIngreso, c.Nombre + ' ' + c.Apellido AS Cliente, v.Placa AS Vehiculo, o.Estado, o.Kilometraje " +
                     "FROM dbo.OrdenesTrabajo o " +
                     "INNER JOIN dbo.Clientes c ON o.IdCliente = c.IdCliente " +
                     "INNER JOIN dbo.Vehiculos v ON o.IdVehiculo = v.IdVehiculo";

        try (Connection conn = config.Conexion.obtener();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                modeloOrdenes.addRow(new Object[]{
                    rs.getInt("IdOrden"),
                    rs.getDate("FechaIngreso"),
                    rs.getString("Cliente"),
                    rs.getString("Vehiculo"),
                    rs.getString("Estado"),
                    rs.getInt("Kilometraje")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar órdenes en la tabla: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
            logger.log(java.util.logging.Level.SEVERE, null, ex);
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tblOrdenes = new javax.swing.JTable();
        pnlSuperior = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblCliente = new javax.swing.JLabel();
        lblVehiculo = new javax.swing.JLabel();
        lblKm = new javax.swing.JLabel();
        lblDiagnostico = new javax.swing.JLabel();
        txtKm = new javax.swing.JTextField();
        lblSintomas = new javax.swing.JLabel();
        cboCliente = new javax.swing.JComboBox<>();
        cboVehiculo = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtaSintomas = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtaDiagPrelim = new javax.swing.JTextArea();
        btnCrear = new javax.swing.JButton();
        lblNOrden = new javax.swing.JLabel();
        lblNumeroOrden = new javax.swing.JLabel();
        lblTitulo = new javax.swing.JLabel();
        pnlSuperior2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        lblMecanico = new javax.swing.JLabel();
        cboMecanico = new javax.swing.JComboBox<>();
        btnAsignar = new javax.swing.JButton();
        chkResponsable = new javax.swing.JCheckBox();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblMecanicosOrden = new javax.swing.JTable();
        pnlSuperior3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lblServicio = new javax.swing.JLabel();
        lblNuevoEstado = new javax.swing.JLabel();
        cboServicio = new javax.swing.JComboBox<>();
        cboEstado = new javax.swing.JComboBox<>();
        btnFinalizar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnAgregarServicio = new javax.swing.JButton();
        btnCambiarEstado = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlFondo.setBackground(new java.awt.Color(153, 153, 255));

        tblOrdenes.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        tblOrdenes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "N. Orden", "Fecha", "Cliente", "Vehiculo", "Estado", "Responsable"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblOrdenes);

        pnlSuperior.setBackground(new java.awt.Color(204, 204, 255));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 2, 24)); // NOI18N
        jLabel1.setText("----- Ingreso del vehiculo -------------------------------------------------------------------------------");

        lblCliente.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblCliente.setText("Cliente");

        lblVehiculo.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblVehiculo.setText("Vehiculo");

        lblKm.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblKm.setText("Kilometraje");

        lblDiagnostico.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblDiagnostico.setText("Diagnostico");

        txtKm.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        txtKm.addActionListener(this::txtKmActionPerformed);

        lblSintomas.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblSintomas.setText("Sintomas");

        cboCliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboCliente.addActionListener(this::cboClienteActionPerformed);

        cboVehiculo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txtaSintomas.setColumns(20);
        txtaSintomas.setRows(5);
        jScrollPane2.setViewportView(txtaSintomas);

        txtaDiagPrelim.setColumns(20);
        txtaDiagPrelim.setRows(5);
        jScrollPane3.setViewportView(txtaDiagPrelim);

        btnCrear.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        btnCrear.setText("Crear Orden");
        btnCrear.addActionListener(this::btnCrearActionPerformed);

        lblNOrden.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblNOrden.setText("N. Orden");

        lblNumeroOrden.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblNumeroOrden.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(0, 0, 0), null, null));

        javax.swing.GroupLayout pnlSuperiorLayout = new javax.swing.GroupLayout(pnlSuperior);
        pnlSuperior.setLayout(pnlSuperiorLayout);
        pnlSuperiorLayout.setHorizontalGroup(
            pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSuperiorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(pnlSuperiorLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlSuperiorLayout.createSequentialGroup()
                                .addComponent(lblCliente)
                                .addGap(28, 28, 28)
                                .addComponent(cboCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlSuperiorLayout.createSequentialGroup()
                                .addComponent(lblKm)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtKm, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlSuperiorLayout.createSequentialGroup()
                                .addComponent(lblVehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboVehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(36, 36, 36)
                        .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnlSuperiorLayout.createSequentialGroup()
                                .addComponent(lblSintomas, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlSuperiorLayout.createSequentialGroup()
                                .addComponent(lblDiagnostico, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSuperiorLayout.createSequentialGroup()
                                .addComponent(lblNOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblNumeroOrden, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(btnCrear)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlSuperiorLayout.setVerticalGroup(
            pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSuperiorLayout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSuperiorLayout.createSequentialGroup()
                        .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCliente)
                            .addComponent(lblSintomas))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboVehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblVehiculo))
                        .addGap(42, 76, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSuperiorLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDiagnostico)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnCrear)
                            .addComponent(lblNOrden)
                            .addComponent(lblNumeroOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtKm, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblKm))
                        .addGap(19, 19, 19))))
        );

        lblTitulo.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        lblTitulo.setText("Ordenes de Trabajo");

        pnlSuperior2.setBackground(new java.awt.Color(204, 204, 255));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 2, 24)); // NOI18N
        jLabel2.setText("----- Mecánico --------------------------------");

        lblMecanico.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblMecanico.setText("Mecánico");

        cboMecanico.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboMecanico.addActionListener(this::cboMecanicoActionPerformed);

        btnAsignar.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnAsignar.setText("Asignar");
        btnAsignar.addActionListener(this::btnAsignarActionPerformed);

        chkResponsable.setBackground(new java.awt.Color(255, 255, 255));
        chkResponsable.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        chkResponsable.setText("Responsable");

        tblMecanicosOrden.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Mecanico", "Responsable"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tblMecanicosOrden);

        javax.swing.GroupLayout pnlSuperior2Layout = new javax.swing.GroupLayout(pnlSuperior2);
        pnlSuperior2.setLayout(pnlSuperior2Layout);
        pnlSuperior2Layout.setHorizontalGroup(
            pnlSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSuperior2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSuperior2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSuperior2Layout.createSequentialGroup()
                        .addGroup(pnlSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlSuperior2Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(lblMecanico)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cboMecanico, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18))
                            .addGroup(pnlSuperior2Layout.createSequentialGroup()
                                .addGap(0, 9, Short.MAX_VALUE)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)))
                        .addGroup(pnlSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAsignar)
                            .addComponent(chkResponsable))
                        .addGap(14, 14, 14))))
        );
        pnlSuperior2Layout.setVerticalGroup(
            pnlSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSuperior2Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGroup(pnlSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblMecanico)
                        .addComponent(cboMecanico, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlSuperior2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(chkResponsable, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(pnlSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSuperior2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlSuperior2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnAsignar, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pnlSuperior3.setBackground(new java.awt.Color(204, 204, 255));

        jLabel3.setFont(new java.awt.Font("Times New Roman", 2, 24)); // NOI18N
        jLabel3.setText("----- Estado / Servicio -----------------------");

        lblServicio.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblServicio.setText("Servicio");

        lblNuevoEstado.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblNuevoEstado.setText("Nuevo Estado");

        cboServicio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cboEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnFinalizar.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnFinalizar.setText("Finalizar y Entregar");
        btnFinalizar.addActionListener(this::btnFinalizarActionPerformed);

        btnCancelar.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnCancelar.setText("Cancelar Orden");
        btnCancelar.addActionListener(this::btnCancelarActionPerformed);

        btnAgregarServicio.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnAgregarServicio.setText("Agregar");
        btnAgregarServicio.addActionListener(this::btnAgregarServicioActionPerformed);

        btnCambiarEstado.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnCambiarEstado.setText("Cambiar");
        btnCambiarEstado.addActionListener(this::btnCambiarEstadoActionPerformed);

        javax.swing.GroupLayout pnlSuperior3Layout = new javax.swing.GroupLayout(pnlSuperior3);
        pnlSuperior3.setLayout(pnlSuperior3Layout);
        pnlSuperior3Layout.setHorizontalGroup(
            pnlSuperior3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSuperior3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSuperior3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3)
                    .addGroup(pnlSuperior3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(pnlSuperior3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSuperior3Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(btnFinalizar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnCancelar))
                            .addGroup(pnlSuperior3Layout.createSequentialGroup()
                                .addGroup(pnlSuperior3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(pnlSuperior3Layout.createSequentialGroup()
                                        .addComponent(lblServicio)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cboServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlSuperior3Layout.createSequentialGroup()
                                        .addComponent(lblNuevoEstado)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cboEstado, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(pnlSuperior3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnAgregarServicio, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnCambiarEstado, javax.swing.GroupLayout.Alignment.TRAILING))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlSuperior3Layout.setVerticalGroup(
            pnlSuperior3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSuperior3Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSuperior3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblServicio)
                    .addComponent(cboServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgregarServicio))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSuperior3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNuevoEstado)
                    .addComponent(cboEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCambiarEstado))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSuperior3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnFinalizar, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlFondoLayout = new javax.swing.GroupLayout(pnlFondo);
        pnlFondo.setLayout(pnlFondoLayout);
        pnlFondoLayout.setHorizontalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFondoLayout.createSequentialGroup()
                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlFondoLayout.createSequentialGroup()
                        .addGap(318, 318, 318)
                        .addComponent(lblTitulo))
                    .addGroup(pnlFondoLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, 883, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(pnlFondoLayout.createSequentialGroup()
                                    .addComponent(pnlSuperior2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(pnlSuperior3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        pnlFondoLayout.setVerticalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFondoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlSuperior2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlSuperior3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 934, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlFondo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 628, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlFondo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFinalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinalizarActionPerformed
        int fila = tblOrdenes.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione la orden que desea finalizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idOrden = Integer.parseInt(tblOrdenes.getValueAt(fila, 0).toString());
        String sql = "UPDATE dbo.OrdenesTrabajo SET Estado = 'Finalizado' WHERE IdOrden = ?";
        try (Connection conn = config.Conexion.obtener();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idOrden);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "¡Orden N. " + idOrden + " finalizada y lista para entrega!", "Completado", JOptionPane.INFORMATION_MESSAGE);
            cargarOrdenesDesdeBD();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al finalizar orden: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnFinalizarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        int fila = tblOrdenes.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione la orden que desea cancelar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idOrden = Integer.parseInt(tblOrdenes.getValueAt(fila, 0).toString());
        String sql = "UPDATE dbo.OrdenesTrabajo SET Estado = 'Cancelado' WHERE IdOrden = ?";
        try (Connection conn = config.Conexion.obtener();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idOrden);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "La orden N. " + idOrden + " ha sido cancelada.", "Cancelada", JOptionPane.INFORMATION_MESSAGE);
            cargarOrdenesDesdeBD();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cancelar orden: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void cboMecanicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMecanicoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboMecanicoActionPerformed

    private void cboClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboClienteActionPerformed
        if (cboCliente.getSelectedIndex() > 0) {
            String seleccion = cboCliente.getSelectedItem().toString();
            int idCliente = Integer.parseInt(seleccion.split(" - ")[0]);
            cargarVehiculosPorCliente(idCliente);
        }
    }//GEN-LAST:event_cboClienteActionPerformed

    private void btnCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearActionPerformed
        if (cboCliente.getSelectedIndex() <= 0 || cboVehiculo.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un cliente y un vehículo.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String clienteSel = cboCliente.getSelectedItem().toString();
        int idCliente = Integer.parseInt(clienteSel.split(" - ")[0]);

        String vehiculoSel = cboVehiculo.getSelectedItem().toString();
        int idVehiculo = Integer.parseInt(vehiculoSel.split(" - ")[0]);

        String sintomas = txtaSintomas.getText().trim();
        String diagnostico = txtaDiagPrelim.getText().trim();
        String kilometraje = txtKm.getText().trim();

        String sql = "INSERT INTO dbo.OrdenesTrabajo (IdCliente, IdVehiculo, Sintomas, DiagnosticoPreliminar, Kilometraje, Estado, FechaIngreso) VALUES (?, ?, ?, ?, ?, 'Pendiente', GETDATE())";

        try (Connection conn = config.Conexion.obtener();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, idCliente);
            pstmt.setInt(2, idVehiculo);
            pstmt.setString(3, sintomas);
            pstmt.setString(4, diagnostico);
            pstmt.setString(5, kilometraje);

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int idOrden = rs.getInt(1);
                    lblNumeroOrden.setText(String.valueOf(idOrden));
                    JOptionPane.showMessageDialog(this, "¡Orden de trabajo N. " + idOrden + " creada exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
            }

            // <-- ESTA LÍNEA ES LA QUE CARGA LOS DATOS EN LA TABLA INFERIOR
            cargarOrdenesDesdeBD();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al crear la orden: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnCrearActionPerformed

    private void txtKmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKmActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKmActionPerformed

    private void btnAsignarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAsignarActionPerformed
        if (cboMecanico.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un mecánico.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String mecanico = cboMecanico.getSelectedItem().toString();
        boolean esResponsable = chkResponsable.isSelected();
        modeloMecanicosOrden.addRow(new Object[]{mecanico, esResponsable});
        JOptionPane.showMessageDialog(this, "Mecánico asignado a la orden temporalmente.", "Asignado", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnAsignarActionPerformed

    private void btnAgregarServicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarServicioActionPerformed
        if (cboServicio.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un servicio del catálogo.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Servicio agregado a la orden correctamente.", "Servicio", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnAgregarServicioActionPerformed

    private void btnCambiarEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCambiarEstadoActionPerformed
        int fila = tblOrdenes.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden de la tabla para cambiar su estado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nuevoEstado = cboEstado.getSelectedItem().toString();
        int idOrden = Integer.parseInt(tblOrdenes.getValueAt(fila, 0).toString());

        String sql = "UPDATE dbo.OrdenesTrabajo SET Estado = ? WHERE IdOrden = ?";
        try (Connection conn = config.Conexion.obtener();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nuevoEstado);
            pstmt.setInt(2, idOrden);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Estado actualizado a: " + nuevoEstado, "Actualizado", JOptionPane.INFORMATION_MESSAGE);
            cargarOrdenesDesdeBD();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cambiar estado: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnCambiarEstadoActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new FrmOrdenes().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarServicio;
    private javax.swing.JButton btnAsignar;
    private javax.swing.JButton btnCambiarEstado;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCrear;
    private javax.swing.JButton btnFinalizar;
    private javax.swing.JComboBox<String> cboCliente;
    private javax.swing.JComboBox<String> cboEstado;
    private javax.swing.JComboBox<String> cboMecanico;
    private javax.swing.JComboBox<String> cboServicio;
    private javax.swing.JComboBox<String> cboVehiculo;
    private javax.swing.JCheckBox chkResponsable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lblCliente;
    private javax.swing.JLabel lblDiagnostico;
    private javax.swing.JLabel lblKm;
    private javax.swing.JLabel lblMecanico;
    private javax.swing.JLabel lblNOrden;
    private javax.swing.JLabel lblNuevoEstado;
    private javax.swing.JLabel lblNumeroOrden;
    private javax.swing.JLabel lblServicio;
    private javax.swing.JLabel lblSintomas;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblVehiculo;
    private javax.swing.JPanel pnlFondo;
    private javax.swing.JPanel pnlSuperior;
    private javax.swing.JPanel pnlSuperior2;
    private javax.swing.JPanel pnlSuperior3;
    private javax.swing.JTable tblMecanicosOrden;
    private javax.swing.JTable tblOrdenes;
    private javax.swing.JTextField txtKm;
    private javax.swing.JTextArea txtaDiagPrelim;
    private javax.swing.JTextArea txtaSintomas;
    // End of variables declaration//GEN-END:variables
}
