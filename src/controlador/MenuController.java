package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import modelo.FacturaModel;
import modelo.ReservacionModel;
import modelo.ServicioModel;
import modelo.UsuarioModel;
import vista.FacturaView;
import vista.LoginView;
import vista.MenuView;
import vista.ReservacionView;
import vista.ServicioView;
import vista.UsuarioView;

public class MenuController implements ActionListener {

    private MenuView MenuView;

    public MenuController(MenuView MenuView) {
        this.MenuView = MenuView;
        this.MenuView.btnUsuario.addActionListener(this);
        this.MenuView.btnServicio.addActionListener(this);
        this.MenuView.btnFactura.addActionListener(this);
        this.MenuView.btnReservacion.addActionListener(this);
        this.MenuView.btnSalir.addActionListener(this);
        iniciarVentana();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == MenuView.btnUsuario) {

            UsuarioModel UsuarioModel = new UsuarioModel();
            UsuarioView UsuarioView = new UsuarioView();

            UsuarioController usc = new UsuarioController(UsuarioModel, UsuarioView);
            UsuarioView.setVisible(true);
            MenuView.dispose();
        }

        if (e.getSource() == MenuView.btnServicio) {

            ServicioModel ServicioModel = new ServicioModel();
            ServicioView ServicioView = new ServicioView();

            ServicioController ssc = new ServicioController(ServicioModel, ServicioView);
            ServicioView.setVisible(true);
            MenuView.dispose();
        }

        if (e.getSource() == MenuView.btnFactura) {

            FacturaModel FacturaModel = new FacturaModel();
            FacturaView FacturaView = new FacturaView();

            FacturaController fsc = new FacturaController(FacturaModel, FacturaView);
            FacturaView.setVisible(true);
            MenuView.dispose();
        }
        
        if (e.getSource() == MenuView.btnReservacion) {

            ReservacionModel ReservacionModel = new ReservacionModel();
            ReservacionView ReservacionView = new ReservacionView();

            ReservacionController resc = new ReservacionController(ReservacionModel, ReservacionView);
            ReservacionView.setVisible(true);
            MenuView.dispose();
        }
        
        if (e.getSource() == MenuView.btnSalir) {

            UsuarioModel LoginModel = new UsuarioModel();
            LoginView LoginView = new LoginView();

            LoginController lsc = new LoginController(LoginModel, LoginView);
            LoginView.setVisible(true);
            MenuView.dispose();
        }
    }

    public void iniciarVentana() {
        MenuView.setTitle("Menu principal");
        MenuView.setLocationRelativeTo(null);
    }
}
