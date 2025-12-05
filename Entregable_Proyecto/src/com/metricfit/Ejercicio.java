package com.metricfit;

public class Ejercicio {
    private int id;
    private String nombre;
    private String zonaMuscular;
    private String descripcion;
    private String gifUrl; 

  
    public Ejercicio(int id, String nombre, String zonaMuscular, String descripcion, String gifUrl) {
        this.id = id;
        this.nombre = nombre;
        this.zonaMuscular = zonaMuscular;
        this.descripcion = descripcion;
        this.gifUrl = gifUrl;
    }

    
    public Ejercicio(int id, String nombre, String zonaMuscular, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.zonaMuscular = zonaMuscular;
        this.descripcion = descripcion;
        this.gifUrl = ""; 
    }
    
   
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getZonaMuscular() { return zonaMuscular; }
    public String getDescripcion() { return descripcion; }
    public String getGifUrl() { return gifUrl; }

    
    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setZonaMuscular(String zonaMuscular) { this.zonaMuscular = zonaMuscular; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setGifUrl(String gifUrl) { this.gifUrl = gifUrl; }

    
    @Override
    public String toString() {
        return nombre;
    }
}