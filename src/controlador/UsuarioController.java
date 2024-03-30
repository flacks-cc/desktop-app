package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import modelo.UsuarioModel;
import vista.MenuView;
import vista.UsuarioView;

public class UsuarioController implements ActionListener {

    private UsuarioModel UsuarioModel;
    private UsuarioView UsuarioView;

    public UsuarioController(UsuarioModel UsuarioModel, UsuarioView UsuarioView) {
        this.UsuarioModel = UsuarioModel;
        this.UsuarioView = UsuarioView;
        this.UsuarioView.btnGuardar.addActionListener(this);
        this.UsuarioView.btnModificar.addActionListener(this);
        this.UsuarioView.btnEliminar.addActionListener(this);
        this.UsuarioView.btnLimpiar.addActionListener(this);
        this.UsuarioView.btnVolver.addActionListener(this);

        this.UsuarioView.tblUsuario.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = this.UsuarioView.tblUsuario.getSelectedRow();
                if (selectedRow != -1) {

                    int id = (int) this.UsuarioView.tblUsuario.getValueAt(selectedRow, 0);
                    String nombre_usuario = (String) this.UsuarioView.tblUsuario.getValueAt(selectedRow, 1);
                    String correo = (String) this.UsuarioView.tblUsuario.getValueAt(selectedRow, 2);

                    this.UsuarioView.txtId.setText(String.valueOf(id));
                    this.UsuarioView.txtNombre.setText(nombre_usuario);
                    this.UsuarioView.txtCorreo.setText(correo);

                }
            }
        });
        iniciarVentana();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == UsuarioView.btnGuardar) {
            if (validarCampos(true)) {
                UsuarioModel.setNombre_usuario(UsuarioView.txtNombre.getText().trim());
                UsuarioModel.setCorreo(UsuarioView.txtCorreo.getText().trim().toLowerCase());
                UsuarioModel.setContrasena(UsuarioView.txtContrasena.getText().trim());

                if (UsuarioModel.existeCorreo()) {
                    UsuarioView.txtCorreo.setText(UsuarioModel.getCorreo().trim());
                    JOptionPane.showMessageDialog(null, "Ya existe un usuario con este correo");
                } else if (UsuarioModel.registrar()) {
                    JOptionPane.showMessageDialog(null, "Registro guardado");
                    actualizarTabla();
                    limpiar();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al guardar");
                    limpiar();
                }
            }
        }

        if (e.getSource() == UsuarioView.btnModificar) {
            if (validarCampos(false)) {
                UsuarioModel.setId_usuario(Integer.parseInt(UsuarioView.txtId.getText().trim()));
                UsuarioModel.setNombre_usuario(UsuarioView.txtNombre.getText().trim());
                UsuarioModel.setCorreo(UsuarioView.txtCorreo.getText().trim().toLowerCase());
                UsuarioModel.setContrasena(UsuarioView.txtContrasena.getText().trim());

                if (UsuarioModel.modificar()) {
                    JOptionPane.showMessageDialog(null, "Registro modificado");
                    actualizarTabla();
                    limpiar();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al modificar");
                    limpiar();
                }
            }
        }

        if (e.getSource() == UsuarioView.btnEliminar) {
            UsuarioModel.setId_usuario(Integer.parseInt(UsuarioView.txtId.getText().trim()));

            if (UsuarioModel.eliminar()) {
                JOptionPane.showMessageDialog(null, "Registro eliminado");
                actualizarTabla();
                limpiar();
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar");
                limpiar();
            }
        }

        if (e.getSource() == UsuarioView.btnLimpiar) {
            limpiar();
        }

        if (e.getSource() == UsuarioView.btnVolver) {

            MenuView MenuView = new MenuView();

            MenuController msc = new MenuController(MenuView);
            MenuView.setVisible(true);
            UsuarioView.dispose();

        }
    }

    private void limpiar() {
        if (UsuarioView != null) {
            UsuarioView.txtId.setText(null);
            UsuarioView.txtNombre.setText(null);
            UsuarioView.txtCorreo.setText(null);
            UsuarioView.txtContrasena.setText(null);
            UsuarioView.tblUsuario.clearSelection();
        }
    }

    public void actualizarTabla() {
        DefaultTableModel tabla = new DefaultTableModel();
        tabla.addColumn("Id");
        tabla.addColumn("Nombre");
        tabla.addColumn("Correo");

        List<UsuarioModel> lista = UsuarioModel.listar();

        for (UsuarioModel u : lista) {
            Object[] fila = new Object[3];
            fila[0] = u.getId_usuario();
            fila[1] = u.getNombre_usuario();
            fila[2] = u.getCorreo();
            tabla.addRow(fila);
        }
        this.UsuarioView.tblUsuario.setModel(tabla);
        TableColumn column = this.UsuarioView.tblUsuario.getColumnModel().getColumn(0);
        column.setPreferredWidth(0);
    }

    private boolean validarCampos(boolean value) {
        if (value) {
            if (UsuarioView.txtCorreo.getText().trim().isEmpty()
                    || UsuarioView.txtNombre.getText().trim().isEmpty()
                    || UsuarioView.txtContrasena.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
                return false;
            } else {
                String correo = UsuarioView.txtCorreo.getText().trim().toLowerCase();
                Pattern pattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
                Matcher matcher = pattern.matcher(correo);
                if (!matcher.matches()) {
                    JOptionPane.showMessageDialog(null, "El correo electrónico no es válido");
                    return false;
                }
                String contrasena = UsuarioView.txtContrasena.getText().trim();
                if (contrasena.length() < 8) {
                    JOptionPane.showMessageDialog(null, "La contraseña debe tener al menos 8 caracteres");
                    return false;
                } else if (!contrasena.matches(".*[a-z].*")) {
                    JOptionPane.showMessageDialog(null, "La contraseña debe tener al menos una letra minúscula");
                    return false;
                } else if (!contrasena.matches(".*[A-Z].*")) {
                    JOptionPane.showMessageDialog(null, "La contraseña debe tener al menos una letra mayúscula");
                    return false;
                }
                return true;
            }
        } else {
            if (UsuarioView.txtCorreo.getText().trim().isEmpty()
                    || UsuarioView.txtNombre.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
                return false;
            } else {
                String correo = UsuarioView.txtCorreo.getText().trim().toLowerCase();
                Pattern pattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
                Matcher matcher = pattern.matcher(correo);
                if (!matcher.matches()) {
                    JOptionPane.showMessageDialog(null, "El correo electrónico no es válido");
                    return false;
                }
                return true;
            }
        }
    }

    public void iniciarVentana() {
        UsuarioView.setTitle("Usuarios");
        UsuarioView.setLocationRelativeTo(null);
        actualizarTabla();
    }
}
