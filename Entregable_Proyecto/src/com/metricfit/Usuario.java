package com.metricfit;

public class Usuario {

    private int id;
    private String nombreUsuario;
    private String email;
    
   
    private int edad;
    private double pesoActual;
    private int altura;          
    private double pesoObjetivo; 
    private String genero;       
    
   
    private int nivelActividad;  
    private int tipoMeta;       
    private int objetivoCalorias; 

  
    public Usuario(int id, String nombreUsuario, String email, int edad, double pesoActual, 
                   int altura, double pesoObjetivo, String genero, 
                   int nivelActividad, int tipoMeta, int objetivoCalorias) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.edad = edad;
        this.pesoActual = pesoActual;
        this.altura = altura;
        this.pesoObjetivo = pesoObjetivo;
        this.genero = genero;
        this.nivelActividad = nivelActividad;
        this.tipoMeta = tipoMeta;
        this.objetivoCalorias = objetivoCalorias;
    }

   
    public int getId() { return id; }
    public String getNombreUsuario() { return nombreUsuario; }
    public String getEmail() { return email; }
    public int getEdad() { return edad; }
    public double getPesoActual() { return pesoActual; }
    public int getObjetivo() { return objetivoCalorias; } 
    
   
    public int getAltura() { return altura; }
    public double getPesoObjetivo() { return pesoObjetivo; }
    public String getGenero() { return genero; }
    public int getNivelActividad() { return nivelActividad; }
    public int getTipoMeta() { return tipoMeta; }

   
    public void setEdad(int edad) { this.edad = edad; }
    public void setPesoActual(double pesoActual) { this.pesoActual = pesoActual; }
    public void setObjetivo(int objetivoCalorias) { this.objetivoCalorias = objetivoCalorias; }
    public void setObjetivo(String s) { try { this.objetivoCalorias = Integer.parseInt(s); } catch(Exception e){} }

   
    public void setAltura(int altura) { this.altura = altura; }
    public void setPesoObjetivo(double pesoObjetivo) { this.pesoObjetivo = pesoObjetivo; }
    public void setGenero(String genero) { this.genero = genero; }
    public void setNivelActividad(int nivelActividad) { this.nivelActividad = nivelActividad; }
    public void setTipoMeta(int tipoMeta) { this.tipoMeta = tipoMeta; }
}