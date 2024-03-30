package modelo;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

public class FacturaModel extends Conexion {

    private int id_factura;
    private LocalDate fecha;
    private LocalTime hora;
    private double total;
    private String estado;
    private String servicio;
    private String empleado;

    public FacturaModel() {
        this.fecha = null;
        this.hora = null;
        this.estado = null;
        this.servicio = null;
        this.empleado = null;
    }

    public FacturaModel(int id_factura, LocalDate fecha, LocalTime hora, double total, String estado, String servicio, String empleado) {
        this.id_factura = id_factura;
        this.fecha = fecha;
        this.hora = hora;
        this.total = total;
        this.estado = estado;
        this.servicio = servicio;
        this.empleado = empleado;
    }

    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;

    public boolean registrar() {
        PreparedStatement ps = null;
        Connection con = getConexion();

        String sql = "INSERT INTO factura (fecha, hora, total, estado, id_usuario, id_servicio ) "
                + "VALUES (?, ?, ?, ?, (SELECT id_usuario FROM usuario WHERE correo = ?), "
                + "(SELECT id_servicio FROM servicio WHERE nombre_servicio = ?))";

        try {
            ps = con.prepareStatement(sql);
            ps.setDate(1, java.sql.Date.valueOf(this.getFecha()));
            ps.setTime(2, java.sql.Time.valueOf(this.getHora()));
            ps.setDouble(3, this.getTotal());
            ps.setString(4, this.getEstado());
            ps.setString(5, this.getEmpleado());
            ps.setString(6, this.getServicio());
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

        String sql = "UPDATE factura SET fecha=?, hora=?, total=?, estado=?, "
                + "id_servicio=(SELECT id_servicio FROM servicio WHERE nombre_servicio=?) WHERE id_factura=?";

        try {
            ps = con.prepareStatement(sql);
            ps.setDate(1, java.sql.Date.valueOf(this.getFecha()));
            ps.setTime(2, java.sql.Time.valueOf(this.getHora()));
            ps.setDouble(3, this.getTotal());
            ps.setString(4, this.getEstado());
            ps.setString(5, this.getServicio());
            ps.setInt(6, this.getId_factura());
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

        String sql = "DELETE FROM factura WHERE id_factura=?";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, this.getId_factura());
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
            ps.setString(1, this.getEmpleado());
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
            ps.setString(1, this.getEmpleado());
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

    public List<FacturaModel> listar() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConexion();

        String sql = "SELECT f.id_factura, f.fecha, f.hora, f.total, f.estado, u.nombre_usuario, s.nombre_servicio "
                + "FROM factura f "
                + "JOIN usuario u ON f.id_usuario = u.id_usuario "
                + "JOIN servicio s ON f.id_servicio = s.id_servicio";
        List<FacturaModel> invoiceList = new ArrayList<>();

        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                FacturaModel invoice = new FacturaModel();
                invoice.setId_factura(rs.getInt("id_factura"));
                invoice.setFecha(rs.getDate("fecha").toLocalDate());
                invoice.setHora(rs.getTime("hora").toLocalTime());
                invoice.setTotal(rs.getDouble("total"));
                invoice.setEstado(rs.getString("estado"));
                invoice.setEmpleado(rs.getString("nombre_usuario"));
                invoice.setServicio(rs.getString("nombre_servicio"));

                invoiceList.add(invoice);
            }

            return invoiceList;
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

    public int getId_factura() {
        return id_factura;
    }

    public void setId_factura(int id_factura) {
        this.id_factura = id_factura;
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

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getEmpleado() {
        return empleado;
    }

    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }

}
