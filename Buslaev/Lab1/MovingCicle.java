package com.company;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class MovingCicle extends JComponent implements ActionListener {
    private Color borderColor = Color.black;
    private Color backgroundColor = Color.yellow;
    private Circle circle;
    private static final int width = 800;
    private static final int height = 800;
    private int centerX = width / 2;
    private int centerY = height / 2;
    private int radius = 50;
    private float border = 4;
    private Timer timer;
    private int step = 50;
    private static final int Timestep = 200;
    private static final int maxradius = 350;
    private static final int minradius = 50;

    private MovingCicle(int delay) {
        timer = new Timer(delay, this);
        setPreferredSize(new Dimension(width, height));
    }

    private void start() {
        timer.start();
    }


    @Override
    public void actionPerformed(ActionEvent arg0) {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setStroke(new BasicStroke(border));
        circle = new Circle(centerX, centerY, radius, borderColor, backgroundColor);
        circle.paint(graphics2D);
        radius += step;
        if ((radius >= maxradius) || (radius <= minradius))
            step *= -1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Moving Circle");
                JPanel panel = new JPanel();
                final MovingCicle MovingCircle = new MovingCicle(Timestep);
                panel.add(MovingCircle);
                frame.getContentPane().add(panel);
                MovingCircle.start();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(width, height);
                frame.setVisible(true);
            }
        });
    }
}
