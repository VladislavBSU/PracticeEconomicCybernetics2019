package package1;
import javax.swing.*;
import java.awt.*;

public class ComplexShapeJPanel extends JPanel {
    private final ComplexShape shape;
    private final Color backgroundColor, shapeColor, fillColor;
    private final Stroke stroke;

    public ComplexShapeJPanel(final ComplexShape shape, final Color backgroundColor, final Color shapeColor, final Color fillColor, final Stroke stroke) {
        super();
        this.shape = shape;
        this.backgroundColor = backgroundColor;
        this.shapeColor = shapeColor;
        this.fillColor = fillColor;
        this.stroke = stroke;
    }

    @Override
    public void paint(final Graphics graphics) {
        super.paint(graphics);
        final Graphics2D g2d = (Graphics2D) graphics;
        g2d.setColor(backgroundColor);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setColor(shapeColor);

        //antialias
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //translating shape so its center matches the frame center
        g2d.translate(getWidth() / 2 - shape.getBounds2D().getCenterX(), getHeight() / 2 - shape.getBounds2D().getCenterY());

        //drawing
        g2d.setStroke(stroke);
        g2d.draw(shape);
        g2d.setColor(fillColor);
        g2d.fill(shape);
    }
}
