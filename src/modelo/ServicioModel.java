package modelo;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ServicioModel extends Conexion {

    private int id_servicio;
    private String nombre_servicio;
    private String descripcion;
    private double precio;

    public ServicioModel() {
        this.nombre_servicio = null;
        this.descripcion = null;
    }

    public ServicioModel(int id_servicio, String nombre_servicio, String descripcion, double precio) {
        this.id_servicio = id_servicio;
        this.nombre_servicio = nombre_servicio;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;

    public boolean registrar() {
        PreparedStatement ps = null;
        Connection con = getConexion();

        String sql = "INSERT INTO servicio (nombre_servicio, descripcion, precio) VALUES(?,?,?)";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, this.getNombre_servicio());
            ps.setString(2, this.getDescripcion());
            ps.setDouble(3, this.getPrecio());
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

        String sql = "UPDATE servicio SET nombre_servicio=?, descripcion=?, precio=? WHERE id_servicio=?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, this.getNombre_servicio());
            ps.setString(2, this.getDescripcion());
            ps.setDouble(3, this.getPrecio());
            ps.setInt(4, this.getId_servicio());
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

        String sql = "DELETE FROM servicio WHERE id_servicio=?";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, this.getId_servicio());
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

    public List<ServicioModel> listar() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConexion();

        String sql = "SELECT * FROM servicio";
        List<ServicioModel> serviceList = new ArrayList<>();

        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                ServicioModel service = new ServicioModel();
                service.setId_servicio(rs.getInt("id_servicio"));
                service.setNombre_servicio(rs.getString("nombre_servicio"));
                service.setDescripcion(rs.getString("descripcion"));
                service.setPrecio(rs.getDouble("precio"));
                serviceList.add(service);
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
        return serviceList;
    }

    public int getId_servicio() {
        return id_servicio;
    }

    public void setId_servicio(int id_servicio) {
        this.id_servicio = id_servicio;
    }

    public String getNombre_servicio() {
        return nombre_servicio;
    }

    public void setNombre_servicio(String nombre_servicio) {
        this.nombre_servicio = nombre_servicio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

}
