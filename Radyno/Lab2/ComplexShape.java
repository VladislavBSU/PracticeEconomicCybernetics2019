package package1;

import javax.swing.*;
import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;

public class ComplexShape implements Shape {
    private Area shapeArea;
    private final static int WIDTH = 300, HEIGHT = 300, FONT_SIZE = 200, BORDER = 80;
    private final static String LINE = "-", FONT_NAME = "Monospaced";

    public ComplexShape(final Graphics2D graphics2D, final JPanel panel) {
        final Font font = new Font(FONT_NAME, Font.BOLD, FONT_SIZE);
        final FontRenderContext fontRenderContext = graphics2D.getFontRenderContext();
        final AffineTransform transform = AffineTransform.getTranslateInstance(panel.getWidth() / 4, panel.getHeight() / 2);

        Shape outterEllipse = new Ellipse2D.Double(-WIDTH / 2, -HEIGHT / 2, WIDTH, HEIGHT);
        Shape innerEllipse = new Ellipse2D.Double((BORDER - WIDTH) / 2, (BORDER - HEIGHT) / 2, WIDTH - BORDER, HEIGHT - BORDER);

        final TextLayout textLayout = new TextLayout(LINE, font, fontRenderContext);
        outterEllipse = transform.createTransformedShape(outterEllipse);
        innerEllipse = transform.createTransformedShape(innerEllipse);
        transform.translate(-FONT_SIZE / 4, FONT_SIZE / 4);
        final Shape line = textLayout.getOutline(transform);

        this.shapeArea = new Area();
        this.shapeArea.add(new Area(outterEllipse));
        this.shapeArea.subtract(new Area(innerEllipse));
        this.shapeArea.add(new Area(line));
    }

    @Override
    public Rectangle getBounds() {
        return this.shapeArea.getBounds();
    }

    @Override
    public Rectangle2D getBounds2D() {
        return this.shapeArea.getBounds2D();
    }

    @Override
    public boolean contains(final double x, final double y) {
        return this.shapeArea.contains(x, y);
    }

    @Override
    public boolean contains(final Point2D p) {
        return this.shapeArea.contains(p);
    }

    @Override
    public boolean intersects(final double x, final double y, final double w, final double h) {
        return this.shapeArea.intersects(x, y, w, h);
    }

    @Override
    public boolean intersects(final Rectangle2D r) {
        return this.shapeArea.intersects(r);
    }

    @Override
    public boolean contains(final double x, final double y, final double w, final double h) {
        return this.shapeArea.contains(x, y, w, h);
    }

    @Override
    public boolean contains(final Rectangle2D r) {
        return this.shapeArea.contains(r);
    }

    @Override
    public PathIterator getPathIterator(final AffineTransform at) {
        return this.shapeArea.getPathIterator(at);
    }

    @Override
    public PathIterator getPathIterator(final AffineTransform at, final double flatness) {
        return this.shapeArea.getPathIterator(at, flatness);
    }
}
