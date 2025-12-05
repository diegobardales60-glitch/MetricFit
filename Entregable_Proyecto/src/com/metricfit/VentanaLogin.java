package com.metricfit;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.prefs.Preferences;

public class VentanaLogin extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtPassword; 
    private JButton btnIniciarSesion;
    private JLabel lblOlvidaste, lblRegistrarse, lblShowHidePass;
    private JCheckBox chkRecordarme;
    private ImageIcon iconEyeOpen, iconEyeClosed, iconCheckbox, iconCheckboxChecked;
    private boolean passwordVisible = false;
    
    private final Color COLOR_FONDO = new Color(36, 36, 36);
    private final Color COLOR_CAMPO_FONDO = new Color(60, 60, 60);
    private final Color COLOR_BORDE_NORMAL = new Color(80, 80, 80);
    private final Color COLOR_BORDE_FOCUS = new Color(255, 102, 0);
    private final Color COLOR_PLACEHOLDER = new Color(150, 150, 150);
    private final Color COLOR_TEXTO = Color.WHITE;
    private final Color COLOR_NARANJA = new Color(255, 102, 0);
    
    private JPanel panelLogin;
    private Preferences prefs;
    private static final String PREF_EMAIL = "email";
    private static final String PREF_PASSWORD = "password";

    public VentanaLogin() {
        prefs = Preferences.userNodeForPackage(VentanaLogin.class);
        setTitle("MetricFit - Iniciar Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(null);

        iconEyeOpen = cargarIcono("/resources/icon_eye_open_white.png");
        iconEyeClosed = cargarIcono("/resources/icon_eye_closed_white.png");
        iconCheckbox = cargarIcono("/resources/icon_checkbox_white.png");
        iconCheckboxChecked = cargarIcono("/resources/icon_checkbox_checked_white.png");

        panelLogin = new JPanel();
        panelLogin.setBackground(COLOR_FONDO);
        panelLogin.setLayout(null);
        add(panelLogin);

        ImageIcon logoIconOriginal = cargarIcono("/resources/logo_metricfit.png");
        if (logoIconOriginal != null) {
            Image logoImg = logoIconOriginal.getImage().getScaledInstance(360, 85, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoImg));
            lblLogo.setBounds(20, 30, 360, 85);
            panelLogin.add(lblLogo);
        } else {
            JLabel lblFallback = new JLabel("MetricFit");
            lblFallback.setForeground(COLOR_TEXTO);
            lblFallback.setFont(new Font("Arial", Font.BOLD, 40));
            lblFallback.setBounds(20, 30, 360, 85);
            panelLogin.add(lblFallback);
        }
        
        JLabel lblBienvenido = new JLabel("Bienvenido");
        lblBienvenido.setForeground(COLOR_TEXTO);
        lblBienvenido.setFont(new Font("Arial", Font.BOLD, 24));
        lblBienvenido.setBounds(0, 125, 400, 30);
        lblBienvenido.setHorizontalAlignment(SwingConstants.CENTER);
        panelLogin.add(lblBienvenido);

        JLabel lblSubtitulo = new JLabel("Inicia sesión en tu cuenta");
        lblSubtitulo.setForeground(COLOR_PLACEHOLDER);
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSubtitulo.setBounds(0, 155, 400, 20);
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        panelLogin.add(lblSubtitulo);

        JPanel panelEmail = new JPanel(new BorderLayout(10, 0));
        panelEmail.setBackground(COLOR_CAMPO_FONDO);
        panelEmail.setBounds(20, 185, 360, 45);
        panelEmail.setBorder(crearBordeCampo());
        panelLogin.add(panelEmail);

        JLabel iconEmail = new JLabel(cargarIcono("/resources/icon_email_white.png"));
        panelEmail.add(iconEmail, BorderLayout.WEST);

        txtEmail = new JTextField("Email");
        personalizarCampoTexto(txtEmail, "Email", panelEmail);
        panelEmail.add(txtEmail, BorderLayout.CENTER);

        JPanel panelPassword = new JPanel(new BorderLayout(10, 0));
        panelPassword.setBackground(COLOR_CAMPO_FONDO);
        panelPassword.setBounds(20, 240, 360, 45);
        panelPassword.setBorder(crearBordeCampo());
        panelLogin.add(panelPassword);

        JLabel iconPassword = new JLabel(cargarIcono("/resources/icon_lock_white.png"));
        panelPassword.add(iconPassword, BorderLayout.WEST);

        txtPassword = new JPasswordField("Contraseña");
        personalizarCampoTexto(txtPassword, "Contraseña", panelPassword);
        panelPassword.add(txtPassword, BorderLayout.CENTER);

        lblShowHidePass = new JLabel(iconEyeClosed);
        lblShowHidePass.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelPassword.add(lblShowHidePass, BorderLayout.EAST);

        btnIniciarSesion = new JButton("INICIAR SESIÓN");
        btnIniciarSesion.setBounds(20, 300, 360, 45);
        btnIniciarSesion.setBackground(COLOR_NARANJA);
        btnIniciarSesion.setForeground(COLOR_TEXTO);
        btnIniciarSesion.setFont(new Font("Arial", Font.BOLD, 16));
        btnIniciarSesion.setFocusPainted(false);
        btnIniciarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelLogin.add(btnIniciarSesion);

        chkRecordarme = new JCheckBox(" Recordarme");
        chkRecordarme.setBounds(20, 355, 150, 25);
        chkRecordarme.setBackground(COLOR_FONDO);
        chkRecordarme.setForeground(COLOR_PLACEHOLDER);
        chkRecordarme.setFont(new Font("Arial", Font.PLAIN, 12));
        chkRecordarme.setIcon(iconCheckbox);
        chkRecordarme.setSelectedIcon(iconCheckboxChecked);
        panelLogin.add(chkRecordarme);

        lblOlvidaste = new JLabel("¿Olvidaste tu contraseña?");
        lblOlvidaste.setBounds(220, 355, 160, 25);
        lblOlvidaste.setHorizontalAlignment(SwingConstants.RIGHT);
        lblOlvidaste.setForeground(COLOR_PLACEHOLDER);
        lblOlvidaste.setFont(new Font("Arial", Font.PLAIN, 12));
        lblOlvidaste.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelLogin.add(lblOlvidaste);

        lblRegistrarse = new JLabel("¿No tienes cuenta? Registrarte");
        lblRegistrarse.setForeground(COLOR_PLACEHOLDER);
        lblRegistrarse.setFont(new Font("Arial", Font.PLAIN, 12));
        lblRegistrarse.setBounds(180, 395, 200, 25);
        lblRegistrarse.setHorizontalAlignment(SwingConstants.RIGHT);
        lblRegistrarse.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelLogin.add(lblRegistrarse);

        agregarLogicaBotones(); 
        
        addComponentListener(new ComponentAdapter() { public void componentResized(ComponentEvent evt) { centrarPanelLogin(); } });
        centrarPanelLogin();
        cargarPreferencias();
    }

    private ImageIcon cargarIcono(String ruta) {
        URL url = getClass().getResource(ruta);
        if (url == null) return null;
        ImageIcon original = new ImageIcon(url);
        if (!ruta.contains("logo")) {
             return new ImageIcon(original.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        }
        return original;
    }
    
    private void centrarPanelLogin() {
        int x = (getWidth() - 400) / 2;
        int y = (getHeight() - 450) / 2;
        panelLogin.setBounds(x, y, 400, 450);
    }
    
    private Border crearBordeCampo() { return BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(COLOR_BORDE_NORMAL, 1), BorderFactory.createEmptyBorder(5, 10, 5, 10)); }
    
    private void personalizarCampoTexto(JTextField campo, String placeholder, JPanel panel) {
        campo.setBackground(COLOR_CAMPO_FONDO);
        campo.setForeground(COLOR_PLACEHOLDER);
        campo.setCaretColor(COLOR_TEXTO);
        campo.setBorder(null);
        campo.setFont(new Font("Arial", Font.PLAIN, 14));
        if (campo instanceof JPasswordField) ((JPasswordField) campo).setEchoChar((char) 0);
        
        campo.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(COLOR_BORDE_FOCUS, 1), BorderFactory.createEmptyBorder(5, 10, 5, 10)));
                if (campo.getText().equals(placeholder)) { campo.setText(""); campo.setForeground(COLOR_TEXTO); if (campo instanceof JPasswordField) ((JPasswordField) campo).setEchoChar('•'); }
            }
            public void focusLost(FocusEvent e) {
                panel.setBorder(crearBordeCampo());
                if (campo.getText().isEmpty()) { campo.setText(placeholder); campo.setForeground(COLOR_PLACEHOLDER); if (campo instanceof JPasswordField) ((JPasswordField) campo).setEchoChar((char) 0); }
            }
        });
    }
    
    private void cargarPreferencias() {
        if (prefs.get(PREF_EMAIL, null) != null) {
            txtEmail.setText(prefs.get(PREF_EMAIL, ""));
            txtEmail.setForeground(COLOR_TEXTO);
            txtPassword.setText(prefs.get(PREF_PASSWORD, ""));
            txtPassword.setEchoChar('•');
            txtPassword.setForeground(COLOR_TEXTO);
            chkRecordarme.setSelected(true);
        }
    }
    
    private void guardarPreferencias() { prefs.put(PREF_EMAIL, txtEmail.getText()); prefs.put(PREF_PASSWORD, String.valueOf(txtPassword.getPassword())); }
    
    private void borrarPreferencias() { prefs.remove(PREF_EMAIL); prefs.remove(PREF_PASSWORD); }

    private void agregarLogicaBotones() {
        
        btnIniciarSesion.addActionListener(evt -> { 
            
            String email = txtEmail.getText().trim();
            String pass = new String(txtPassword.getPassword()); 
            
            boolean emailVacio = email.isEmpty() || email.equals("Email");
            boolean passVacio = pass.isEmpty() || pass.equals("Contraseña");

            if (emailVacio || passVacio) { 
                JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE); 
                return; 
            }
            
            DatabaseManager db = new DatabaseManager();
            Usuario u = db.iniciarSesion(email, pass);
            
            if (u != null) { 
                if (chkRecordarme.isSelected()) guardarPreferencias(); else borrarPreferencias(); 
                new VentanaPrincipal(u, db).setVisible(true); 
                dispose(); 
            }
            else { 
                JOptionPane.showMessageDialog(this, "Email o contraseña incorrectos.", "Error de Acceso", JOptionPane.ERROR_MESSAGE); 
            }
        });
        
        lblRegistrarse.addMouseListener(new MouseAdapter() { 
            public void mouseClicked(MouseEvent e) { 
                dispose(); 
                new VentanaRegistro().setVisible(true); 
            } 
        });
        
        
        lblOlvidaste.addMouseListener(new MouseAdapter() { 
            public void mouseClicked(MouseEvent e) { 
                new DialogoRecuperar(VentanaLogin.this).setVisible(true); 
            } 
        });
        
        lblShowHidePass.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                passwordVisible = !passwordVisible;
                txtPassword.setEchoChar(passwordVisible ? (char) 0 : '•');
                if(passwordVisible && iconEyeOpen != null) lblShowHidePass.setIcon(iconEyeOpen);
                else if (!passwordVisible && iconEyeClosed != null) lblShowHidePass.setIcon(iconEyeClosed);
            }
        });
    }
}