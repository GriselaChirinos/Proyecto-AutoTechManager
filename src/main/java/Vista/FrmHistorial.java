/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista;

import DAO.ClienteDAO;
import DAO.VehiculoDAO;
import Modelo.Cliente;
import Modelo.Vehiculo;
import config.Conexion;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.List;

/**
 *
 * @author HP
 */
public class FrmHistorial extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmHistorial.class.getName());

    public FrmHistorial() {
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        cargarClientes();
        cargarComboReportes();
        cmbCliente.addActionListener(e -> {
            Object obj = cmbCliente.getSelectedItem();
            if (obj instanceof ItemCombo) {
                ItemCombo seleccionado = (ItemCombo) obj;
                cargarVehiculosPorCliente(seleccionado.getId());
            }
        });

        btnConsultar.addActionListener(e -> consultarHistorialVehiculo());
        btnEjecutar.addActionListener(e -> ejecutarReporteDinamico());
    }

    private static class ItemCombo {
        private int id;
        private String texto;

        public ItemCombo(int id, String texto) {
            this.id = id;
            this.texto = texto;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return texto;
        }
    }

    private void cargarClientes() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        cmbCliente.setModel(model);

        try {
            ClienteDAO clienteDAO = new ClienteDAO();
            List<Cliente> clientes = clienteDAO.listar(); 
            
            for (Cliente c : clientes) {
                String nombreCompleto = c.getNombre() + " " + c.getApellido();
                model.addElement(new ItemCombo(c.getId(), nombreCompleto));
            }

            if (model.getSize() > 0) {
                ItemCombo primerCliente = (ItemCombo) model.getElementAt(0);
                cargarVehiculosPorCliente(primerCliente.getId());
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar clientes desde DAO: " + ex.getMessage());
        }
    }

    private void cargarVehiculosPorCliente(int idCliente) {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        cmbVehiculo.setModel(model);

        try {
            VehiculoDAO vehiculoDAO = new VehiculoDAO();
            List<Vehiculo> vehiculos = vehiculoDAO.listar(); 
            
            for (Vehiculo v : vehiculos) {
                if (v.getIdCliente() == idCliente) { 
                    String desc = v.getPlaca() + " - " + v.getMarca() + " " + v.getModelo();
                    model.addElement(new ItemCombo(v.getId(), desc));
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar vehículos desde DAO: " + ex.getMessage());
        }
    }

    private void cargarComboReportes() {
        cmbReportes.removeAllItems();
        cmbReportes.addItem("Stock Crítico de Repuestos");
        cmbReportes.addItem("Saldo Pendiente de Órdenes");
        cmbReportes.addItem("Productividad de Mecánicos");
    }

    private void consultarHistorialVehiculo() {
        Object objSeleccionado = cmbVehiculo.getSelectedItem();
        if (objSeleccionado == null || !(objSeleccionado instanceof ItemCombo)) {
            JOptionPane.showMessageDialog(this, "Seleccione un vehículo válido.");
            return;
        }

        ItemCombo vehiculoSelec = (ItemCombo) objSeleccionado;

        // Columnas alineadas con los campos reales de tu tabla de SQL Server
        String[] columnas = {"N. Orden", "Fecha Ingreso", "Kilometraje", "Estado", "Síntomas"};
        DefaultTableModel model = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String sql = "SELECT IdOrden, NumeroOrden, FechaIngreso, Kilometraje, Estado, Sintomas " +
                     "FROM OrdenesTrabajo WHERE IdVehiculo = ?";

        try (Connection con = Conexion.obtener();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, vehiculoSelec.getId());
            try (ResultSet rs = pst.executeQuery()) {
                boolean hayDatos = false;
                while (rs.next()) {
                    Object[] fila = {
                        rs.getString("NumeroOrden") != null ? rs.getString("NumeroOrden") : "ORD-" + rs.getInt("IdOrden"),
                        rs.getTimestamp("FechaIngreso"),
                        rs.getInt("Kilometraje"),
                        rs.getString("Estado"),
                        rs.getString("Sintomas")
                    };
                    model.addRow(fila);
                    hayDatos = true;
                }
                if (!hayDatos) {
                    JOptionPane.showMessageDialog(this, "Este vehículo no registra órdenes de trabajo.");
                }
            }
            jTableHistorial.setModel(model); 
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al consultar historial: " + ex.getMessage());
        }
    }

    private void ejecutarReporteDinamico() {
        String reporteSeleccionado = (String) cmbReportes.getSelectedItem();
        String sql = "";

        if ("Stock Crítico de Repuestos".equals(reporteSeleccionado)) {
            sql = "SELECT * FROM Repuestos WHERE Stock <= StockMinimo"; 
        } else if ("Saldo Pendiente de Órdenes".equals(reporteSeleccionado)) {
            sql = "SELECT * FROM OrdenesTrabajo WHERE Estado != 'FINALIZADA'"; 
        } else if ("Productividad de Mecánicos".equals(reporteSeleccionado)) {
            sql = "SELECT m.IdMecanico, m.Nombre + ' ' + m.Apellido AS Mecanico, m.Especialidad, COUNT(om.IdOrden) AS OrdenesAsignadas FROM Mecanicos m LEFT JOIN OrdenMecanico om ON m.IdMecanico = om.IdMecanico GROUP BY m.IdMecanico, m.Nombre, m.Apellido, m.Especialidad";
        }

        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try (Connection con = Conexion.obtener();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            ResultSetMetaData metaData = rs.getMetaData();
            int numColumnas = metaData.getColumnCount();

            for (int i = 1; i <= numColumnas; i++) {
                model.addColumn(metaData.getColumnName(i));
            }

            int contadorFilas = 0;
            while (rs.next()) {
                Object[] fila = new Object[numColumnas];
                for (int i = 0; i < numColumnas; i++) {
                    fila[i] = rs.getObject(i + 1);
                }
                model.addRow(fila);
                contadorFilas++;
            }

            jTableReportes.setModel(model); 
            lblTotalFilas.setText("Total Filas: " + contadorFilas);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al ejecutar reporte: " + ex.getMessage());
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
        jTableReportes = new javax.swing.JTable();
        pnlSuperior = new javax.swing.JPanel();
        lblCliente = new javax.swing.JLabel();
        lblTotalFilas = new javax.swing.JLabel();
        cmbReportes = new javax.swing.JComboBox<>();
        btnEjecutar = new javax.swing.JButton();
        lblTitulo = new javax.swing.JLabel();
        pnlSuperior1 = new javax.swing.JPanel();
        lblCliente1 = new javax.swing.JLabel();
        lblVehiculo1 = new javax.swing.JLabel();
        cmbCliente = new javax.swing.JComboBox<>();
        cmbVehiculo = new javax.swing.JComboBox<>();
        btnConsultar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableHistorial = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlFondo.setBackground(new java.awt.Color(153, 153, 255));

        jTableReportes.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jTableReportes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "(Columnas dinamicas segun el reporte seleccionado)"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableReportes);

        pnlSuperior.setBackground(new java.awt.Color(204, 204, 255));

        lblCliente.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblCliente.setText("Reportes:");

        lblTotalFilas.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblTotalFilas.setText("Total Filas: ");

        cmbReportes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnEjecutar.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        btnEjecutar.setText("Ejecutar");
        btnEjecutar.addActionListener(this::btnEjecutarActionPerformed);

        javax.swing.GroupLayout pnlSuperiorLayout = new javax.swing.GroupLayout(pnlSuperior);
        pnlSuperior.setLayout(pnlSuperiorLayout);
        pnlSuperiorLayout.setHorizontalGroup(
            pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSuperiorLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lblCliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbReportes, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(btnEjecutar, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(lblTotalFilas)
                .addContainerGap(291, Short.MAX_VALUE))
        );
        pnlSuperiorLayout.setVerticalGroup(
            pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSuperiorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCliente)
                    .addComponent(cmbReportes, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotalFilas)
                    .addComponent(btnEjecutar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblTitulo.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        lblTitulo.setText("Historial y Reportes Administrativos");

        pnlSuperior1.setBackground(new java.awt.Color(204, 204, 255));

        lblCliente1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblCliente1.setText("Cliente:");

        lblVehiculo1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblVehiculo1.setText("Vehiculo:");

        cmbCliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbVehiculo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnConsultar.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        btnConsultar.setText("Consultar");
        btnConsultar.addActionListener(this::btnConsultarActionPerformed);

        javax.swing.GroupLayout pnlSuperior1Layout = new javax.swing.GroupLayout(pnlSuperior1);
        pnlSuperior1.setLayout(pnlSuperior1Layout);
        pnlSuperior1Layout.setHorizontalGroup(
            pnlSuperior1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSuperior1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lblCliente1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblVehiculo1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbVehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                .addComponent(btnConsultar, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        pnlSuperior1Layout.setVerticalGroup(
            pnlSuperior1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSuperior1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSuperior1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCliente1)
                    .addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblVehiculo1)
                    .addComponent(cmbVehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConsultar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTableHistorial.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jTableHistorial.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Fecha", "N. Orden", "Servicios", "Mecanicos", "Repuestos", "Mano de Obra", "Repuestos L.", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTableHistorial);
        if (jTableHistorial.getColumnModel().getColumnCount() > 0) {
            jTableHistorial.getColumnModel().getColumn(1).setHeaderValue("N. Orden");
            jTableHistorial.getColumnModel().getColumn(2).setHeaderValue("Servicios");
            jTableHistorial.getColumnModel().getColumn(3).setHeaderValue("Mecanicos");
            jTableHistorial.getColumnModel().getColumn(4).setHeaderValue("Repuestos");
            jTableHistorial.getColumnModel().getColumn(5).setHeaderValue("Mano de Obra");
            jTableHistorial.getColumnModel().getColumn(6).setHeaderValue("Repuestos L.");
            jTableHistorial.getColumnModel().getColumn(7).setHeaderValue("Total");
        }

        javax.swing.GroupLayout pnlFondoLayout = new javax.swing.GroupLayout(pnlFondo);
        pnlFondo.setLayout(pnlFondoLayout);
        pnlFondoLayout.setHorizontalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFondoLayout.createSequentialGroup()
                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlFondoLayout.createSequentialGroup()
                            .addGap(15, 15, 15)
                            .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane1)
                                .addComponent(pnlSuperior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFondoLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(pnlSuperior1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlFondoLayout.createSequentialGroup()
                        .addGap(205, 205, 205)
                        .addComponent(lblTitulo)))
                .addContainerGap(40, Short.MAX_VALUE))
            .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlFondoLayout.createSequentialGroup()
                    .addGap(16, 16, 16)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 956, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(37, Short.MAX_VALUE)))
        );
        pnlFondoLayout.setVerticalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFondoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlSuperior1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 253, Short.MAX_VALUE)
                .addComponent(pnlSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54))
            .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlFondoLayout.createSequentialGroup()
                    .addGap(125, 125, 125)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(386, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1015, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlFondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 735, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(pnlFondo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 6, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEjecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEjecutarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEjecutarActionPerformed

    private void btnConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnConsultarActionPerformed

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
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FrmHistorial().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConsultar;
    private javax.swing.JButton btnEjecutar;
    private javax.swing.JComboBox<String> cmbCliente;
    private javax.swing.JComboBox<String> cmbReportes;
    private javax.swing.JComboBox<String> cmbVehiculo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableHistorial;
    private javax.swing.JTable jTableReportes;
    private javax.swing.JLabel lblCliente;
    private javax.swing.JLabel lblCliente1;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTotalFilas;
    private javax.swing.JLabel lblVehiculo1;
    private javax.swing.JPanel pnlFondo;
    private javax.swing.JPanel pnlSuperior;
    private javax.swing.JPanel pnlSuperior1;
    // End of variables declaration//GEN-END:variables
}
