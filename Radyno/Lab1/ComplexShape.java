package package1;
import java.awt.*;
import java.awt.geom.*;

public class ComplexShape implements Shape {

	private Area shapeArea = new Area();
	private final int width, height;
	
	public ComplexShape(final int width, final int height) throws IllegalArgumentException {
		if(width <= 0 || height <= 0) {
			throw new IllegalArgumentException("0 or negative value is passed as an argument");
		}
		
		this.width = width;
		this.height = height;
		setAngle(0);
	}
	
	public void setAngle(final double angle) {
		final Shape rectangle = AffineTransform.getRotateInstance(angle, width / 2, height / 2).createTransformedShape(new Rectangle(width, height));
		final Shape ellipse = AffineTransform.getRotateInstance(-angle, width / 2, height / 2).createTransformedShape(new Ellipse2D.Double(0, 0, width, height));

		if(!shapeArea.isEmpty()) {
			shapeArea.reset();
		}
		shapeArea.add(new Area(rectangle));
		shapeArea.add(new Area(ellipse));
	}
	
	@Override
	public boolean contains(final Point2D arg0) {
		return shapeArea.contains(arg0);
	}

	@Override
	public boolean contains(final Rectangle2D arg0) {
		return shapeArea.contains(arg0);
	}

	@Override
	public boolean contains(final double arg0, final double arg1) {
		return shapeArea.contains(arg0, arg1);
	}

	@Override
	public boolean contains(final double arg0, final double arg1, final double arg2, final double arg3) {
		return shapeArea.contains(arg0, arg1, arg2, arg3);
	}

	@Override
	public Rectangle getBounds() {
		return shapeArea.getBounds();
	}

	@Override
	public Rectangle2D getBounds2D() {
		return shapeArea.getBounds2D();
	}

	@Override
	public PathIterator getPathIterator(final AffineTransform arg0) {
		return shapeArea.getPathIterator(arg0);
	}

	@Override
	public PathIterator getPathIterator(final AffineTransform arg0, final double arg1) {
		return shapeArea.getPathIterator(arg0, arg1);
	}

	@Override
	public boolean intersects(final Rectangle2D arg0) {
		return shapeArea.intersects(arg0);
	}

	@Override
	public boolean intersects(final double arg0, final double arg1, final double arg2, final double arg3) {
		return shapeArea.intersects(arg0, arg1, arg2, arg3);
	}

}
