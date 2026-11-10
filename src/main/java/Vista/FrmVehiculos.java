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
public class FrmVehiculos extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmVehiculos.class.getName());
    private DefaultTableModel modeloTabla;

    /**
     * Creates new form FrmVehiculos
     */
    public FrmVehiculos() {
        initComponents();
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        inicializarTabla();
        cargarClientesEnComboBox();
        cargarCombustible();
        cargarVehiculosDesdeBD();
        bloquearCamposVehiculo(false); // Inicia bloqueado hasta dar en "Nuevo"
    }

    private void inicializarTabla() {
        modeloTabla = new DefaultTableModel(
            new Object [][] {},
            new String [] { "IdVehiculo", "Cliente", "Placa", "VIN/Chasis", "Marca", "Modelo", "Año", "Color", "Kilometraje", "Combustible" }
        ) {
            boolean[] canEdit = new boolean [] { false, false, false, false, false, false, false, false, false, false };
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };
        tblVehiculos.setModel(modeloTabla);
    }

    private void cargarCombustible() {
        cboCombustible.removeAllItems();
        cboCombustible.addItem("Gasolina");
        cboCombustible.addItem("Diesel");
        cboCombustible.addItem("Híbrido");
        cboCombustible.addItem("Eléctrico");
    }

    private void bloquearCamposVehiculo(boolean estado) {
        txtPlaca.setEnabled(estado);
        txtVin.setEnabled(estado);
        txtMarca.setEnabled(estado);
        txtModelo.setEnabled(estado);
        spnAnio.setEnabled(estado);
        txtColor.setEnabled(estado);
        txtKm.setEnabled(estado);
        cboCombustible.setEnabled(estado);
    }

    private void limpiarCamposVehiculo() {
        txtPlaca.setText("");
        txtVin.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        spnAnio.setValue(2026);
        txtColor.setText("");
        txtKm.setText("");
        if(cboCombustible.getItemCount() > 0) cboCombustible.setSelectedIndex(0);
    }

    // Punto 2: Cargar clientes en el ComboBox desde la base de datos
    private void cargarClientesEnComboBox() {
        cboCliente.removeAllItems();
        cboCliente.addItem("-- Seleccione un Cliente --");
        
        String sql = "SELECT IdCliente, Identidad, Nombre, Apellido FROM dbo.Clientes WHERE Activo = 1";
        
        try (Connection conn = config.Conexion.obtener();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String item = rs.getInt("IdClientE") + " - " + rs.getString("Identidad") + " (" + rs.getString("Nombre") + " " + rs.getString("Apellido") + ")";
                cboCliente.addItem(item);
            }
        } catch (SQLException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    // Punto 6: Mostrar información en la parte de abajo (Tabla)
    private void cargarVehiculosDesdeBD() {
        modeloTabla.setRowCount(0);
        String sql = "SELECT v.IdVehiculo, c.Nombre, c.Apellido, v.Placa, v.Vin, v.Marca, v.Modelo, v.Anio, v.Color, v.Kilometraje, v.Combustible " +
                     "FROM dbo.Vehiculos v INNER JOIN dbo.Clientes c ON v.IdCliente = c.IdCliente";

        try (Connection conn = config.Conexion.obtener();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                modeloTabla.addRow(new Object[]{
                    rs.getInt("IdVehiculo"),
                    rs.getString("Nombre") + " " + rs.getString("Apellido"),
                    rs.getString("Placa"),
                    rs.getString("Vin"),
                    rs.getString("Marca"),
                    rs.getString("Modelo"),
                    rs.getInt("Anio"),
                    rs.getString("Color"),
                    rs.getString("Kilometraje"),
                    rs.getString("Combustible")
                });
            }
        } catch (SQLException ex) {
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

        pnlSuperior2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        lblIdentidad = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        lblApellido = new javax.swing.JLabel();
        lblCorreo = new javax.swing.JLabel();
        tctTelefono = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        txtApellido = new javax.swing.JTextField();
        lblDireccion = new javax.swing.JLabel();
        lblTelefono2 = new javax.swing.JLabel();
        txtIdentidad1 = new javax.swing.JTextField();
        txtDireccion = new javax.swing.JTextField();
        txtCorreo = new javax.swing.JTextField();
        pnlSuperior3 = new javax.swing.JPanel();
        btnBuscar = new javax.swing.JButton();
        lblBuscar1 = new javax.swing.JLabel();
        txtBuscar1 = new javax.swing.JTextField();
        chkActivo = new javax.swing.JCheckBox();
        pnlFondo = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblVehiculos = new javax.swing.JTable();
        btnHistorial = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        pnlSuperior = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblPlaca = new javax.swing.JLabel();
        lblVin = new javax.swing.JLabel();
        lblMarca = new javax.swing.JLabel();
        lblColor = new javax.swing.JLabel();
        txtKm = new javax.swing.JTextField();
        txtVin = new javax.swing.JTextField();
        txtMarca = new javax.swing.JTextField();
        lblAnio = new javax.swing.JLabel();
        lblModelo = new javax.swing.JLabel();
        txtPlaca = new javax.swing.JTextField();
        txtColor = new javax.swing.JTextField();
        txtModelo = new javax.swing.JTextField();
        spnAnio = new javax.swing.JSpinner();
        lblModelo1 = new javax.swing.JLabel();
        lblCombustible = new javax.swing.JLabel();
        cboCombustible = new javax.swing.JComboBox<>();
        lblTitulo = new javax.swing.JLabel();
        cboCliente = new javax.swing.JComboBox<>();
        lblBuscar = new javax.swing.JLabel();

        pnlSuperior2.setBackground(new java.awt.Color(204, 204, 255));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 2, 24)); // NOI18N
        jLabel2.setText("----- Datos del Cliente -------------------------------------------------------------------------------------------------------");

        lblIdentidad.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblIdentidad.setText("Identidad");

        lblNombre.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblNombre.setText("Nombre");

        lblApellido.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblApellido.setText("Apellido");

        lblCorreo.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblCorreo.setText("Correo");

        tctTelefono.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        txtNombre.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        txtApellido.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        lblDireccion.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblDireccion.setText("Dirección");

        lblTelefono2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblTelefono2.setText("Telefono");

        txtIdentidad1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        txtDireccion.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        txtCorreo.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        pnlSuperior3.setBackground(new java.awt.Color(255, 255, 255));
        pnlSuperior3.setForeground(new java.awt.Color(255, 255, 255));

        btnBuscar.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        btnBuscar.setText("Buscar");

        lblBuscar1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblBuscar1.setText("Buscar:");

        txtBuscar1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        chkActivo.setBackground(new java.awt.Color(255, 255, 255));
        chkActivo.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        chkActivo.setText("Activo");
        chkActivo.addActionListener(this::chkActivoActionPerformed);

        javax.swing.GroupLayout pnlSuperior3Layout = new javax.swing.GroupLayout(pnlSuperior3);
        pnlSuperior3.setLayout(pnlSuperior3Layout);
        pnlSuperior3Layout.setHorizontalGroup(
            pnlSuperior3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSuperior3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblBuscar1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBuscar1, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53)
                .addComponent(chkActivo, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );
        pnlSuperior3Layout.setVerticalGroup(
            pnlSuperior3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSuperior3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSuperior3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBuscar1)
                    .addComponent(txtBuscar1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkActivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlSuperior2Layout = new javax.swing.GroupLayout(pnlSuperior2);
        pnlSuperior2.setLayout(pnlSuperior2Layout);
        pnlSuperior2Layout.setHorizontalGroup(
            pnlSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSuperior2Layout.createSequentialGroup()
                .addGroup(pnlSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSuperior2Layout.createSequentialGroup()
                        .addGroup(pnlSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlSuperior2Layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(lblIdentidad))
                            .addGroup(pnlSuperior2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(pnlSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtIdentidad1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(72, 72, 72)
                        .addGroup(pnlSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlSuperior2Layout.createSequentialGroup()
                                .addComponent(lblDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlSuperior2Layout.createSequentialGroup()
                                .addComponent(lblCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlSuperior2Layout.createSequentialGroup()
                                .addComponent(lblTelefono2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(tctTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(pnlSuperior2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(pnlSuperior3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlSuperior2Layout.setVerticalGroup(
            pnlSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSuperior2Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdentidad)
                    .addComponent(tctTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTelefono2)
                    .addComponent(txtIdentidad1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblNombre))
                    .addGroup(pnlSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblCorreo)
                        .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblApellido)
                    .addComponent(lblDireccion)
                    .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(pnlSuperior3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlFondo.setBackground(new java.awt.Color(153, 153, 255));

        tblVehiculos.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        tblVehiculos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "IdVehiculo", "Placa", "VIN/Chasis", "Marca", "Modelo", "Anio", "Color", "Kilometraje", "Combustible"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblVehiculos);

        btnHistorial.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        btnHistorial.setText("Ver Historial");
        btnHistorial.addActionListener(this::btnHistorialActionPerformed);

        btnNuevo.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(this::btnNuevoActionPerformed);

        btnGuardar.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);

        pnlSuperior.setBackground(new java.awt.Color(204, 204, 255));
        pnlSuperior.setMaximumSize(new java.awt.Dimension(327, 32767));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 2, 24)); // NOI18N
        jLabel1.setText("----- Datos del Vehículo -------------------------------------");

        lblPlaca.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblPlaca.setText("Placa");

        lblVin.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblVin.setText("VIN/Chasis");

        lblMarca.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblMarca.setText("Marca");

        lblColor.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblColor.setText("Color");

        txtKm.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        txtVin.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        txtMarca.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        lblAnio.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblAnio.setText("Año");

        lblModelo.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblModelo.setText("Modelo");

        txtPlaca.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        txtColor.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        txtModelo.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        spnAnio.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        lblModelo1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblModelo1.setText("Kilometraje");

        lblCombustible.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblCombustible.setText("Combustible");

        cboCombustible.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout pnlSuperiorLayout = new javax.swing.GroupLayout(pnlSuperior);
        pnlSuperior.setLayout(pnlSuperiorLayout);
        pnlSuperiorLayout.setHorizontalGroup(
            pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSuperiorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(pnlSuperiorLayout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblModelo1)
                            .addComponent(lblVin, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPlaca))
                        .addGap(18, 18, 18)
                        .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtVin, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtKm, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(102, 102, 102)
                        .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlSuperiorLayout.createSequentialGroup()
                                .addComponent(lblModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlSuperiorLayout.createSequentialGroup()
                                .addComponent(lblAnio, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(spnAnio, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlSuperiorLayout.createSequentialGroup()
                                .addComponent(lblColor, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtColor, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlSuperiorLayout.createSequentialGroup()
                                .addComponent(lblCombustible)
                                .addGap(18, 18, 18)
                                .addComponent(cboCombustible, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(83, 83, 83))
        );
        pnlSuperiorLayout.setVerticalGroup(
            pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSuperiorLayout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPlaca)
                    .addComponent(lblModelo)
                    .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtVin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblVin)
                    .addComponent(lblAnio)
                    .addComponent(spnAnio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblMarca)
                        .addComponent(txtMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblColor)
                        .addComponent(txtColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(4, 4, 4)
                .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtKm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblModelo1)
                    .addComponent(lblCombustible)
                    .addComponent(cboCombustible, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        lblTitulo.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        lblTitulo.setText("Vehículos del Cliente");

        cboCliente.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        cboCliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboCliente.addActionListener(this::cboClienteActionPerformed);

        lblBuscar.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblBuscar.setText("Buscar:");

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
                        .addGap(21, 21, 21)
                        .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlFondoLayout.createSequentialGroup()
                                .addComponent(lblBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 535, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(pnlFondoLayout.createSequentialGroup()
                                    .addComponent(pnlSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, 714, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnHistorial, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap(13, Short.MAX_VALUE))
        );
        pnlFondoLayout.setVerticalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFondoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlFondoLayout.createSequentialGroup()
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnHistorial, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
        );

        pnlSuperior.getAccessibleContext().setAccessibleParent(pnlSuperior);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 967, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlFondo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 631, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlFondo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
       if (cboCliente.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un cliente primero en la parte superior.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        limpiarCamposVehiculo();
        bloquearCamposVehiculo(true);
        txtPlaca.requestFocus();
        JOptionPane.showMessageDialog(this, "Ingrese la información del nuevo vehículo.", "Nuevo Vehículo", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void chkActivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkActivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkActivoActionPerformed

    private void cboClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboClienteActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
       if (cboCliente.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un cliente.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (txtPlaca.getText().trim().isEmpty() || txtVin.getText().trim().isEmpty() || txtMarca.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete los campos obligatorios: Placa, VIN y Marca.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Extraer IdCliente del ComboBox (Ejemplo formato: "1 - 0506-2000-03428 (Sandra Avalo)")
        String seleccion = cboCliente.getSelectedItem().toString();
        int idCliente = Integer.parseInt(seleccion.split(" - ")[0]);

        String sql = "INSERT INTO dbo.Vehiculos (IdCliente, Placa, Vin, Marca, Modelo, Anio, Color, Kilometraje, Combustible) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = config.Conexion.obtener();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idCliente);
            pstmt.setString(2, txtPlaca.getText().trim());
            pstmt.setString(3, txtVin.getText().trim());
            pstmt.setString(4, txtMarca.getText().trim());
            pstmt.setString(5, txtModelo.getText().trim());
            pstmt.setInt(6, (Integer) spnAnio.getValue());
            pstmt.setString(7, txtColor.getText().trim());
            pstmt.setString(8, txtKm.getText().trim());
            pstmt.setString(9, cboCombustible.getSelectedItem().toString());

            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "¡Vehículo guardado exitosamente en la base de datos!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarVehiculosDesdeBD();
            limpiarCamposVehiculo();
            bloquearCamposVehiculo(false);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar el vehículo: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnHistorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistorialActionPerformed
        cargarVehiculosDesdeBD();
        JOptionPane.showMessageDialog(this, "Mostrando historial completo de vehículos registrados.", "Historial", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnHistorialActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new FrmVehiculos().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnHistorial;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JComboBox<String> cboCliente;
    private javax.swing.JComboBox<String> cboCombustible;
    private javax.swing.JCheckBox chkActivo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAnio;
    private javax.swing.JLabel lblApellido;
    private javax.swing.JLabel lblBuscar;
    private javax.swing.JLabel lblBuscar1;
    private javax.swing.JLabel lblColor;
    private javax.swing.JLabel lblCombustible;
    private javax.swing.JLabel lblCorreo;
    private javax.swing.JLabel lblDireccion;
    private javax.swing.JLabel lblIdentidad;
    private javax.swing.JLabel lblMarca;
    private javax.swing.JLabel lblModelo;
    private javax.swing.JLabel lblModelo1;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblPlaca;
    private javax.swing.JLabel lblTelefono2;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblVin;
    private javax.swing.JPanel pnlFondo;
    private javax.swing.JPanel pnlSuperior;
    private javax.swing.JPanel pnlSuperior2;
    private javax.swing.JPanel pnlSuperior3;
    private javax.swing.JSpinner spnAnio;
    private javax.swing.JTable tblVehiculos;
    private javax.swing.JTextField tctTelefono;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtBuscar1;
    private javax.swing.JTextField txtColor;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtIdentidad1;
    private javax.swing.JTextField txtKm;
    private javax.swing.JTextField txtMarca;
    private javax.swing.JTextField txtModelo;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPlaca;
    private javax.swing.JTextField txtVin;
    // End of variables declaration//GEN-END:variables
}
