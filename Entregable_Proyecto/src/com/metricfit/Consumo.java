package com.metricfit;

import java.util.Date;

public class Consumo {

    private int idConsumo;
    private int idUsuario;
    private Alimento alimento; 
    private double cantidad;    
    private Date fecha;
    private String tipoComida; 

   
    public Consumo(int idConsumo, int idUsuario, Alimento alimento,
                   double cantidad, Date fecha, String tipoComida) {

        this.idConsumo = idConsumo;
        this.idUsuario = idUsuario;
        this.alimento = alimento;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.tipoComida = tipoComida;
    }

 
    public Consumo(int idUsuario, Alimento alimento,
                   double cantidad, Date fecha, String tipoComida) {
        
        this(-1, idUsuario, alimento, cantidad, fecha, tipoComida);
    }

   

    public int getIdConsumo() { return idConsumo; }
    public int getIdUsuario() { return idUsuario; }
    public Alimento getAlimento() { return alimento; }
    public double getCantidad() { return cantidad; }
    public Date getFecha() { return fecha; }
    public String getTipoComida() { return tipoComida; }

 
    public double getCaloriasTotales() { 
        return (alimento.getCalorias() / 100.0) * cantidad; 
    }
    
 
    public double getProteinasTotales() { 
        return (alimento.getProteinas() / 100.0) * cantidad; 
    }
    
 
    public double getCarbohidratosTotales() { 
        return (alimento.getCarbohidratos() / 100.0) * cantidad; 
    }
    
  
    public double getGrasasTotales() { 
        return (alimento.getGrasas() / 100.0) * cantidad; 
    }

    
    @Override
    public String toString() {
        double totalKcal = Math.round(getCaloriasTotales());
        return String.format("%.0f %s %s (%.0f kcal)",
                cantidad,
                alimento.getUnidadMedida(),
                alimento.getNombre(),
                totalKcal
        );
    }
}