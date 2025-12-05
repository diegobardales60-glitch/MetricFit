package com.metricfit;

public class Rutina {
    
    private int idRutina;
    private int idUsuario;
    private String nombreRutina;
    private String diaSemana;
    
    private String imagenNombre; 

    private static final String IMAGEN_DEFAULT = "icon_default.png";

   
    public Rutina(int idRutina, int idUsuario, String nombreRutina, String diaSemana, String imagenNombre) {
        this.idRutina = idRutina;
        this.idUsuario = idUsuario;
        this.nombreRutina = nombreRutina;
        this.diaSemana = diaSemana;
        this.imagenNombre = (imagenNombre != null && !imagenNombre.isEmpty()) ? imagenNombre : IMAGEN_DEFAULT;
    }

  
    public Rutina(int idRutina, int idUsuario, String nombreRutina, String diaSemana) {
        this(idRutina, idUsuario, nombreRutina, diaSemana, IMAGEN_DEFAULT);
    }

    
    public Rutina(int idRutina, String nombreRutina, String diaSemana) {
        this(idRutina, 0, nombreRutina, diaSemana, IMAGEN_DEFAULT);
    }

    
    public int getIdRutina() {
        return idRutina;
    }

    public int getIdUsuario() {
        return idUsuario;
    }
    
    public String getNombreRutina() {
        return nombreRutina;
    }
    
    public String getDiaSemana() {
        return diaSemana;
    }

    public String getImagenNombre() {
        return imagenNombre;
    }


    public void setIdRutina(int idRutina) {
        this.idRutina = idRutina;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setNombreRutina(String nombreRutina) {
        this.nombreRutina = nombreRutina;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public void setImagenNombre(String imagenNombre) {
        this.imagenNombre = (imagenNombre != null && !imagenNombre.isEmpty()) ? imagenNombre : IMAGEN_DEFAULT;
    }
    
    @Override
    public String toString() {
        return nombreRutina + " (" + diaSemana + ")";
    }
}