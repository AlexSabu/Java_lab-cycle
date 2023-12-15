import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

class DrawingPanel extends JPanel {
    private ArrayList<Shape> shapes = new ArrayList<>();
    private Shape currentShape;

    public DrawingPanel() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Create a new shape based on user choice (line, rectangle, oval)
                if (currentShape == null) {
                    // No shape selected
                    return;
                }

                currentShape.setStartPoint(e.getPoint());
                shapes.add(currentShape);
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (currentShape != null) {
                    currentShape.setEndPoint(e.getPoint());
                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (currentShape != null) {
                    currentShape.setEndPoint(e.getPoint());
                    repaint();
                }
            }
        });
    }

    public void setCurrentShape(Shape shape) {
        this.currentShape = shape;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Shape shape : shapes) {
            shape.draw(g);
        }

        if (currentShape != null) {
            currentShape.draw(g);
        }
    }
}

abstract class Shape {
    protected Point startPoint;
    protected Point endPoint;

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }

    public abstract void draw(Graphics g);
}

class Line extends Shape {
    @Override
    public void draw(Graphics g) {
        if (startPoint != null && endPoint != null) {
            g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
        }
    }
}

class RectangleShape extends Shape {
    @Override
    public void draw(Graphics g) {
        if (startPoint != null && endPoint != null) {
            int width = Math.abs(endPoint.x - startPoint.x);
            int height = Math.abs(endPoint.y - startPoint.y);
            int x = Math.min(startPoint.x, endPoint.x);
            int y = Math.min(startPoint.y, endPoint.y);
            g.drawRect(x, y, width, height);
        }
    }
}

class Oval extends Shape {
    @Override
    public void draw(Graphics g) {
        if (startPoint != null && endPoint != null) {
            int width = Math.abs(endPoint.x - startPoint.x);
            int height = Math.abs(endPoint.y - startPoint.y);
            int x = Math.min(startPoint.x, endPoint.x);
            int y = Math.min(startPoint.y, endPoint.y);
            g.drawOval(x, y, width, height);
        }
    }
}

public class DrawingApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Drawing App");
            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            DrawingPanel drawingPanel = new DrawingPanel();
            frame.add(drawingPanel, BorderLayout.CENTER);

            JToggleButton lineButton = new JToggleButton("Line");
            JToggleButton rectangleButton = new JToggleButton("Rectangle");
            JToggleButton ovalButton = new JToggleButton("Oval");

            ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add(lineButton);
            buttonGroup.add(rectangleButton);
            buttonGroup.add(ovalButton);

            lineButton.addActionListener(e -> drawingPanel.setCurrentShape(new Line()));
            rectangleButton.addActionListener(e -> drawingPanel.setCurrentShape(new RectangleShape()));
            ovalButton.addActionListener(e -> drawingPanel.setCurrentShape(new Oval()));

            JPanel toolPanel = new JPanel();
            toolPanel.add(lineButton);
            toolPanel.add(rectangleButton);
            toolPanel.add(ovalButton);

            frame.add(toolPanel, BorderLayout.NORTH);

            frame.setVisible(true);
        });
    }
}
