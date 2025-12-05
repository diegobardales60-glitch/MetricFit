package com.metricfit;

public class ItemRutina {
    private int idItem;
    private int idRutina;
    private Ejercicio ejercicio;
    private int orden;
    
    
    private String seriesObjetivo;
    private String repsObjetivo;
    private String pesoObjetivo;
    private String notas;
    private String tipoSet;

  
    public ItemRutina(int idItem, int idRutina, Ejercicio ejercicio, int orden, String seriesObjetivo, String repsObjetivo, String pesoObjetivo, String notas, String tipoSet) {
        this.idItem = idItem;
        this.idRutina = idRutina;
        this.ejercicio = ejercicio;
        this.orden = orden;
        this.seriesObjetivo = seriesObjetivo;
        this.repsObjetivo = repsObjetivo;
        this.pesoObjetivo = pesoObjetivo;
        this.notas = notas;
        this.tipoSet = tipoSet;
    }


    public ItemRutina(int idRutina, Ejercicio ejercicio) {
        this(0, idRutina, ejercicio, 0, "3", "10", "0", "", "Normal");
    }

   
    public ItemRutina(int idItem, int idRutina, int idEjercicio, String nombreEjercicio, int series, int reps, double peso, String tipo, int orden) {
        this.idItem = idItem;
        this.idRutina = idRutina;
        this.ejercicio = new Ejercicio(idEjercicio, nombreEjercicio, "", ""); 
        this.orden = orden;
        this.seriesObjetivo = String.valueOf(series);
        this.repsObjetivo = String.valueOf(reps);
        this.pesoObjetivo = String.valueOf(peso);
        this.tipoSet = tipo;
        this.notas = "";
    }

   
    public int getIdItem() { return idItem; }
    public int getIdRutina() { return idRutina; }
    public Ejercicio getEjercicio() { return ejercicio; }
    public int getOrden() { return orden; }
    public String getSeriesObjetivo() { return seriesObjetivo; }
    public String getRepsObjetivo() { return repsObjetivo; }
    public String getPesoObjetivo() { return pesoObjetivo; }
    public String getNotas() { return notas; }
    
    public String getTipoSet() { return tipoSet; }
    public String getTipoSerie() { return tipoSet; }


    
    public int getIdEjercicio() {
        return (ejercicio != null) ? ejercicio.getId() : 0;
    }

    public int getSeries() {
        try { return Integer.parseInt(seriesObjetivo); } catch (Exception e) { return 0; }
    }

    public int getRepeticiones() {
        try { return Integer.parseInt(repsObjetivo); } catch (Exception e) { return 0; }
    }

    public double getPesoKg() {
        try { return Double.parseDouble(pesoObjetivo); } catch (Exception e) { return 0.0; }
    }

   

    public void setIdItem(int idItem) { this.idItem = idItem; }
    public void setIdItemRutina(int id) { this.idItem = id; } 
    public void setOrden(int orden) { this.orden = orden; }
    public void setNotas(String notas) { this.notas = notas; }
    public void setTipoSet(String tipo) { this.tipoSet = tipo; }
    public void setTipoSerie(String tipo) { this.tipoSet = tipo; } 

    
    public void setSeries(int series) {
        this.seriesObjetivo = String.valueOf(series);
    }

    public void setRepeticiones(int reps) {
        this.repsObjetivo = String.valueOf(reps);
    }

    public void setPesoKg(double peso) {
        this.pesoObjetivo = String.valueOf(peso);
    }

    @Override
    public String toString() {
        return ejercicio.getNombre() + " (" + seriesObjetivo + "x" + repsObjetivo + ")";
    }

   
    void setSeriesObjetivo(String valor) {
        this.seriesObjetivo = valor;
    }

    void setRepsObjetivo(String valor) {
        this.repsObjetivo = valor;
    }

    void setPesoObjetivo(String valor) {
        this.pesoObjetivo = valor;
    }
}
