package com.metricfit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.List;

public class DialogoSelectorEjercicio extends JDialog {

   
    private final Color COL_FONDO = new Color(30, 30, 30);
    private final Color COL_PANEL = new Color(45, 45, 45);
    private final Color COL_ACENTO = new Color(255, 102, 0);
    private final Color COL_TEXTO = Color.WHITE;
    private final Color COL_INPUT = new Color(60, 60, 60);

    
    private final DatabaseManager dbManager;
    private final int idRutina;
    private Ejercicio ejercicioSeleccionado = null;
    private String zonaSeleccionada = "Pecho"; 

    
    private JList<Ejercicio> listaEjercicios;
    private DefaultListModel<Ejercicio> modeloEjercicios;
    private JTextField txtSeries, txtReps, txtPeso;
    private JComboBox<String> comboTipoSerie;
    private JLabel lblPreviewEjercicio;
    private JPanel panelBotonesZonas;

    
    private final String[] ZONAS = {"Pecho", "Espalda", "Pierna", "Hombro", "Brazos", "Abdominales", "Cardio"};

    public DialogoSelectorEjercicio(Frame owner, DatabaseManager dbManager, int idRutina) {
        super(owner, "Añadir Ejercicio", true);
        this.dbManager = dbManager;
        this.idRutina = idRutina;

        setSize(1000, 650); 
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COL_FONDO);

       
        JPanel mainPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        mainPanel.setBackground(COL_FONDO);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        mainPanel.add(crearPanelZonas());
        mainPanel.add(crearPanelLista());
        mainPanel.add(crearPanelDetalles()); 

        add(mainPanel, BorderLayout.CENTER);
        
