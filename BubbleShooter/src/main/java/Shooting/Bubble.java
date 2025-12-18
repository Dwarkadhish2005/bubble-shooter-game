package Shooting;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Bubble {
    public int x, y;
    public int vx, vy; // velocity
    public Color color;
    public static final int SIZE = 35; // Increased size for better visibility
    
    public Bubble(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.vx = 0;
        this.vy = 0;
    }
    
    public void draw(Graphics2D g2d) {
        // Enhanced bubble drawing with multiple layers
        
        // Shadow effect
        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.fillOval(x + 3, y + 3, SIZE, SIZE);
        
        // Outer glow
        RadialGradientPaint outerGlow = new RadialGradientPaint(
            x + SIZE / 2, y + SIZE / 2, SIZE / 2 + 5,
            new float[]{0f, 1f},
            new Color[]{new Color(color.getRed(), color.getGreen(), color.getBlue(), 80), 
                       new Color(color.getRed(), color.getGreen(), color.getBlue(), 0)}
        );
        g2d.setPaint(outerGlow);
        g2d.fillOval(x - 5, y - 5, SIZE + 10, SIZE + 10);
        
        // Main bubble with radial gradient
        RadialGradientPaint mainGradient = new RadialGradientPaint(
            x + SIZE / 3, y + SIZE / 3, SIZE / 2,
            new float[]{0f, 0.6f, 1f},
            new Color[]{color.brighter().brighter(), color, color.darker().darker()}
        );
        g2d.setPaint(mainGradient);
        
        Ellipse2D.Double bubble = new Ellipse2D.Double(x, y, SIZE, SIZE);
        g2d.fill(bubble);
        
        // Glossy highlight (larger and more prominent)
        RadialGradientPaint highlight = new RadialGradientPaint(
            x + SIZE / 4, y + SIZE / 4, SIZE / 3,
            new float[]{0f, 0.8f, 1f},
            new Color[]{new Color(255, 255, 255, 220), 
                       new Color(255, 255, 255, 100),
                       new Color(255, 255, 255, 0)}
        );
        g2d.setPaint(highlight);
        g2d.fillOval(x + 6, y + 6, SIZE / 2, SIZE / 2);
        
        // Secondary smaller highlight
        g2d.setColor(new Color(255, 255, 255, 180));
        g2d.fillOval(x + SIZE / 6, y + SIZE / 6, SIZE / 6, SIZE / 6);
        
        // Border with subtle animation effect
        g2d.setColor(new Color(255, 255, 255, 150));
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.draw(bubble);
        
        // Inner border for depth
        g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 100));
        g2d.setStroke(new BasicStroke(1));
        g2d.drawOval(x + 2, y + 2, SIZE - 4, SIZE - 4);
    }
    
    public boolean collidesWith(Bubble other) {
        double dx = (x + SIZE / 2) - (other.x + SIZE / 2);
        double dy = (y + SIZE / 2) - (other.y + SIZE / 2);
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < SIZE - 3; // Slightly adjusted for new size
    }
    
    public boolean isAdjacent(Bubble other) {
        double dx = (x + SIZE / 2) - (other.x + SIZE / 2);
        double dy = (y + SIZE / 2) - (other.y + SIZE / 2);
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance <= SIZE + 8; // Adjusted for hexagonal grid with new size
    }
    
    public Point getGridPosition() {
        int gridY = (y - SIZE) / SIZE;
        int gridX;
        
        if (gridY % 2 == 0) {
            gridX = (x - SIZE) / SIZE;
        } else {
            gridX = (x - SIZE - SIZE / 2) / SIZE;
        }
        
        return new Point(gridX, gridY);
    }
}
