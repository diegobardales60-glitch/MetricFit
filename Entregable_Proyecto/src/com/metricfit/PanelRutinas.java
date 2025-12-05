package com.metricfit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.net.URL;

public class PanelRutinas extends JPanel {

    
    private final Color COLOR_FONDO_BASE = new Color(30, 30, 30);
    private final Color COLOR_TARJETA = new Color(45, 45, 45);
    private final Color COLOR_TARJETA_HOVER = new Color(55, 55, 55);
    private final Color COLOR_TEXTO_PRINCIPAL = Color.WHITE;
    private final Color COLOR_TEXTO_SECUNDARIO = new Color(170, 170, 170);
    private final Color COLOR_ACENTO = new Color(255, 102, 0); // Naranja
    private final Color COLOR_ROJO = new Color(220, 50, 50);

  
    private final Usuario usuarioActual;
    private final VentanaPrincipal ventanaPrincipal;
    private final DatabaseManager dbManager;

    private JPanel panelContenedorTarjetas;

   
    public PanelRutinas(Usuario usuario, VentanaPrincipal vp, DatabaseManager dbManager) {
        this.usuarioActual = usuario;
        this.ventanaPrincipal = vp;
        this.dbManager = dbManager;
        setName(VentanaPrincipal.PANEL_RUTINAS);

        setLayout(new BorderLayout(0, 0));
        setBackground(COLOR_FONDO_BASE);
        
        inicializarUI();
        cargarRutinas();
    }
    
    public void recargarDatos() {
        cargarRutinas();
    }

