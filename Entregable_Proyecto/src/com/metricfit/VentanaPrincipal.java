package com.metricfit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Random;

public class VentanaPrincipal extends JFrame {

    
    public static final String PANEL_DASHBOARD = "Dashboard";
    public static final String PANEL_RUTINAS = "MisRutinas";    
    public static final String PANEL_PERFIL = "Perfil";
    public static final String PANEL_CALORIAS = "Calorias";
    public static final String PANEL_CONSTRUCTOR_RUTINA = "ConstructorRutina";      

   
    private JPanel cardsPanel; 
    private CardLayout cardLayout;
    private Usuario usuarioActual;
    private PanelPerfil panelPerfil; 
    private PanelRutinas panelRutinas;
    
    
    private JButton btnPerfil, btnCerrarSesion;
    private JLabel lblFraseMotivadora;
    
    
    private final Color COLOR_FONDO_BASE = new Color(36, 36, 36); 
    private final Color COLOR_PANEL_FONDO = new Color(45, 45, 45);
    private final Color COLOR_BORDE = new Color(60, 60, 60); 
    private final Color COLOR_TEXTO_PRINCIPAL = Color.WHITE;
    private final Color COLOR_TEXTO_SECUNDARIO = new Color(180, 180, 180); 
    private final Color COLOR_ACENTO = new Color(255, 102, 0);
    private final Color COLOR_ACENTO_HOVER = new Color(255, 140, 60);
    private final Color COLOR_CERRAR_SESION = new Color(220, 50, 50);
    private final Color COLOR_CERRAR_SESION_HOVER = new Color(255, 70, 70);

   
    private static final String[] FRASES_MOTIVADORAS = {
        "EL DOLOR DE HOY ES LA FUERZA DE MAÑANA.",
        "NO PARES CUANDO TE CANSES, PARA CUANDO TERMINES.",
        "LA DISCIPLINA ES EL PUENTE ENTRE METAS Y LOGROS.",
        "TU ÚNICO LÍMITE ES TU MENTE.",
        "CADA REPETICIÓN CUENTA, CADA COMIDA SUMA.",
        "TRANSFORMA TUS EXCUSAS EN RESULTADOS."
    };
    
    private Timer timerMotivacion;
    private DatabaseManager dbManager;

    
    
