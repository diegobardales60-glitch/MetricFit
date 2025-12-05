package com.metricfit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelConfiguracionItem extends JPanel {

    
    private final PanelConstructorRutina parentPanel; 
    private final DatabaseManager dbManager; 
    private ItemRutina item;
    private final boolean esNuevo;

    private JTextField txtSeries;
    private JTextField txtReps;
    private JTextField txtPeso;
    private JTextArea txtNotas;
    private JButton btnGuardar;
    private JButton btnEliminar;

  
    private final Color COLOR_PANEL_FONDO = new Color(45, 45, 45);
    private final Color COLOR_TEXTO = Color.WHITE;
    private final Color COLOR_FONDO_INPUT = new Color(36, 36, 36);
    private final Color COLOR_NARANJA = new Color(255, 102, 0);

    
    public PanelConfiguracionItem(PanelConstructorRutina parent, Ejercicio ejercicio, Rutina rutina, DatabaseManager dbManager) {
       
        this(parent, new ItemRutina(rutina.getIdRutina(), ejercicio), true, dbManager);
    }
    
    
    public PanelConfiguracionItem(PanelConstructorRutina parent, ItemRutina itemExistente, DatabaseManager dbManager) {
       
        this(parent, itemExistente, false, dbManager);
    }
    
   
    public PanelConfiguracionItem(PanelConstructorRutina parent, ItemRutina itemExistente, Rutina rutina, DatabaseManager dbManager) {
        
        this(parent, itemExistente, dbManager); 
    }
    
    
    private PanelConfiguracionItem(PanelConstructorRutina parent, ItemRutina item, boolean esNuevo, DatabaseManager dbManager) {
        this.parentPanel = parent;
        this.item = item;
        this.esNuevo = esNuevo;
        this.dbManager = dbManager;

        setLayout(new BorderLayout(10, 10));
        setBackground(COLOR_PANEL_FONDO);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        inicializarComponentes();
        cargarDatosItem();
        agregarListeners();
    }

    private void inicializarComponentes() {
        txtSeries = crearCampoTexto();
        txtReps = crearCampoTexto();
        txtPeso = crearCampoTexto();
        txtNotas = new JTextArea(3, 20);
        txtNotas.setBackground(COLOR_FONDO_INPUT);
        txtNotas.setForeground(COLOR_TEXTO);
        txtNotas.setLineWrap(true);
        
        btnGuardar = crearBotonEstilizado(esNuevo ? "AÑADIR A RUTINA" : "ACTUALIZAR", COLOR_NARANJA);
        btnEliminar = crearBotonEstilizado("QUITAR DE RUTINA", Color.RED.darker());
    }

    private void cargarDatosItem() {
       
        JLabel lblTitulo = new JLabel("Configurar: " + item.getEjercicio().getNombre(), SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(COLOR_TEXTO);
        add(lblTitulo, BorderLayout.NORTH);

     
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(COLOR_PANEL_FONDO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        
       
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3; panelForm.add(crearEtiqueta("Series Objetivo:"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 0.7; panelForm.add(txtSeries, gbc);
        txtSeries.setText(item.getSeriesObjetivo());

        
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3; panelForm.add(crearEtiqueta("Reps Objetivo:"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 0.7; panelForm.add(txtReps, gbc);
        txtReps.setText(item.getRepsObjetivo());

      
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3; panelForm.add(crearEtiqueta("Peso (kg):"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 0.7; panelForm.add(txtPeso, gbc);
        txtPeso.setText(item.getPesoObjetivo());

        
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3; panelForm.add(crearEtiqueta("Notas:"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 0.7; 
        
       
        JScrollPane scrollNotas = new JScrollPane(txtNotas);
        scrollNotas.setBorder(BorderFactory.createLineBorder(COLOR_FONDO_INPUT.darker(), 1));
        panelForm.add(scrollNotas, gbc);
        
        txtNotas.setText(item.getNotas());
        
        add(panelForm, BorderLayout.CENTER);

     
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelBotones.setBackground(COLOR_PANEL_FONDO);
        panelBotones.add(btnGuardar);
        if (!esNuevo) {
            panelBotones.add(btnEliminar);
        }
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void agregarListeners() {
        btnGuardar.addActionListener(e -> guardarItem());
        
        if (!esNuevo) {
            btnEliminar.addActionListener(e -> eliminarItem());
        }
    }

    private void guardarItem() {
       
        item.setSeriesObjetivo(txtSeries.getText().trim());
        item.setRepsObjetivo(txtReps.getText().trim());
        item.setPesoObjetivo(txtPeso.getText().trim());
        item.setNotas(txtNotas.getText().trim());
        
        boolean exito = false;

        if (esNuevo) {
           
            item.setOrden(parentPanel.modeloEjerciciosEnRutina.getSize() + 1);
            
           
            int nuevoId = dbManager.agregarItemARutina(item); 
            if (nuevoId != -1) {
                item.setIdItem(nuevoId); 
                exito = true;
            } 
        } else {
            
            exito = dbManager.actualizarItemRutina(item);
        }

        if (exito) {
            
            parentPanel.agregarOActualizarItemEnLista(item);
        } else {
             JOptionPane.showMessageDialog(this, "Error al guardar el ítem en la base de datos.", "Error de BD", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void eliminarItem() {
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Quitar el ejercicio '" + item.getEjercicio().getNombre() + "' de la rutina?",
            "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
            
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (dbManager.quitarItemDeRutina(item.getIdItem())) {
                 
                 parentPanel.modeloEjerciciosEnRutina.removeElement(item);
                 
                
                 parentPanel.panelDetalleEjercicio.removeAll();
                 parentPanel.panelDetalleEjercicio.revalidate();
                 parentPanel.panelDetalleEjercicio.repaint();
                 
                 JOptionPane.showMessageDialog(this, "Ejercicio quitado de la rutina.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                 
            } else {
                 JOptionPane.showMessageDialog(this, "Error al eliminar el ítem de la base de datos.", "Error de BD", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    
    private JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(COLOR_TEXTO);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        return lbl;
    }
    
    private JTextField crearCampoTexto() {
        JTextField txt = new JTextField(5);
        txt.setBackground(COLOR_FONDO_INPUT);
        txt.setForeground(COLOR_TEXTO);
        txt.setCaretColor(COLOR_TEXTO);
        txt.setBorder(BorderFactory.createLineBorder(COLOR_FONDO_INPUT.darker(), 1));
        return txt;
    }
    
    private JButton crearBotonEstilizado(String texto, Color bgColor) {
        JButton boton = new JButton(texto);
        boton.setBackground(bgColor);
        boton.setForeground(COLOR_TEXTO);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setPreferredSize(new Dimension(170, 35));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }
}