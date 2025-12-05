package com.metricfit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DialogoOpcionesRutina extends JDialog {

    private final Color COL_FONDO = new Color(40, 40, 40);
    private final Color COL_HOVER = new Color(60, 60, 60);
    private final Color COL_TEXTO = Color.WHITE;
    private final Color COL_ACENTO = new Color(255, 102, 0);

    private int seleccion = -1; 

    public DialogoOpcionesRutina(Frame owner, String nombreRutina) {
        super(owner, true);
        setUndecorated(true); 
        setSize(300, 220);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COL_FONDO);
        mainPanel.setBorder(BorderFactory.createLineBorder(COL_ACENTO, 2));
        add(mainPanel);

     
        JLabel lblTitulo = new JLabel("Gestionar: " + nombreRutina, SwingConstants.CENTER);
        lblTitulo.setForeground(COL_ACENTO);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitulo.setBorder(new EmptyBorder(15, 10, 15, 10));
        mainPanel.add(lblTitulo, BorderLayout.NORTH);

       
        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 5, 5));
        panelBotones.setOpaque(false);
        panelBotones.setBorder(new EmptyBorder(0, 20, 20, 20));

        panelBotones.add(crearBotonOpcion("🏋️ Gestionar Ejercicios", 1));
        panelBotones.add(crearBotonOpcion("✎ Editar Nombre/Icono", 0));
        panelBotones.add(crearBotonOpcion("✖ Cancelar", -1));

        mainPanel.add(panelBotones, BorderLayout.CENTER);
    }

    private JButton crearBotonOpcion(String texto, int valorRetorno) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn.setForeground(COL_TEXTO);
        btn.setBackground(COL_FONDO);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80,80,80)),
            new EmptyBorder(10, 10, 10, 10)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(COL_HOVER); }
            public void mouseExited(MouseEvent e) { btn.setBackground(COL_FONDO); }
        });

        btn.addActionListener(e -> {
            this.seleccion = valorRetorno;
            dispose();
        });
        return btn;
    }

    public int getSeleccion() {
        return seleccion;
    }
}