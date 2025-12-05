package com.metricfit;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*; 
import java.net.URL; 

public class VentanaRegistro extends JFrame {

   
    private JTextField txtNombre;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JPasswordField txtPasswordRepetir;
    private JButton btnRegistrarse;
    private JLabel lblVolverLogin;

   
    private JProgressBar barStrength;
    private JLabel lblShowHidePass1;
    private JLabel lblShowHidePass2;
    private ImageIcon iconEyeOpen;
    private ImageIcon iconEyeClosed;
    private boolean passwordVisible1 = false;
    private boolean passwordVisible2 = false;

    private final Color COLOR_FONDO = new Color(36, 36, 36);
    private final Color COLOR_CAMPO_FONDO = new Color(60, 60, 60);
    private final Color COLOR_BORDE_NORMAL = new Color(80, 80, 80);
    private final Color COLOR_BORDE_FOCUS = new Color(255, 102, 0);
    private final Color COLOR_PLACEHOLDER = new Color(150, 150, 150);
    private final Color COLOR_TEXTO = Color.WHITE;
    private final Color COLOR_NARANJA = new Color(255, 102, 0);

    private JPanel panelRegistro;

    public VentanaRegistro() {
        setTitle("MetricFit - Crear Cuenta");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null);

        getContentPane().setBackground(COLOR_FONDO);
        setLayout(null);

        iconEyeOpen = cargarIcono("/resources/icon_eye_open_white.png");
        iconEyeClosed = cargarIcono("/resources/icon_eye_closed_white.png");

        panelRegistro = new JPanel();
        panelRegistro.setBackground(COLOR_FONDO);
        panelRegistro.setLayout(null);
        add(panelRegistro);

        ImageIcon logoIconOriginal = cargarIcono("/resources/logo_metricfit.png");

        if (logoIconOriginal.getImage().getHeight(null) > 1) { // Pequeña verificación para ver si se cargó algo real
            Image logoImg = logoIconOriginal.getImage().getScaledInstance(360, 85, Image.SCALE_SMOOTH);
            ImageIcon logoIcon = new ImageIcon(logoImg);
            JLabel lblLogo = new JLabel(logoIcon);
            lblLogo.setBounds(20, 30, 360, 85);
            panelRegistro.add(lblLogo);
        } else {
            JLabel lblError = new JLabel("MetricFit (Logo No Encontrado)");
            lblError.setForeground(Color.RED);
            lblError.setFont(new Font("Arial", Font.BOLD, 16));
            lblError.setBounds(20, 30, 360, 85);
            panelRegistro.add(lblError);
        }

        JLabel lblTitulo = new JLabel("Crear una cuenta");
        lblTitulo.setForeground(COLOR_TEXTO);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setBounds(0, 125, 400, 30);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        panelRegistro.add(lblTitulo);

        JLabel lblSubtitulo = new JLabel("Únete a MetricFit hoy mismo");
        lblSubtitulo.setForeground(COLOR_PLACEHOLDER);
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSubtitulo.setBounds(0, 155, 400, 20);
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        panelRegistro.add(lblSubtitulo);

        JPanel panelNombre = new JPanel(new BorderLayout(10, 0));
        panelNombre.setBackground(COLOR_CAMPO_FONDO);
        panelNombre.setBounds(20, 185, 360, 45);
        panelNombre.setBorder(crearBordeCampo());
        panelRegistro.add(panelNombre);

        JLabel iconNombre = new JLabel(cargarIcono("/resources/icon_user_white.png"));
        panelNombre.add(iconNombre, BorderLayout.WEST);

        txtNombre = new JTextField("Nombre de Usuario");
        personalizarCampoTexto(txtNombre, "Nombre de Usuario", panelNombre);
        panelNombre.add(txtNombre, BorderLayout.CENTER);

        JPanel panelEmail = new JPanel(new BorderLayout(10, 0));
        panelEmail.setBackground(COLOR_CAMPO_FONDO);
        panelEmail.setBounds(20, 240, 360, 45);
        panelEmail.setBorder(crearBordeCampo());
        panelRegistro.add(panelEmail);

        JLabel iconEmail = new JLabel(cargarIcono("/resources/icon_email_white.png"));
        panelEmail.add(iconEmail, BorderLayout.WEST);

        txtEmail = new JTextField("Email");
        personalizarCampoTexto(txtEmail, "Email", panelEmail);
        panelEmail.add(txtEmail, BorderLayout.CENTER);

