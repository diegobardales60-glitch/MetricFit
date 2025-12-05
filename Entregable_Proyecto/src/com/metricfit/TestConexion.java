package com.metricfit;

import java.sql.*;

public class TestConexion {
    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();
        
        System.out.println("--- INICIANDO DIAGNÓSTICO DE CONEXIÓN ---");
        try (Connection conn = db.obtenerConexion()) {
            System.out.println("✅ Conexión exitosa a: " + conn.getMetaData().getURL());
            
            DatabaseMetaData md = conn.getMetaData();
            ResultSet rs = md.getTables(null, null, "%", null);
            
            System.out.println("\nTABLAS ENCONTRADAS EN LA BASE DE DATOS:");
            boolean found = false;
            while (rs.next()) {
                System.out.println(" - " + rs.getString(3)); 
                found = true;
            }
            
            if (!found) {
                System.out.println("⚠️ ALERTA: No se encontraron tablas. La base de datos está vacía.");
            }
            
        } catch (SQLException e) {
            System.out.println("❌ ERROR DE CONEXIÓN: " + e.getMessage());
        }
    }
}