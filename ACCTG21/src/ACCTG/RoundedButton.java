package ACCTG;

import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {

    private boolean hover;

    // 🔹 Constructor for text-only buttons
    public RoundedButton(String text) {
        this(text, null); // delegate to the full constructor
    }

    // 🔹 Constructor for text + icon buttons
    public RoundedButton(String text, Icon icon) {
        super(text, icon);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 13));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setHorizontalTextPosition(SwingConstants.RIGHT); // icon left, text right
        setIconTextGap(5); // space between icon and text

        // Hover animation
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                hover = true;
                repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                hover = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 🔹 Theme colors
        Color base1 = new Color(0, 170, 170);
        Color base2 = new Color(0, 140, 140);
        Color hover1 = new Color(0, 190, 190);
        Color hover2 = new Color(0, 160, 160);

        // Gradient background
        GradientPaint gradient = new GradientPaint(
                0, 0,
                hover ? hover1 : base1,
                0, getHeight(),
                hover ? hover2 : base2
        );

        int arc = 12;
        g2.setPaint(gradient);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

        // Soft outline
        g2.setColor(new Color(0, 0, 0, 50));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);

        // Draw text & icon properly
        super.paintComponent(g2);
        g2.dispose();
    }
}
