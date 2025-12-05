package com.metricfit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PanelConstructorRutina extends JPanel {

    private final Usuario usuarioActual;
    private final VentanaPrincipal ventanaPrincipal;
    private final DatabaseManager dbManager; 
    private Rutina rutinaActual; 
    
   
    private List<Ejercicio> todosLosEjercicios; 

  
    private JList<Ejercicio> listaEjerciciosDisponibles;
    private DefaultListModel<Ejercicio> modeloEjerciciosDisponibles;
    private JList<ItemRutina> listaEjerciciosEnRutina;
    DefaultListModel<ItemRutina> modeloEjerciciosEnRutina;
    
    private JComboBox<String> comboFiltroZonaMuscular;
    private JTextField txtBuscarEjercicio;
    private JButton btnGuardarRutina;
    
    
    JPanel panelDetalleEjercicio; 

    
    private final Color COLOR_FONDO = new Color(36, 36, 36);
    private final Color COLOR_PANEL_FONDO = new Color(45, 45, 45);
    private final Color COLOR_BORDE = new Color(80, 80, 80);
    private final Color COLOR_TEXTO = Color.WHITE;
    private final Color COLOR_PLACEHOLDER = new Color(150, 150, 150);
    private final Color COLOR_NARANJA = new Color(255, 102, 0);
    private final Color COLOR_NARANJA_CLARO = new Color(255, 140, 60);

 
    public PanelConstructorRutina(Usuario usuario, VentanaPrincipal vp, DatabaseManager dbManager) {
        this.usuarioActual = usuario;
        this.ventanaPrincipal = vp;
        this.dbManager = dbManager; 
        this.todosLosEjercicios = new ArrayList<>(); 

        setName("PanelConstructorRutina"); 

        setBackground(COLOR_FONDO);
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(30, 30, 30, 30));

        inicializarComponentes();
       
        configurarLayout("Seleccionada"); 
        agregarListeners();
    }
    
    public void cargarRutina(Rutina nuevaRutina) {
        this.rutinaActual = nuevaRutina;
        
     
        removeAll();
        configurarLayout(nuevaRutina.getNombreRutina());
        
        
        panelDetalleEjercicio.removeAll(); 
        
        cargarDatos(); 
        
        revalidate();
        repaint();
    }
    
   
    private void configurarLayout(String nombreRutina) {
        
        JLabel lblTitulo = new JLabel("Construyendo Rutina: " + nombreRutina, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitulo.setForeground(COLOR_NARANJA);
        add(lblTitulo, BorderLayout.NORTH);

       
        JPanel panelIzquierdo = new JPanel(new BorderLayout(10, 10));
        panelIzquierdo.setBackground(COLOR_FONDO);
        
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelFiltros.setBackground(COLOR_FONDO);
        panelFiltros.add(crearEtiqueta("Zona Muscular:"));
        personalizarComboBox(comboFiltroZonaMuscular);
        panelFiltros.add(comboFiltroZonaMuscular);
        
        personalizarCampoTexto(txtBuscarEjercicio, "Buscar ejercicio...", true);
        panelFiltros.add(txtBuscarEjercicio);
        panelIzquierdo.add(panelFiltros, BorderLayout.NORTH);
        
       
        JSplitPane splitEjerciciosDetalle = new JSplitPane(JSplitPane.VERTICAL_SPLIT, 
                                                           new JScrollPane(listaEjerciciosDisponibles), 
                                                           panelDetalleEjercicio);
        splitEjerciciosDetalle.setDividerLocation(0.6); // 60% lista, 40% detalle
        splitEjerciciosDetalle.setResizeWeight(0.6);
        splitEjerciciosDetalle.setBorder(null);

        panelIzquierdo.add(splitEjerciciosDetalle, BorderLayout.CENTER);


      
        JPanel panelDerecho = new JPanel(new BorderLayout(10, 10));
        panelDerecho.setBackground(COLOR_FONDO);
        
        JLabel lblRutinaActual = new JLabel("Ejercicios en " + nombreRutina, SwingConstants.LEFT);
        lblRutinaActual.setFont(new Font("Arial", Font.BOLD, 20));
        lblRutinaActual.setForeground(COLOR_TEXTO);
        panelDerecho.add(lblRutinaActual, BorderLayout.NORTH);
        
        panelDerecho.add(new JScrollPane(listaEjerciciosEnRutina), BorderLayout.CENTER);
        
        JPanel panelGuardar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelGuardar.setBackground(COLOR_FONDO);
        panelGuardar.add(btnGuardarRutina);
        panelDerecho.add(panelGuardar, BorderLayout.SOUTH);

       
        JSplitPane splitPanePrincipal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelIzquierdo, panelDerecho);
        splitPanePrincipal.setDividerLocation(0.5); 
        splitPanePrincipal.setResizeWeight(0.5);
        splitPanePrincipal.setBackground(COLOR_FONDO);
        splitPanePrincipal.setBorder(null);

        add(splitPanePrincipal, BorderLayout.CENTER);
    }

    private void inicializarComponentes() {
        modeloEjerciciosDisponibles = new DefaultListModel<>();
        listaEjerciciosDisponibles = new JList<>(modeloEjerciciosDisponibles);
      
        listaEjerciciosDisponibles.setCellRenderer(new EjercicioListCellRenderer()); 
        personalizarLista(listaEjerciciosDisponibles);

        modeloEjerciciosEnRutina = new DefaultListModel<>();
        listaEjerciciosEnRutina = new JList<>(modeloEjerciciosEnRutina);
        
        listaEjerciciosEnRutina.setCellRenderer(new ItemRutinaListCellRenderer()); 
        personalizarLista(listaEjerciciosEnRutina);
        
        comboFiltroZonaMuscular = new JComboBox<>();
        txtBuscarEjercicio = new JTextField("Buscar ejercicio...");
        
        btnGuardarRutina = crearBotonEstilizado("GUARDAR Y SALIR", COLOR_NARANJA);
        
        panelDetalleEjercicio = new JPanel(new BorderLayout()); 
        panelDetalleEjercicio.setBackground(COLOR_PANEL_FONDO);
        panelDetalleEjercicio.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1));
    }

    private void cargarDatos() {
        if (rutinaActual == null) return; 
        
       
        todosLosEjercicios = dbManager.obtenerTodosLosEjercicios();
        
       
        comboFiltroZonaMuscular.removeAllItems();
        comboFiltroZonaMuscular.addItem("Todas las Zonas");
        todosLosEjercicios.stream()
             .map(Ejercicio::getZonaMuscular)
             .distinct()
             .sorted()
             .forEach(comboFiltroZonaMuscular::addItem);

        
        filtrarEjercicios();
        
        
        modeloEjerciciosEnRutina.clear();
        List<ItemRutina> items = dbManager.obtenerItemsDeRutina(rutinaActual.getIdRutina());
        for (ItemRutina item : items) {
            modeloEjerciciosEnRutina.addElement(item);
        }
    }

    
    private void agregarListeners() {
        
        listaEjerciciosDisponibles.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && listaEjerciciosDisponibles.getSelectedValue() != null) {
               
                mostrarPanelConfiguracionItem(listaEjerciciosDisponibles.getSelectedValue(), null, true);
            }
        });
        
     
        listaEjerciciosEnRutina.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && listaEjerciciosEnRutina.getSelectedValue() != null) {
                
                mostrarPanelConfiguracionItem(null, listaEjerciciosEnRutina.getSelectedValue(), false);
            }
        });

       
        comboFiltroZonaMuscular.addActionListener(e -> filtrarEjercicios());
        
       
        txtBuscarEjercicio.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarEjercicios();
            }
        });

       
        btnGuardarRutina.addActionListener(e -> guardarRutina());
    }
    
    
    private void filtrarEjercicios() {
        String zonaSeleccionada = (String) comboFiltroZonaMuscular.getSelectedItem();
        String textoBusqueda = txtBuscarEjercicio.getText().trim();
        
        
        if (textoBusqueda.equals("Buscar ejercicio...")) {
            textoBusqueda = "";
        }
        final String busqueda = textoBusqueda.toLowerCase();

        List<Ejercicio> filtrada = todosLosEjercicios.stream()
            .filter(ej -> "Todas las Zonas".equals(zonaSeleccionada) || ej.getZonaMuscular().equals(zonaSeleccionada))
            .filter(ej -> busqueda.isEmpty() || ej.getNombre().toLowerCase().contains(busqueda))
            .collect(Collectors.toList());

        modeloEjerciciosDisponibles.clear();
        for (Ejercicio ej : filtrada) {
            modeloEjerciciosDisponibles.addElement(ej);
        }
    }
    
 
    private void mostrarPanelConfiguracionItem(Ejercicio ejercicio, ItemRutina item, boolean esNuevo) {
        panelDetalleEjercicio.removeAll();
        
        if (rutinaActual == null) {
           
            JOptionPane.showMessageDialog(this, "Debe seleccionar o crear una rutina primero.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PanelConfiguracionItem panel;
        if (esNuevo && ejercicio != null) {
            
            panel = new PanelConfiguracionItem(this, ejercicio, rutinaActual, dbManager);
        } else if (!esNuevo && item != null) {
           
            panel = new PanelConfiguracionItem(this, item, rutinaActual, dbManager);
        } else {
             
            return;
        }
        
        panelDetalleEjercicio.add(panel, BorderLayout.CENTER);
        panelDetalleEjercicio.revalidate();
        panelDetalleEjercicio.repaint();
    }
    
    
    void agregarOActualizarItemEnLista(ItemRutina item) {
      
        boolean existe = false;
        for (int i = 0; i < modeloEjerciciosEnRutina.getSize(); i++) {
            if (modeloEjerciciosEnRutina.getElementAt(i).getIdItem() == item.getIdItem()) {
               
                modeloEjerciciosEnRutina.set(i, item);
                existe = true;
                break;
            }
        }
        
       
        if (!existe) {
            modeloEjerciciosEnRutina.addElement(item);
        }

       
        panelDetalleEjercicio.removeAll();
        panelDetalleEjercicio.revalidate();
        panelDetalleEjercicio.repaint();
        JOptionPane.showMessageDialog(this, "Ítem guardado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
    
    
    private void guardarRutina() {
        
        JOptionPane.showMessageDialog(this, 
                                      "Rutina '" + rutinaActual.getNombreRutina() + "' guardada. ¡A entrenar!", 
                                      "Rutina Finalizada", 
                                      JOptionPane.INFORMATION_MESSAGE);
        
        
        if(ventanaPrincipal != null) {
           
            ventanaPrincipal.manejarNavegacion("PanelRutinas"); 
        } else {
            System.err.println("Error: La referencia a VentanaPrincipal es nula. No se puede navegar.");
        }
    } 

   
    private void personalizarLista(JList<?> lista) {
        lista.setBackground(COLOR_PANEL_FONDO);
        lista.setForeground(COLOR_TEXTO);
        lista.setFont(new Font("Arial", Font.PLAIN, 16));
        lista.setSelectionBackground(COLOR_NARANJA_CLARO.darker());
        lista.setSelectionForeground(Color.WHITE);
        lista.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1));
        lista.setFixedCellHeight(70); 
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(COLOR_TEXTO);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        return lbl;
    }
    
    private void personalizarComboBox(JComboBox<String> combo) {
        combo.setBackground(COLOR_PANEL_FONDO);
        combo.setForeground(COLOR_TEXTO);
        combo.setFont(new Font("Arial", Font.PLAIN, 14));
        combo.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1));
    }
    
    private void personalizarCampoTexto(JTextField campo, String placeholder, boolean esBusqueda) {
        campo.setBackground(COLOR_PANEL_FONDO);
        campo.setForeground(COLOR_PLACEHOLDER); 
        campo.setCaretColor(COLOR_TEXTO); 
        campo.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1));
        campo.setPreferredSize(new Dimension(150, 30));
        
       
        campo.setText(placeholder);
        campo.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                    campo.setForeground(COLOR_TEXTO);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setText(placeholder);
                    campo.setForeground(COLOR_PLACEHOLDER);
                }
            }
        });
    }

    private JButton crearBotonEstilizado(String texto, Color bgColor) {
        JButton boton = new JButton(texto);
        boton.setBackground(bgColor);
        boton.setForeground(COLOR_TEXTO);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setPreferredSize(new Dimension(180, 40));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1)); 
        return boton;
    }
}