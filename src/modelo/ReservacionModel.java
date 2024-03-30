package modelo;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

public class ReservacionModel extends Conexion {

    private int id_reservacion;
    private LocalDate fecha;
    private LocalTime hora;
    private String nota;
    private String cliente;
    private String servicio;

    public ReservacionModel() {
        this.fecha = null;
        this.hora = null;
        this.nota = null;
        this.cliente = null;
        this.servicio = null;
    }

    public ReservacionModel(int id_reservacion, LocalDate fecha, LocalTime hora, String nota, String correo, String nombre_servicio) {
        this.id_reservacion = id_reservacion;
        this.fecha = fecha;
        this.hora = hora;
        this.nota = nota;
        this.cliente = correo;
        this.servicio = nombre_servicio;
    }

    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;

    public boolean registrar() {
        PreparedStatement ps = null;
        Connection con = getConexion();

        String sql = "INSERT INTO reservacion (fecha, hora, nota, id_usuario, id_servicio) "
                + "VALUES (?, ?, ?, (SELECT id_usuario FROM usuario WHERE correo = ?), "
                + "(SELECT id_servicio FROM servicio WHERE nombre_servicio = ?))";

        try {
            ps = con.prepareStatement(sql);
            ps.setDate(1, java.sql.Date.valueOf(this.getFecha()));
            ps.setTime(2, java.sql.Time.valueOf(this.getHora()));
            ps.setString(3, this.getNota());
            ps.setString(4, this.getCliente());
            ps.setString(5, this.getServicio());

            ps.execute();
            return true;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                con.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }

    public boolean modificar() {
        PreparedStatement ps = null;
        Connection con = getConexion();

        String sql = "UPDATE reservacion SET fecha=?, hora=?, nota=?, "
                + "id_servicio=(SELECT id_servicio FROM servicio WHERE nombre_servicio=?) WHERE id_reservacion=?";

        try {
            ps = con.prepareStatement(sql);
            ps.setDate(1, java.sql.Date.valueOf(this.getFecha()));
            ps.setTime(2, java.sql.Time.valueOf(this.getHora()));
            ps.setString(3, this.getNota());
            ps.setString(4, this.getServicio());
            ps.setInt(5, this.getId_reservacion());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                con.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }

    public boolean eliminar() {
        PreparedStatement ps = null;
        Connection con = getConexion();

        String sql = "DELETE FROM reservacion WHERE id_reservacion=?";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, this.getId_reservacion());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                con.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }

    public boolean existeUsuario() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConexion();

        String sql = "SELECT * FROM usuario WHERE nombre_usuario=?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, this.getCliente());
            rs = ps.executeQuery();

            if (rs.next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                con.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }
    
    public boolean existeCorreo() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConexion();

        String sql = "SELECT * FROM usuario WHERE correo=?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, this.getCliente());
            rs = ps.executeQuery();

            if (rs.next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                con.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }
    
    public boolean existeServicio() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConexion();

        String sql = "SELECT * FROM servicio WHERE nombre_servicio=?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, this.getServicio());
            rs = ps.executeQuery();

            if (rs.next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                con.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }
    
    public List<ReservacionModel> listar() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConexion();

        String sql = "SELECT r.id_reservacion, r.fecha, r.hora, r.nota, u.nombre_usuario, s.nombre_servicio "
                + "FROM reservacion r "
                + "JOIN usuario u ON r.id_usuario = u.id_usuario "
                + "JOIN servicio s ON r.id_servicio = s.id_servicio";
        List<ReservacionModel> reservationList = new ArrayList<>();

        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                ReservacionModel reservation = new ReservacionModel();
                reservation.setId_reservacion(rs.getInt("id_reservacion"));
                reservation.setFecha(rs.getDate("fecha").toLocalDate());
                reservation.setHora(rs.getTime("hora").toLocalTime());
                reservation.setNota(rs.getString("nota"));
                reservation.setCliente(rs.getString("nombre_usuario"));
                reservation.setServicio(rs.getString("nombre_servicio"));

                reservationList.add(reservation);
            }

            return reservationList;
        } catch (SQLException e) {
            System.err.println(e);
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                con.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }

    public int getId_reservacion() {
        return id_reservacion;
    }

    public void setId_reservacion(int id_reservacion) {
        this.id_reservacion = id_reservacion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

}
