package com.metricfit;

import javax.swing.*;
import java.awt.*;


public class ItemRutinaListCellRenderer extends DefaultListCellRenderer {
    
    private final Color COLOR_NARANJA = new Color(255, 102, 0);

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof ItemRutina) {
            ItemRutina item = (ItemRutina) value;
            
            String nombreEj = item.getEjercicio().getNombre();
            
            
            String detalles = String.format("%s sets x %s reps @ %s kg", 
                                            item.getSeriesObjetivo(), 
                                            item.getRepsObjetivo(), 
                                            item.getPesoObjetivo());
            
            String htmlText = String.format(
                "<html><b>%s</b><br><span style='color:%s'>%s</span></html>",
                nombreEj,
                toHex(COLOR_NARANJA), detalles
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