    public VentanaPrincipal(Usuario usuario, DatabaseManager db) {
        this.usuarioActual = usuario; 
        this.dbManager = db;
        
        setTitle("MetricFit - Panel Principal de " + usuarioActual.getNombreUsuario());
        setSize(1200, 800);     
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO_BASE); 
        setLayout(new BorderLayout(10, 10));        
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        inicializarComponentes();
        configurarLayout();

        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);
        cardsPanel.setBackground(COLOR_FONDO_BASE);
        add(cardsPanel, BorderLayout.CENTER);
        
        inicializarPaneles();
        iniciarTimerMotivacion();
        
        cardLayout.show(cardsPanel, PANEL_DASHBOARD);
    }
    
   
    private void inicializarPaneles() {
        cardsPanel.add(crearPanelDashboard(), PANEL_DASHBOARD);
        cardsPanel.add(crearPanelCalorias(), PANEL_CALORIAS);
        
        // Perfil
        panelPerfil = new PanelPerfil(usuarioActual, cardLayout, cardsPanel, dbManager);
        cardsPanel.add(panelPerfil, PANEL_PERFIL);
        
        // Rutinas
        panelRutinas = new PanelRutinas(usuarioActual, this, dbManager);
        cardsPanel.add(panelRutinas, PANEL_RUTINAS);
        
        // Placeholder
        cardsPanel.add(new PlaceholderPanel(PANEL_CONSTRUCTOR_RUTINA), PANEL_CONSTRUCTOR_RUTINA);
    }
    
   
    
    private void inicializarComponentes() {
        btnPerfil = crearBotonNavegacion("PERFIL", COLOR_ACENTO, new Dimension(120, 40)); 
        btnCerrarSesion = crearBotonNavegacion("CERRAR SESIÓN", COLOR_CERRAR_SESION, new Dimension(160, 40)); 

        lblFraseMotivadora = new JLabel("Cargando motivación...", SwingConstants.CENTER);
        lblFraseMotivadora.setForeground(COLOR_ACENTO_HOVER); 
        lblFraseMotivadora.setFont(new Font("Arial", Font.BOLD, 18)); 
    }
    
    private void configurarLayout() {
        
        JPanel panelHeader = new JPanel(new BorderLayout(10, 10));
        panelHeader.setBackground(COLOR_PANEL_FONDO);
        panelHeader.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); 

        
        JLabel lblLogo = new JLabel();
        Dimension logoSize = new Dimension(220, 55); 
        lblLogo.setPreferredSize(logoSize); 
        
        try {
            URL logoURL = getClass().getResource("/resources/logo_metricfit.png"); 
            if (logoURL != null) {
                ImageIcon originalIcon = new ImageIcon(logoURL);
                Image image = originalIcon.getImage().getScaledInstance(
                    logoSize.width, logoSize.height, Image.SCALE_SMOOTH); 
                lblLogo.setIcon(new ImageIcon(image));
            } else {
                lblLogo.setText("MetricFit");
                lblLogo.setForeground(Color.WHITE);
                lblLogo.setFont(new Font("Arial", Font.BOLD, 24));
            }
        } catch (Exception e) {}
        panelHeader.add(lblLogo, BorderLayout.WEST);
        
        
        JPanel panelCentralContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15)); 
        panelCentralContainer.setOpaque(false);
        panelCentralContainer.add(lblFraseMotivadora);
        panelHeader.add(panelCentralContainer, BorderLayout.CENTER);
        
       
        JPanel panelEste = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 8)); 
        panelEste.setBackground(COLOR_PANEL_FONDO);
        
        btnPerfil.addActionListener(e -> manejarNavegacion(PANEL_PERFIL));
        panelEste.add(btnPerfil);
        
        btnCerrarSesion.addActionListener(e -> manejarCerrarSesion());
        panelEste.add(btnCerrarSesion); 
        
        panelHeader.add(panelEste, BorderLayout.EAST);
        add(panelHeader, BorderLayout.NORTH);
    }

   
    
    void manejarNavegacion(String panelKey) {
        if (panelKey.equals(PANEL_DASHBOARD)) {
            cardLayout.show(cardsPanel, PANEL_DASHBOARD);
            return;
        }
        
        if (panelKey.equals(PANEL_CALORIAS)) {
            cardLayout.show(cardsPanel, PANEL_CALORIAS);
            return;
        }
        
        if (panelKey.equals(PANEL_PERFIL)) {
            if (panelPerfil != null) panelPerfil.recargarDatos(); 
            cardLayout.show(cardsPanel, PANEL_PERFIL);
            return;
        }
        
        if (panelKey.equals(PANEL_RUTINAS)) {
            if (panelRutinas != null) panelRutinas.recargarDatos();
            cardLayout.show(cardsPanel, PANEL_RUTINAS);
            return;
        }
        
        if (panelKey.equals(PANEL_CONSTRUCTOR_RUTINA)) {
             cardLayout.show(cardsPanel, panelKey); 
             return;
        }
        
        cardLayout.show(cardsPanel, PANEL_DASHBOARD);
    }

   

    private JPanel crearPanelDashboard() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(COLOR_FONDO_BASE); 
        panel.setName(PANEL_DASHBOARD); 
        panel.setBorder(new EmptyBorder(30, 60, 30, 60)); 

        JPanel panelTitulo = new JPanel(new GridLayout(2, 1, 0, 5));
        panelTitulo.setOpaque(false);
        
        JLabel lblBienvenida = new JLabel("BIENVENIDO, " + usuarioActual.getNombreUsuario().toUpperCase() + "!", SwingConstants.CENTER); 
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 46)); 
        lblBienvenida.setForeground(COLOR_TEXTO_PRINCIPAL); 
        
        JLabel lblPregunta = new JLabel("¿Qué camino tomarás hoy?", SwingConstants.CENTER); 
        lblPregunta.setFont(new Font("Arial", Font.PLAIN, 24));
        lblPregunta.setForeground(COLOR_ACENTO); 
        
        panelTitulo.add(lblBienvenida);
        panelTitulo.add(lblPregunta);
        panel.add(panelTitulo, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 40, 0));
        panelCentral.setOpaque(false);
        panelCentral.setBorder(new EmptyBorder(40, 0, 40, 0));
        
     
        
        panelCentral.add(crearTarjetaDashboard(
            "MIS RUTINAS", 
            "Diseña y gestiona tu plan de entrenamiento semanal. Crea rutinas personalizadas, organiza tus ejercicios por grupo muscular y lleva un control total de tus series y repeticiones.", 
            "gym_icon.png", 
            PANEL_RUTINAS
        ));
        
        panelCentral.add(crearTarjetaDashboard(
            "CONTADOR DE CALORÍAS", 
            "Controla tu nutrición al detalle. Registra cada comida del día, visualiza tu consumo calórico en tiempo real y asegúrate de cumplir con tus macros para alcanzar tus objetivos.", 
            "food_icon.png", 
            PANEL_CALORIAS
        ));
        
        panel.add(panelCentral, BorderLayout.CENTER);
        
        JLabel lblProgreso = new JLabel("<< MANTÉN LA CONSTANCIA PARA VER RESULTADOS >>", SwingConstants.CENTER);
        lblProgreso.setFont(new Font("Arial", Font.BOLD, 16));
        lblProgreso.setForeground(COLOR_ACENTO);
        lblProgreso.setBackground(COLOR_PANEL_FONDO);
        lblProgreso.setOpaque(true);
        lblProgreso.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.add(lblProgreso, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearTarjetaDashboard(String titulo, String descripcion, String iconoNombre, String panelDestino) {
        final int cornerRadius = 20;
        JPanel panel = new JPanel(new BorderLayout(20, 20)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
                g2.dispose();
            }
            @Override protected void paintBorder(Graphics g) { }
        };
        
        panel.setBackground(COLOR_PANEL_FONDO);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.setOpaque(false); 

       
        JPanel headerCard = new JPanel(new BorderLayout(20, 0));
        headerCard.setOpaque(false);
        
        JLabel lblIcono = new JLabel();
        try {
            URL iconURL = getClass().getResource("/resources/" + iconoNombre);
            if (iconURL != null) {
                ImageIcon originalIcon = new ImageIcon(iconURL);
                Image image = originalIcon.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH); 
                lblIcono.setIcon(new ImageIcon(image));
            }
        } catch (Exception e) {}
        lblIcono.setPreferredSize(new Dimension(90, 90)); 
        headerCard.add(lblIcono, BorderLayout.WEST);

        
        JPanel textContainer = new JPanel(new BorderLayout(5, 10));
        textContainer.setOpaque(false);
        
        JLabel lblTit = new JLabel(titulo, SwingConstants.LEFT);
        lblTit.setFont(new Font("Arial", Font.BOLD, 22));
        lblTit.setForeground(COLOR_ACENTO);
        
        JTextArea txtDesc = new JTextArea(descripcion);
        txtDesc.setFont(new Font("Arial", Font.PLAIN, 15));
        txtDesc.setForeground(COLOR_TEXTO_SECUNDARIO);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setLineWrap(true);
        txtDesc.setEditable(false);
        txtDesc.setOpaque(false);
        txtDesc.setHighlighter(null); 
        
        textContainer.add(lblTit, BorderLayout.NORTH);
        textContainer.add(txtDesc, BorderLayout.CENTER);
        
        headerCard.add(textContainer, BorderLayout.CENTER);
        panel.add(headerCard, BorderLayout.NORTH); 

        
        JButton btnAccion = crearBotonNavegacion("IR AL MÓDULO", COLOR_ACENTO, new Dimension(200, 45));
        btnAccion.setFont(new Font("Arial", Font.BOLD, 16));
        btnAccion.addActionListener(e -> manejarNavegacion(panelDestino));
        
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setOpaque(false);
        footerPanel.add(btnAccion);
        panel.add(footerPanel, BorderLayout.SOUTH);

       
        panel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { panel.setBackground(COLOR_PANEL_FONDO.brighter()); panel.repaint(); }
            public void mouseExited(MouseEvent e) { panel.setBackground(COLOR_PANEL_FONDO); panel.repaint(); }
            public void mouseClicked(MouseEvent e) { if (e.getComponent() == panel) manejarNavegacion(panelDestino); }
        });

        return panel;
    }

    
    
    private JButton crearBotonNavegacion(String texto, Color bgColor, Dimension size) {
        JButton boton = new JButton(texto);
        boton.setBackground(bgColor);
        boton.setForeground(COLOR_TEXTO_PRINCIPAL);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false); 
        boton.setPreferredSize(size);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        boton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { boton.setBackground(bgColor.brighter()); }
            public void mouseExited(MouseEvent e) { boton.setBackground(bgColor); }
        });
        return boton;
    }
    
    private JPanel crearPanelCalorias() {
        return new PanelCalorias(usuarioActual, dbManager);
    }
    
    private class PlaceholderPanel extends JPanel {
        public PlaceholderPanel(String name) {
            this.setName(name);
            this.setBackground(COLOR_FONDO_BASE);
        }
    }

    private void iniciarTimerMotivacion() {
        Random random = new Random();
        timerMotivacion = new Timer(5000, e -> {
            lblFraseMotivadora.setText(FRASES_MOTIVADORAS[random.nextInt(FRASES_MOTIVADORAS.length)]);
        });
        timerMotivacion.start();
    }
    
    private void manejarCerrarSesion() {
        int opt = JOptionPane.showConfirmDialog(this, "¿Cerrar Sesión?", "Salir", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            if (timerMotivacion != null) timerMotivacion.stop();
            dispose(); 
            System.exit(0); 
        }
    }
    
    
    public void notificarActualizacionGlobal() {
        Component[] comps = cardsPanel.getComponents();
        for (Component c : comps) {
            if (c instanceof PanelCalorias) {
                ((PanelCalorias) c).cargarDatos(); 
            }
        }
    }
}