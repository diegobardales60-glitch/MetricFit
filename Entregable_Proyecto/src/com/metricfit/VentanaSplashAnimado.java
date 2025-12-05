package com.metricfit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.TimerTask; 

public class VentanaSplashAnimado extends JWindow {

    private Image logoOriginal;
    private Image logoEscalado;
    private Timer timer;
    
    private final int FIXED_LOGO_SIZE = 150; 
    private final int ANIMATION_DURATION = 3000; 
    private final int TIMER_DELAY = 20;
    
    private long startTime;
    private String currentLoadingText = "Cargando"; 
    private Timer textTimer;
    private int dotCount = 0; 

    public VentanaSplashAnimado() {
        setBackground(new Color(0, 0, 0, 0));
        
       
        URL imgUrl = getClass().getResource("/resources/logo.png"); 
        
        if (imgUrl != null) {
            ImageIcon icon = new ImageIcon(imgUrl);
            logoOriginal = icon.getImage();
        } else {
            System.err.println("SPLASH: ERROR. No se encuentra el logo en /resources/logo.png");
            logoOriginal = new java.awt.image.BufferedImage(100, 100, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        }
        
        
        if (logoOriginal != null) {
            logoEscalado = logoOriginal.getScaledInstance(FIXED_LOGO_SIZE, FIXED_LOGO_SIZE, Image.SCALE_SMOOTH);
        }

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                
                g2d.setColor(new Color(30, 30, 30, 240));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.setColor(new Color(255, 102, 0, 200));
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRoundRect(5, 5, getWidth() - 11, getHeight() - 11, 20, 20);

              
                if (logoEscalado != null) {
                    int logoX = (getWidth() - FIXED_LOGO_SIZE) / 2;
                    int logoY = (getHeight() - FIXED_LOGO_SIZE) / 2 - 20; 
                    g2d.drawImage(logoEscalado, logoX, logoY, FIXED_LOGO_SIZE, FIXED_LOGO_SIZE, this);
                } else {
                    g2d.setColor(Color.RED);
                    g2d.setFont(new Font("Arial", Font.BOLD, 12));
                    g2d.drawString("LOGO MISSING", 5, 15);
                }

               
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 18));
                FontMetrics metrics = g2d.getFontMetrics();
                
                String animatedText = currentLoadingText + ".".repeat(dotCount);
                
                int textX = (getWidth() - metrics.stringWidth(animatedText)) / 2;
                int textY = (getHeight() / 2) + (FIXED_LOGO_SIZE / 2) + 40; 
                
                g2d.drawString(animatedText, textX, textY); 
                
                g2d.dispose();
            }
        };
        panel.setOpaque(false);
        getContentPane().add(panel);
        setSize(400, 400); 
        setLocationRelativeTo(null); 

        
        timer = new Timer(TIMER_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                
                if (elapsedTime >= ANIMATION_DURATION) {
                    timer.stop();
                    textTimer.stop();
                    
                    
                    SwingUtilities.invokeLater(() -> {
                         dispose();
                         new VentanaLogin().setVisible(true);
                    });
                }
            }
        });
        
        
        textTimer = new Timer(500, e -> {
            dotCount = (dotCount + 1) % 4;
            panel.repaint();
        });
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            startTime = System.currentTimeMillis();
            timer.start();
            textTimer.start();
        }
    }
    
    
}