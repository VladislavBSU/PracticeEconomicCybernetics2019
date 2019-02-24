package package1;

import javax.swing.*;
import java.awt.*;

public final class Main {
    private static final int WINDOW_HEIGHT = 400, WINDOW_WIDTH = 2 * WINDOW_HEIGHT;

    private Main() {}

    public static void main(final String[] args) {
        final JFrame frame = new JFrame();
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        final ComplexShapeJPanel panel = new ComplexShapeJPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}