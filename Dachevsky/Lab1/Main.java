package graphic;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Main extends JComponent implements ActionListener {

    private static final Color borderColor = Color.gray;
    private static final Color backgroundColor = Color.lightGray;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    private double theta = 0;
    private static final float BORDER = 4;
    private RectangleAffine rectangleAffine = new RectangleAffine(
            WIDTH / 2 - 200,
            HEIGHT / 2 - 100,
            200,
            100,
            theta,
            borderColor,
            backgroundColor);
    private final Timer timer;

    private Main() {
        timer = new Timer(20, this);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    private void start() {
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        rectangleAffine.setTheta(theta);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setStroke(new BasicStroke(BORDER));
        rectangleAffine.paint(graphics2D);
        theta += Math.PI / 180;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Rectangle");
            JPanel panel = new JPanel();
            Main movingRectangle = new Main();
            panel.add(movingRectangle);
            frame.getContentPane().add(panel);
            movingRectangle.start();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(WIDTH, HEIGHT);
            frame.setVisible(true);
        });
    }

}
