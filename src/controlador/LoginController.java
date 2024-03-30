package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import modelo.UsuarioModel;
import vista.LoginView;
import vista.MenuView;
import vista.RegistroView;

public class LoginController implements ActionListener {

    private UsuarioModel LoginModel;
    private LoginView LoginView;

    public LoginController(UsuarioModel LoginModel, LoginView LoginView) {
        this.LoginModel = LoginModel;
        this.LoginView = LoginView;
        this.LoginView.btnIngresar.addActionListener(this);
        this.LoginView.btnRegistrar.addActionListener(this);
        iniciarVentana();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == LoginView.btnIngresar) {
            if (!validarCampos()) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
                return;
            }
            LoginModel.setCorreo(LoginView.txtCorreo.getText().trim().toLowerCase());
            LoginModel.setContrasena(LoginView.txtContrasena.getText().trim());

            if (LoginModel.existeUsuario()) {
                LoginView.txtCorreo.setText(LoginModel.getCorreo().trim());
                LoginView.txtContrasena.setText(LoginModel.getContrasena().trim());

                MenuView MenuView = new MenuView();

                MenuController msc = new MenuController(MenuView);
                MenuView.setVisible(true);
                LoginView.dispose();

            } else {
                JOptionPane.showMessageDialog(null, "Los datos son incorrectos");
            }
        }

        if (e.getSource() == LoginView.btnRegistrar) {
            limpiar();

            UsuarioModel RegistroModel = new UsuarioModel();
            RegistroView RegistroView = new RegistroView();

            RegistroController rsc = new RegistroController(RegistroModel, RegistroView);
            RegistroView.setVisible(true);
            LoginView.dispose();
        }
    }

    private void limpiar() {
        if (LoginView != null) {
            LoginView.txtCorreo.setText(null);
            LoginView.txtContrasena.setText(null);
        }
    }

    private boolean validarCampos() {
        if (LoginView.txtCorreo.getText().trim().isEmpty()
                || LoginView.txtContrasena.getText().trim().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public void iniciarVentana() {
        LoginView.setTitle("Inicio de sesión");
        LoginView.setLocationRelativeTo(null);
    }
}
