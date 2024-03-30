package modelo;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UsuarioModel extends Conexion {

    private int id_usuario;
    private String nombre_usuario;
    private String correo;
    private String contrasena;
    private int tipo_usuario;

    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;

    public UsuarioModel(int id_usuario, String nombre, String correo, String contrasena, int tipo_usuario) {
        this.id_usuario = id_usuario;
        this.nombre_usuario = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.tipo_usuario = tipo_usuario;
    }

    public UsuarioModel() {
        this.nombre_usuario = null;
        this.correo = null;
        this.contrasena = null;
    }

    public boolean registrar() {
        PreparedStatement ps = null;
        Connection con = getConexion();

        String sql = "INSERT INTO usuario (nombre_usuario, correo, contrasena) VALUES(?,?,?)";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, this.getNombre_usuario());
            ps.setString(2, this.getCorreo());
            ps.setString(3, this.getContrasena());
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

        try {
            if (!this.getContrasena().trim().isEmpty()) {
                String sql = "UPDATE usuario SET nombre_usuario=?, correo=?, contrasena=? WHERE id_usuario=?";
                ps = con.prepareStatement(sql);
                ps.setString(1, this.getNombre_usuario());
                ps.setString(2, this.getCorreo());
                ps.setString(3, this.getContrasena());
                ps.setInt(4, this.getId_usuario());
                ps.execute();

            } else {
                String sql = "UPDATE usuario SET nombre_usuario=?, correo=? WHERE id_usuario=?";
                ps = con.prepareStatement(sql);
                ps.setString(1, this.getNombre_usuario());
                ps.setString(2, this.getCorreo());
                ps.setInt(3, this.getId_usuario());
                ps.execute();

            }
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

        String sql = "DELETE FROM usuario WHERE id_usuario=?";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, this.getId_usuario());
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

        String sql = "SELECT * FROM usuario WHERE correo=? and contrasena=?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, this.getCorreo());
            ps.setString(2, this.getContrasena());
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
            ps.setString(1, this.getCorreo());
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

    public List<UsuarioModel> listar() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConexion();

        String sql = "SELECT * FROM usuario";
        List<UsuarioModel> userList = new ArrayList<>();

        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                UsuarioModel usuario = new UsuarioModel();
                usuario.setId_usuario(rs.getInt("id_usuario"));
                usuario.setNombre_usuario(rs.getString("nombre_usuario"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setContrasena(rs.getString("contrasena"));
                userList.add(usuario);
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
        return userList;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public int getTipo_usuario() {
        return tipo_usuario;
    }

    public void setTipo_usuario(int tipo_usuario) {
        this.tipo_usuario = tipo_usuario;
    }

}
