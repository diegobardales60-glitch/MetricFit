package com.metricfit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PanelDetalleRutina extends JDialog {

    private final DatabaseManager dbManager;
    private final int idRutina;
    private final String nombreRutina;
    
   
    private JPanel panelListaEjercicios;
    private JPanel panelEdicion;
    
   
    private JLabel lblNombreEjercicio;
    private JTextField txtSeries, txtReps, txtPeso;
    private JTextArea txtNotas;
    private JButton btnGuardar;
    private ItemRutina itemSeleccionado;

   
    private final Color COL_FONDO_LISTA = new Color(30, 30, 30);
    private final Color COL_FONDO_EDIT = new Color(40, 40, 40);
    private final Color COL_ACENTO = new Color(255, 102, 0);
    private final Color COL_TEXTO = Color.WHITE;
    private final Color COL_INPUT = new Color(60, 60, 60);

    public PanelDetalleRutina(Frame parent, DatabaseManager db, int idRutina, String nombre) {
        super(parent, "Gestión de Rutina: " + nombre, true);
        this.dbManager = db;
        this.idRutina = idRutina;
        this.nombreRutina = nombre;

        setSize(950, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        
        inicializarUI();
        cargarEjercicios();
    }

    private void inicializarUI() {
        
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(45, 45, 45));
        header.setBorder(new EmptyBorder(15, 25, 15, 25));
        
        JLabel title = new JLabel(nombreRutina.toUpperCase());
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(COL_ACENTO);
        header.add(title, BorderLayout.WEST);
        
        JButton btnAgregar = new JButton("＋ Agregar Ejercicio");
        btnAgregar.setBackground(COL_ACENTO);
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAgregar.addActionListener(e -> agregarNuevoEjercicio());
        header.add(btnAgregar, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerLocation(400); 
        split.setDividerSize(2);
        split.setBorder(null);
        
        
        panelListaEjercicios = new JPanel();
        panelListaEjercicios.setLayout(new BoxLayout(panelListaEjercicios, BoxLayout.Y_AXIS));
        panelListaEjercicios.setBackground(COL_FONDO_LISTA);
        panelListaEjercicios.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollLista = new JScrollPane(panelListaEjercicios);
        scrollLista.setBorder(null);
        split.setLeftComponent(scrollLista);

        
        panelEdicion = new JPanel(null); 
        panelEdicion.setBackground(COL_FONDO_EDIT);
        inicializarPanelEdicion();
        split.setRightComponent(panelEdicion);

        add(split, BorderLayout.CENTER);
    }

    private void inicializarPanelEdicion() {
        int xLabel = 30;
        int xInput = 140; 
        int y = 30;
        int gap = 50;

        JLabel lblTitulo = new JLabel("EDITAR EJERCICIO");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setForeground(Color.LIGHT_GRAY);
        lblTitulo.setBounds(xLabel, y, 300, 30);
        panelEdicion.add(lblTitulo);

        y += 40;
        lblNombreEjercicio = new JLabel("Selecciona un ejercicio ←");
        lblNombreEjercicio.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblNombreEjercicio.setForeground(COL_ACENTO);
        lblNombreEjercicio.setBounds(xLabel, y, 350, 30);
        panelEdicion.add(lblNombreEjercicio);

        y += 50;
       
        panelEdicion.add(crearLabel("Series:", xLabel, y));
        txtSeries = crearInput(xInput, y, 100);
        panelEdicion.add(txtSeries);

        y += gap;
        
        panelEdicion.add(crearLabel("Repeticiones:", xLabel, y));
        txtReps = crearInput(xInput, y, 100);
        panelEdicion.add(txtReps);

        y += gap;
      
        panelEdicion.add(crearLabel("Peso (kg):", xLabel, y));
        txtPeso = crearInput(xInput, y, 100);
        panelEdicion.add(txtPeso);

        y += gap;
      
        panelEdicion.add(crearLabel("Notas:", xLabel, y));
        txtNotas = new JTextArea();
        txtNotas.setBackground(COL_INPUT);
        txtNotas.setForeground(COL_TEXTO);
        txtNotas.setCaretColor(COL_ACENTO);
        txtNotas.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JScrollPane scrollNotas = new JScrollPane(txtNotas);
        scrollNotas.setBounds(xInput, y, 250, 80);
        scrollNotas.setBorder(new LineBorder(new Color(80,80,80)));
        panelEdicion.add(scrollNotas);

        y += 100;
       
        btnGuardar = new JButton("💾 Guardar Cambios");
        btnGuardar.setBackground(COL_ACENTO);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGuardar.setBounds(xLabel, y, 360, 45);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.addActionListener(e -> guardarEdicion());
        panelEdicion.add(btnGuardar);
        
        habilitarEdicion(false);
    }

    private void cargarEjercicios() {
        panelListaEjercicios.removeAll();
        List<ItemRutina> items = dbManager.obtenerItemsDeRutina(idRutina);

        if (items.isEmpty()) {
            JLabel empty = new JLabel("Lista vacía. ¡Agrega ejercicios!");
            empty.setForeground(Color.GRAY);
            panelListaEjercicios.add(empty);
        }

        for (ItemRutina item : items) {
            JPanel row = new JPanel(new BorderLayout(10, 0));
            row.setBackground(new Color(50, 50, 50));
            row.setBorder(new EmptyBorder(10, 10, 10, 10));
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
            row.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            JLabel lbl = new JLabel(item.getEjercicio().getNombre());
            lbl.setForeground(COL_TEXTO);
            lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
            
            JLabel info = new JLabel(item.getSeries() + " x " + item.getRepeticiones());
            info.setForeground(Color.LIGHT_GRAY);

            row.add(lbl, BorderLayout.CENTER);
            row.add(info, BorderLayout.EAST);
            
            row.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    cargarEnEdicion(item);
                    // Resaltar visualmente
                    for(Component c : panelListaEjercicios.getComponents()) c.setBackground(new Color(50,50,50));
                    row.setBackground(new Color(70,70,70));
                }
            });

            panelListaEjercicios.add(row);
            panelListaEjercicios.add(Box.createVerticalStrut(5));
        }
        panelListaEjercicios.revalidate();
        panelListaEjercicios.repaint();
    }

    private void cargarEnEdicion(ItemRutina item) {
        this.itemSeleccionado = item;
        lblNombreEjercicio.setText(item.getEjercicio().getNombre());
        txtSeries.setText(String.valueOf(item.getSeries()));
        txtReps.setText(String.valueOf(item.getRepeticiones()));
        txtPeso.setText(String.valueOf(item.getPesoKg()));
        txtNotas.setText(item.getNotas());
        habilitarEdicion(true);
    }

    private void guardarEdicion() {
        if (itemSeleccionado == null) return;
        try {
            itemSeleccionado.setSeries(Integer.parseInt(txtSeries.getText()));
            itemSeleccionado.setRepeticiones(Integer.parseInt(txtReps.getText()));
            itemSeleccionado.setPesoKg(Double.parseDouble(txtPeso.getText()));
            itemSeleccionado.setNotas(txtNotas.getText());

            if (dbManager.actualizarItemRutina(itemSeleccionado)) {
                JOptionPane.showMessageDialog(this, "Actualizado");
                cargarEjercicios();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error: Revisa los números.");
        }
    }
    
    private void agregarNuevoEjercicio() {
        DialogoSelectorEjercicio sel = new DialogoSelectorEjercicio((Frame) getOwner(), dbManager, idRutina);
        sel.setVisible(true);
        cargarEjercicios(); 
    }

    private void habilitarEdicion(boolean b) {
        txtSeries.setEnabled(b);
        txtReps.setEnabled(b);
        txtPeso.setEnabled(b);
        txtNotas.setEnabled(b);
        btnGuardar.setEnabled(b);
        if(!b) {
            lblNombreEjercicio.setText("Selecciona un ejercicio ←");
            txtSeries.setText(""); txtReps.setText(""); txtPeso.setText(""); txtNotas.setText("");
        }
    }

   
    private JLabel crearLabel(String t, int x, int y) {
        JLabel l = new JLabel(t);
        l.setForeground(Color.LIGHT_GRAY);
        l.setFont(new Font("SansSerif", Font.PLAIN, 14));
        l.setBounds(x, y, 100, 30);
        return l;
    }
    
    private JTextField crearInput(int x, int y, int w) {
        JTextField t = new JTextField();
        t.setBounds(x, y, w, 30);
        t.setBackground(COL_INPUT);
        t.setForeground(COL_TEXTO);
        t.setCaretColor(COL_ACENTO);
        t.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(80,80,80)), new EmptyBorder(0,5,0,5)));
        return t;
    }
}