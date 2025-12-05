package com.metricfit;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import javax.swing.border.EmptyBorder;


public class PanelPerfil extends JPanel {

  
    private Usuario usuarioActual;
    private DatabaseManager dbManager;
    private final CardLayout cardLayout;
    private final JPanel cardsPanel;

    
    private JTextField txtEdad;
    private JTextField txtPeso;
    private JTextField txtAltura;
    private JTextField txtPesoObjetivo; 
    private JComboBox<String> comboGenero;
    private JComboBox<String> comboActividad;
    private JComboBox<String> comboMeta;    
    
    
    private JLabel lblPesoDisplay;
    private JLabel lblIMC;
    private JLabel lblCaloriasObjetivo;

    
    private final Color COL_FONDO = new Color(25, 25, 25);
    private final Color COL_TARJETA = new Color(40, 40, 40);
    private final Color COL_ACENTO = new Color(255, 102, 0); 
    private final Color COL_TEXTO_MAIN = Color.WHITE;
    private final Color COL_TEXTO_SEC = new Color(170, 170, 170);
    private final Color COL_INPUT_BG = new Color(50, 50, 50);

    public PanelPerfil(Usuario usuario, CardLayout cardLayout, JPanel cardsPanel, DatabaseManager dbManager) {
        this.usuarioActual = usuario;
        this.cardLayout = cardLayout;
        this.cardsPanel = cardsPanel;
        this.dbManager = dbManager;
        
        setLayout(null);
        setBackground(COL_FONDO);

        inicializarUI();
    }

