package com.metricfit;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;

public class MetricFitApp {

    public static void main(String[] args) {
        

        try {
            UIManager.setLookAndFeel( new FlatDarkLaf() );
        } catch( Exception ex ) {
            System.err.println( "Error al iniciar el Look and Feel: " + ex.getMessage() );
        }

        
        SwingUtilities.invokeLater(() -> {
            VentanaSplashAnimado splash = new VentanaSplashAnimado(); 
            splash.setVisible(true);
        });
    }
}