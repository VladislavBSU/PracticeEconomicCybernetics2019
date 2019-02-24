package package1;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;

public class ComplexShapeJPanel extends JPanel {
    private final static int START_COLOR_VAL = 0x111111, END_COLOR_VAL = 0xEEEEEE;
    private final static int SHADOW_COLOR_VAL = 0x33111111;
    private final static Color START_COLOR = new Color(START_COLOR_VAL);
    private final static Color END_COLOR = new Color(END_COLOR_VAL);

    @Override
    protected void paintComponent(final Graphics graphics) {
        super.paintComponent(graphics);
        BufferedImage bufferedImage = new BufferedImage(getWidth() / 2, getHeight(), BufferedImage.TYPE_INT_RGB);
        final Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        final GradientPaint backPaint = new GradientPaint(0, 0, START_COLOR, getWidth(), 0, END_COLOR);

        g2d.setPaint(backPaint);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        final ComplexShape shape = new ComplexShape(g2d, this);
        final Color shadowColor = new Color(SHADOW_COLOR_VAL, true);
        final AffineTransform shadowTransform = AffineTransform.getTranslateInstance(-shape.getBounds2D().getWidth() / 4, shape.getBounds2D().getHeight() / 4);
        final Shape transformedShape = shadowTransform.createTransformedShape(shape);
        g2d.setColor(shadowColor);
        g2d.fill(transformedShape);
        g2d.setColor(Color.red);
        g2d.fill(shape);

        final Kernel kernel = getBlurKernel();
        final ConvolveOp convolveOp = new ConvolveOp(kernel);
        graphics.drawImage(bufferedImage, 0, 0, null);
        bufferedImage = convolveOp.filter(bufferedImage, null);
        graphics.drawImage(bufferedImage, bufferedImage.getWidth(), 0, null);
    }

    private static Kernel getBlurKernel() {
        final float[] kernelData = new float[]{
                1, 4, 6, 4, 1,
                4, 16, 24, 16, 4,
                6, 24, 36, 24, 6,
                1, 4, 6, 4, 1,
                4, 16, 24, 16, 4
        };
        for (int i = 0; i < kernelData.length; ++i) {
            kernelData[i] /= 256f;
        }
        return new Kernel(5, 5, kernelData);
    }
}
