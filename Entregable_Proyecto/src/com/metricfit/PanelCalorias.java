package com.metricfit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class PanelCalorias extends JPanel {

   
    private final Color COLOR_FONDO_BASE = new Color(30, 30, 30);
    private final Color COLOR_TARJETA = new Color(45, 45, 45);
    private final Color COLOR_TEXTO_PRINCIPAL = Color.WHITE;
    private final Color COLOR_TEXTO_SECUNDARIO = new Color(180, 180, 180);
    private final Color COLOR_ACENTO = new Color(255, 102, 0);
    private final Color COLOR_ROJO = new Color(220, 50, 50);
    private final String TIPO_OTROS = "Otros";

    
    private final Usuario usuarioActual;
    private List<Consumo> consumoDelDia;
    private final DatabaseManager dbManager;

    private int caloriasConsumidas = 0;
    private int caloriasObjetivo = 2500;
    private final int OBJETIVO_DEFAULT = 2000;
    private Map<String, Integer> macros = new HashMap<>();

    private final Set<String> tiposComidaBase = new LinkedHashSet<>(Arrays.asList("Desayuno", "Almuerzo", "Cena", "Snack"));
    private final Set<String> tiposComidaPersonalizados = new LinkedHashSet<>();
    private List<String> tiposComidaActivos = new ArrayList<>();

  
    private ProgressCirclePanel progressCircle;
    private JPanel panelComidasPorTipo;
    private JLabel lblValorCalorias;
    private JLabel lblRestantesCalorias;
    private JPanel panelMacros;

 
    public PanelCalorias(Usuario usuario, DatabaseManager dbManager) {
        this.usuarioActual = usuario;
        this.dbManager = dbManager;
        this.tiposComidaActivos.addAll(tiposComidaBase);

        cargarDatos(); 

        setLayout(new BorderLayout(20, 20));
        setBackground(COLOR_FONDO_BASE);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        add(crearHeader(), BorderLayout.NORTH);
        add(crearContenidoPrincipal(), BorderLayout.CENTER);
    }

 
    
    public void cargarDatos() {
        try {
            
            consumoDelDia = dbManager.obtenerConsumoDelDia(usuarioActual.getId(), new Date());
            
            
            if (usuarioActual != null) {
                this.caloriasObjetivo = usuarioActual.getObjetivo();
            }
            if (this.caloriasObjetivo <= 0) this.caloriasObjetivo = OBJETIVO_DEFAULT;
            
        } catch (Exception e) {
            consumoDelDia = new ArrayList<>();
            this.caloriasObjetivo = OBJETIVO_DEFAULT;
        }

        
        double totalKcal = 0, totalProt = 0, totalCarb = 0, totalGrasa = 0;
        for (Consumo consumo : consumoDelDia) {
            totalKcal += consumo.getCaloriasTotales();
            totalProt += consumo.getProteinasTotales();
            totalCarb += consumo.getCarbohidratosTotales();
            totalGrasa += consumo.getGrasasTotales();
        }

        this.caloriasConsumidas = (int) Math.round(totalKcal);
        macros.put("Proteínas", (int) Math.round(totalProt));
        macros.put("Carbohidratos", (int) Math.round(totalCarb));
        macros.put("Grasas", (int) Math.round(totalGrasa));

        tiposComidaActivos = new ArrayList<>(tiposComidaBase);
        tiposComidaActivos.addAll(tiposComidaPersonalizados);
        
        
        if (progressCircle != null) actualizarUI();
    }

    public void actualizarUI() {
        if (progressCircle != null) {
            progressCircle.setValores(caloriasConsumidas, caloriasObjetivo);
            int restantes = caloriasObjetivo - caloriasConsumidas;
            lblValorCalorias.setText(String.valueOf(caloriasConsumidas));
            lblValorCalorias.setForeground(restantes < 0 ? COLOR_ROJO : COLOR_ACENTO);
            
            String textoRestante = (restantes >= 0) ? restantes + " restantes" : (restantes * -1) + " excedidas";
            lblRestantesCalorias.setText("de " + caloriasObjetivo + " kcal (" + textoRestante + ")");
            lblRestantesCalorias.setForeground(restantes < 0 ? COLOR_ROJO.brighter() : COLOR_TEXTO_SECUNDARIO);
        }

        if (panelMacros != null) reconstruirPanelMacros(panelMacros);
        reconstruirPanelComidas(panelComidasPorTipo);

        revalidate();
        repaint();
    }

    public List<String> getTiposComidaActivos() {
        return tiposComidaActivos;
    }

    private void eliminarConsumo(Consumo consumo) {
        int confirm = JOptionPane.showConfirmDialog(this, "¿Borrar '" + consumo.getAlimento().getNombre() + "'?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (dbManager.eliminarConsumo(consumo.getIdConsumo())) {
                    cargarDatos(); 
                }
            } catch (Exception e) { /* Manejo de error */ }
        }
    }
    
    private void editarConsumo(Consumo consumo) {
        JOptionPane.showMessageDialog(this, "Editar: " + consumo.getAlimento().getNombre());
    }

    private void agregarTipoComidaPersonalizado(String tipo) {
        if (!tiposComidaPersonalizados.contains(tipo)) {
            tiposComidaPersonalizados.add(tipo);
            actualizarUI();
        }
    }
    
    private void eliminarTipoComidaPersonalizado(String tipo) {
        tiposComidaPersonalizados.remove(tipo);
        try { dbManager.reasignarConsumos(usuarioActual.getId(), tipo, TIPO_OTROS); } catch (Exception e) {}
        cargarDatos();
    }

    private void mostrarDialogoRegistro(String tipo) {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        DialogoRegistroComida dialogo = new DialogoRegistroComida(frame, this, usuarioActual, tipo, dbManager);
        dialogo.setVisible(true);
        cargarDatos();
    }
    
    private void mostrarDialogoRegistroGeneral() {
        Object[] ops = tiposComidaActivos.toArray();
        if (ops.length > 0) {
            String s = (String) JOptionPane.showInputDialog(this, "Selecciona apartado:", "Añadir", JOptionPane.PLAIN_MESSAGE, null, ops, ops[0]);
            if (s != null) mostrarDialogoRegistro(s);
        }
    }

  

    private JPanel crearHeader() {
      
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        
       
        JLabel lbl = new JLabel("📊 Contador de Calorías");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 32));
        lbl.setForeground(COLOR_TEXTO_PRINCIPAL);
        header.add(lbl, BorderLayout.WEST);
        
      
        BotonModerno btnRegresar = new BotonModerno("⬅ Regresar", new Color(60, 60, 60), Color.WHITE);
        btnRegresar.setPreferredSize(new Dimension(100, 35));
        btnRegresar.addActionListener(e -> {
           
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(PanelCalorias.this);
            if (frame instanceof VentanaPrincipal) {
                ((VentanaPrincipal) frame).manejarNavegacion(VentanaPrincipal.PANEL_DASHBOARD);
            }
        });
        
        
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBtn.setOpaque(false);
        pnlBtn.add(btnRegresar);
        
        header.add(pnlBtn, BorderLayout.EAST);
        
        return header;
    }

    private JPanel crearContenidoPrincipal() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 30, 0));
        panel.setOpaque(false);

     
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setOpaque(false);
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.add(crearTarjetaResumenCalorias());
        panelIzquierdo.add(Box.createVerticalStrut(20));
        panelMacros = crearTarjetaMacros();
        panelIzquierdo.add(panelMacros);
        panelIzquierdo.add(Box.createVerticalGlue());

      
        JPanel panelDerecho = crearTarjetaRegistroComidas();

        panel.add(panelIzquierdo);
        panel.add(panelDerecho);
        return panel;
    }

    private JPanel crearTarjetaResumenCalorias() {
        TarjetaPanel tarjeta = new TarjetaPanel();
        tarjeta.setLayout(new BorderLayout());
        tarjeta.setPreferredSize(new Dimension(0, 320));

        lblValorCalorias = new JLabel(String.valueOf(caloriasConsumidas), SwingConstants.CENTER);
        lblValorCalorias.setFont(new Font("SansSerif", Font.BOLD, 55));
        lblValorCalorias.setForeground(COLOR_ACENTO);

        lblRestantesCalorias = new JLabel("Cargando...", SwingConstants.CENTER);
        lblRestantesCalorias.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblRestantesCalorias.setForeground(COLOR_TEXTO_SECUNDARIO);

        JPanel infoCentro = new JPanel(new BorderLayout());
        infoCentro.setOpaque(false);
        infoCentro.add(lblValorCalorias, BorderLayout.CENTER);
        infoCentro.add(lblRestantesCalorias, BorderLayout.SOUTH);

        progressCircle = new ProgressCirclePanel(caloriasConsumidas, caloriasObjetivo);
        progressCircle.setOpaque(false);
        progressCircle.setLayout(new GridBagLayout());
        progressCircle.add(infoCentro);

        tarjeta.add(progressCircle, BorderLayout.CENTER);
        return tarjeta;
    }

    private JPanel crearTarjetaMacros() {
        TarjetaPanel tarjeta = new TarjetaPanel();
        reconstruirPanelMacros(tarjeta);
        return tarjeta;
    }

    private void reconstruirPanelMacros(JPanel tarjeta) {
        tarjeta.removeAll();
        tarjeta.setLayout(new BorderLayout(0, 15));
        JLabel lbl = new JLabel("Macronutrientes (Hoy)");
        lbl.setForeground(COLOR_TEXTO_PRINCIPAL);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        tarjeta.add(lbl, BorderLayout.NORTH);

        JPanel barras = new JPanel(new GridLayout(3, 1, 0, 15));
        barras.setOpaque(false);
        barras.add(crearBarraMacro("Proteínas", macros.getOrDefault("Proteínas", 0), 150, new Color(50, 150, 255)));
        barras.add(crearBarraMacro("Carbohidratos", macros.getOrDefault("Carbohidratos", 0), 250, new Color(255, 200, 0)));
        barras.add(crearBarraMacro("Grasas", macros.getOrDefault("Grasas", 0), 75, new Color(0, 200, 50)));

        tarjeta.add(barras, BorderLayout.CENTER);
        tarjeta.revalidate();
        tarjeta.repaint();
    }

    private JPanel crearTarjetaRegistroComidas() {
        TarjetaPanel tarjeta = new TarjetaPanel();
        tarjeta.setLayout(new BorderLayout(0, 10));

        
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        topBar.setOpaque(false);
        BotonModerno btnNewCat = new BotonModerno("＋ Nuevo Apartado", COLOR_TARJETA.brighter(), COLOR_TEXTO_PRINCIPAL);
        btnNewCat.setPreferredSize(new Dimension(130, 30));
        btnNewCat.addActionListener(e -> {
            String t = JOptionPane.showInputDialog(this, "Nombre:");
            if(t != null && !t.isBlank()) agregarTipoComidaPersonalizado(t);
        });
        topBar.add(btnNewCat);
        tarjeta.add(topBar, BorderLayout.NORTH);

      
        panelComidasPorTipo = new JPanel();
        panelComidasPorTipo.setLayout(new BoxLayout(panelComidasPorTipo, BoxLayout.Y_AXIS));
        panelComidasPorTipo.setOpaque(false);

        JScrollPane scroll = new JScrollPane(panelComidasPorTipo);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        tarjeta.add(scroll, BorderLayout.CENTER);

        
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        bottomBar.setOpaque(false);
        BotonModerno btnAddGeneral = new BotonModerno("＋ Añadir Comida", COLOR_ACENTO, Color.WHITE);
        btnAddGeneral.setPreferredSize(new Dimension(160, 40));
        btnAddGeneral.addActionListener(e -> mostrarDialogoRegistroGeneral());
        bottomBar.add(btnAddGeneral);
        tarjeta.add(bottomBar, BorderLayout.SOUTH);

        reconstruirPanelComidas(panelComidasPorTipo);
        return tarjeta;
    }

    private void reconstruirPanelComidas(JPanel container) {
        if (container == null) return;
        container.removeAll();

        Map<String, List<Consumo>> agrupados = consumoDelDia.stream()
            .collect(Collectors.groupingBy(c -> (c.getTipoComida() == null || c.getTipoComida().isEmpty()) ? TIPO_OTROS : c.getTipoComida()));

        Set<String> mostrar = new LinkedHashSet<>(tiposComidaActivos);
        mostrar.add(TIPO_OTROS);
        mostrar.addAll(agrupados.keySet());

        boolean hayDatos = false;
        for (String tipo : mostrar) {
            List<Consumo> lista = agrupados.getOrDefault(tipo, Collections.emptyList());
            boolean esBase = tiposComidaBase.contains(tipo);
            boolean esPers = tiposComidaPersonalizados.contains(tipo);

            if (!lista.isEmpty() || esBase || esPers || tipo.equals(TIPO_OTROS)) {
                container.add(crearApartadoVisual(tipo, lista, esPers));
                container.add(Box.createVerticalStrut(15));
                hayDatos = true;
            }
        }

        if (!hayDatos) {
            JLabel lbl = new JLabel("Sin registros hoy", SwingConstants.CENTER);
            lbl.setForeground(COLOR_TEXTO_SECUNDARIO);
            lbl.setFont(new Font("SansSerif", Font.ITALIC, 14));
            lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            container.add(lbl);
        }
        container.revalidate();
        container.repaint();
    }

    private JPanel crearApartadoVisual(String titulo, List<Consumo> consumos, boolean esEliminable) {
        JPanel card = new JPanel(new BorderLayout(0, 10)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_FONDO_BASE.brighter());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(10, 15, 10, 15));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 5000)); 

        
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        
        double kcalTotal = consumos.stream().mapToDouble(Consumo::getCaloriasTotales).sum();
        JLabel lblTit = new JLabel("<html><span style='font-size:14px; color:#FF6600;'>●</span> " + titulo + " <span style='font-size:10px; color:#999;'>(" + (int)kcalTotal + " kcal)</span></html>");
        lblTit.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTit.setForeground(Color.WHITE);
        
        JPanel headerBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        headerBtns.setOpaque(false);
        
        BotonModerno btnAdd = new BotonModerno("+", COLOR_ACENTO, Color.WHITE);
        btnAdd.setPreferredSize(new Dimension(25, 25));
        btnAdd.addActionListener(e -> mostrarDialogoRegistro(titulo));
        headerBtns.add(btnAdd);
        
        if(esEliminable) {
            BotonModerno btnDel = new BotonModerno("×", COLOR_ROJO, Color.WHITE);
            btnDel.setPreferredSize(new Dimension(25, 25));
            btnDel.addActionListener(e -> eliminarTipoComidaPersonalizado(titulo));
            headerBtns.add(btnDel);
        }
        
        header.add(lblTit, BorderLayout.WEST);
        header.add(headerBtns, BorderLayout.EAST);
        card.add(header, BorderLayout.NORTH);

        
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        if (consumos.isEmpty()) {
            JLabel empty = new JLabel("Sin registros");
            empty.setForeground(COLOR_TEXTO_SECUNDARIO);
            empty.setFont(new Font("SansSerif", Font.ITALIC, 12));
            listPanel.add(empty);
        } else {
            for (Consumo c : consumos) {
                listPanel.add(crearFilaComida(c));
                listPanel.add(Box.createVerticalStrut(5));
            }
        }
        
        card.add(listPanel, BorderLayout.CENTER);
        return card;
    }

    private JPanel crearFilaComida(Consumo c) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(60, 60, 60)));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel lblName = new JLabel("<html><b>" + c.getAlimento().getNombre() + "</b> <span style='color:#999; font-size:10px;'>" + (int)c.getCantidad() + "g</span></html>");
        lblName.setForeground(COLOR_TEXTO_PRINCIPAL);
        lblName.setFont(new Font("SansSerif", Font.PLAIN, 13));
        
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        right.setOpaque(false);
        
        JLabel lblKcal = new JLabel((int)c.getCaloriasTotales() + " kcal");
        lblKcal.setForeground(COLOR_ACENTO);
        lblKcal.setFont(new Font("SansSerif", Font.BOLD, 13));
        
        BotonModerno btnEdit = new BotonModerno("✎", new Color(60, 60, 60), Color.WHITE);
        btnEdit.setPreferredSize(new Dimension(24, 24));
        btnEdit.addActionListener(e -> editarConsumo(c));

        BotonModerno btnDel = new BotonModerno("🗑", COLOR_ROJO.darker(), Color.WHITE);
        btnDel.setPreferredSize(new Dimension(24, 24));
        btnDel.addActionListener(e -> eliminarConsumo(c));

        right.add(lblKcal);
        right.add(Box.createHorizontalStrut(5));
        right.add(btnEdit);
        right.add(btnDel);

        row.add(lblName, BorderLayout.CENTER);
        row.add(right, BorderLayout.EAST);
        return row;
    }

   

    private class TarjetaPanel extends JPanel {
        public TarjetaPanel() {
            setOpaque(false);
            setBorder(new EmptyBorder(15, 15, 15, 15));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 50));
            g2.fillRoundRect(3, 3, getWidth()-6, getHeight()-6, 20, 20);
            g2.setColor(COLOR_TARJETA);
            g2.fillRoundRect(0, 0, getWidth()-3, getHeight()-3, 20, 20);
            g2.setColor(new Color(60, 60, 60));
            g2.drawRoundRect(0, 0, getWidth()-3, getHeight()-3, 20, 20);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private class BotonModerno extends JButton {
        private Color colorNormal;
        public BotonModerno(String text, Color bg, Color fg) {
            super(text);
            this.colorNormal = bg;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(fg);
            setFont(new Font("SansSerif", Font.BOLD, 12));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { colorNormal = bg.brighter(); repaint(); }
                public void mouseExited(MouseEvent e) { colorNormal = bg; repaint(); }
            });
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(colorNormal);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    private JPanel crearBarraMacro(String titulo, int val, int max, Color col) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setOpaque(false);
        JLabel lbl = new JLabel(titulo + " (" + val + "/" + max + "g)");
        lbl.setForeground(COLOR_TEXTO_PRINCIPAL);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        JProgressBar pb = new JProgressBar(0, max);
        pb.setValue(val);
        pb.setPreferredSize(new Dimension(100, 8));
        pb.setForeground(col);
        pb.setBackground(new Color(30, 30, 30));
        pb.setBorderPainted(false);
        p.add(lbl, BorderLayout.NORTH);
        p.add(pb, BorderLayout.CENTER);
        return p;
    }

    private class ProgressCirclePanel extends JPanel {
        private int val, max;
        public ProgressCirclePanel(int v, int m) { this.val = v; this.max = m; }
        public void setValores(int v, int m) { this.val = v; this.max = m; repaint(); }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int s = Math.min(getWidth(), getHeight()) - 20;
            int x = (getWidth() - s) / 2, y = (getHeight() - s) / 2;
            g2.setStroke(new BasicStroke(15, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(new Color(40, 40, 40));
            g2.drawArc(x, y, s, s, 0, 360);
            g2.setColor((val > max) ? COLOR_ROJO : COLOR_ACENTO);
            double pct = (max > 0) ? (double)val/max : 0;
            g2.drawArc(x, y, s, s, 90, -(int)(Math.min(pct, 1.0) * 360));
        }
    }
}