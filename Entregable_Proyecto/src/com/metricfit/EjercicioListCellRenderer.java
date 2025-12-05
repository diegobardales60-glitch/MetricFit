package com.metricfit;

import javax.swing.*;
import java.awt.*;

public class EjercicioListCellRenderer extends DefaultListCellRenderer {
    
   
    private final Color COLOR_NARANJA = new Color(255, 102, 0);
    private final Color COLOR_PLACEHOLDER = new Color(150, 150, 150);

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        if (value instanceof Ejercicio) {
            Ejercicio ejercicio = (Ejercicio) value;
            
        
            String htmlText = String.format(
                "<html><b style='color:%s'>%s</b><br><span style='color:%s'>%s</span></html>",
                toHex(COLOR_NARANJA), ejercicio.getNombre(),
                toHex(COLOR_PLACEHOLDER), ejercicio.getZonaMuscular()
            );
            
            label.setText(htmlText);
            label.setIcon(null); 
            label.setOpaque(true);
            label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
        }
        
        return label;
    }
    
    private String toHex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }
}