package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import modelo.ServicioModel;
import vista.MenuView;
import vista.ServicioView;

public class ServicioController implements ActionListener {

    private ServicioModel ServicioModel;
    private ServicioView ServicioView;

    public ServicioController(ServicioModel ServicioModel, ServicioView ServicioView) {
        this.ServicioModel = ServicioModel;
        this.ServicioView = ServicioView;
        this.ServicioView.btnGuardar.addActionListener(this);
        this.ServicioView.btnModificar.addActionListener(this);
        this.ServicioView.btnEliminar.addActionListener(this);
        this.ServicioView.btnLimpiar.addActionListener(this);
        this.ServicioView.btnVolver.addActionListener(this);

        this.ServicioView.tblServicio.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = this.ServicioView.tblServicio.getSelectedRow();
                if (selectedRow != -1) {

                    int id = (int) this.ServicioView.tblServicio.getValueAt(selectedRow, 0);
                    String nombre = (String) this.ServicioView.tblServicio.getValueAt(selectedRow, 1);
                    String descripcion = (String) this.ServicioView.tblServicio.getValueAt(selectedRow, 2);
                    Double precio = (Double) this.ServicioView.tblServicio.getValueAt(selectedRow, 3);

                    this.ServicioView.txtId.setText(String.valueOf(id));
                    this.ServicioView.txtNombre.setText(nombre);
                    this.ServicioView.txtDescripcion.setText(descripcion);
                    this.ServicioView.txtPrecio.setText(String.valueOf(precio));
                }
            }
        });
        iniciarVentana();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ServicioView.btnGuardar) {
            if (validarCampos()) {
                ServicioModel.setNombre_servicio(ServicioView.txtNombre.getText().trim());
                ServicioModel.setDescripcion(ServicioView.txtDescripcion.getText().trim());
                ServicioModel.setPrecio(Double.parseDouble(ServicioView.txtPrecio.getText().trim()));
                if (ServicioModel.registrar()) {
                    JOptionPane.showMessageDialog(null, "Registro guardado");
                    actualizarTabla();
                    limpiar();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al guardar");
                    limpiar();
                }
            }
        }

        if (e.getSource() == ServicioView.btnModificar) {
            if (validarCampos()) {
                ServicioModel.setId_servicio(Integer.parseInt(ServicioView.txtId.getText().trim()));
                ServicioModel.setNombre_servicio(ServicioView.txtNombre.getText().trim());
                ServicioModel.setDescripcion(ServicioView.txtDescripcion.getText().trim());
                ServicioModel.setPrecio(Double.parseDouble(ServicioView.txtPrecio.getText().trim()));

                if (ServicioModel.modificar()) {
                    JOptionPane.showMessageDialog(null, "Registro modificado");
                    actualizarTabla();
                    limpiar();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al modificar");
                    limpiar();
                }
            }
        }

        if (e.getSource() == ServicioView.btnEliminar) {
            ServicioModel.setId_servicio(Integer.parseInt(ServicioView.txtId.getText().trim()));

            if (ServicioModel.eliminar()) {
                JOptionPane.showMessageDialog(null, "Registro eliminado");
                actualizarTabla();
                limpiar();
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar");
                limpiar();
            }
        }

        if (e.getSource() == ServicioView.btnLimpiar) {
            limpiar();
        }

        if (e.getSource() == ServicioView.btnVolver) {

            MenuView MenuView = new MenuView();

            MenuController msc = new MenuController(MenuView);
            MenuView.setVisible(true);
            ServicioView.dispose();

        }
    }

    private void limpiar() {
        if (ServicioView != null) {
            ServicioView.txtId.setText(null);
            ServicioView.txtNombre.setText(null);
            ServicioView.txtDescripcion.setText(null);
            ServicioView.txtPrecio.setText(null);
            ServicioView.tblServicio.clearSelection();
        }
    }

    public void actualizarTabla() {
        DefaultTableModel tabla = new DefaultTableModel();
        tabla.addColumn("Id");
        tabla.addColumn("Nombre");
        tabla.addColumn("Descripcion");
        tabla.addColumn("Precio");

        List<ServicioModel> lista = ServicioModel.listar();

        for (ServicioModel u : lista) {
            Object[] fila = new Object[4];
            fila[0] = u.getId_servicio();
            fila[1] = u.getNombre_servicio();
            fila[2] = u.getDescripcion();
            fila[3] = u.getPrecio();
            tabla.addRow(fila);
        }
        ServicioView.tblServicio.setModel(tabla);
        TableColumn column1 = this.ServicioView.tblServicio.getColumnModel().getColumn(0);
        column1.setPreferredWidth(0);
        TableColumn column4 = this.ServicioView.tblServicio.getColumnModel().getColumn(3);
        column4.setPreferredWidth(0);
    }

    private boolean validarCampos() {
        boolean validacion = true;

        if (ServicioView.txtNombre.getText().isEmpty() || ServicioView.txtDescripcion.getText().isEmpty() || ServicioView.txtPrecio.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            validacion = false;
        } else {
            try {
                double precio = Double.parseDouble(ServicioView.txtPrecio.getText().trim());
                if (precio < 0) {
                    JOptionPane.showMessageDialog(null, "El precio no puede ser negativo");
                    validacion = false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ingrese un valor numérico válido para el precio");
                validacion = false;
            }
        }
        return validacion;
    }

    public void iniciarVentana() {
        ServicioView.setTitle("Servicios");
        ServicioView.setLocationRelativeTo(null);
        actualizarTabla();
    }
}
