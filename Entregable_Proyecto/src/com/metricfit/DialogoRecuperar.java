package com.metricfit;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class DialogoRecuperar extends JDialog {

    private JTextField txtEmail, txtUsuario;
    private JPasswordField txtNuevaPass;
    private final Color COL_FONDO = new Color(36, 36, 36);
    private final Color COL_INPUT = new Color(60, 60, 60);
    private final Color COL_TEXTO = Color.WHITE;
    private final Color COL_ACENTO = new Color(255, 102, 0);

    public DialogoRecuperar(Frame parent) {
        super(parent, "Recuperar Contraseña", true);
        setSize(400, 450);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(COL_FONDO);
        setLayout(null);

        inicializarUI();
    }

    private void inicializarUI() {
        JLabel lblTitulo = new JLabel("Restablecer Password", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitulo.setForeground(COL_TEXTO);
        lblTitulo.setBounds(0, 20, 400, 30);
        add(lblTitulo);

        int y = 70;
        
        add(crearLabel("Tu Correo Electrónico:", 40, y));
        txtEmail = crearInput(40, y + 25);
        add(txtEmail);
        
        y += 70;
        add(crearLabel("Tu Nombre de Usuario:", 40, y));
        txtUsuario = crearInput(40, y + 25);
        add(txtUsuario);

        y += 70;
        add(crearLabel("Nueva Contraseña:", 40, y));
        txtNuevaPass = new JPasswordField();
        txtNuevaPass.setBounds(40, y + 25, 300, 35);
        estilizarInput(txtNuevaPass);
        add(txtNuevaPass);

        y += 80;
        JButton btnCambiar = new JButton("CONFIRMAR CAMBIO");
        btnCambiar.setBounds(40, y, 300, 45);
        btnCambiar.setBackground(COL_ACENTO);
        btnCambiar.setForeground(COL_TEXTO);
        btnCambiar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCambiar.setFocusPainted(false);
        btnCambiar.setBorderPainted(false);
        btnCambiar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnCambiar.addActionListener(e -> procesarCambio());
        add(btnCambiar);
    }

    private void procesarCambio() {
        String email = txtEmail.getText().trim();
        String user = txtUsuario.getText().trim();
        String pass = String.valueOf(txtNuevaPass.getPassword());

        if (email.isEmpty() || user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos.");
            return;
        }

        DatabaseManager db = new DatabaseManager();
        
        if (db.validarUsuarioParaRecuperacion(email, user)) {
            if (db.restablecerContrasena(email, pass)) {
                JOptionPane.showMessageDialog(this, "¡Contraseña actualizada correctamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Los datos no coinciden con ningún usuario.", "Error de Seguridad", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Helpers UI
    private JLabel crearLabel(String t, int x, int y) {
        JLabel l = new JLabel(t);
        l.setForeground(Color.LIGHT_GRAY);
        l.setFont(new Font("SansSerif", Font.BOLD, 12));
        l.setBounds(x, y, 300, 20);
        return l;
    }
    private JTextField crearInput(int x, int y) {
        JTextField t = new JTextField();
        t.setBounds(x, y, 300, 35);
        estilizarInput(t);
        return t;
    }
    private void estilizarInput(JTextField t) {
        t.setBackground(COL_INPUT);
        t.setForeground(COL_TEXTO);
        t.setCaretColor(COL_ACENTO);
        t.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80,80,80)), 
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    }
}