        JPanel panelPassword = new JPanel(new BorderLayout(10, 0));
        panelPassword.setBackground(COLOR_CAMPO_FONDO);
        panelPassword.setBounds(20, 295, 360, 45);
        panelPassword.setBorder(crearBordeCampo());
        panelRegistro.add(panelPassword);

        JLabel iconPassword = new JLabel(cargarIcono("/resources/icon_lock_white.png"));
        panelPassword.add(iconPassword, BorderLayout.WEST);

        txtPassword = new JPasswordField("Contraseña");
        personalizarCampoTexto(txtPassword, "Contraseña", panelPassword);
        panelPassword.add(txtPassword, BorderLayout.CENTER);

        lblShowHidePass1 = new JLabel(iconEyeClosed);
        lblShowHidePass1.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelPassword.add(lblShowHidePass1, BorderLayout.EAST);

        barStrength = new JProgressBar(0, 100);
        barStrength.setBounds(20, 342, 360, 10); 
        barStrength.setBorder(null);
        barStrength.setBorderPainted(false);
        barStrength.setBackground(COLOR_CAMPO_FONDO);
        barStrength.setForeground(Color.RED); 
        barStrength.setValue(0);
        panelRegistro.add(barStrength);

        JPanel panelPasswordRepetir = new JPanel(new BorderLayout(10, 0));
        panelPasswordRepetir.setBackground(COLOR_CAMPO_FONDO);
        panelPasswordRepetir.setBounds(20, 357, 360, 45); 
        panelPasswordRepetir.setBorder(crearBordeCampo());
        panelRegistro.add(panelPasswordRepetir);

        JLabel iconPasswordRepetir = new JLabel(cargarIcono("/resources/icon_lock_white.png"));
        panelPasswordRepetir.add(iconPasswordRepetir, BorderLayout.WEST);

        txtPasswordRepetir = new JPasswordField("Repetir Contraseña");
        personalizarCampoTexto(txtPasswordRepetir, "Repetir Contraseña", panelPasswordRepetir);
        panelPasswordRepetir.add(txtPasswordRepetir, BorderLayout.CENTER);

        lblShowHidePass2 = new JLabel(iconEyeClosed);
        lblShowHidePass2.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelPasswordRepetir.add(lblShowHidePass2, BorderLayout.EAST);

        btnRegistrarse = new JButton("REGISTRARSE");
        btnRegistrarse.setBounds(20, 415, 360, 45); 
        btnRegistrarse.setBackground(COLOR_NARANJA);
        btnRegistrarse.setForeground(COLOR_TEXTO);
        btnRegistrarse.setFont(new Font("Arial", Font.BOLD, 16));
        btnRegistrarse.setBorderPainted(false);
        btnRegistrarse.setFocusPainted(false);
        btnRegistrarse.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelRegistro.add(btnRegistrarse);

        lblVolverLogin = new JLabel("¿Ya tienes cuenta? Inicia Sesión");
        lblVolverLogin.setForeground(COLOR_PLACEHOLDER);
        lblVolverLogin.setFont(new Font("Arial", Font.PLAIN, 12));
        lblVolverLogin.setBounds(180, 470, 200, 25); // <-- Y-pos ajustada
        lblVolverLogin.setHorizontalAlignment(SwingConstants.RIGHT);
        lblVolverLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelRegistro.add(lblVolverLogin);

