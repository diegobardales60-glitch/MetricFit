package com.metricfit;


import com.metricfit.Alimento;
import com.metricfit.Usuario;
import com.metricfit.Consumo;
import com.metricfit.AlimentoListRenderer;


import com.metricfit.DatabaseManager;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class DialogoRegistroComida extends JDialog {

    private final Color COLOR_FONDO_BASE = new Color(36, 36, 36);
    private final Color COLOR_PANEL_FONDO = new Color(45, 45, 45);
    private final Color COLOR_TEXTO_PRINCIPAL = Color.WHITE;
    private final Color COLOR_ACENTO = new Color(255, 102, 0);
    private final Color COLOR_ACENTO_CLARO = new Color(255, 140, 60);
    private final Color COLOR_BORDE_INPUT = new Color(80, 80, 80);

    private final PanelCalorias panelPadre;
    private final Usuario usuarioActual;
    private final DatabaseManager dbManager; 
    private List<Alimento> catalogoAlimentos;

    // Componentes de la interfaz
    private JComboBox<String> comboTipoComida;
    private JTextField txtCantidad;
    private JLabel lblCaloriasEstimadas;

    // Alimento seleccionado
    private Alimento alimentoSeleccionado;


    public DialogoRegistroComida(Window owner, PanelCalorias panelPadre, Usuario usuarioActual, String tipoComida, DatabaseManager dbManager) {
        super(owner, "🍽️ Registrar Nuevo Consumo", ModalityType.APPLICATION_MODAL);
        this.panelPadre = panelPadre;
        this.usuarioActual = usuarioActual;
        this.dbManager = dbManager; 

        try {
            this.catalogoAlimentos = dbManager.getCatalogoAlimentos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(owner, "Error al cargar el catálogo de alimentos: " + e.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            this.catalogoAlimentos = Collections.emptyList();
        }

        setSize(800, 650); 
        setLocationRelativeTo(owner);
        getContentPane().setBackground(COLOR_FONDO_BASE);
        setLayout(new BorderLayout(0, 0)); 
        
      
        JPanel mainContent = new JPanel(new BorderLayout(20, 20));
        mainContent.setBackground(COLOR_FONDO_BASE);
        mainContent.setBorder(new EmptyBorder(25, 25, 25, 25));
        add(mainContent, BorderLayout.CENTER);

      
        mainContent.add(crearPanelControles(tipoComida), BorderLayout.NORTH);

      
        mainContent.add(crearPanelPestanas(), BorderLayout.CENTER);

    
        mainContent.add(crearPanelAcciones(), BorderLayout.SOUTH);

       
        alimentoSeleccionado = null;
        actualizarInfoEstimada();
    }


    private JPanel crearPanelControles(String tipoComidaInicial) {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JPanel panelCampos = new JPanel(new GridLayout(1, 2, 30, 0)); 
        panelCampos.setOpaque(false);

        panelCampos.add(crearEtiquetaCampo("Apartado:", crearSelectorTipoComida(tipoComidaInicial)));
        panelCampos.add(crearEtiquetaCampo("Cantidad (Gramos):", crearCampoCantidad()));

        panel.add(panelCampos, BorderLayout.NORTH);

        lblCaloriasEstimadas = new JLabel("Selecciona un alimento y cantidad para ver estimación", SwingConstants.CENTER);
        lblCaloriasEstimadas.setForeground(COLOR_ACENTO_CLARO);
        lblCaloriasEstimadas.setFont(new Font("SansSerif", Font.BOLD, 15));
        
      
        JPanel panelEstimacion = new JPanel(new BorderLayout());
        panelEstimacion.setBackground(COLOR_PANEL_FONDO);
        panelEstimacion.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelEstimacion.add(lblCaloriasEstimadas, BorderLayout.CENTER);
        
        panel.add(panelEstimacion, BorderLayout.SOUTH);

        return panel;
    }

    private JComboBox<String> crearSelectorTipoComida(String inicial) {
       
        List<String> listaTipos = panelPadre.getTiposComidaActivos();
        comboTipoComida = new JComboBox<>(new Vector<>(listaTipos));
        
        comboTipoComida.setFont(new Font("SansSerif", Font.PLAIN, 14));
        comboTipoComida.setBackground(COLOR_PANEL_FONDO.brighter());
        comboTipoComida.setForeground(COLOR_TEXTO_PRINCIPAL);
        comboTipoComida.setBorder(new LineBorder(COLOR_BORDE_INPUT, 1));

        if (inicial != null) {
            comboTipoComida.setSelectedItem(inicial);
        }
        return comboTipoComida;
    }

    private JTextField crearCampoCantidad() {
        txtCantidad = new JTextField("100");
        estilizarCampoTexto(txtCantidad);
        txtCantidad.addActionListener(e -> actualizarInfoEstimada());
        return txtCantidad;
    }

    private JTabbedPane crearPanelPestanas() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(COLOR_PANEL_FONDO);
        tabbedPane.setForeground(COLOR_TEXTO_PRINCIPAL);
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        
        UIManager.put("TabbedPane.selected", COLOR_ACENTO);
        UIManager.put("TabbedPane.contentAreaColor", COLOR_PANEL_FONDO);

        tabbedPane.addTab("🔍 Buscar Alimento", crearPanelBusqueda());
        tabbedPane.addTab("🕒 Recientes", crearPanelRecientes());
        tabbedPane.addTab("📝 Crear Nuevo", crearPanelCreacion());

        return tabbedPane;
    }

    private JPanel crearPanelAcciones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(15, 0, 0, 0));

        JButton btnCancelar = crearBotonModerno("Cancelar", new Color(70, 70, 70), Color.WHITE);
        btnCancelar.addActionListener(e -> dispose());
        
        JButton btnRegistrar = crearBotonModerno("✔️ Registrar Consumo", COLOR_ACENTO, Color.WHITE);
        btnRegistrar.setPreferredSize(new Dimension(180, 40));
        btnRegistrar.addActionListener(e -> registrarConsumo());

        panel.add(btnCancelar);
        panel.add(btnRegistrar);
        return panel;
    }

  

    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(COLOR_PANEL_FONDO);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

       
        JPanel searchContainer = new JPanel(new BorderLayout(5, 0));
        searchContainer.setOpaque(false);
        JLabel lblIcon = new JLabel("🔎");
        lblIcon.setForeground(Color.GRAY);
        JTextField searchBar = new JTextField();
        estilizarCampoTexto(searchBar);
        
        searchContainer.add(lblIcon, BorderLayout.WEST);
        searchContainer.add(searchBar, BorderLayout.CENTER);
        panel.add(searchContainer, BorderLayout.NORTH);

        
        JList<Alimento> listResultados = new JList<>(new Vector<>(catalogoAlimentos));
        listResultados.setBackground(COLOR_FONDO_BASE); 
        listResultados.setForeground(COLOR_TEXTO_PRINCIPAL);
        listResultados.setFont(new Font("SansSerif", Font.PLAIN, 14));
        listResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listResultados.setSelectionBackground(COLOR_ACENTO_CLARO);
        listResultados.setSelectionForeground(Color.WHITE);
        listResultados.setCellRenderer(new AlimentoListRenderer());
        listResultados.setBorder(new EmptyBorder(5,5,5,5));

        listResultados.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                alimentoSeleccionado = listResultados.getSelectedValue();
                actualizarInfoEstimada();
            }
        });

        JScrollPane scroll = new JScrollPane(listResultados);
        scroll.setBorder(new LineBorder(COLOR_BORDE_INPUT));
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelRecientes() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_PANEL_FONDO);
        JLabel lbl = new JLabel("<html><center>🕒<br>Historial de últimos 7 días<br>(Próximamente)</center></html>", SwingConstants.CENTER);
        lbl.setForeground(Color.GRAY);
        panel.add(lbl, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelCreacion() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_PANEL_FONDO);
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        
        JTextField txtNombre = crearCampoTextoSimple();
        JTextField txtKcal = crearCampoTextoSimple();
        JTextField txtProt = crearCampoTextoSimple();
        JTextField txtCarb = crearCampoTextoSimple();
        JTextField txtGrasa = crearCampoTextoSimple();

        agregarFilaFormulario(panel, gbc, 0, "📝 Nombre Alimento:", txtNombre);
        agregarFilaFormulario(panel, gbc, 1, "🔥 Calorías (100g):", txtKcal);
        agregarFilaFormulario(panel, gbc, 2, "🥩 Proteínas (g):", txtProt);
        agregarFilaFormulario(panel, gbc, 3, "🍚 Carbohidratos (g):", txtCarb);
        agregarFilaFormulario(panel, gbc, 4, "🥑 Grasas (g):", txtGrasa);

       
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 10, 10, 10);
        JButton btnGuardarNuevo = crearBotonModerno("💾 Guardar y Usar", COLOR_ACENTO, Color.WHITE);
        btnGuardarNuevo.addActionListener(e -> {
            
            try {
                String nombre = txtNombre.getText().trim();
                double kcal = Double.parseDouble(txtKcal.getText());
                double prot = Double.parseDouble(txtProt.getText());
                double carb = Double.parseDouble(txtCarb.getText());
                double grasa = Double.parseDouble(txtGrasa.getText());

                Alimento nuevoAlimento = new Alimento(-1, nombre, kcal, prot, carb, grasa);
                Alimento alimentoGuardado = dbManager.guardarNuevoAlimento(nuevoAlimento);

                alimentoSeleccionado = alimentoGuardado;
                actualizarInfoEstimada();
                JOptionPane.showMessageDialog(this, "Alimento '" + nombre + "' guardado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(btnGuardarNuevo, gbc);

        return panel;
    }

    

    private void actualizarInfoEstimada() {
        if (alimentoSeleccionado == null) {
            lblCaloriasEstimadas.setText("Selecciona un alimento y cantidad para ver estimación");
            return;
        }

        try {
            double cantidad = Double.parseDouble(txtCantidad.getText());
            double factor = cantidad / 100.0;

            double kcal = alimentoSeleccionado.getCaloriasPor100g() * factor;
            double prot = alimentoSeleccionado.getProteinasPor100g() * factor;
            double carb = alimentoSeleccionado.getCarbohidratosPor100g() * factor;
            double grasa = alimentoSeleccionado.getGrasasPor100g() * factor;

            lblCaloriasEstimadas.setText(String.format(
                "<html>Estimación: <font color='#FF6600'>%.0f kcal</font> <font color='#AAAAAA'>| P: %.1f | C: %.1f | G: %.1f</font></html>",
                kcal, prot, carb, grasa
            ));

        } catch (NumberFormatException e) {
            lblCaloriasEstimadas.setText("ERROR: Cantidad inválida");
        }
    }

    private void registrarConsumo() {
        if (alimentoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un alimento.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String tipo = (String) comboTipoComida.getSelectedItem();
            int cantidad = Integer.parseInt(txtCantidad.getText());

            Consumo nuevoConsumo = new Consumo(
                -1, 
                usuarioActual.getId(),
                alimentoSeleccionado,
                cantidad,
                new Date(),
                tipo
            );

            dbManager.guardarConsumo(nuevoConsumo);

            panelPadre.actualizarUI();
            dispose();

        } catch (Exception e) {
             JOptionPane.showMessageDialog(this, "Error al registrar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void agregarFilaFormulario(JPanel panel, GridBagConstraints gbc, int row, String label, Component comp) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel lbl = new JLabel(label);
        lbl.setForeground(COLOR_TEXTO_PRINCIPAL);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(comp, gbc);
    }

    private JPanel crearEtiquetaCampo(String labelText, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setOpaque(false);
        JLabel label = new JLabel(labelText);
        label.setForeground(COLOR_TEXTO_PRINCIPAL);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        panel.add(label, BorderLayout.NORTH);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    private void estilizarCampoTexto(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBackground(COLOR_PANEL_FONDO.brighter());
        field.setForeground(COLOR_TEXTO_PRINCIPAL);
        field.setCaretColor(COLOR_ACENTO);
        
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(COLOR_BORDE_INPUT, 1),
            new EmptyBorder(5, 8, 5, 8) 
        ));
    }
    
    private JTextField crearCampoTextoSimple() {
        JTextField tf = new JTextField();
        estilizarCampoTexto(tf);
        return tf;
    }

    
    private JButton crearBotonModerno(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(bg.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(bg.brighter());
                } else {
                    g2.setColor(bg);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(fg);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        btn.setForeground(fg);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 35));
        return btn;
    }
}