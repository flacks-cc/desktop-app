package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import modelo.FacturaModel;
import vista.FacturaView;
import vista.MenuView;

public class FacturaController implements ActionListener {

    private FacturaModel FacturaModel;
    private FacturaView FacturaView;

    public FacturaController(FacturaModel FacturaModel, FacturaView FacturaView) {
        this.FacturaModel = FacturaModel;
        this.FacturaView = FacturaView;
        this.FacturaView.btnGuardar.addActionListener(this);
        this.FacturaView.btnModificar.addActionListener(this);
        this.FacturaView.btnEliminar.addActionListener(this);
        this.FacturaView.btnLimpiar.addActionListener(this);
        this.FacturaView.btnVolver.addActionListener(this);

        this.FacturaView.tblFactura.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = this.FacturaView.tblFactura.getSelectedRow();
                if (selectedRow != -1) {

                    int id = (int) this.FacturaView.tblFactura.getValueAt(selectedRow, 0);
                    String fecha = ((LocalDate) this.FacturaView.tblFactura.getValueAt(selectedRow, 1)).toString();
                    String hora = ((LocalTime) this.FacturaView.tblFactura.getValueAt(selectedRow, 2)).toString();
                    Double total = (Double) this.FacturaView.tblFactura.getValueAt(selectedRow, 3);
                    String estado = (String) this.FacturaView.tblFactura.getValueAt(selectedRow, 4);
                    String empleado = (String) this.FacturaView.tblFactura.getValueAt(selectedRow, 5);
                    String servicio = (String) this.FacturaView.tblFactura.getValueAt(selectedRow, 6);

                    this.FacturaView.txtId.setText(String.valueOf(id));
                    this.FacturaView.txtFecha.setText(fecha);
                    this.FacturaView.txtHora.setText(hora);
                    this.FacturaView.txtTotal.setText(String.valueOf(total));
                    this.FacturaView.txtEstado.setText(estado);
                    this.FacturaView.txtEmpleado.setText(empleado);
                    this.FacturaView.txtServicio.setText(servicio);
                }
            }
        });
        iniciarVentana();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == FacturaView.btnGuardar) {
            if (validarCampos(true)) {
                FacturaModel.setFecha(LocalDate.parse(FacturaView.txtFecha.getText().trim()));
                FacturaModel.setHora(LocalTime.parse(FacturaView.txtHora.getText().trim()));
                FacturaModel.setTotal(Double.parseDouble(FacturaView.txtTotal.getText().trim()));
                FacturaModel.setEstado(FacturaView.txtEstado.getText().trim());
                FacturaModel.setServicio(FacturaView.txtServicio.getText().trim());
                FacturaModel.setEmpleado(FacturaView.txtEmpleado.getText().trim());

                if (!FacturaModel.existeServicio()) {
                    FacturaView.txtServicio.setText(FacturaModel.getServicio().trim());
                    JOptionPane.showMessageDialog(null, "No existe un servicio con ese nombre");
                } else if (!FacturaModel.existeCorreo()) {
                    FacturaView.txtEmpleado.setText(FacturaModel.getEmpleado().trim());
                    JOptionPane.showMessageDialog(null, "No existe un usuario con este correo");
                } else if (FacturaModel.registrar()) {
                    JOptionPane.showMessageDialog(null, "Registro guardado");
                    actualizarTabla();
                    limpiar();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al guardar");
                    limpiar();
                }
            }
        }

        if (e.getSource() == FacturaView.btnModificar) {
            if (validarCampos(false)) {
                FacturaModel.setId_factura(Integer.parseInt(FacturaView.txtId.getText().trim()));
                FacturaModel.setFecha(LocalDate.parse(FacturaView.txtFecha.getText().trim()));
                FacturaModel.setHora(LocalTime.parse(FacturaView.txtHora.getText().trim()));
                FacturaModel.setTotal(Double.parseDouble(FacturaView.txtTotal.getText().trim()));
                FacturaModel.setEstado(FacturaView.txtEstado.getText().trim());
                FacturaModel.setServicio(FacturaView.txtServicio.getText().trim());
                FacturaModel.setEmpleado(FacturaView.txtEmpleado.getText().trim());

                if (!FacturaModel.existeUsuario()) {
                    FacturaView.txtEmpleado.setText(FacturaModel.getEmpleado().trim());
                    JOptionPane.showMessageDialog(null, "Para modificar, debes ingresar un nombre\nNo existe un usuario con ese nombre");
                } else if (FacturaModel.modificar()) {
                    JOptionPane.showMessageDialog(null, "Registro modificado");
                    actualizarTabla();
                    limpiar();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al modificar");
                    limpiar();
                }
            }
        }

        if (e.getSource() == FacturaView.btnEliminar) {
            FacturaModel.setId_factura(Integer.parseInt(FacturaView.txtId.getText().trim()));

            if (FacturaModel.eliminar()) {
                JOptionPane.showMessageDialog(null, "Registro eliminado");
                actualizarTabla();
                limpiar();
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar");
                limpiar();
            }
        }

        if (e.getSource() == FacturaView.btnLimpiar) {
            limpiar();
        }

        if (e.getSource() == FacturaView.btnVolver) {

            MenuView MenuView = new MenuView();

            MenuController msc = new MenuController(MenuView);
            MenuView.setVisible(true);
            FacturaView.dispose();

        }
    }

    private void limpiar() {
        if (FacturaView != null) {
            FacturaView.txtId.setText(null);
            FacturaView.txtFecha.setText("aaaa-mm-dd");
            FacturaView.txtHora.setText("hh:mm");
            FacturaView.txtTotal.setText(null);
            FacturaView.txtEstado.setText(null);
            FacturaView.txtEmpleado.setText(null);
            FacturaView.txtServicio.setText(null);   
            FacturaView.tblFactura.clearSelection();
        }
    }

    public void actualizarTabla() {
        DefaultTableModel tabla = new DefaultTableModel();
        tabla.addColumn("Id");
        tabla.addColumn("Fecha");
        tabla.addColumn("Hora");
        tabla.addColumn("Total");
        tabla.addColumn("Estado");
        tabla.addColumn("Empleado");
        tabla.addColumn("Servicio");

        List<FacturaModel> lista = FacturaModel.listar();

        for (FacturaModel u : lista) {
            Object[] fila = new Object[7];
            fila[0] = u.getId_factura();
            fila[1] = u.getFecha();
            fila[2] = u.getHora();
            fila[3] = u.getTotal();
            fila[4] = u.getEstado();
            fila[5] = u.getEmpleado();
            fila[6] = u.getServicio();
            tabla.addRow(fila);
        }
        FacturaView.tblFactura.setModel(tabla);
        /*TableColumn column1 = this.FacturaView.tblFactura.getColumnModel().getColumn(0);
        column1.setPreferredWidth(2);*/
    }

    private boolean validarCampos(boolean value) {
        if (value) {
            // Validación para agregar factura
            String fecha = FacturaView.txtFecha.getText().trim();
            String hora = FacturaView.txtHora.getText().trim();
            double total = 0;

            try {
                total = Double.parseDouble(FacturaView.txtTotal.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ingrese un valor numérico válido para el precio");
                return false;
            }
            String estado = FacturaView.txtEstado.getText().trim();
            String servicio = FacturaView.txtServicio.getText().trim();
            String empleado = FacturaView.txtEmpleado.getText().trim().toLowerCase();

            if (fecha.isEmpty() || hora.isEmpty() || total == 0 || estado.isEmpty() || servicio.isEmpty() || empleado.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
                return false;
            }

            // Validación de formato de fecha
            Pattern patternFecha = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
            Matcher matcherFecha = patternFecha.matcher(fecha);
            if (!matcherFecha.matches()) {
                JOptionPane.showMessageDialog(null, "El formato de la fecha es inválido\nDebe ser aaaa-mm-dd");
                return false;
            }

            // Validación de formato de hora
            Pattern patternHora = Pattern.compile("^\\d{2}:\\d{2}$");
            Matcher matcherHora = patternHora.matcher(hora);
            if (!matcherHora.matches()) {
                JOptionPane.showMessageDialog(null, "El formato de la hora es inválido\nDebe ser hh:mm");
                return false;
            }

            // Validación de correo electrónico
            Pattern patternCorreo = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
            Matcher matcherCorreo = patternCorreo.matcher(empleado);
            if (!matcherCorreo.matches()) {
                JOptionPane.showMessageDialog(null, "Para guardar, debes ingresar un correo electrónico\nIngresa un correo electrónico válido");
                return false;
            }

            //Validación para el monto total
            if (total < 0) {
                JOptionPane.showMessageDialog(null, "El precio no puede ser negativo");
                return false;
            }
            return true;

        } else {
            // Validación para modificar factura
            String fecha = FacturaView.txtFecha.getText().trim();
            String hora = FacturaView.txtHora.getText().trim();
            double total = 0;

            try {
                total = Double.parseDouble(FacturaView.txtTotal.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ingrese un valor numérico válido para el precio");
                return false;
            }
            String estado = FacturaView.txtEstado.getText().trim();
            String servicio = FacturaView.txtServicio.getText().trim();
            String empleado = FacturaView.txtEmpleado.getText().trim();

            if (fecha.isEmpty() || hora.isEmpty() || total == 0 || estado.isEmpty() || servicio.isEmpty() || empleado.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
                return false;
            } else {

                // Validación de formato de fecha
                Pattern patternFecha = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
                Matcher matcherFecha = patternFecha.matcher(fecha);
                if (!matcherFecha.matches()) {
                    JOptionPane.showMessageDialog(null, "El formato de la fecha es inválido\nDebe ser aaaa-mm-dd");
                    return false;
                }

                // Validación de formato de hora
                Pattern patternHora = Pattern.compile("^\\d{2}:\\d{2}$");
                Matcher matcherHora = patternHora.matcher(hora);
                if (!matcherHora.matches()) {
                    JOptionPane.showMessageDialog(null, "El formato de la hora es inválido\nDebe ser hh:mm");
                    return false;
                }

                //Validación para el monto total
                if (total < 0) {
                    JOptionPane.showMessageDialog(null, "El precio no puede ser negativo");
                    return false;
                }
                return true;
            }
        }
    }

    public void iniciarVentana() {
        FacturaView.setTitle("Facturas");
        FacturaView.setLocationRelativeTo(null);
        actualizarTabla();
    }
}
