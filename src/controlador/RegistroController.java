package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import modelo.UsuarioModel;
import vista.LoginView;
import vista.RegistroView;

public class RegistroController implements ActionListener {

    private UsuarioModel RegistroModel;
    private RegistroView RegistroView;

    public RegistroController(UsuarioModel RegistroModel, RegistroView RegistroView) {
        this.RegistroModel = RegistroModel;
        this.RegistroView = RegistroView;
        RegistroView.btnVolver.addActionListener(this);
        RegistroView.btnGuardar.addActionListener(this);
        iniciarVentana();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == RegistroView.btnGuardar) {
            if (!validarCampos()) {
                return;
            }
            RegistroModel.setNombre_usuario(RegistroView.txtNombre.getText().trim());
            RegistroModel.setCorreo(RegistroView.txtCorreo.getText().trim().toLowerCase());
            RegistroModel.setContrasena(RegistroView.txtContrasena.getText().trim());
            if (RegistroModel.existeCorreo()) {
                JOptionPane.showMessageDialog(null, "Ya existe un usuario con este correo");
            } else if (RegistroModel.registrar()) {
                JOptionPane.showMessageDialog(null, "Registro guardado");
                limpiar();
            } else {
                JOptionPane.showMessageDialog(null, "Error al guardar");
            }
        }

        if (e.getSource() == RegistroView.btnVolver) {
            limpiar();

            LoginView LoginView = new LoginView();

            LoginController lsc = new LoginController(RegistroModel, LoginView);
            LoginView.setVisible(true);
            RegistroView.dispose();
        }
    }

    private void limpiar() {
        if (RegistroView != null) {
            RegistroView.txtNombre.setText(null);
            RegistroView.txtCorreo.setText(null);
            RegistroView.txtContrasena.setText(null);
            RegistroView.txtConfirmarContrasena.setText(null);
        }
    }

    private boolean validarCampos() {
        if (RegistroView.txtCorreo.getText().trim().isEmpty()
                || RegistroView.txtNombre.getText().trim().isEmpty()
                || RegistroView.txtContrasena.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            return false;
        } else {
            String correo = RegistroView.txtCorreo.getText().trim().toLowerCase();
            Pattern pattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
            Matcher matcher = pattern.matcher(correo);
            if (!matcher.matches()) {
                JOptionPane.showMessageDialog(null, "El correo electrónico no es válido");
                return false;
            }
            String contrasena = RegistroView.txtContrasena.getText().trim();
            if (contrasena.length() < 8) {
                JOptionPane.showMessageDialog(null, "La contraseña debe tener al menos 8 caracteres");
                return false;
            } else if (!contrasena.matches(".*[a-z].*")) {
                JOptionPane.showMessageDialog(null, "La contraseña debe tener al menos una letra minúscula");
                return false;
            } else if (!contrasena.matches(".*[A-Z].*")) {
                JOptionPane.showMessageDialog(null, "La contraseña debe tener al menos una letra mayúscula");
                return false;
            } else if (!contrasena.equals(RegistroView.txtConfirmarContrasena.getText().trim())) {
                JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden");
                return false;
            }
            return true;
        }
    }

    public void iniciarVentana() {
        RegistroView.setTitle("Registro");
        RegistroView.setLocationRelativeTo(null);

        for (ActionListener al : RegistroView.btnGuardar.getActionListeners()) {
            RegistroView.btnGuardar.removeActionListener(al);
        }
        for (ActionListener al : RegistroView.btnVolver.getActionListeners()) {
            RegistroView.btnVolver.removeActionListener(al);
        }

        RegistroView.btnVolver.addActionListener(this);
        RegistroView.btnGuardar.addActionListener(this);
    }
}
