package com.metricfit;

public class Alimento {
    private int id;
    private String nombre;
    private String unidadMedida;
    private double calorias;
    private double proteinas;
    private double carbohidratos;
    private double grasas;

  
    public Alimento(int id, String nombre, String unidadMedida, double calorias, double proteinas, double carbohidratos, double grasas) {
        this.id = id;
        this.nombre = nombre;
        this.unidadMedida = unidadMedida;
        this.calorias = calorias;
        this.proteinas = proteinas;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
    }

    Alimento(int id, String nombre, double kcal, double prot, double carb, double grasa) {
        this.id = id;
        this.nombre = nombre;
        this.unidadMedida = "100g"; 
        this.calorias = kcal;
        this.proteinas = prot;
        this.carbohidratos = carb;
        this.grasas = grasa;
        
    }

   
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getUnidadMedida() { return unidadMedida; }
    public double getCalorias() { return calorias; }
    public double getProteinas() { return proteinas; }
    public double getCarbohidratos() { return carbohidratos; }
    public double getGrasas() { return grasas; }

    @Override
    public String toString() {
        return nombre + " (" + calorias + " kcal/" + unidadMedida + ")";
    }

    
    double getCaloriasPor100g() {
        return calorias; 
    }

    double getProteinasPor100g() {
        return proteinas;
    }

    double getCarbohidratosPor100g() {
        return carbohidratos;
    }

    double getGrasasPor100g() {
        return grasas;
    }
}