    private void inicializarUI() {
        
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_FONDO_BASE);
        header.setBorder(new EmptyBorder(30, 40, 20, 40));
        
        
        JLabel lblTitulo = new JLabel("Mis Rutinas");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 36));
        lblTitulo.setForeground(COLOR_TEXTO_PRINCIPAL);
        header.add(lblTitulo, BorderLayout.WEST);
        
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotones.setOpaque(false);

        
        BotonModerno btnVolver = new BotonModerno("Regresar", new Color(60, 60, 60), Color.WHITE);
        btnVolver.setPreferredSize(new Dimension(120, 40)); // Ajusté el tamaño para el texto más corto
        btnVolver.addActionListener(e -> {
            if (ventanaPrincipal != null) {
                ventanaPrincipal.manejarNavegacion(VentanaPrincipal.PANEL_DASHBOARD);
            }
        });

        
        BotonModerno btnNueva = new BotonModerno("＋ Nueva Rutina", COLOR_ACENTO, Color.WHITE);
        btnNueva.setPreferredSize(new Dimension(160, 40));
        btnNueva.addActionListener(e -> mostrarDialogoCrearRutina());
        
        
        panelBotones.add(btnVolver);
        panelBotones.add(btnNueva);

        header.add(panelBotones, BorderLayout.EAST);
        
        add(header, BorderLayout.NORTH);

        
        panelContenedorTarjetas = new JPanel();
        panelContenedorTarjetas.setBackground(COLOR_FONDO_BASE);
        panelContenedorTarjetas.setBorder(new EmptyBorder(10, 40, 40, 40));

        JScrollPane scroll = new JScrollPane(panelContenedorTarjetas);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(COLOR_FONDO_BASE);
        scroll.getViewport().setBackground(COLOR_FONDO_BASE);
        
        add(scroll, BorderLayout.CENTER);
    }

    
    private void cargarRutinas() {
        panelContenedorTarjetas.removeAll();
        
        List<Rutina> rutinas = List.of();
        try {
            rutinas = dbManager.obtenerRutinasDeUsuario(usuarioActual.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (rutinas.isEmpty()) {
            mostrarEstadoVacio();
        } else {
            
            panelContenedorTarjetas.setLayout(new GridLayout(0, 3, 25, 25)); 
            
           
            panelContenedorTarjetas.add(crearTarjetaAgregar());

            for (Rutina rutina : rutinas) {
                panelContenedorTarjetas.add(crearTarjetaRutina(rutina));
            }
            
            while (panelContenedorTarjetas.getComponentCount() < 3) {
                JPanel dummy = new JPanel();
                dummy.setOpaque(false);
                panelContenedorTarjetas.add(dummy);
            }
        }

        panelContenedorTarjetas.revalidate();
        panelContenedorTarjetas.repaint();
    }

    private void mostrarEstadoVacio() {
        panelContenedorTarjetas.setLayout(new GridBagLayout());
        
        JPanel panelVacio = new JPanel();
        panelVacio.setLayout(new BoxLayout(panelVacio, BoxLayout.Y_AXIS));
        panelVacio.setOpaque(false);
        
        JLabel icon = new JLabel("🏋️‍♂️", SwingConstants.CENTER);
        icon.setFont(new Font("SansSerif", Font.PLAIN, 80));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel title = new JLabel("No tienes rutinas activas");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(COLOR_TEXTO_PRINCIPAL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel sub = new JLabel("Crea tu primer plan de entrenamiento para empezar.");
        sub.setForeground(COLOR_TEXTO_SECUNDARIO);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        
        panelVacio.add(icon);
        panelVacio.add(Box.createVerticalStrut(10));
        panelVacio.add(title);
        panelVacio.add(Box.createVerticalStrut(5));
        panelVacio.add(sub);
        panelVacio.add(Box.createVerticalStrut(30));
        
        
        BotonModerno btnCrear = new BotonModerno("Crear Primera Rutina", COLOR_ACENTO, Color.WHITE);
        btnCrear.setPreferredSize(new Dimension(200, 50));
        btnCrear.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCrear.addActionListener(e -> mostrarDialogoCrearRutina());
        
        panelVacio.add(btnCrear);
        
        panelContenedorTarjetas.add(panelVacio);
    }

    
    
    private JPanel crearTarjetaAgregar() {
        TarjetaPanel card = new TarjetaPanel();
        card.setLayout(new BorderLayout());
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        JLabel lbl = new JLabel("<html><center><h1 style='color:#FF6600; font-size:40px;'>＋</h1><p style='color:white; font-size:14px;'>Crear Nueva<br>Rutina</p></center></html>", SwingConstants.CENTER);
        card.add(lbl, BorderLayout.CENTER);
        
        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { mostrarDialogoCrearRutina(); }
            public void mouseEntered(MouseEvent e) { card.setBackground(COLOR_TARJETA.brighter()); }
            public void mouseExited(MouseEvent e) { card.setBackground(COLOR_TARJETA); }
        });
        
        return card;
    }

    private JPanel crearTarjetaRutina(Rutina rutina) {
        TarjetaPanel card = new TarjetaPanel();
        card.setLayout(new BorderLayout());
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(220, 280));

        
        JLabel lblImg = new JLabel();
        lblImg.setHorizontalAlignment(SwingConstants.CENTER);
        lblImg.setPreferredSize(new Dimension(0, 140));
        lblImg.setOpaque(true);
        lblImg.setBackground(new Color(60, 60, 60)); 
        
        String imgName = rutina.getImagenNombre();
        if (imgName == null || imgName.isEmpty()) imgName = "icon_default.png";
        
        try {
            URL url = getClass().getResource("/resources/" + imgName);
            if (url != null) {
                ImageIcon icon = new ImageIcon(
                    new ImageIcon(url).getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH)
                );
                lblImg.setIcon(icon);
            } else {
                lblImg.setText(imgName); 
                lblImg.setForeground(Color.LIGHT_GRAY);
            }
        } catch (Exception e) { 
            lblImg.setText("Error Img"); 
            lblImg.setForeground(Color.RED);
        }
        
        card.add(lblImg, BorderLayout.NORTH);

       
        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setOpaque(false);
        info.setBorder(new EmptyBorder(5, 15, 0, 15));
        
        JLabel title = new JLabel(rutina.getNombreRutina());
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(COLOR_TEXTO_PRINCIPAL);
        
        int count = dbManager.contarItemsRutina(rutina.getIdRutina());
        JLabel sub = new JLabel(count + " Ejercicios • " + rutina.getDiaSemana());
        sub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        sub.setForeground(COLOR_TEXTO_SECUNDARIO);
        
        info.add(title);
        info.add(sub);
        card.add(info, BorderLayout.CENTER);

        
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setOpaque(false);
        
        BotonIcono btnEdit = new BotonIcono("✎", new Color(60, 60, 60));
        btnEdit.addActionListener(e -> editarRutina(rutina));
        
        BotonIcono btnDel = new BotonIcono("🗑", COLOR_ROJO.darker());
        btnDel.addActionListener(e -> eliminarRutina(rutina));
        
        actions.add(btnEdit);
        actions.add(btnDel);
        card.add(actions, BorderLayout.SOUTH);

        
        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { 
                if(e.getSource() == card) gestionarEjercicios(rutina); 
            }
            public void mouseEntered(MouseEvent e) { card.setBackground(COLOR_TARJETA_HOVER); }
            public void mouseExited(MouseEvent e) { card.setBackground(COLOR_TARJETA); }
        });

        return card;
    }

    

    private void mostrarDialogoCrearRutina() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        DialogoCrearRutina d = new DialogoCrearRutina(frame, usuarioActual, dbManager);
        d.setVisible(true);
        if (d.fueExitoso()) cargarRutinas();
    }

    private void editarRutina(Rutina r) {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        Object[] opciones = {"Editar Datos (Nombre/Img)", "Gestionar Ejercicios", "Cancelar"};
        int seleccion = JOptionPane.showOptionDialog(
                frame,
                "¿Qué deseas hacer con '" + r.getNombreRutina() + "'?",
                "Editar Rutina",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (seleccion == 0) {
            DialogoCrearRutina d = new DialogoCrearRutina(frame, usuarioActual, dbManager, r);
            d.setVisible(true);
            if (d.fueExitoso()) cargarRutinas();
        } else if (seleccion == 1) {
            gestionarEjercicios(r);
        }
    }

    private void eliminarRutina(Rutina r) {
        int opt = JOptionPane.showConfirmDialog(this, "¿Borrar " + r.getNombreRutina() + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            if (dbManager.eliminarRutina(r.getIdRutina())) cargarRutinas();
        }
    }
    
    
    private void gestionarEjercicios(Rutina rutina) {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);

        
        PanelDetalleRutina panel = new PanelDetalleRutina(
            frame,
            dbManager,
            rutina.getIdRutina(),
            rutina.getNombreRutina()
        );

        panel.setVisible(true);

        
        cargarRutinas();
    }

    

    private class TarjetaPanel extends JPanel {
        public TarjetaPanel() { 
            setOpaque(false); 
            setBackground(COLOR_TARJETA); 
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    private class BotonModerno extends JButton {
        private Color bg;
        public BotonModerno(String t, Color b, Color f) {
            super(t); this.bg = b; setForeground(f);
            setFocusPainted(false); setBorderPainted(false); setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setFont(new Font("SansSerif", Font.BOLD, 14));
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            super.paintComponent(g);
            g2.dispose();
        }
    }
    
    private class BotonIcono extends JButton {
        private Color bg;
        public BotonIcono(String t, Color b) {
            super(t); this.bg = b; setForeground(Color.WHITE);
            setFocusPainted(false); setBorderPainted(false); setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(30, 30));
            setFont(new Font("SansSerif", Font.BOLD, 12));
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillOval(0, 0, getWidth(), getHeight());
            super.paintComponent(g);
            g2.dispose();
        }
    }
}