        agregarLogicaBotones();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                centrarPanel();
            }
        });

        centrarPanel();
    }
    
    private ImageIcon cargarIcono(String ruta) {
        URL url = getClass().getResource(ruta);
        if (url == null) {
            return new ImageIcon(new java.awt.image.BufferedImage(1, 1, java.awt.image.BufferedImage.TYPE_INT_ARGB));
        }
        ImageIcon original = new ImageIcon(url);
        if (!ruta.contains("logo")) {
             return new ImageIcon(original.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        }
        return original;
    }

    private void centrarPanel() {
        int anchoVentana = getWidth();
        int altoVentana = getHeight();
        int anchoPanel = 400;
        int altoPanel = 550;

        int x = (anchoVentana - anchoPanel) / 2;
        int y = (altoVentana - altoPanel) / 2;

        panelRegistro.setBounds(x, y, anchoPanel, altoPanel);
    }

    private Border crearBordeCampo() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDE_NORMAL, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        );
    }

    private void actualizarFortalezaPassword() {
        String pass = String.valueOf(txtPassword.getPassword());
        int strength = 0;

        if (pass.length() >= 8) {
            strength += 25;
        }
        if (pass.matches(".*[A-Z].*")) {
            strength += 25;
        }
        if (pass.matches(".*[0-9].*")) {
            strength += 25;
        }
        if (pass.matches(".*[\\W_].*")) { // \W es "no-palabra" (símbolo)
            strength += 25;
        }

        barStrength.setValue(strength);
        if (strength < 50) {
            barStrength.setForeground(Color.RED);
        } else if (strength < 75) {
            barStrength.setForeground(Color.YELLOW);
        } else {
            barStrength.setForeground(Color.GREEN);
        }
    }

    private void personalizarCampoTexto(JTextField campo, String placeholder, JPanel panel) {
        campo.setBackground(COLOR_CAMPO_FONDO);
        campo.setForeground(COLOR_PLACEHOLDER);
        campo.setCaretColor(COLOR_TEXTO);
        campo.setBorder(null);
        campo.setFont(new Font("Arial", Font.PLAIN, 14));

        if (campo instanceof JPasswordField) {
            ((JPasswordField) campo).setEchoChar((char) 0);
            if (campo == txtPassword) {
                campo.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        actualizarFortalezaPassword();
                    }
                });
            }
        }

        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_BORDE_FOCUS, 1),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));

                String textoActual = (campo instanceof JPasswordField) ? String.valueOf(((JPasswordField) campo).getPassword()) : campo.getText();

                if (textoActual.equals(placeholder)) {
                    campo.setText("");
                    campo.setForeground(COLOR_TEXTO);
                    if (campo instanceof JPasswordField) {
                        ((JPasswordField) campo).setEchoChar('•');
                    }
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                panel.setBorder(crearBordeCampo());

                String textoActual = (campo instanceof JPasswordField) ? String.valueOf(((JPasswordField) campo).getPassword()) : campo.getText();

                if (textoActual.isEmpty()) {
                    campo.setText(placeholder);
                    campo.setForeground(COLOR_PLACEHOLDER);
                    if (campo instanceof JPasswordField) {
                        ((JPasswordField) campo).setEchoChar((char) 0);
                    }
                }
            }
        });
    }

    private void agregarLogicaBotones() {
        btnRegistrarse.addActionListener(e -> {
            String nombre = txtNombre.getText();
            String email = txtEmail.getText();
            String pass = String.valueOf(txtPassword.getPassword());
            String passRep = String.valueOf(txtPasswordRepetir.getPassword());

            if (nombre.equals("Nombre de Usuario")) nombre = "";
            if (email.equals("Email")) email = "";
            if (pass.equals("Contraseña")) pass = "";
            if (passRep.equals("Repetir Contraseña")) passRep = "";

            if (nombre.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
            if (!email.matches(emailRegex)) {
                JOptionPane.showMessageDialog(this, "Por favor, introduce un email válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!pass.equals(passRep)) {
                JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            DatabaseManager db = new DatabaseManager();
            boolean registrado = db.registrarUsuario(nombre, email, pass);
            

            if (registrado) {
                JOptionPane.showMessageDialog(this, "¡Registro exitoso! Ahora puedes iniciar sesión con tu email.", "Registro Completo", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new VentanaLogin().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar. Es posible que el nombre de usuario o el email ya estén en uso.", "Error de Registro", JOptionPane.ERROR_MESSAGE);
            }
        });

        lblVolverLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new VentanaLogin().setVisible(true);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                lblVolverLogin.setText("<html>¿Ya tienes cuenta? <u>Inicia Sesión</u></html>");
            }
            @Override
            public void mouseExited(MouseEvent e) {
                lblVolverLogin.setText("¿Ya tienes cuenta? Inicia Sesión");
            }
        });

        
        lblShowHidePass1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (passwordVisible1) {
                    txtPassword.setEchoChar('•');
                    lblShowHidePass1.setIcon(iconEyeClosed);
                } else {
                    txtPassword.setEchoChar((char) 0);
                    lblShowHidePass1.setIcon(iconEyeOpen);
                }
                passwordVisible1 = !passwordVisible1;
            }
        });

        lblShowHidePass2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (passwordVisible2) {
                    txtPasswordRepetir.setEchoChar('•');
                    lblShowHidePass2.setIcon(iconEyeClosed);
                } else {
                    txtPasswordRepetir.setEchoChar((char) 0);
                    lblShowHidePass2.setIcon(iconEyeOpen);
                }
                passwordVisible2 = !passwordVisible2;
            }
        });
    }
}