        cargarEjercicios(zonaSeleccionada);
    }

   
    private JPanel crearPanelZonas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        JLabel title = new JLabel("1. ZONA MUSCULAR");
        title.setForeground(COL_ACENTO);
        title.setFont(new Font("SansSerif", Font.BOLD, 14));
        title.setBorder(new EmptyBorder(0,0,15,0));
        panel.add(title, BorderLayout.NORTH);

        panelBotonesZonas = new JPanel(new GridLayout(7, 1, 0, 10)); 
        panelBotonesZonas.setOpaque(false);

        for (String zona : ZONAS) {
            JButton btn = crearBotonZona(zona);
            panelBotonesZonas.add(btn);
        }
        
        JScrollPane scroll = new JScrollPane(panelBotonesZonas);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JButton crearBotonZona(String zona) {
        JButton btn = new JButton(zona);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setForeground(COL_TEXTO);
        btn.setBackground(COL_PANEL);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        
        String imgName = "icon_" + zona.toLowerCase() + ".png"; 
        if(zona.equals("Brazos")) imgName = "icon_arm.png"; 
        if(zona.equals("Abdominales")) imgName = "icon_abs.png";

        try {
            URL url = getClass().getResource("/resources/" + imgName);
            if (url != null) {
                ImageIcon icon = new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
                btn.setIcon(icon);
                btn.setIconTextGap(15);
            }
        } catch (Exception e) {}

        btn.addActionListener(e -> {
            this.zonaSeleccionada = zona;
            cargarEjercicios(zona);
            actualizarEstiloBotones(btn);
        });
        return btn;
    }

    private void actualizarEstiloBotones(JButton activo) {
        for (Component c : panelBotonesZonas.getComponents()) {
            if (c instanceof JButton) {
                c.setBackground(COL_PANEL);
                ((JButton) c).setForeground(COL_TEXTO);
            }
        }
        activo.setBackground(COL_ACENTO);
        activo.setForeground(Color.WHITE);
    }

    // --- COLUMNA 2: LISTA ---
    private JPanel crearPanelLista() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        JLabel title = new JLabel("2. EJERCICIOS");
        title.setForeground(COL_ACENTO);
        title.setFont(new Font("SansSerif", Font.BOLD, 14));
        title.setBorder(new EmptyBorder(0,0,15,0));
        panel.add(title, BorderLayout.NORTH);

        modeloEjercicios = new DefaultListModel<>();
        listaEjercicios = new JList<>(modeloEjercicios);
        listaEjercicios.setBackground(COL_PANEL);
        listaEjercicios.setForeground(COL_TEXTO);
        listaEjercicios.setFont(new Font("SansSerif", Font.PLAIN, 14));
        listaEjercicios.setSelectionBackground(COL_ACENTO.darker());
        listaEjercicios.setSelectionForeground(Color.WHITE);
        listaEjercicios.setFixedCellHeight(40);
        
        listaEjercicios.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                ejercicioSeleccionado = listaEjercicios.getSelectedValue();
                actualizarPreview();
            }
        });

        JScrollPane scroll = new JScrollPane(listaEjercicios);
        scroll.setBorder(new LineBorder(COL_INPUT));
        panel.add(scroll, BorderLayout.CENTER);

        JButton btnNuevo = new JButton("＋ Crear Nuevo Ejercicio");
        btnNuevo.setBackground(COL_INPUT);
        btnNuevo.setForeground(COL_TEXTO);
        btnNuevo.setFocusPainted(false);
        btnNuevo.setPreferredSize(new Dimension(0, 40));
        btnNuevo.addActionListener(e -> crearEjercicioPersonalizado());
        panel.add(btnNuevo, BorderLayout.SOUTH);

        return panel;
    }

 
    private JPanel crearPanelDetalles() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); 
        panel.setBackground(COL_PANEL);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Título
        JLabel title = new JLabel("3. CONFIGURACIÓN");
        title.setForeground(COL_ACENTO);
        title.setFont(new Font("SansSerif", Font.BOLD, 14));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));

        // Preview del ejercicio (Nombre grande)
        lblPreviewEjercicio = new JLabel("Selecciona un ejercicio...");
        lblPreviewEjercicio.setForeground(Color.GRAY);
        lblPreviewEjercicio.setFont(new Font("SansSerif", Font.ITALIC, 14));
        lblPreviewEjercicio.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblPreviewEjercicio);
        panel.add(Box.createVerticalStrut(30));

        
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 15, 15)); 
        formPanel.setOpaque(false);
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.setMaximumSize(new Dimension(1000, 250)); 

      
        formPanel.add(crearLabelInput("Series:"));
        txtSeries = crearTextFieldInput("4");
        formPanel.add(txtSeries);

      
        formPanel.add(crearLabelInput("Repeticiones:"));
        txtReps = crearTextFieldInput("12");
        formPanel.add(txtReps);

       
        formPanel.add(crearLabelInput("Peso (kg):"));
        txtPeso = crearTextFieldInput("0");
        formPanel.add(txtPeso);

      
        formPanel.add(crearLabelInput("Tipo de Serie:"));
        comboTipoSerie = new JComboBox<>(new String[]{"Normal", "Biserie", "Triserie", "Gigante", "Drop Set"});
        comboTipoSerie.setBackground(COL_INPUT);
        comboTipoSerie.setForeground(COL_TEXTO);
        comboTipoSerie.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        formPanel.add(comboTipoSerie);

        panel.add(formPanel);
        
        
        panel.add(Box.createVerticalGlue()); 

       
        JButton btnAdd = new JButton("AÑADIR A RUTINA");
        btnAdd.setBackground(COL_ACENTO);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnAdd.setFocusPainted(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnAdd.setMaximumSize(new Dimension(1000, 50)); // Ancho total
        
        btnAdd.addActionListener(e -> agregarItemARutina());

        panel.add(btnAdd);

        return panel;
    }

   
    private JLabel crearLabelInput(String texto) {
        JLabel l = new JLabel(texto);
        l.setForeground(COL_TEXTO);
        l.setFont(new Font("SansSerif", Font.BOLD, 14));
        return l;
    }

    private JTextField crearTextFieldInput(String placeholder) {
        JTextField t = new JTextField(placeholder);
        t.setBackground(COL_INPUT);
        t.setForeground(COL_TEXTO);
        t.setCaretColor(Color.WHITE);
        t.setFont(new Font("SansSerif", Font.PLAIN, 16));
        t.setHorizontalAlignment(JTextField.CENTER);
        t.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.GRAY), 
            new EmptyBorder(5, 5, 5, 5)
        ));
        return t;
    }

    

    private void cargarEjercicios(String zona) {
        modeloEjercicios.clear();
        List<Ejercicio> lista = dbManager.obtenerEjerciciosPorZona(zona);
        for (Ejercicio ej : lista) modeloEjercicios.addElement(ej);
    }

    private void actualizarPreview() {
        if (ejercicioSeleccionado != null) {
            lblPreviewEjercicio.setText("<html><b style='color:white; font-size:16px'>" + ejercicioSeleccionado.getNombre() + "</b><br><span style='color:gray; font-size:12px'>" + ejercicioSeleccionado.getDescripcion() + "</span></html>");
            lblPreviewEjercicio.setForeground(Color.WHITE);
        }
    }

    private void crearEjercicioPersonalizado() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre del nuevo ejercicio (" + zonaSeleccionada + "):");
        if (nombre != null && !nombre.trim().isEmpty()) {
            Ejercicio nuevo = dbManager.crearEjercicioPersonalizado(nombre, zonaSeleccionada);
            if (nuevo != null) {
                cargarEjercicios(zonaSeleccionada);
                JOptionPane.showMessageDialog(this, "Ejercicio añadido.");
            }
        }
    }

    private void agregarItemARutina() {
        if (ejercicioSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "¡Debes seleccionar un ejercicio de la lista!");
            return;
        }
        try {
            int series = Integer.parseInt(txtSeries.getText());
            int reps = Integer.parseInt(txtReps.getText());
            double peso = Double.parseDouble(txtPeso.getText());
            String tipo = (String) comboTipoSerie.getSelectedItem();

            ItemRutina item = new ItemRutina(0, idRutina, ejercicioSeleccionado.getId(), 
                                             ejercicioSeleccionado.getNombre(), series, reps, peso, tipo, 0);
            
            if (dbManager.insertarItemRutina(item)) {
                JOptionPane.showMessageDialog(this, "¡Ejercicio añadido correctamente!");
                
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar en BD.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa números válidos en Series, Reps y Peso.");
        }
    }
}