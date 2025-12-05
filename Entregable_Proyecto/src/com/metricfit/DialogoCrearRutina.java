package com.metricfit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class DialogoCrearRutina extends JDialog {

    private final Usuario usuarioActual;
    private final DatabaseManager dbManager;
    private Rutina rutinaAEditar;
    
    
    private JTextField txtNombreRutina;
    private JComboBox<String> comboDiaSemana;
    private String imagenSeleccionada = "icon_fullbody.png"; // Imagen por defecto
    private JPanel panelIconos; 

    
    private final String[] ICONOS_DISPONIBLES = {
        "routine_fullbody.png", 
        "routine_chest.png", 
        "routine_back.png", 
        "routine_legs.png", 
        "routine_arms.png", 
        "routine_cardio.png", 
        "routine_abs.png" 
    };

    private boolean fueExitoso = false;

    // Colores
    private final Color COLOR_FONDO = new Color(36, 36, 36);
    private final Color COLOR_INPUT = new Color(45, 45, 45);
    private final Color COLOR_TEXTO = Color.WHITE;
    private final Color COLOR_ACENTO = new Color(255, 102, 0);

    public DialogoCrearRutina(Frame owner, Usuario usuario, DatabaseManager dbManager, Rutina rutina) {
        super(owner, rutina != null ? "Editar Rutina" : "Nueva Rutina", true);
        this.usuarioActual = usuario;
        this.dbManager = dbManager;
        this.rutinaAEditar = rutina;

       
        if (rutina != null && rutina.getImagenNombre() != null) {
            this.imagenSeleccionada = rutina.getImagenNombre();
        }

        setSize(520, 600);
        setLocationRelativeTo(owner);
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new BorderLayout());

        inicializarUI();
    }
    
   
    public DialogoCrearRutina(Frame owner, Usuario usuario, DatabaseManager dbManager) {
        this(owner, usuario, dbManager, null);
    }

    private void inicializarUI() {
        
        JLabel lblTitulo = new JLabel(rutinaAEditar == null ? "Crear Nueva Rutina" : "Editar Rutina", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(COLOR_TEXTO);
        lblTitulo.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(COLOR_FONDO);
        panelForm.setBorder(new EmptyBorder(0, 40, 0, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0; gbc.weightx = 1.0;

        
        panelForm.add(crearLabel("Nombre de la Rutina:"), gbc);
        txtNombreRutina = crearTextField();
        if (rutinaAEditar != null) txtNombreRutina.setText(rutinaAEditar.getNombreRutina());
        panelForm.add(txtNombreRutina, gbc);

        
        panelForm.add(crearLabel("Día Habitual:"), gbc);
        String[] dias = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo", "Flexible"};
        comboDiaSemana = new JComboBox<>(dias);
        comboDiaSemana.setBackground(COLOR_INPUT);
        comboDiaSemana.setForeground(COLOR_TEXTO);
        if (rutinaAEditar != null) comboDiaSemana.setSelectedItem(rutinaAEditar.getDiaSemana());
        panelForm.add(comboDiaSemana, gbc);

       
        panelForm.add(crearLabel("Selecciona una Imagen:"), gbc);
        
        panelIconos = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelIconos.setBackground(COLOR_FONDO);
        panelIconos.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), "Iconos Disponibles", 0, 0, new Font("SansSerif", Font.PLAIN, 10), Color.GRAY)
        );
        
        
        for (String nombreIcono : ICONOS_DISPONIBLES) {
            panelIconos.add(crearCuadroIcono(nombreIcono));
        }
        refrescarSeleccionIcono(); 
        
        JScrollPane scrollIconos = new JScrollPane(panelIconos);
        scrollIconos.setBorder(null);
        scrollIconos.setPreferredSize(new Dimension(0, 140)); 
        scrollIconos.getViewport().setBackground(COLOR_FONDO);
        
        panelForm.add(scrollIconos, gbc);

        add(panelForm, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        panelBotones.setBackground(COLOR_FONDO);

        JButton btnCancelar = crearBoton("Cancelar", new Color(70, 70, 70));
        btnCancelar.addActionListener(e -> dispose());

        JButton btnGuardar = crearBoton("Guardar Rutina", COLOR_ACENTO);
        btnGuardar.addActionListener(e -> guardar());

        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    
    private JLabel crearCuadroIcono(String nombreArchivo) {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(80, 80)); 
        label.setOpaque(true);
        label.setBackground(COLOR_INPUT);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
       
        try {
            
            URL url = getClass().getResource("/resources/" + nombreArchivo);
            
            if (url != null) {
                
                ImageIcon iconOriginal = new ImageIcon(url);
                Image imgEscalada = iconOriginal.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(imgEscalada));
                label.setText(""); 
            } else {
                
                label.setText("<html><center style='font-size:9px; color:#999'>IMAGEN<br>NO<br>ENCONTRADA</center></html>");
                label.setForeground(Color.GRAY);
            }
        } catch (Exception e) {
            label.setText("ERROR");
        }

      
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                imagenSeleccionada = nombreArchivo;
                refrescarSeleccionIcono();
            }
        });
        
     
        label.putClientProperty("nombreArchivo", nombreArchivo);
        
        return label;
    }

    private void refrescarSeleccionIcono() {
        for (Component c : panelIconos.getComponents()) {
            if (c instanceof JLabel) {
                JLabel l = (JLabel) c;
                String nombre = (String) l.getClientProperty("nombreArchivo");
                
                if (nombre != null && nombre.equals(imagenSeleccionada)) {
             
                    l.setBorder(BorderFactory.createLineBorder(COLOR_ACENTO, 3)); 
                    l.setBackground(COLOR_ACENTO.darker().darker());
                } else {
                 
                    l.setBorder(BorderFactory.createLineBorder(COLOR_INPUT, 1));
                    l.setBackground(COLOR_INPUT);
                }
            }
        }
    }

    private void guardar() {
        String nombre = txtNombreRutina.getText().trim();
        String dia = (String) comboDiaSemana.getSelectedItem();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.");
            return;
        }

        boolean exito;
        if (rutinaAEditar != null) {
            rutinaAEditar.setNombreRutina(nombre);
            rutinaAEditar.setDiaSemana(dia);
            rutinaAEditar.setImagenNombre(imagenSeleccionada); 
            exito = dbManager.actualizarRutina(rutinaAEditar);
        } else {
        
            Rutina nueva = new Rutina(0, usuarioActual.getId(), nombre, dia, imagenSeleccionada);
            exito = dbManager.crearRutina(nueva);
        }

        if (exito) {
            fueExitoso = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar en la base de datos.");
        }
    }

    public boolean fueExitoso() { return fueExitoso; }

  
    private JLabel crearLabel(String t) {
        JLabel l = new JLabel(t);
        l.setForeground(COLOR_TEXTO);
        l.setFont(new Font("SansSerif", Font.BOLD, 12));
        return l;
    }
    private JTextField crearTextField() {
        JTextField t = new JTextField();
        t.setBackground(COLOR_INPUT);
        t.setForeground(COLOR_TEXTO);
        t.setCaretColor(COLOR_TEXTO);
        t.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return t;
    }
    private JButton crearBoton(String t, Color bg) {
        JButton b = new JButton(t);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setPreferredSize(new Dimension(140, 35));
        return b;
    }
}