    private void inicializarUI() {
        int xStart = 50; 
        
      
        try {
            URL url = getClass().getResource("/resources/male_bodybuilder.jpg");
            if (url != null) {
                ImageIcon imgOriginal = new ImageIcon(url);
                Image imgEscalada = imgOriginal.getImage().getScaledInstance(450, 700, Image.SCALE_SMOOTH);
                JLabel lblImagen = new JLabel(new ImageIcon(imgEscalada));
                lblImagen.setBorder(BorderFactory.createLineBorder(COL_ACENTO, 2));
                lblImagen.setBounds(700, 30, 400, 680); 
                add(lblImagen);
            }
        } catch (Exception e) {}

      
        JLabel lblTitulo = new JLabel("CALCULADORA METABÓLICA PRO");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 30));
        lblTitulo.setForeground(COL_ACENTO);
        lblTitulo.setBounds(xStart, 20, 550, 40);
        add(lblTitulo);

        JLabel lblSub = new JLabel("Define tu perfil para obtener tu plan nutricional exacto.");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblSub.setForeground(COL_TEXTO_SEC);
        lblSub.setBounds(xStart, 60, 550, 20);
        add(lblSub);

       
        int yCards = 100;
        
       
        double imc = calcularIMC(usuarioActual.getPesoActual(), 1.75);
        JPanel cardIMC = crearTarjetaEstadistica("IMC", String.format("%.1f", imc));
        lblIMC = (JLabel) cardIMC.getComponent(1);
        cardIMC.setBounds(xStart, yCards, 120, 80);
        add(cardIMC);

        
        JPanel cardPeso = crearTarjetaEstadistica("PESO ACTUAL", usuarioActual.getPesoActual() + " kg");
        lblPesoDisplay = (JLabel) cardPeso.getComponent(1);
        lblPesoDisplay.setForeground(COL_ACENTO);
        cardPeso.setBounds(xStart + 140, yCards, 150, 80);
        add(cardPeso);

        // CALORÍAS (Objetivo)
        String calBD = String.valueOf(usuarioActual.getObjetivo());
        JPanel cardCalorias = crearTarjetaEstadistica("OBJETIVO DIARIO", calBD + " kcal");
        cardCalorias.setBorder(new LineBorder(COL_ACENTO, 1));
        lblCaloriasObjetivo = (JLabel) cardCalorias.getComponent(1);
        lblCaloriasObjetivo.setForeground(COL_ACENTO);
        cardCalorias.setBounds(xStart + 310, yCards, 250, 80);
        add(cardCalorias);

       
        int yForm = 210; 
        int col1 = xStart;
        int col2 = xStart + 280; 
        int wInput = 250;       
        int gapY = 75; 

        
        add(crearLabelCampo("Edad", col1, yForm));
        txtEdad = crearInput(String.valueOf(usuarioActual.getEdad()), col1, yForm + 25, 120);
        add(txtEdad);

        add(crearLabelCampo("Altura (cm)", col1 + 130, yForm));
        txtAltura = crearInput(String.valueOf(usuarioActual.getAltura()), col1 + 130, yForm + 25, 120);
        add(txtAltura);

        add(crearLabelCampo("Peso Actual (kg)", col2, yForm));
        txtPeso = crearInput(String.valueOf(usuarioActual.getPesoActual()), col2, yForm + 25, wInput);
        add(txtPeso);

        yForm += gapY;

        
        add(crearLabelCampo("Género", col1, yForm));
        comboGenero = crearCombo(new String[]{"Hombre", "Mujer"}, col1, yForm + 25, wInput);
        comboGenero.setSelectedItem(usuarioActual.getGenero());
        add(comboGenero);

        add(crearLabelCampo("Peso Objetivo (kg)", col2, yForm));
        txtPesoObjetivo = crearInput(String.valueOf(usuarioActual.getPesoObjetivo()), col2, yForm + 25, wInput);
        add(txtPesoObjetivo);

        yForm += gapY;

        
        add(crearLabelCampo("Nivel de Actividad", col1, yForm));
        String[] actividades = {
            "Sedentario (Oficina)",
            "Ligero (1-3 días)",
            "Moderado (3-5 días)",
            "Activo (6-7 días)",
            "Atleta (Doble sesión)"
        };
        comboActividad = crearCombo(actividades, col1, yForm + 25, wInput);
        comboActividad.setSelectedIndex(usuarioActual.getNivelActividad());
        add(comboActividad);

        add(crearLabelCampo("Meta Fitness", col2, yForm));
        String[] metas = {
            "📉 Déficit (Bajar Grasa)",
            "⚖️ Mantenimiento",
            "📈 Superávit (Ganar Músculo)"
        };
        comboMeta = crearCombo(metas, col2, yForm + 25, wInput);
        comboMeta.setSelectedIndex(usuarioActual.getTipoMeta());
        add(comboMeta);

        yForm += 90; 

        
        
       
        BotonModerno btnCalcular = new BotonModerno("🔄 Calcular", COL_INPUT_BG, COL_TEXTO_MAIN);
        btnCalcular.setBounds(col1, yForm, 180, 45);
        btnCalcular.addActionListener(e -> realizarCalculo());
        add(btnCalcular);

        
        BotonModerno btnGuardar = new BotonModerno("💾 Guardar Datos", COL_ACENTO, COL_TEXTO_MAIN);
        btnGuardar.setBounds(col1 + 200, yForm, 200, 45);
        btnGuardar.addActionListener(e -> guardarDatosDefinitivos());
        add(btnGuardar);

        
        BotonModerno btnRegresar = new BotonModerno("← Regresar", new Color(60, 60, 60), COL_TEXTO_SEC);
        btnRegresar.setBounds(col2 + 130, yForm, 120, 45); 
        btnRegresar.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(PanelPerfil.this);
            if (frame instanceof VentanaPrincipal) ((VentanaPrincipal) frame).notificarActualizacionGlobal();
            cardLayout.show(cardsPanel, VentanaPrincipal.PANEL_DASHBOARD);
        });
        add(btnRegresar);
        
        realizarCalculo(); 
    }

    

    private void realizarCalculo() {
        try {
            int edad = Integer.parseInt(txtEdad.getText());
            double peso = Double.parseDouble(txtPeso.getText());
            double pObj = Double.parseDouble(txtPesoObjetivo.getText());
            int altura = Integer.parseInt(txtAltura.getText());
            int idxAct = comboActividad.getSelectedIndex();
            int idxMeta = comboMeta.getSelectedIndex();
            boolean esHombre = comboGenero.getSelectedItem().equals("Hombre");

            lblPesoDisplay.setText(peso + " kg");

            double tmb = (10 * peso) + (6.25 * altura) - (5 * edad) + (esHombre ? 5 : -161);
            double factor = switch(idxAct) { case 0->1.2; case 1->1.375; case 2->1.55; case 3->1.725; default->1.9; };
            double tdee = tmb * factor;
            
            if (idxMeta == 0) tdee -= 400; else if (idxMeta == 2) tdee += 300;

            double altM = altura / 100.0;
            double imc = peso / (altM * altM);

            lblIMC.setText(String.format("%.1f", imc));
            lblCaloriasObjetivo.setText((int)tdee + " kcal");
            
            if(imc < 18.5) lblIMC.setForeground(Color.CYAN);
            else if(imc < 25) lblIMC.setForeground(Color.GREEN);
            else if(imc < 30) lblIMC.setForeground(Color.YELLOW);
            else lblIMC.setForeground(Color.RED);

        } catch (NumberFormatException e) {}
    }

    private void guardarDatosDefinitivos() {
        try {
            realizarCalculo(); 
            int caloriasInt = Integer.parseInt(lblCaloriasObjetivo.getText().replace(" kcal", "").trim());
            
            usuarioActual.setEdad(Integer.parseInt(txtEdad.getText()));
            usuarioActual.setPesoActual(Double.parseDouble(txtPeso.getText()));
            usuarioActual.setAltura(Integer.parseInt(txtAltura.getText()));
            usuarioActual.setPesoObjetivo(Double.parseDouble(txtPesoObjetivo.getText()));
            usuarioActual.setGenero((String)comboGenero.getSelectedItem());
            usuarioActual.setNivelActividad(comboActividad.getSelectedIndex());
            usuarioActual.setTipoMeta(comboMeta.getSelectedIndex());
            usuarioActual.setObjetivo(caloriasInt);

            if (dbManager != null && dbManager.actualizarUsuario(usuarioActual)) {
                JOptionPane.showMessageDialog(this, "¡Perfil actualizado!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error de conexión.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Datos inválidos.");
        }
    }

    public void recargarDatos() {
        if (usuarioActual != null) {
            txtEdad.setText(String.valueOf(usuarioActual.getEdad()));
            txtPeso.setText(String.valueOf(usuarioActual.getPesoActual()));
            txtAltura.setText(String.valueOf(usuarioActual.getAltura()));
            txtPesoObjetivo.setText(String.valueOf(usuarioActual.getPesoObjetivo()));
            comboGenero.setSelectedItem(usuarioActual.getGenero());
            comboActividad.setSelectedIndex(usuarioActual.getNivelActividad());
            comboMeta.setSelectedIndex(usuarioActual.getTipoMeta());
            realizarCalculo();
        }
    }

    
    private double calcularIMC(double peso, double alturaM) {
        if (alturaM <= 0) return 0;
        return peso / (alturaM * alturaM);
    }

    private JPanel crearTarjetaEstadistica(String t, String v) {
        JPanel p = new JPanel(new GridLayout(2,1));
        p.setBackground(COL_TARJETA);
        p.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(60,60,60),1), new EmptyBorder(10,15,10,15)));
        JLabel l1 = new JLabel(t); l1.setForeground(COL_TEXTO_SEC); l1.setFont(new Font("SansSerif", Font.BOLD, 11));
        JLabel l2 = new JLabel(v); l2.setForeground(COL_TEXTO_MAIN); l2.setFont(new Font("SansSerif", Font.BOLD, 20));
        p.add(l1); p.add(l2); return p;
    }
    private JLabel crearLabelCampo(String t, int x, int y) {
        JLabel l = new JLabel(t); l.setForeground(COL_TEXTO_SEC); l.setFont(new Font("SansSerif", Font.BOLD, 12)); l.setBounds(x, y, 200, 20); return l;
    }
    private JTextField crearInput(String t, int x, int y, int w) {
        JTextField tf = new JTextField(t); tf.setBounds(x, y, w, 35); tf.setBackground(COL_INPUT_BG); tf.setForeground(COL_TEXTO_MAIN); tf.setCaretColor(COL_ACENTO); tf.setBorder(new LineBorder(new Color(70,70,70))); return tf;
    }
    private JComboBox<String> crearCombo(String[] i, int x, int y, int w) {
        JComboBox<String> cb = new JComboBox<>(i); cb.setBounds(x, y, w, 35); cb.setBackground(COL_INPUT_BG); cb.setForeground(COL_TEXTO_MAIN); cb.setBorder(new LineBorder(new Color(70,70,70))); return cb;
    }
    private class BotonModerno extends JButton {
        Color b, h; public BotonModerno(String t, Color c, Color f) { super(t); b=c; h=c.brighter(); setContentAreaFilled(false); setFocusPainted(false); setBorderPainted(false); setForeground(f); setFont(new Font("SansSerif", Font.BOLD, 14)); setCursor(new Cursor(Cursor.HAND_CURSOR)); addMouseListener(new MouseAdapter(){ public void mouseEntered(MouseEvent e){b=h;repaint();} public void mouseExited(MouseEvent e){b=c;repaint();} }); }
        @Override protected void paintComponent(Graphics g) { g.setColor(b); g.fillRoundRect(0,0,getWidth(),getHeight(),10,10); super.paintComponent(g); }
    }
}