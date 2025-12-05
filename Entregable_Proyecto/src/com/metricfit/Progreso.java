package com.metricfit;

import java.util.Date;

public class Progreso {
    private int id;
    private int idUsuario;
    private int idEjercicio;
    private double peso;
    private int repeticiones;
    private int series;
    private Date fecha;

    
    public Progreso(int id, int idUsuario, int idEjercicio, double peso, int repeticiones, int series, Date fecha) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idEjercicio = idEjercicio;
        this.peso = peso;
        this.repeticiones = repeticiones;
        this.series = series;
        this.fecha = fecha;
    }
    
    public int getId() { return id; }
    public int getIdUsuario() { return idUsuario; }
    public int getIdEjercicio() { return idEjercicio; }
    public double getPeso() { return peso; }
    public int getRepeticiones() { return repeticiones; }
    public int getSeries() { return series; }
    public Date getFecha() { return fecha; }
}