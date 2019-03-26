import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class QDragAndDrop extends JComponent
        implements DragGestureListener,
        DragSourceListener,
        DropTargetListener,
        MouseListener,
        KeyListener {
    static final QStroke spikyStroke = new QStroke(2, 10, 10);
    static final Border normalBorder = new BevelBorder(BevelBorder.LOWERED);
    static final Border dropBorder = new BevelBorder(BevelBorder.RAISED);
    final ArrayList<Quadratrix> graphs = new ArrayList<>();
    DragSource dragSource = null;
    Quadratrix beingDragged = null;
    Quadratrix selectedGraph = null;

    Color selectedGraphColor = Color.BLUE;
    Color graphColor = Color.RED;

    public QDragAndDrop() {
        super();
        graphs.add(new Quadratrix());
        dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this);

        DropTarget dropTarget = new DropTarget(this, this);
        this.setDropTarget(dropTarget);
    }

    @Override
    public void paintComponent(Graphics g) {    	
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Stroke oldStroke = g2.getStroke();
        g2.setStroke(spikyStroke);
        g2.setColor(graphColor);        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (Quadratrix graph: graphs) {            
            if (graph == selectedGraph) {
                Color oldColor = g2.getColor();
                g2.setColor(selectedGraphColor);                
                g2.draw(graph);
                g2.setColor(oldColor);
            }
            else {
                g2.draw(graph);
            }
        }
        g2.setStroke(oldStroke);
    }
    
    public void clear() {
        graphs.clear();
        repaint();
    }

   
    @Override
    public void dragGestureRecognized(DragGestureEvent e) {
        MouseEvent inputEvent = (MouseEvent) e.getTriggerEvent();
        int x = inputEvent.getX();
        int y = inputEvent.getY();
        
        for (Quadratrix graph : graphs) {
            if (graph.getBounds().contains(x, y)) {
                beingDragged = graph;

                Quadratrix dragGraph = (Quadratrix) graph.clone();
                dragGraph.x0 = x;
                dragGraph.y0 = y;                
                Cursor cursor;
                switch (e.getDragAction()) {
                    case DnDConstants.ACTION_COPY:
                        cursor = DragSource.DefaultCopyDrop;
                        break;
                    case DnDConstants.ACTION_MOVE:
                        cursor = DragSource.DefaultMoveDrop;
                        break;
                    default:
                        return;
                }
                e.startDrag(cursor, dragGraph, this);
                return;
            }
        }
    }

    // DrawSourceListener

    
    public void dragDropEnd(DragSourceDropEvent e) {
        if (!e.getDropSuccess())
            return;
        int action = e.getDropAction();       
        if (action == DnDConstants.ACTION_MOVE) {
            graphs.remove(beingDragged);
            beingDragged = null;
            repaint();
        }
    }

    @Override
    public void dragEnter(DragSourceDragEvent e) {
        // Empty
    }

    @Override
    public void dragExit(DragSourceEvent e) {
        // Empty
    }

    @Override
    public void dropActionChanged(DragSourceDragEvent e) {
        // Empty
    }

    @Override
    public void dragOver(DragSourceDragEvent e) {
        // Empty
    }

    // DropTargetListener

   
    @Override
    public void dragEnter(DropTargetDragEvent e) {
        if (e.isDataFlavorSupported(Quadratrix.qDataFlavor)) {
            e.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
            this.setBorder(dropBorder);
        }
    }

    
    @Override
    public void dragExit(DropTargetEvent e) {
        this.setBorder(normalBorder);
    }

    @Override
    public void drop(DropTargetDropEvent e) {
        this.setBorder(normalBorder);

      
        if (e.isDataFlavorSupported(Quadratrix.qDataFlavor)) {
            e.acceptDrop(e.getDropAction());
        } else {
            e.rejectDrop();
            return;
        }

        Transferable t = e.getTransferable();
        Quadratrix droppedGraph;

       
        try {
            droppedGraph = (Quadratrix) t.getTransferData(Quadratrix.qDataFlavor);
        } catch (Exception ex) {
            e.dropComplete(false);
            return;
        }

        
        Point p = e.getLocation();
        droppedGraph.x0 = p.x;
        droppedGraph.y0 = p.y;
        graphs.add(droppedGraph);
        repaint();
        e.dropComplete(true);
    }

    @Override
    public void dragOver(DropTargetDragEvent e) {
        // Empty
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent e) {
        // Empty
    }

    // MouseListner

    @Override
    public void mouseClicked(MouseEvent e) {
        // Empty
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.isAltDown()) {
            graphs.add(new Quadratrix(200, e.getX(), e.getY(), 1, 1));
        }
        else {
            selectedGraph = null;
            for (Quadratrix graph: graphs) {
                if (graph.getBounds().contains(e.getX(), e.getY())) {
                    selectedGraph = graph;
                    break;
                }
            }
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Empty
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Empty
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Empty
    }

    // KeyListner

    // Передвижение выделенного графика
    // при помощи стрелок.
    @Override
    public void keyPressed(KeyEvent e) {
        if (selectedGraph != null) {
            int key = e.getKeyCode();
            switch (key) {
                case KeyEvent.VK_LEFT:
                    selectedGraph.move(-5, 0);
                    break;
                case KeyEvent.VK_RIGHT:
                    selectedGraph.move(5, 0);
                    break;
                case KeyEvent.VK_UP:
                    selectedGraph.move(0, -5);
                    break;
                case KeyEvent.VK_DOWN:
                    selectedGraph.move(0, 5);
                    break;
                default:
                    return;
            }
            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Empty
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Empty
    }
    public static void main(String[] args) {
		// Create a frame and put a scribble pane in it
		JFrame frame = new JFrame("Quadratrix DragAndDrop");		
		final QDragAndDrop qPanel = new QDragAndDrop();		
		frame.getContentPane().add(qPanel, BorderLayout.CENTER);
		// Pop up the window
		frame.setSize(1000, 1000);
		frame.setVisible(true);
	}
}
