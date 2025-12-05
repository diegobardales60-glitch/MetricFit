package com.metricfit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AlimentoListRenderer extends DefaultListCellRenderer {


    private static final Color COLOR_PANEL_FONDO = new Color(45, 45, 45);
    private static final Color COLOR_TEXTO_PRINCIPAL = Color.WHITE;
    private static final Color COLOR_ACENTO = new Color(255, 102, 0);

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value,
                                                  int index, boolean isSelected,
                                                  boolean cellHasFocus) {

        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof Alimento alimento) {
            String text = String.format("<html><b>%s</b> <span style='color:#FF9900;'>| %.0f kcal</span> | P: %.1fg | C: %.1fg | G: %.1fg</html>",
                alimento.getNombre(),
                alimento.getCaloriasPor100g(),
                alimento.getProteinasPor100g(),
                alimento.getCarbohidratosPor100g(),
                alimento.getGrasasPor100g()
            );

            label.setText(text);
            label.setBorder(new EmptyBorder(5, 10, 5, 10));
            label.setBackground(isSelected ? COLOR_ACENTO.darker() : COLOR_PANEL_FONDO.brighter());
            label.setForeground(COLOR_TEXTO_PRINCIPAL); 

        } else {
            label.setText(value.toString());
        }
        
        return label;
    }
}