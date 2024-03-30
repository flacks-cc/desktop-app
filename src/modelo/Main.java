package modelo;

import controlador.LoginController;
import vista.LoginView;

public class Main {

    public static void main(String[] args) {

        UsuarioModel LoginModel = new UsuarioModel();
        LoginView LoginView = new LoginView();
        
        LoginController lsc = new LoginController(LoginModel, LoginView);
        LoginView.setVisible(true);

    }
}