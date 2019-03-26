import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.Serializable;

public class Quadratrix implements Shape, Serializable, Cloneable, Transferable {
	private static final long serialVersionUID = 1L;
	private float a = 200;
	public float x0 = 300, y0 = 200;
	private float minX, maxX;
	private float scale = 1;
	private float step = 1;

	public static DataFlavor qDataFlavor = new DataFlavor(Quadratrix.class, "Quadratrix");
	public static DataFlavor[] supportedFlavors = { qDataFlavor };

	private float graphFunc(float x) {
		return (x != 0) ? (float) (x / Math.tan(Math.PI * x / (2.0 * a)))
				: (float) (1 / (Math.PI / (2.0 * a)));
	}

	public Quadratrix() {		
		this.minX = -a;
		this.maxX = a;
	}

	public Quadratrix(float a, float x0, float y0, float scale, float step) {
		this.a = a;
		this.x0 = x0;
		this.y0 = y0;
		this.minX = -a;
		this.maxX = a;
		this.scale = scale;
		this.step = step;
	}

	public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
	public void move(float dx, float dy) {
        x0 += dx;
        y0 += dy;
    }
	@Override
	public Rectangle getBounds() {
		float maxY = graphFunc(0);
		float minY = 0;
		return new Rectangle((int) (x0 + minX * scale), (int) (y0 - maxY * scale),
                (int) ((maxX - minX) * scale), (int) ((maxY - minY) * scale));
	}

	@Override
	public Rectangle2D getBounds2D() {
		float maxY = graphFunc(0);
		float minY = 0;
		return new Rectangle2D.Float(x0 + minX * scale, y0 - maxY * scale,
                (maxX - minX) * scale, (maxY - minY) * scale);
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
		return intersects(new Rectangle2D.Double(x, y, w, h));
	}

	@Override
	public boolean intersects(Rectangle2D r) {
		for (float valX = minX; valX <= maxX; valX += step) {
            if (r.contains(x0 + valX, y0 - graphFunc(valX))) {
                return true;
            }
        }
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
		return new GraphIterator(at);
	}

	@Override
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		return new GraphIterator(at);
	}

	private class GraphIterator implements PathIterator {
		AffineTransform transform;
		private float x = -a;

		public GraphIterator(AffineTransform transform) {
			this.transform = transform;
		}

		@Override
		public int getWindingRule() {
			return WIND_NON_ZERO;
		}

		@Override
		public boolean isDone() {
			return x > maxX;
		}

		@Override
		public void next() {
			x += step;
		}

		@Override
		public int currentSegment(float[] coords) {
			coords[0] = (float) (x0 + scale * x);
			coords[1] = (float) (y0 - scale * graphFunc(x));
			if (transform != null)
				transform.transform(coords, 0, coords, 0, 1);
			if (x == minX)
				return SEG_MOVETO;
			else
				return SEG_LINETO;
		}

		@Override
		public int currentSegment(double[] coords) {
			coords[0] = x0 + scale * x;
			coords[1] = y0 - scale * graphFunc(x);
			if (transform != null)
				transform.transform(coords, 0, coords, 0, 1);
			if (x == minX)
				return SEG_MOVETO;
			else
				return SEG_LINETO;
		}
	}

	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (flavor.equals(qDataFlavor)) {
			return this;
		} else
			throw new UnsupportedFlavorException(flavor);
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return (DataFlavor[]) supportedFlavors.clone();
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return (flavor.equals(qDataFlavor));
	}
}
