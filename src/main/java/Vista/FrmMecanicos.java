/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista;

import static config.Conexion.obtener;
import Modelo.Mecanico;
import DAO.MecanicoDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.List;
import java.sql.Connection;
/**
 *
 * @author HP
 */
public class FrmMecanicos extends javax.swing.JFrame {
    private MecanicoDAO mecanicoDAO = new MecanicoDAO();
    private int idMecanicoSeleccionado = -1;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmMecanicos.class.getName());

    /**
     * Creates new form FrmMecanicos
     */
    public FrmMecanicos() {
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        cargarEspecialidades();
        listarMecanicos();

        // Enlazamos los eventos de los botones
        btnNuevo.addActionListener(e -> limpiarFormulario());
        btnGuardar.addActionListener(e -> guardarOActualizarMecanico());
        btnDesactivar.addActionListener(e -> desactivarMecanico());

        jTableMecanicos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cargarDatosSeleccionados();
            }
        });
    }
    
    private void cargarEspecialidades() {
        cmbEspecialidad.removeAllItems();
        cmbEspecialidad.addItem("Motor y transmisión");
        cmbEspecialidad.addItem("Frenos y suspensión");
        cmbEspecialidad.addItem("Electricidad");
        cmbEspecialidad.addItem("Mantenimiento general");
    }

    private void listarMecanicos() {
        String[] columnas = {"IdMecanico", "Identidad", "Nombre", "Apellido", "Teléfono", "Especialidad", "Activo", "Órdenes Activas"};
        DefaultTableModel model = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try {
            List<Mecanico> lista = mecanicoDAO.listar();
            for (Mecanico m : lista) {
                int ordenesActivas = mecanicoDAO.contarOrdenesActivas(m.getId());
                Object[] fila = {
                    m.getId(),
                    m.getIdentidad(),
                    m.getNombre(),
                    m.getApellido(),
                    m.getTelefono(),
                    m.getEspecialidad(),
                    m.isActivo() ? "Sí" : "No",
                    ordenesActivas
                };
                model.addRow(fila);
            }
            jTableMecanicos.setModel(model);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al listar los mecánicos: " + ex.getMessage());
        }
    }

    private void guardarOActualizarMecanico() {
        String identidad = txtIdentidad.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String especialidad = (String) cmbEspecialidad.getSelectedItem();
        boolean activo = chkActivo.isSelected();

        if (identidad.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete los campos obligatorios: Identidad, Nombre y Apellido.");
            return;
        }

        try {
            if (idMecanicoSeleccionado == -1) {
                Mecanico nuevoMecanico = new Mecanico(0, identidad, nombre, apellido, telefono, especialidad);
                nuevoMecanico.setActivo(activo);
                int idGenerado = mecanicoDAO.insertar(nuevoMecanico);
                if (idGenerado > 0) {
                    JOptionPane.showMessageDialog(this, "Mecánico registrado exitosamente.");
                    listarMecanicos();
                    limpiarFormulario();
                }
            } else {
                Mecanico mecanicoExistente = new Mecanico(idMecanicoSeleccionado, identidad, nombre, apellido, telefono, especialidad);
                mecanicoExistente.setActivo(activo);
                boolean actualizado = mecanicoDAO.actualizar(mecanicoExistente);
                if (actualizado) {
                    JOptionPane.showMessageDialog(this, "Mecánico actualizado exitosamente.");
                    listarMecanicos();
                    limpiarFormulario();
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar (¿Identidad duplicada?): " + ex.getMessage());
        }
    }

    private void desactivarMecanico() {
        if (idMecanicoSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un mecánico de la tabla para desactivar.");
            return;
        }

        String sql = "UPDATE Mecanicos SET Activo = 0 WHERE IdMecanico = ?";

        try (Connection con = config.Conexion.obtener();
             java.sql.PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setInt(1, idMecanicoSeleccionado);
            int filasAfectadas = pst.executeUpdate();
            
            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(this, "Mecánico desactivado correctamente.");
                listarMecanicos();
                limpiarFormulario();
            }
        } catch (java.sql.SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al desactivar el mecánico: " + ex.getMessage());
        }
    }

    private void cargarDatosSeleccionados() {
        int fila = jTableMecanicos.getSelectedRow();
        if (fila >= 0) {
            idMecanicoSeleccionado = (int) jTableMecanicos.getValueAt(fila, 0);
            txtIdentidad.setText(jTableMecanicos.getValueAt(fila, 1).toString());
            txtNombre.setText(jTableMecanicos.getValueAt(fila, 2).toString());
            txtApellido.setText(jTableMecanicos.getValueAt(fila, 3).toString());
            txtTelefono.setText(jTableMecanicos.getValueAt(fila, 4) != null ? jTableMecanicos.getValueAt(fila, 4).toString() : "");
            cmbEspecialidad.setSelectedItem(jTableMecanicos.getValueAt(fila, 5).toString());
            chkActivo.setSelected(jTableMecanicos.getValueAt(fila, 6).toString().equals("Sí"));
        }
    }

    private void limpiarFormulario() {
        idMecanicoSeleccionado = -1;
        txtIdentidad.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtTelefono.setText("");
        cmbEspecialidad.setSelectedIndex(0);
        chkActivo.setSelected(true);
        jTableMecanicos.clearSelection();
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
        lblIdentidad = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        lblApellido = new javax.swing.JLabel();
        lblCorreo = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        txtApellido = new javax.swing.JTextField();
        lblTelefono2 = new javax.swing.JLabel();
        txtIdentidad = new javax.swing.JTextField();
        chkActivo = new javax.swing.JCheckBox();
        cmbEspecialidad = new javax.swing.JComboBox<>();
        lblTitulo = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableMecanicos = new javax.swing.JTable();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnDesactivar = new javax.swing.JButton();
        lblCarga = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlFondo.setBackground(new java.awt.Color(153, 153, 255));

        pnlSuperior.setBackground(new java.awt.Color(204, 204, 255));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 2, 24)); // NOI18N
        jLabel1.setText("----- Datos del Mecánico ------------------------------------------------------------------------------");

        lblIdentidad.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblIdentidad.setText("Identidad");

        lblNombre.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblNombre.setText("Nombre");

        lblApellido.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblApellido.setText("Apellido");

        lblCorreo.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblCorreo.setText("Especialidad");

        txtTelefono.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        txtNombre.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        txtApellido.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        lblTelefono2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblTelefono2.setText("Telefono");

        txtIdentidad.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        chkActivo.setBackground(new java.awt.Color(255, 255, 255));
        chkActivo.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        chkActivo.setText("Activo");
        chkActivo.addActionListener(this::chkActivoActionPerformed);

        cmbEspecialidad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbEspecialidad.addActionListener(this::cmbEspecialidadActionPerformed);

        javax.swing.GroupLayout pnlSuperiorLayout = new javax.swing.GroupLayout(pnlSuperior);
        pnlSuperior.setLayout(pnlSuperiorLayout);
        pnlSuperiorLayout.setHorizontalGroup(
            pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSuperiorLayout.createSequentialGroup()
                .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSuperiorLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 874, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlSuperiorLayout.createSequentialGroup()
                        .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlSuperiorLayout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(lblIdentidad))
                            .addGroup(pnlSuperiorLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtIdentidad, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(51, 51, 51)
                        .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(pnlSuperiorLayout.createSequentialGroup()
                                    .addComponent(lblTelefono2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(39, 39, 39)
                                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(pnlSuperiorLayout.createSequentialGroup()
                                    .addComponent(lblCorreo)
                                    .addGap(18, 18, 18)
                                    .addComponent(cmbEspecialidad, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(chkActivo, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlSuperiorLayout.setVerticalGroup(
            pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSuperiorLayout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdentidad)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTelefono2)
                    .addComponent(txtIdentidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNombre)
                    .addComponent(lblCorreo)
                    .addComponent(cmbEspecialidad, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblApellido)
                    .addComponent(chkActivo))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        lblTitulo.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        lblTitulo.setText("Módulo Mecánicos");

        jTableMecanicos.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jTableMecanicos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "IdMecanico", "Identidad", "Nombre", "Apellido", "Telefono", "Especialidad", "Activo", "Ordenes Activas"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableMecanicos);

        btnNuevo.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        btnNuevo.setText(" Nuevo ");

        btnGuardar.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        btnGuardar.setText("Guardar");

        btnDesactivar.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        btnDesactivar.setText("Desactivar ");
        btnDesactivar.addActionListener(this::btnDesactivarActionPerformed);

        lblCarga.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblCarga.setText("Ordenes activas del mecánico seleccionado:");

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
                        .addGap(166, 166, 166)
                        .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDesactivar, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlFondoLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pnlSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, 883, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1)
                            .addComponent(lblCarga, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        pnlFondoLayout.setVerticalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFondoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDesactivar, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(lblCarga)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 930, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlFondo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 553, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlFondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDesactivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDesactivarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDesactivarActionPerformed

    private void chkActivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkActivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkActivoActionPerformed

    private void cmbEspecialidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbEspecialidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbEspecialidadActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new FrmMecanicos().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDesactivar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JCheckBox chkActivo;
    private javax.swing.JComboBox<String> cmbEspecialidad;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableMecanicos;
    private javax.swing.JLabel lblApellido;
    private javax.swing.JLabel lblCarga;
    private javax.swing.JLabel lblCorreo;
    private javax.swing.JLabel lblIdentidad;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblTelefono2;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel pnlFondo;
    private javax.swing.JPanel pnlSuperior;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtIdentidad;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
