package graphic;

import java.awt.*;

import java.awt.geom.*;

public class RectangleAffine extends Canvas implements Shape {

    private Rectangle2D rectangle2D;
    private Color borderColor;
    private Color backgroundColor;
    private double gravityCenterX;
    private double gravityCenterY;
    private int startCoordinateX;
    private int startCoordinateY;
    private int width;
    private int height;
    private double theta;

    public void setTheta(double theta) {
        this.theta = theta;
    }

    public RectangleAffine(int startCoordinateX, int startCoordinateY, int width, int height, double theta, Color border, Color background) {
        this.startCoordinateX = startCoordinateX;
        this.startCoordinateY = startCoordinateY;
        this.theta = theta;
        this.width = width;
        this.height = height;
        this.gravityCenterX = width / 2;
        this.gravityCenterY = height / 2;
        this.rectangle2D = new Rectangle2D.Double(startCoordinateX, startCoordinateY, width, height);
        this.borderColor = border;
        this.backgroundColor = background;
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        AffineTransform at =
                AffineTransform.getRotateInstance(
                        this.theta,
                        this.gravityCenterX + this.startCoordinateX,
                        this.gravityCenterY + this.startCoordinateY);
        graphics2D.setTransform(at);
        graphics2D.setColor(backgroundColor);
        graphics2D.fillRect(this.startCoordinateX, this.startCoordinateY,
                this.width, this.height);
        graphics2D.setColor(borderColor);
        graphics2D.draw(rectangle2D);
    }

    @Override
    public Rectangle2D getBounds2D() {
        return null;
    }

    @Override
    public boolean contains(double x, double y) {
        return false;
    }

    @Override
    public boolean contains(Point2D p) {
        return false;
    }

    @Override
    public boolean intersects(double x, double y, double w, double h) {
        return false;
    }

    @Override
    public boolean intersects(Rectangle2D r) {
        return false;
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        return false;
    }

    @Override
    public boolean contains(Rectangle2D r) {
        return false;
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        return null;
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return null;
    }
}
