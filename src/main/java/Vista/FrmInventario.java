/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista;

import DAO.RepuestoDAO;
import Modelo.Proveedor;
import Modelo.Repuesto;
import config.Conexion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

/**
 *
 * @author HP
 */
public class FrmInventario extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmInventario.class.getName());
    private int idRepuestoSeleccionado = 0; // Para saber si estamos editando o creando

    /**
     * Creates new form FrmInventario
     */
    public FrmInventario() {
        initComponents();
        cargarProveedoresCombo();
        cargarCategoriasCombo();
        cargarTablaRepuestos();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        // 1. El listener del mouse va dentro del constructor
        jTableInventario.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                seleccionarRepuestoDeTabla();
            }
        });

        // 2. Botones principales dentro del constructor
        btnGuardar.addActionListener(e -> guardarRepuesto());
        btnNuevo.addActionListener(e -> limpiarFormulario());
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

    private void cargarProveedoresCombo() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        cmbProveedor.setModel(model);
        try {
            RepuestoDAO dao = new RepuestoDAO();
            List<Proveedor> proveedores = dao.listarProveedores();
            for (Proveedor p : proveedores) {
                model.addElement(new ItemCombo(p.getId(), p.getNombre()));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar proveedores: " + ex.getMessage());
        }
    }

    private void cargarCategoriasCombo() {
        cmbCategoria.removeAllItems();
        cmbCategoria.addItem("Motor");
        cmbCategoria.addItem("Suspensión");
        cmbCategoria.addItem("Frenos");
        cmbCategoria.addItem("Electricidad");
        cmbCategoria.addItem("Transmisión");
        cmbCategoria.addItem("General");
    }

    private void cargarTablaRepuestos() {
        String[] columnas = {"ID", "Código", "Nombre", "Categoría", "Stock", "Mínimo", "Costo", "Precio"};
        DefaultTableModel model = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try {
            RepuestoDAO dao = new RepuestoDAO();
            List<Repuesto> lista = dao.listarActivos();

            for (Repuesto r : lista) {
                Object[] fila = {
                    r.getId(),
                    r.getCodigo(),
                    r.getNombre(),
                    r.getCategoria(),
                    r.getStock(),
                    r.getStockMinimo(),
                    r.getCostoUnitario(),
                    r.getPrecioVenta()
                };
                model.addRow(fila);
            }
            jTableInventario.setModel(model);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar inventario: " + ex.getMessage());
        }
    }
    
    private void guardarRepuesto() {
        try {
            String codigo = txtCodigo.getText().trim();
            String nombre = txtNombre.getText().trim();
            String categoria = cmbCategoria.getSelectedItem().toString();
            
            ItemCombo provSel = (ItemCombo) cmbProveedor.getSelectedItem();
            if (provSel == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un proveedor.");
                return;
            }
            
            BigDecimal costo = new BigDecimal(txtCosto.getText().trim());
            BigDecimal precio = new BigDecimal(txtPrecioVenta.getText().trim());
            int stockMin = Integer.parseInt(txtStockMin.getText().trim());

            Repuesto r = new Repuesto();
            r.setCodigo(codigo);
            r.setNombre(nombre);
            r.setCategoria(categoria);
            r.setIdProveedor(provSel.getId());
            r.setCostoUnitario(costo);
            r.setPrecioVenta(precio);
            r.setStockMinimo(stockMin);

            RepuestoDAO dao = new RepuestoDAO();

            if (idRepuestoSeleccionado == 0) {
                dao.insertar(r);
                JOptionPane.showMessageDialog(this, "Repuesto registrado exitosamente.");
            } else {
                r.setId(idRepuestoSeleccionado);
                r.setStock(Integer.parseInt(txtStockActual.getText().trim()));
                dao.actualizar(r);
                JOptionPane.showMessageDialog(this, "Repuesto actualizado exitosamente.");
            }

            limpiarFormulario();
            cargarTablaRepuestos();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor verifique que los campos numéricos sean válidos.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar en la base de datos: " + ex.getMessage());
        }
    }
    
    private void seleccionarRepuestoDeTabla() {
        int fila = jTableInventario.getSelectedRow();
        if (fila >= 0) {
            idRepuestoSeleccionado = Integer.parseInt(jTableInventario.getValueAt(fila, 0).toString());
            txtCodigo.setText(jTableInventario.getValueAt(fila, 1).toString());
            txtNombre.setText(jTableInventario.getValueAt(fila, 2).toString());
            cmbCategoria.setSelectedItem(jTableInventario.getValueAt(fila, 3).toString());
            txtStockActual.setText(jTableInventario.getValueAt(fila, 4).toString());
            txtStockMin.setText(jTableInventario.getValueAt(fila, 5).toString());
            txtCosto.setText(jTableInventario.getValueAt(fila, 6).toString());
            txtPrecioVenta.setText(jTableInventario.getValueAt(fila, 7).toString());
        }
    }

    private void limpiarFormulario() {
        idRepuestoSeleccionado = 0;
        txtCodigo.setText("");
        txtNombre.setText("");
        txtCosto.setText("");
        txtPrecioVenta.setText("");
        txtStockMin.setText("");
        txtStockActual.setText("0");
        jTableInventario.clearSelection();
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
        jTableInventario = new javax.swing.JTable();
        pnlSuperior = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblCantidad = new javax.swing.JLabel();
        lblCostoUni = new javax.swing.JLabel();
        lblReferencia = new javax.swing.JLabel();
        lblSerieMotor = new javax.swing.JLabel();
        txtCostoEntrada = new javax.swing.JTextField();
        txtSerieMotor = new javax.swing.JTextField();
        txtCantidadEntrada = new javax.swing.JTextField();
        txtReferencia = new javax.swing.JTextField();
        btnEntrada = new javax.swing.JButton();
        chkCritico = new javax.swing.JCheckBox();
        lblTitulo = new javax.swing.JLabel();
        pnlSuperior1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        lblCodigo = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        lblProveedor = new javax.swing.JLabel();
        lblCosto = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtStockMin = new javax.swing.JTextField();
        txtStockActual = new javax.swing.JLabel();
        lblCategoria1 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        cmbCategoria = new javax.swing.JComboBox<>();
        txtCosto = new javax.swing.JTextField();
        cmbProveedor = new javax.swing.JComboBox<>();
        lblStockMin2 = new javax.swing.JLabel();
        lblPrecio1 = new javax.swing.JLabel();
        lblStockMin3 = new javax.swing.JLabel();
        txtPrecioVenta = new javax.swing.JTextField();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlFondo.setBackground(new java.awt.Color(153, 153, 255));

        jTableInventario.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jTableInventario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Codigo", "Repuesto", "Categoria", "Proveedor", "Stock", "Minimo", "Costo", "Precio"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableInventario);

        pnlSuperior.setBackground(new java.awt.Color(204, 204, 255));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 2, 24)); // NOI18N
        jLabel1.setText("--- Entrada de Inventario ---------");

        lblCantidad.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblCantidad.setText("Cantidad");

        lblCostoUni.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblCostoUni.setText("Costo Unitario");

        lblReferencia.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblReferencia.setText("Referencia");

        lblSerieMotor.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblSerieMotor.setText("Serie Motor");

        txtCostoEntrada.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        txtSerieMotor.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        txtSerieMotor.addActionListener(this::txtSerieMotorActionPerformed);

        txtCantidadEntrada.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        txtCantidadEntrada.addActionListener(this::txtCantidadEntradaActionPerformed);

        txtReferencia.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        btnEntrada.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        btnEntrada.setText("Registrar Entrada");
        btnEntrada.addActionListener(this::btnEntradaActionPerformed);

        chkCritico.setBackground(new java.awt.Color(255, 255, 255));
        chkCritico.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        chkCritico.setText("Solo Stock Critico");
        chkCritico.addActionListener(this::chkCriticoActionPerformed);

        javax.swing.GroupLayout pnlSuperiorLayout = new javax.swing.GroupLayout(pnlSuperior);
        pnlSuperior.setLayout(pnlSuperiorLayout);
        pnlSuperiorLayout.setHorizontalGroup(
            pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSuperiorLayout.createSequentialGroup()
                .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSuperiorLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1))
                    .addGroup(pnlSuperiorLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlSuperiorLayout.createSequentialGroup()
                                .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblCostoUni, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(pnlSuperiorLayout.createSequentialGroup()
                                        .addGap(35, 35, 35)
                                        .addComponent(lblCantidad))
                                    .addComponent(lblSerieMotor, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtSerieMotor, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtCantidadEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtCostoEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(pnlSuperiorLayout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(chkCritico, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSuperiorLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41))
        );
        pnlSuperiorLayout.setVerticalGroup(
            pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSuperiorLayout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCantidad)
                    .addComponent(txtCantidadEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCostoEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCostoUni))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblReferencia)
                    .addComponent(txtReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSerieMotor)
                    .addComponent(txtSerieMotor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addComponent(chkCritico, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        lblTitulo.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        lblTitulo.setText("Inventario de Repuestos");

        pnlSuperior1.setBackground(new java.awt.Color(204, 204, 255));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 2, 24)); // NOI18N
        jLabel2.setText("----- Repuestos ------------------------------------");

        lblCodigo.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblCodigo.setText("Codigo");

        lblNombre.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblNombre.setText("Nombre");

        lblProveedor.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblProveedor.setText("Proveedor");

        lblCosto.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblCosto.setText("Costo L.");

        txtNombre.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        txtStockMin.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        txtStockActual.setBackground(new java.awt.Color(255, 255, 255));
        txtStockActual.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        txtStockActual.setText("0");
        txtStockActual.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(0, 0, 0), null, null));

        lblCategoria1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblCategoria1.setText("Categoria");

        txtCodigo.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        cmbCategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txtCosto.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        cmbProveedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblStockMin2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblStockMin2.setText("Stock Min.");

        lblPrecio1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblPrecio1.setText("Precio Venta L.");

        lblStockMin3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblStockMin3.setText("Stock Actual");

        txtPrecioVenta.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        txtPrecioVenta.addActionListener(this::txtPrecioVentaActionPerformed);

        btnNuevo.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        btnNuevo.setText("Nuevo");

        btnGuardar.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        btnGuardar.setText("Guardar");

        javax.swing.GroupLayout pnlSuperior1Layout = new javax.swing.GroupLayout(pnlSuperior1);
        pnlSuperior1.setLayout(pnlSuperior1Layout);
        pnlSuperior1Layout.setHorizontalGroup(
            pnlSuperior1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSuperior1Layout.createSequentialGroup()
                .addGroup(pnlSuperior1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlSuperior1Layout.createSequentialGroup()
                        .addGroup(pnlSuperior1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlSuperior1Layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(lblNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlSuperior1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblCodigo))
                            .addComponent(lblCosto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlSuperior1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlSuperior1Layout.createSequentialGroup()
                                .addComponent(txtCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblPrecio1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(pnlSuperior1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtStockActual, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtPrecioVenta)))
                            .addGroup(pnlSuperior1Layout.createSequentialGroup()
                                .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblCategoria1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtNombre)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlSuperior1Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(pnlSuperior1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblStockMin2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlSuperior1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlSuperior1Layout.createSequentialGroup()
                                .addComponent(txtStockMin, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblStockMin3, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cmbProveedor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(pnlSuperior1Layout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(53, 53, 53)
                        .addComponent(btnGuardar)))
                .addGap(472, 472, 472))
            .addGroup(pnlSuperior1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlSuperior1Layout.setVerticalGroup(
            pnlSuperior1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSuperior1Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSuperior1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbCategoria, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlSuperior1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblCodigo)
                        .addComponent(lblCategoria1)
                        .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSuperior1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNombre))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSuperior1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblProveedor)
                    .addComponent(cmbProveedor))
                .addGap(3, 3, 3)
                .addGroup(pnlSuperior1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCosto)
                    .addComponent(lblPrecio1)
                    .addComponent(txtPrecioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(pnlSuperior1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblStockMin2)
                    .addGroup(pnlSuperior1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtStockMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblStockMin3)
                        .addComponent(txtStockActual)))
                .addGap(18, 18, 18)
                .addGroup(pnlSuperior1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo)
                    .addComponent(btnGuardar))
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlFondoLayout = new javax.swing.GroupLayout(pnlFondo);
        pnlFondo.setLayout(pnlFondoLayout);
        pnlFondoLayout.setHorizontalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFondoLayout.createSequentialGroup()
                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(pnlFondoLayout.createSequentialGroup()
                        .addGap(270, 270, 270)
                        .addComponent(lblTitulo)
                        .addGap(189, 189, 189))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlFondoLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 829, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlFondoLayout.createSequentialGroup()
                                .addComponent(pnlSuperior1, javax.swing.GroupLayout.PREFERRED_SIZE, 489, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(pnlSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        pnlFondoLayout.setVerticalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFondoLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(lblTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlSuperior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlSuperior1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 879, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlFondo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 594, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlFondo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntradaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEntradaActionPerformed

    private void chkCriticoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCriticoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkCriticoActionPerformed

    private void txtCantidadEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantidadEntradaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantidadEntradaActionPerformed

    private void txtSerieMotorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSerieMotorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSerieMotorActionPerformed

    private void txtPrecioVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrecioVentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrecioVentaActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new FrmInventario().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEntrada;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JCheckBox chkCritico;
    private javax.swing.JComboBox<String> cmbCategoria;
    private javax.swing.JComboBox<String> cmbProveedor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableInventario;
    private javax.swing.JLabel lblCantidad;
    private javax.swing.JLabel lblCategoria1;
    private javax.swing.JLabel lblCodigo;
    private javax.swing.JLabel lblCosto;
    private javax.swing.JLabel lblCostoUni;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblPrecio1;
    private javax.swing.JLabel lblProveedor;
    private javax.swing.JLabel lblReferencia;
    private javax.swing.JLabel lblSerieMotor;
    private javax.swing.JLabel lblStockMin2;
    private javax.swing.JLabel lblStockMin3;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel pnlFondo;
    private javax.swing.JPanel pnlSuperior;
    private javax.swing.JPanel pnlSuperior1;
    private javax.swing.JTextField txtCantidadEntrada;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtCosto;
    private javax.swing.JTextField txtCostoEntrada;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPrecioVenta;
    private javax.swing.JTextField txtReferencia;
    private javax.swing.JTextField txtSerieMotor;
    private javax.swing.JLabel txtStockActual;
    private javax.swing.JTextField txtStockMin;
    // End of variables declaration//GEN-END:variables
}
