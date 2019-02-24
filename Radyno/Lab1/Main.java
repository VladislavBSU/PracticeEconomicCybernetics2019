package package1;
import java.awt.*;
import javax.swing.*;

public final class Main {
    private static final int WINDOW_WIDTH = 300, WINDOW_HEIGHT = 400;
    private static final int SHAPE_WIDTH = 200, SHAPE_HEIGHT = 100;
    private static final int MAX_ANGLE = 360;
    private static final int DEF_SHAPE_CLR = 0x000000, DEF_FILL_CLR = 0xAABBCC;
    private static final float DEF_LINE_WIDTH = 2.0f;

    private static final ComplexShape SHAPE = new ComplexShape(SHAPE_WIDTH, SHAPE_HEIGHT);
    private static double degAngle, radAngle;
    private static final Color BACKGROUND_COLOR = new Color(0xFFFFFF);

    private Main() {}

    public static void main(final String[] args) {
        Color shapeColor;
        Color fillColor;
        Stroke stroke;
        try {
            final String strokeWidthStr = args[0];
            final float strokeWidth = Float.parseFloat(strokeWidthStr);
            stroke = new BasicStroke(strokeWidth);
            final String shapeColorStr = args[1];
            final int shapeColorValue = Integer.parseInt(shapeColorStr, 16);
            shapeColor = new Color(shapeColorValue);
            final String fillColorStr = args[2];
            final int fillColorValue = Integer.parseInt(fillColorStr, 16);
            fillColor = new Color(fillColorValue);
        } catch (Exception ignored) {
            stroke = new BasicStroke(DEF_LINE_WIDTH);
            shapeColor = new Color(DEF_SHAPE_CLR);
            fillColor = new Color(DEF_FILL_CLR);
        }

        final JFrame frame = new JFrame();
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        final ComplexShapeJPanel panel = new ComplexShapeJPanel(SHAPE, BACKGROUND_COLOR, shapeColor, fillColor, stroke);

        final Timer timer = new Timer(5, arg0 -> {
            degAngle = degAngle < MAX_ANGLE ? degAngle + 1 : 1;
            radAngle = degAngle * Math.PI / 180;
            SHAPE.setAngle(radAngle);

            panel.revalidate();
            panel.repaint();
        });
        timer.start();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
