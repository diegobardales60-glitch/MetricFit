package com.metricfit;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseManager {

 
    private static final String URL = "jdbc:mysql://diegobardales.helioho.st:3306/diegobardales_proyecto";
    private static final String USER = "diegobardales_1";
    private static final String PASSWORD = "959373020";

   
    public Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }



    public boolean registrarUsuario(String nombre, String email, String pass) {
        String sql = "INSERT INTO usuarios (nombre_usuario, email, contrasena, edad, peso_actual, objetivo, altura, peso_objetivo, genero, nivel_actividad, tipo_meta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, email);
            pstmt.setString(3, pass);
            pstmt.setInt(4, 0);
            pstmt.setDouble(5, 0.0);
            pstmt.setInt(6, 2000);
            pstmt.setInt(7, 170);
            pstmt.setDouble(8, 70.0);
            pstmt.setString(9, "Hombre");
            pstmt.setInt(10, 1);
            pstmt.setInt(11, 1);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace(); return false;
        }
    }

    public Usuario iniciarSesion(String email, String pass) {
        String sql = "SELECT * FROM usuarios WHERE email = ? AND contrasena = ?";
        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, pass);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre_usuario"),
                        rs.getString("email"),
                        rs.getInt("edad"),
                        rs.getDouble("peso_actual"),
                        rs.getInt("altura"),
                        rs.getDouble("peso_objetivo"),
                        rs.getString("genero"),
                        rs.getInt("nivel_actividad"),
                        rs.getInt("tipo_meta"),
                        rs.getInt("objetivo")
                    );
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean actualizarUsuario(Usuario u) {
        String sql = "UPDATE usuarios SET edad=?, peso_actual=?, objetivo=?, altura=?, peso_objetivo=?, genero=?, nivel_actividad=?, tipo_meta=? WHERE id_usuario=?";
        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, u.getEdad());
            pstmt.setDouble(2, u.getPesoActual());
            pstmt.setInt(3, u.getObjetivo());
            pstmt.setInt(4, u.getAltura());
            pstmt.setDouble(5, u.getPesoObjetivo());
            pstmt.setString(6, u.getGenero());
            pstmt.setInt(7, u.getNivelActividad());
            pstmt.setInt(8, u.getTipoMeta());
            pstmt.setInt(9, u.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace(); return false;
        }
    }

    public int obtenerObjetivoCalorico(int idUsuario) throws SQLException {
        int objetivo = 2000;
        String sql = "SELECT objetivo FROM usuarios WHERE id_usuario = ?"; 
        try (Connection conn = obtenerConexion(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    objetivo = rs.getInt("objetivo"); 
                }
            }
        } catch (SQLException e) { throw e; }
        return objetivo;
    }

  
    public void inicializarEjerciciosPorDefecto() {
        System.out.println("Los ejercicios ya están cargados en la Base de Datos.");
    }

    public List<Ejercicio> obtenerTodosLosEjercicios() {
        String sql = "SELECT * FROM ejercicios ORDER BY nombre";
        List<Ejercicio> ejercicios = new ArrayList<>();
        try (Connection conn = obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ejercicios.add(new Ejercicio(
                     rs.getInt("id_ejercicio"),
                     rs.getString("nombre"),
                     rs.getString("zona_muscular"),
                     rs.getString("descripcion"),
                     rs.getString("gif_url")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return ejercicios;
    }

    public List<Ejercicio> obtenerEjerciciosPorZona(String zona) {
        List<Ejercicio> lista = new ArrayList<>();
        String sql = "SELECT * FROM ejercicios WHERE zona_muscular = ? ORDER BY nombre";
        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, zona);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                lista.add(new Ejercicio(
                    rs.getInt("id_ejercicio"),
                    rs.getString("nombre"),
                    rs.getString("zona_muscular"),
                    rs.getString("descripcion")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public Ejercicio crearEjercicioPersonalizado(String nombre, String zona) {
        String sql = "INSERT INTO ejercicios (nombre, zona_muscular, descripcion) VALUES (?, ?, ?)";
        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, zona);
            pstmt.setString(3, "Personalizado");
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if(rs.next()) return new Ejercicio(rs.getInt(1), nombre, zona, "Personalizado");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

  

    public boolean crearRutina(Rutina nuevaRutina) {
    
        String sql = "INSERT INTO rutinas (id_usuario, nombre_rutina, dia_semana, imagen_nombre) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, nuevaRutina.getIdUsuario());
            pstmt.setString(2, nuevaRutina.getNombreRutina());
            pstmt.setString(3, nuevaRutina.getDiaSemana());
          
            String img = (nuevaRutina.getImagenNombre() == null) ? "routine_fullbody.png" : nuevaRutina.getImagenNombre();
            pstmt.setString(4, img);

            int filas = pstmt.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        nuevaRutina.setIdRutina(rs.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    
    public boolean actualizarRutina(Rutina r) {
        
        String sql = "UPDATE rutinas SET nombre_rutina = ?, dia_semana = ?, imagen_nombre = ? WHERE id_rutina = ?";
        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, r.getNombreRutina());
            pstmt.setString(2, r.getDiaSemana());
            String img = (r.getImagenNombre() == null) ? "routine_fullbody.png" : r.getImagenNombre();
            pstmt.setString(3, img);
            pstmt.setInt(4, r.getIdRutina());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean eliminarRutina(int idRutina) {
        String sql = "DELETE FROM rutinas WHERE id_rutina = ?";
        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idRutina);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public List<Rutina> obtenerRutinasDeUsuario(int idUsuario) {
        String sql = "SELECT * FROM rutinas WHERE id_usuario = ?";
        List<Rutina> rutinas = new ArrayList<>();
        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                   
                    String img = rs.getString("imagen_nombre");
                    if (img == null || img.isEmpty()) img = "routine_fullbody.png"; // Fallback por si es null en BD antigua

                    rutinas.add(new Rutina(
                             rs.getInt("id_rutina"),
                             rs.getInt("id_usuario"),
                             rs.getString("nombre_rutina"),
                             rs.getString("dia_semana"),
                             img 
                    ));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return rutinas;
    }
    
    public int contarItemsRutina(int idRutina) {
        String sql = "SELECT COUNT(*) FROM items_rutina WHERE id_rutina = ?";
        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idRutina);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1); 
            }
        } catch (SQLException e) { }
        return 0;
    }
    
  

    public List<ItemRutina> obtenerItemsDeRutina(int idRutina) {
        List<ItemRutina> items = new ArrayList<>();
        String sql = """
            SELECT i.*, e.* FROM items_rutina i
            JOIN ejercicios e ON i.id_ejercicio = e.id_ejercicio
            WHERE i.id_rutina = ? ORDER BY i.orden
        """;
        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idRutina);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Ejercicio ej = new Ejercicio(
                             rs.getInt("id_ejercicio"),
                             rs.getString("nombre"),
                             rs.getString("zona_muscular"),
                             rs.getString("descripcion"),
                             rs.getString("gif_url")
                    );
                    ItemRutina item = new ItemRutina(
                             rs.getInt("id_item_rutina"), 
                             rs.getInt("id_rutina"),
                             ej,
                             rs.getInt("orden"),
                             rs.getString("series"), 
                             rs.getString("repeticiones"),
                             rs.getString("peso_kg"),
                             rs.getString("notas"),
                             rs.getString("tipo_serie")
                    );
                    items.add(item);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return items;
    }

    public boolean insertarItemRutina(ItemRutina item) {
        String sql = "INSERT INTO items_rutina (id_rutina, id_ejercicio, series, repeticiones, peso_kg, tipo_serie, orden) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, item.getIdRutina());
            pstmt.setInt(2, item.getIdEjercicio());
            pstmt.setInt(3, item.getSeries());
            pstmt.setInt(4, item.getRepeticiones());
            pstmt.setDouble(5, item.getPesoKg());
            pstmt.setString(6, item.getTipoSerie());
            pstmt.setInt(7, item.getOrden());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) item.setIdItemRutina(rs.getInt(1));
                }
                return true; 
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false; 
    }

    public boolean quitarItemDeRutina(int idItem) {
        String sql = "DELETE FROM items_rutina WHERE id_item_rutina = ?"; 
        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idItem);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

  

    public List<Alimento> obtenerTodosLosAlimentos() {
        String sql = "SELECT * FROM alimentos ORDER BY nombre";
        List<Alimento> alimentos = new ArrayList<>();
        try (Connection conn = obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                alimentos.add(new Alimento(
                             rs.getInt("id_alimento"),
                             rs.getString("nombre"),
                             rs.getString("unidad_medida"),
                             rs.getDouble("calorias"),
                             rs.getDouble("proteinas"),
                             rs.getDouble("carbohidratos"),
                             rs.getDouble("grasas")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return alimentos;
    }
    
    public List<Alimento> getCatalogoAlimentos() { return obtenerTodosLosAlimentos(); }

    public Alimento guardarNuevoAlimento(Alimento a) {
        String sql = "INSERT INTO alimentos (nombre, unidad_medida, calorias, proteinas, carbohidratos, grasas) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, a.getNombre());
            pstmt.setString(2, a.getUnidadMedida());
            pstmt.setDouble(3, a.getCalorias());
            pstmt.setDouble(4, a.getProteinas());
            pstmt.setDouble(5, a.getCarbohidratos());
            pstmt.setDouble(6, a.getGrasas());
            if (pstmt.executeUpdate() > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) return new Alimento(rs.getInt(1), a.getNombre(), a.getUnidadMedida(), a.getCalorias(), a.getProteinas(), a.getCarbohidratos(), a.getGrasas());
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<Consumo> obtenerConsumoDelDia(int idUsuario, Date fecha) {
        List<Consumo> lista = new ArrayList<>();
        String sql = "SELECT c.*, a.* FROM consumo_diario c JOIN alimentos a ON c.id_alimento = a.id_alimento WHERE c.id_usuario = ? AND c.fecha = ?";
        java.sql.Date sqlFecha = new java.sql.Date(fecha.getTime());
        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            pstmt.setDate(2, sqlFecha);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Alimento a = new Alimento(
                             rs.getInt("id_alimento"), rs.getString("nombre"), rs.getString("unidad_medida"),
                             rs.getDouble("calorias"), rs.getDouble("proteinas"), rs.getDouble("carbohidratos"), rs.getDouble("grasas")
                    );
                    lista.add(new Consumo(
                             rs.getInt("id_consumo"), rs.getInt("id_usuario"), a,
                             rs.getDouble("cantidad"), rs.getDate("fecha"), rs.getString("tipo_comida")
                    ));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public boolean registrarConsumo(Consumo c) {
        String sql = "INSERT INTO consumo_diario (id_usuario, id_alimento, cantidad, fecha, tipo_comida) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, c.getIdUsuario());
            pstmt.setInt(2, c.getAlimento().getId());
            pstmt.setDouble(3, c.getCantidad());
            pstmt.setDate(4, new java.sql.Date(c.getFecha().getTime()));
            pstmt.setString(5, c.getTipoComida());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    
    public void guardarConsumo(Consumo c) { registrarConsumo(c); }

    public boolean eliminarConsumo(int id) {
        try (Connection conn = obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement("DELETE FROM consumo_diario WHERE id_consumo = ?")) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public void reasignarConsumos(int id, String v, String n) throws SQLException {
        try (Connection conn = obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement("UPDATE consumo_diario SET tipo_comida = ? WHERE id_usuario = ? AND tipo_comida = ?")) {
            pstmt.setString(1, n); pstmt.setInt(2, id); pstmt.setString(3, v);
            pstmt.executeUpdate();
        }
    }


    public boolean actualizarItemRutina(ItemRutina item) {
        String sql = "UPDATE items_rutina SET series = ?, repeticiones = ?, peso_kg = ?, tipo_serie = ?, notas = ? WHERE id_item_rutina = ?";
        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, item.getSeries());
            pstmt.setInt(2, item.getRepeticiones());
            pstmt.setDouble(3, item.getPesoKg());
            pstmt.setString(4, item.getTipoSerie()); 
            pstmt.setString(5, item.getNotas());
            pstmt.setInt(6, item.getIdItem());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int agregarItemARutina(ItemRutina item) {
        if (insertarItemRutina(item)) return item.getIdItem();
        return -1;
    }
    public boolean validarUsuarioParaRecuperacion(String email, String nombreUsuario) {
        String sql = "SELECT id_usuario FROM usuarios WHERE email = ? AND nombre_usuario = ?";
        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, nombreUsuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean restablecerContrasena(String email, String nuevaPass) {
        String sql = "UPDATE usuarios SET contrasena = ? WHERE email = ?";
        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nuevaPass);
            pstmt.setString(2, email);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}