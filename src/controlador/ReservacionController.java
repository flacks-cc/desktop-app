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
import modelo.ReservacionModel;
import vista.MenuView;
import vista.ReservacionView;

public class ReservacionController implements ActionListener {

    private ReservacionModel ReservacionModel;
    private ReservacionView ReservacionView;

    public ReservacionController(ReservacionModel ReservacionModel, ReservacionView ReservacionView) {
        this.ReservacionModel = ReservacionModel;
        this.ReservacionView = ReservacionView;
        this.ReservacionView.btnGuardar.addActionListener(this);
        this.ReservacionView.btnModificar.addActionListener(this);
        this.ReservacionView.btnEliminar.addActionListener(this);
        this.ReservacionView.btnLimpiar.addActionListener(this);
        this.ReservacionView.btnVolver.addActionListener(this);

        this.ReservacionView.tblReservacion.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = this.ReservacionView.tblReservacion.getSelectedRow();
                if (selectedRow != -1) {

                    int id = (int) this.ReservacionView.tblReservacion.getValueAt(selectedRow, 0);
                    String fecha = ((LocalDate) this.ReservacionView.tblReservacion.getValueAt(selectedRow, 1)).toString();
                    String hora = ((LocalTime) this.ReservacionView.tblReservacion.getValueAt(selectedRow, 2)).toString();
                    String nota = (String) this.ReservacionView.tblReservacion.getValueAt(selectedRow, 3);
                    String cliente = (String) this.ReservacionView.tblReservacion.getValueAt(selectedRow, 4);
                    String servicio = (String) this.ReservacionView.tblReservacion.getValueAt(selectedRow, 5);

                    this.ReservacionView.txtId.setText(String.valueOf(id));
                    this.ReservacionView.txtFecha.setText(fecha);
                    this.ReservacionView.txtHora.setText(hora);
                    this.ReservacionView.txtNota.setText(nota);
                    this.ReservacionView.txtCliente.setText(cliente);
                    this.ReservacionView.txtServicio.setText(servicio);
                }
            }
        });
        iniciarVentana();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ReservacionView.btnGuardar) {
            if (validarCampos(true)) {
                ReservacionModel.setFecha(LocalDate.parse(ReservacionView.txtFecha.getText().trim()));
                ReservacionModel.setHora(LocalTime.parse(ReservacionView.txtHora.getText().trim()));
                ReservacionModel.setNota(ReservacionView.txtNota.getText().trim());
                ReservacionModel.setCliente(ReservacionView.txtCliente.getText().trim());
                ReservacionModel.setServicio(ReservacionView.txtServicio.getText().trim());

                if (!ReservacionModel.existeServicio()) {
                    ReservacionView.txtServicio.setText(ReservacionModel.getServicio().trim());
                    JOptionPane.showMessageDialog(null, "No existe un servicio con ese nombre");
                } else if (!ReservacionModel.existeCorreo()) {
                    ReservacionView.txtCliente.setText(ReservacionModel.getCliente().trim());
                    JOptionPane.showMessageDialog(null, "No existe un usuario con este correo");
                } else if (ReservacionModel.registrar()) {
                    JOptionPane.showMessageDialog(null, "Registro guardado");
                    actualizarTabla();
                    limpiar();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al guardar");
                    limpiar();
                }
            }
        }

        if (e.getSource() == ReservacionView.btnModificar) {
            if (validarCampos(false)) {
                ReservacionModel.setId_reservacion(Integer.parseInt(ReservacionView.txtId.getText().trim()));
                ReservacionModel.setFecha(LocalDate.parse(ReservacionView.txtFecha.getText().trim()));
                ReservacionModel.setHora(LocalTime.parse(ReservacionView.txtHora.getText().trim()));
                ReservacionModel.setNota(ReservacionView.txtNota.getText().trim());
                ReservacionModel.setServicio(ReservacionView.txtServicio.getText().trim());
                ReservacionModel.setCliente(ReservacionView.txtCliente.getText().trim());

                if (!ReservacionModel.existeUsuario()) {
                    ReservacionView.txtCliente.setText(ReservacionModel.getCliente().trim());
                    JOptionPane.showMessageDialog(null, "Para modificar, debes ingresar un nombre\nNo existe un usuario con ese nombre");
                } else if (ReservacionModel.modificar()) {
                    JOptionPane.showMessageDialog(null, "Registro modificado");
                    actualizarTabla();
                    limpiar();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al modificar");
                    limpiar();
                }
            }
        }

        if (e.getSource() == ReservacionView.btnEliminar) {
            ReservacionModel.setId_reservacion(Integer.parseInt(ReservacionView.txtId.getText().trim()));

            if (ReservacionModel.eliminar()) {
                JOptionPane.showMessageDialog(null, "Registro eliminado");
                actualizarTabla();
                limpiar();
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar");
                limpiar();
            }
        }

        if (e.getSource() == ReservacionView.btnLimpiar) {
            limpiar();
        }

        if (e.getSource() == ReservacionView.btnVolver) {

            MenuView MenuView = new MenuView();

            MenuController msc = new MenuController(MenuView);
            MenuView.setVisible(true);
            ReservacionView.dispose();

        }
    }

    private void limpiar() {
        if (ReservacionView != null) {
            ReservacionView.txtId.setText(null);
            ReservacionView.txtFecha.setText("aaaa-mm-dd");
            ReservacionView.txtHora.setText("hh:mm");
            ReservacionView.txtNota.setText(null);
            ReservacionView.txtCliente.setText(null);
            ReservacionView.txtServicio.setText(null);
            ReservacionView.tblReservacion.clearSelection();
        }
    }

    public void actualizarTabla() {
        DefaultTableModel tabla = new DefaultTableModel();
        tabla.addColumn("Id");
        tabla.addColumn("Fecha");
        tabla.addColumn("Hora");
        tabla.addColumn("Nota");
        tabla.addColumn("Cliente");
        tabla.addColumn("Servicio");

        List<ReservacionModel> lista = ReservacionModel.listar();

        for (ReservacionModel u : lista) {
            Object[] fila = new Object[6];
            fila[0] = u.getId_reservacion();
            fila[1] = u.getFecha();
            fila[2] = u.getHora();
            fila[3] = u.getNota();
            fila[4] = u.getCliente();
            fila[5] = u.getServicio();
            tabla.addRow(fila);
        }
        ReservacionView.tblReservacion.setModel(tabla);
        TableColumn column1 = this.ReservacionView.tblReservacion.getColumnModel().getColumn(0);
        column1.setPreferredWidth(2);
    }

    private boolean validarCampos(boolean value) {
        if (value) {
            // Validación para agregar reservación
            String fecha = ReservacionView.txtFecha.getText().trim();
            String hora = ReservacionView.txtHora.getText().trim();
            String cliente = ReservacionView.txtCliente.getText().trim().toLowerCase();
            String servicio = ReservacionView.txtServicio.getText().trim();

            if (fecha.isEmpty() || hora.isEmpty() || cliente.isEmpty() || servicio.isEmpty()) {
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
            Matcher matcherCorreo = patternCorreo.matcher(cliente);
            if (!matcherCorreo.matches()) {
                JOptionPane.showMessageDialog(null, "Para guardar, debes ingresar un correo electronico\nIngresa un correo electronico valido");
                return false;
            }
            return true;
        } else {
            // Validación para modificar reservación
            String fecha = ReservacionView.txtFecha.getText().trim();
            String hora = ReservacionView.txtHora.getText().trim();
            String cliente = ReservacionView.txtCliente.getText().trim();
            String servicio = ReservacionView.txtServicio.getText().trim();

            if (fecha.isEmpty() || hora.isEmpty() || cliente.isEmpty() || servicio.isEmpty()) {
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
                return true;
            }
        }
    }

    public void iniciarVentana() {
        ReservacionView.setTitle("Servicios");
        ReservacionView.setLocationRelativeTo(null);
        actualizarTabla();
    }
}
