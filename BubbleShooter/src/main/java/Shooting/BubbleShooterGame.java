package Shooting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BubbleShooterGame extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
    private static final int WINDOW_WIDTH = 900;
    private static final int WINDOW_HEIGHT = 700;
    private static final int BUBBLE_SIZE = 35;
    private static final int ROWS = 8;
    private static final int COLS = 15;
    private static final int SHOOTER_Y = WINDOW_HEIGHT - 120;
    private static final int UI_HEIGHT = 80;
    
    private Timer gameTimer;
    private List<Bubble> bubbles;
    private List<Bubble> shootingBubbles;
    private Bubble nextBubble;
    private Bubble previewBubble;
    private Point mousePosition;
    private int score;
    private int level;
    private int bubblesRemaining;
    private boolean gameOver;
    private boolean gameWon;
    private Random random;
    private int animationFrame;
    private List<FloatingScore> floatingScores;
    private List<BackgroundParticle> backgroundParticles;
    
    // Enhanced color palette
    private Color[] bubbleColors = {
        new Color(255, 87, 90),   // Red
        new Color(87, 165, 255),  // Blue  
        new Color(87, 255, 87),   // Green
        new Color(255, 215, 87),  // Yellow
        new Color(255, 140, 87),  // Orange
        new Color(255, 87, 245),  // Pink
        new Color(87, 255, 255),  // Cyan
        new Color(200, 87, 255)   // Purple
    };
    
    // UI Colors
    private Color primaryColor = new Color(45, 52, 70);
    private Color secondaryColor = new Color(65, 75, 95);
    private Color accentColor = new Color(100, 200, 255);
    private Color successColor = new Color(87, 255, 87);
    private Color warningColor = new Color(255, 215, 87);
    private Color dangerColor = new Color(255, 87, 87);
    
    public BubbleShooterGame() {
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setBackground(new Color(20, 25, 35));
        setFocusable(true);
        
        addMouseListener(this);
        addMouseMotionListener(this);
        
        random = new Random();
        initializeGame();
        
        gameTimer = new Timer(16, this); // ~60 FPS
        gameTimer.start();
    }
    
    private void initializeGame() {
        bubbles = new ArrayList<>();
        shootingBubbles = new ArrayList<>();
        floatingScores = new ArrayList<>();
        backgroundParticles = new ArrayList<>();
        mousePosition = new Point(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
        score = 0;
        level = 1;
        gameOver = false;
        gameWon = false;
        animationFrame = 0;
        
        // Create background particles
        for (int i = 0; i < 50; i++) {
            backgroundParticles.add(new BackgroundParticle());
        }
        
        // Create initial bubble grid with better distribution
        bubblesRemaining = 0;
        for (int row = 0; row < ROWS / 2; row++) {
            for (int col = 0; col < COLS; col++) {
                if (random.nextDouble() < 0.75) { // 75% chance of bubble
                    int x = col * BUBBLE_SIZE + (row % 2) * (BUBBLE_SIZE / 2) + BUBBLE_SIZE;
                    int y = row * BUBBLE_SIZE + BUBBLE_SIZE + UI_HEIGHT;
                    Color color = bubbleColors[random.nextInt(Math.min(4 + level, bubbleColors.length))];
                    bubbles.add(new Bubble(x, y, color));
                    bubblesRemaining++;
                }
            }
        }
        
        // Create next and preview bubbles
        nextBubble = new Bubble(WINDOW_WIDTH / 2 - BUBBLE_SIZE / 2, SHOOTER_Y, 
                               bubbleColors[random.nextInt(Math.min(4 + level, bubbleColors.length))]);
        previewBubble = new Bubble(WINDOW_WIDTH / 2 + 80, SHOOTER_Y + 10, 
                                  bubbleColors[random.nextInt(Math.min(4 + level, bubbleColors.length))]);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        animationFrame++;
        
        // Draw animated background
        drawBackground(g2d);
        
        // Draw game area border
        drawGameArea(g2d);
        
        // Draw background particles
        for (BackgroundParticle particle : backgroundParticles) {
            particle.draw(g2d);
        }
        
        // Draw grid bubbles with enhanced effects
        for (Bubble bubble : bubbles) {
            drawEnhancedBubble(g2d, bubble);
        }
        
        // Draw shooting bubbles
        for (Bubble bubble : shootingBubbles) {
            drawEnhancedBubble(g2d, bubble);
        }
        
        // Draw floating scores
        for (FloatingScore floatingScore : floatingScores) {
            floatingScore.draw(g2d);
        }
        
        // Draw enhanced UI
        drawEnhancedUI(g2d);
        
        // Draw shooter area
        drawShooterArea(g2d);
        
        // Draw next bubble with glow effect
        if (nextBubble != null) {
            drawShooterBubble(g2d, nextBubble);
        }
        
        // Draw preview bubble
        if (previewBubble != null) {
            drawPreviewBubble(g2d, previewBubble);
        }
        
        // Draw enhanced aiming line
        drawAimingLine(g2d);
        
        // Draw game over/won screen
        if (gameOver || gameWon) {
            drawGameEndScreen(g2d);
        }
    }
    
    private void drawBackground(Graphics2D g2d) {
        // Animated gradient background
        float wave = (float) Math.sin(animationFrame * 0.01) * 0.1f + 0.9f;
        Color bg1 = new Color((int)(20 * wave), (int)(25 * wave), (int)(35 * wave));
        Color bg2 = new Color((int)(45 * wave), (int)(52 * wave), (int)(70 * wave));
        
        GradientPaint gradient = new GradientPaint(0, 0, bg1, 0, WINDOW_HEIGHT, bg2);
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        
        // Add subtle pattern
        g2d.setColor(new Color(255, 255, 255, 5));
        for (int i = 0; i < WINDOW_WIDTH; i += 40) {
            for (int j = 0; j < WINDOW_HEIGHT; j += 40) {
                g2d.fillOval(i, j, 2, 2);
            }
        }
    }
    
    private void drawGameArea(Graphics2D g2d) {
        // Game area with rounded border
        RoundRectangle2D gameArea = new RoundRectangle2D.Double(
            10, UI_HEIGHT + 10, WINDOW_WIDTH - 20, SHOOTER_Y - UI_HEIGHT - 20, 20, 20);
        
        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.fill(gameArea);
        
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(accentColor);
        g2d.draw(gameArea);
        
        // Add inner glow
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 100));
        RoundRectangle2D innerGlow = new RoundRectangle2D.Double(
            12, UI_HEIGHT + 12, WINDOW_WIDTH - 24, SHOOTER_Y - UI_HEIGHT - 24, 18, 18);
        g2d.draw(innerGlow);
    }
    
    private void drawEnhancedBubble(Graphics2D g2d, Bubble bubble) {
        // Enhanced bubble with multiple layers
        int x = bubble.x;
        int y = bubble.y;
        Color color = bubble.color;
        
        // Outer glow
        RadialGradientPaint outerGlow = new RadialGradientPaint(
            x + BUBBLE_SIZE / 2, y + BUBBLE_SIZE / 2, BUBBLE_SIZE / 2 + 8,
            new float[]{0f, 1f},
            new Color[]{new Color(color.getRed(), color.getGreen(), color.getBlue(), 100), 
                       new Color(color.getRed(), color.getGreen(), color.getBlue(), 0)}
        );
        g2d.setPaint(outerGlow);
        g2d.fillOval(x - 8, y - 8, BUBBLE_SIZE + 16, BUBBLE_SIZE + 16);
        
        // Main bubble with radial gradient
        RadialGradientPaint mainGradient = new RadialGradientPaint(
            x + BUBBLE_SIZE / 3, y + BUBBLE_SIZE / 3, BUBBLE_SIZE / 2,
            new float[]{0f, 0.7f, 1f},
            new Color[]{color.brighter().brighter(), color, color.darker()}
        );
        g2d.setPaint(mainGradient);
        g2d.fillOval(x, y, BUBBLE_SIZE, BUBBLE_SIZE);
        
        // Glossy highlight
        RadialGradientPaint highlight = new RadialGradientPaint(
            x + BUBBLE_SIZE / 4, y + BUBBLE_SIZE / 4, BUBBLE_SIZE / 4,
            new float[]{0f, 1f},
            new Color[]{new Color(255, 255, 255, 200), new Color(255, 255, 255, 0)}
        );
        g2d.setPaint(highlight);
        g2d.fillOval(x + 5, y + 5, BUBBLE_SIZE / 3, BUBBLE_SIZE / 3);
        
        // Border with animation
        float borderAlpha = 0.7f + 0.3f * (float) Math.sin(animationFrame * 0.05);
        g2d.setColor(new Color(254, 255, 255, (int)(255 * borderAlpha)));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(x, y, BUBBLE_SIZE, BUBBLE_SIZE);
    }
    
    private void drawShooterBubble(Graphics2D g2d, Bubble bubble) {
        // Shooter bubble with pulsing effect
        float pulse = 1.0f + 0.1f * (float) Math.sin(animationFrame * 0.1);
        int size = (int) (BUBBLE_SIZE * pulse);
        int offset = (BUBBLE_SIZE - size) / 2;
        
        // Pulsing glow
        RadialGradientPaint glow = new RadialGradientPaint(
            bubble.x + BUBBLE_SIZE / 2, bubble.y + BUBBLE_SIZE / 2, size / 2 + 15,
            new float[]{0f, 1f},
            new Color[]{new Color(bubble.color.getRed(), bubble.color.getGreen(), bubble.color.getBlue(), 150), 
                       new Color(bubble.color.getRed(), bubble.color.getGreen(), bubble.color.getBlue(), 0)}
        );
        g2d.setPaint(glow);
        g2d.fillOval(bubble.x + offset - 15, bubble.y + offset - 15, size + 30, size + 30);
        
        // Main bubble
        drawEnhancedBubble(g2d, new Bubble(bubble.x + offset, bubble.y + offset, bubble.color));
    }
    
    private void drawPreviewBubble(Graphics2D g2d, Bubble bubble) {
        // Smaller preview bubble
        int previewSize = BUBBLE_SIZE * 2 / 3;
        int x = bubble.x;
        int y = bubble.y;
        
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillOval(x - 2, y + 2, previewSize, previewSize);
        
        RadialGradientPaint gradient = new RadialGradientPaint(
            x + previewSize / 3, y + previewSize / 3, previewSize / 2,
            new float[]{0f, 1f},
            new Color[]{bubble.color.brighter(), bubble.color.darker()}
        );
        g2d.setPaint(gradient);
        g2d.fillOval(x, y, previewSize, previewSize);
        
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawOval(x, y, previewSize, previewSize);
        
        // "Next" label
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics fm = g2d.getFontMetrics();
        String nextText = "NEXT";
        g2d.setColor(accentColor);
        g2d.drawString(nextText, x + (previewSize - fm.stringWidth(nextText)) / 2, y - 5);
    }
    
    private void drawAimingLine(Graphics2D g2d) {
        if (nextBubble != null && mousePosition != null && !gameOver && !gameWon) {
            int startX = nextBubble.x + BUBBLE_SIZE / 2;
            int startY = nextBubble.y + BUBBLE_SIZE / 2;
            
            // Calculate trajectory with wall bounces
            List<Point> trajectory = calculateTrajectory(startX, startY, mousePosition.x, mousePosition.y);
            
            if (trajectory.size() > 1) {
                // Draw trajectory line with gradient
                for (int i = 0; i < trajectory.size() - 1; i++) {
                    Point p1 = trajectory.get(i);
                    Point p2 = trajectory.get(i + 1);
                    
                    float alpha = 1.0f - (float) i / trajectory.size();
                    g2d.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 
                                         (int)(255 * alpha * 0.8)));
                    g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
                
                // Draw target indicator
                Point target = trajectory.get(trajectory.size() - 1);
                float pulse = 0.8f + 0.2f * (float) Math.sin(animationFrame * 0.2);
                int targetSize = (int) (20 * pulse);
                
                g2d.setColor(new Color(255, 255, 255, 200));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawOval(target.x - targetSize / 2, target.y - targetSize / 2, targetSize, targetSize);
                g2d.drawLine(target.x - targetSize / 2, target.y, target.x + targetSize / 2, target.y);
                g2d.drawLine(target.x, target.y - targetSize / 2, target.x, target.y + targetSize / 2);
            }
        }
    }
    
    private List<Point> calculateTrajectory(int startX, int startY, int targetX, int targetY) {
        List<Point> points = new ArrayList<>();
        



        double dx = targetX - startX;
        double dy = targetY - startY;
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        if (distance == 0) return points;
        
        double stepX = (dx / distance) * 5;
        double stepY = (dy / distance) * 5;
        
        double currentX = startX;
        double currentY = startY;
        
        for (int i = 0; i < 100; i++) {
            points.add(new Point((int) currentX, (int) currentY));
            
            currentX += stepX;
            currentY += stepY;
            
            // Wall bounce
            if (currentX <= 20 || currentX >= WINDOW_WIDTH - 20) {
                stepX = -stepX;
                currentX = Math.max(20, Math.min(WINDOW_WIDTH - 20, currentX));
            }
            
            // Stop at top or when hitting bubbles
            if (currentY <= UI_HEIGHT + 20) break;
            
            // Check collision with existing bubbles
            for (Bubble bubble : bubbles) {
                double bubbleDx = currentX - (bubble.x + BUBBLE_SIZE / 2);
                double bubbleDy = currentY - (bubble.y + BUBBLE_SIZE / 2);
                if (Math.sqrt(bubbleDx * bubbleDx + bubbleDy * bubbleDy) < BUBBLE_SIZE) {
                    return points;
                }
            }
        }
        
        return points;
    }
    
    private void drawShooterArea(Graphics2D g2d) {
        // Shooter platform
        RoundRectangle2D platform = new RoundRectangle2D.Double(
            50, SHOOTER_Y + 50, WINDOW_WIDTH - 100, 40, 20, 20);
        
        GradientPaint platformGradient = new GradientPaint(
            0, SHOOTER_Y + 50, secondaryColor.brighter(),
            0, SHOOTER_Y + 90, secondaryColor.darker());
        g2d.setPaint(platformGradient);
        g2d.fill(platform);
        
        g2d.setColor(accentColor);
        g2d.setStroke(new BasicStroke(2));
        g2d.draw(platform);
    }
    
    private void drawEnhancedUI(Graphics2D g2d) {
        RoundRectangle2D uiPanel = new RoundRectangle2D.Double(10, 10, WINDOW_WIDTH - 20, UI_HEIGHT - 20, 15, 15);
        
        GradientPaint panelGradient = new GradientPaint(0, 10, primaryColor, 0, UI_HEIGHT, secondaryColor);
        g2d.setPaint(panelGradient);
        g2d.fill(uiPanel);
        
        g2d.setColor(accentColor);
        g2d.setStroke(new BasicStroke(2));
        g2d.draw(uiPanel);
        
        // Score section
        drawUISection(g2d, 30, 25, "SCORE", String.valueOf(score), successColor);
        
        // Level section  
        drawUISection(g2d, 200, 25, "LEVEL", String.valueOf(level), accentColor);
        
        drawUISection(g2d, 370, 25, "BUBBLES", String.valueOf(bubblesRemaining), warningColor);
        
        drawProgressBar(g2d, 550, 35, 300, 20);
    }
    
    private void drawUISection(Graphics2D g2d, int x, int y, String label, String value, Color color) {
        // Label
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.setColor(Color.WHITE);
        g2d.drawString(label, x, y);
        
        // Value with glow effect
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics fm = g2d.getFontMetrics();
        
        // Glow
        g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 100));
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                g2d.drawString(value, x + i, y + 20 + j);
            }
        }
        
        // Main text
        g2d.setColor(color);
        g2d.drawString(value, x, y + 20);
    }
    
    private void drawProgressBar(Graphics2D g2d, int x, int y, int width, int height) {
        // Background
        RoundRectangle2D bg = new RoundRectangle2D.Double(x, y, width, height, 10, 10);
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fill(bg);
        
        // Progress
        int totalBubbles = bubbles.size() + bubblesRemaining;
        float progress = totalBubbles > 0 ? (float) bubblesRemaining / totalBubbles : 0;
        int progressWidth = (int) (width * progress);
        
        if (progressWidth > 0) {
            RoundRectangle2D progressBar = new RoundRectangle2D.Double(x, y, progressWidth, height, 10, 10);
            
            Color progressColor = progress > 0.6f ? successColor : 
                                 progress > 0.3f ? warningColor : dangerColor;
            
            GradientPaint progressGradient = new GradientPaint(
                x, y, progressColor.brighter(),
                x, y + height, progressColor.darker());
            g2d.setPaint(progressGradient);
            g2d.fill(progressBar);
        }
        
        // Border
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(1));
        g2d.draw(bg);
        
        // Label
        g2d.setFont(new Font("Arial", Font.BOLD, 10));
        g2d.setColor(Color.WHITE);
        String progressText = "PROGRESS";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(progressText, x + (width - fm.stringWidth(progressText)) / 2, y - 5);
    }
    
    private void drawGameEndScreen(Graphics2D g2d) {
        // Overlay
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        
        // Main panel
        RoundRectangle2D panel = new RoundRectangle2D.Double(
            WINDOW_WIDTH / 4, WINDOW_HEIGHT / 4, WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2, 30, 30);
        
        GradientPaint panelGradient = new GradientPaint(
            0, WINDOW_HEIGHT / 4, primaryColor,
            0, 3 * WINDOW_HEIGHT / 4, secondaryColor);
        g2d.setPaint(panelGradient);
        g2d.fill(panel);
        
        g2d.setColor(gameWon ? successColor : dangerColor);
        g2d.setStroke(new BasicStroke(4));
        g2d.draw(panel);
        
        // Title
        String title = gameWon ? "LEVEL COMPLETE!" : "GAME OVER";
        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        FontMetrics fm = g2d.getFontMetrics();
        
        // Title glow
        Color titleColor = gameWon ? successColor : dangerColor;
        g2d.setColor(new Color(titleColor.getRed(), titleColor.getGreen(), titleColor.getBlue(), 100));
        for (int i = -3; i <= 3; i++) {
            for (int j = -3; j <= 3; j++) {
                g2d.drawString(title, WINDOW_WIDTH / 2 - fm.stringWidth(title) / 2 + i, 
                              WINDOW_HEIGHT / 2 - 50 + j);
            }
        }
        
        g2d.setColor(titleColor);
        g2d.drawString(title, WINDOW_WIDTH / 2 - fm.stringWidth(title) / 2, WINDOW_HEIGHT / 2 - 50);
        
        // Score
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        fm = g2d.getFontMetrics();
        String scoreText = "Final Score: " + score;
        g2d.setColor(Color.WHITE);
        g2d.drawString(scoreText, WINDOW_WIDTH / 2 - fm.stringWidth(scoreText) / 2, WINDOW_HEIGHT / 2);
        
        // Instructions
        g2d.setFont(new Font("Arial", Font.PLAIN, 18));
        fm = g2d.getFontMetrics();
        String instruction = "Click anywhere to " + (gameWon ? "continue" : "restart");
        g2d.setColor(accentColor);
        g2d.drawString(instruction, WINDOW_WIDTH / 2 - fm.stringWidth(instruction) / 2, WINDOW_HEIGHT / 2 + 50);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver && !gameWon) {
            updateGame();
        }
        updateAnimations();
        repaint();
    }
    
    private void updateGame() {
        // Update background particles
        for (BackgroundParticle particle : backgroundParticles) {
            particle.update();
        }
        
        // Update floating scores
        List<FloatingScore> scoresToRemove = new ArrayList<>();
        for (FloatingScore floatingScore : floatingScores) {
            floatingScore.update();
            if (floatingScore.isFinished()) {
                scoresToRemove.add(floatingScore);
            }
        }
        floatingScores.removeAll(scoresToRemove);
        
        // Update shooting bubbles
        List<Bubble> toRemove = new ArrayList<>();
        
        for (Bubble shootingBubble : shootingBubbles) {
            shootingBubble.x += shootingBubble.vx;
            shootingBubble.y += shootingBubble.vy;
            
            // Wall collision
            if (shootingBubble.x <= 20 || shootingBubble.x >= WINDOW_WIDTH - BUBBLE_SIZE - 20) {
                shootingBubble.vx = -shootingBubble.vx;
                shootingBubble.x = Math.max(20, Math.min(WINDOW_WIDTH - BUBBLE_SIZE - 20, shootingBubble.x));
            }
            
            // Top collision
            if (shootingBubble.y <= UI_HEIGHT + 20) {
                attachBubble(shootingBubble);
                toRemove.add(shootingBubble);
                continue;
            }
            
            // Collision with existing bubbles
            boolean collided = false;
            for (Bubble bubble : bubbles) {
                if (shootingBubble.collidesWith(bubble)) {
                    attachBubble(shootingBubble);
                    toRemove.add(shootingBubble);
                    collided = true;
                    break;
                }
            }
            
            // Remove if goes off screen
            if (shootingBubble.y > WINDOW_HEIGHT) {
                toRemove.add(shootingBubble);
            }
        }
        
        shootingBubbles.removeAll(toRemove);
        
        // Check win condition
        if (bubbles.isEmpty()) {
            gameWon = true;
        }
        
        // Check lose condition
        for (Bubble bubble : bubbles) {
            if (bubble.y > SHOOTER_Y - BUBBLE_SIZE - 50) {
                gameOver = true;
                break;
            }
        }
    }
    
    private void updateAnimations() {
        // Update background particles
        for (BackgroundParticle particle : backgroundParticles) {
            particle.update();
        }
    }
    
    private void attachBubble(Bubble shootingBubble) {
        // Find the best position to attach the bubble
        int gridX = (int) Math.round((shootingBubble.x - BUBBLE_SIZE) / (double) BUBBLE_SIZE);
        int gridY = (int) Math.round((shootingBubble.y - BUBBLE_SIZE - UI_HEIGHT) / (double) BUBBLE_SIZE);
        
        // Adjust for hexagonal grid
        if (gridY % 2 == 1) {
            gridX = (int) Math.round((shootingBubble.x - BUBBLE_SIZE - BUBBLE_SIZE / 2) / (double) BUBBLE_SIZE);
        }
        
        int newX = gridX * BUBBLE_SIZE + (gridY % 2) * (BUBBLE_SIZE / 2) + BUBBLE_SIZE;
        int newY = gridY * BUBBLE_SIZE + BUBBLE_SIZE + UI_HEIGHT;
        
        Bubble newBubble = new Bubble(newX, newY, shootingBubble.color);
        bubbles.add(newBubble);
        bubblesRemaining++;
        
        // Check for matches
        checkMatches(newBubble);
        
        // Create next bubble and move preview to current
        nextBubble = previewBubble;
        nextBubble.x = WINDOW_WIDTH / 2 - BUBBLE_SIZE / 2;
        nextBubble.y = SHOOTER_Y;
        
        previewBubble = new Bubble(WINDOW_WIDTH / 2 + 80, SHOOTER_Y + 10, 
                                  bubbleColors[random.nextInt(Math.min(4 + level, bubbleColors.length))]);
    }
    
    private void checkMatches(Bubble startBubble) {
        List<Bubble> matchingBubbles = new ArrayList<>();
        List<Bubble> visited = new ArrayList<>();
        
        findMatchingBubbles(startBubble, matchingBubbles, visited);
        
        if (matchingBubbles.size() >= 3) {
            bubbles.removeAll(matchingBubbles);
            bubblesRemaining -= matchingBubbles.size();
            
            int points = matchingBubbles.size() * 10 * level;
            score += points;
            
            // Add floating score
            floatingScores.add(new FloatingScore(startBubble.x, startBubble.y, points));
            
            // Remove floating bubbles
            removeFloatingBubbles();
        }
    }
    
    private void findMatchingBubbles(Bubble bubble, List<Bubble> matching, List<Bubble> visited) {
        if (visited.contains(bubble)) return;
        
        visited.add(bubble);
        matching.add(bubble);
        
        for (Bubble other : bubbles) {
            if (!visited.contains(other) && bubble.isAdjacent(other) && 
                bubble.color.equals(other.color)) {
                findMatchingBubbles(other, matching, visited);
            }
        }
    }
    
    private void removeFloatingBubbles() {
        List<Bubble> connected = new ArrayList<>();
        List<Bubble> visited = new ArrayList<>();
        
        // Find all bubbles connected to the top
        for (Bubble bubble : bubbles) {
            if (bubble.y <= BUBBLE_SIZE * 2 + UI_HEIGHT && !visited.contains(bubble)) {
                findConnectedBubbles(bubble, connected, visited);
            }
        }
        
        // Remove bubbles not connected to top
        List<Bubble> toRemove = new ArrayList<>();
        for (Bubble bubble : bubbles) {
            if (!connected.contains(bubble)) {
                toRemove.add(bubble);
                score += 5 * level; // Bonus points for floating bubbles
                floatingScores.add(new FloatingScore(bubble.x, bubble.y, 5 * level));
            }
        }
        
        bubblesRemaining -= toRemove.size();
        bubbles.removeAll(toRemove);
    }
    
    private void findConnectedBubbles(Bubble bubble, List<Bubble> connected, List<Bubble> visited) {
        if (visited.contains(bubble)) return;
        
        visited.add(bubble);
        connected.add(bubble);
        
        for (Bubble other : bubbles) {
            if (!visited.contains(other) && bubble.isAdjacent(other)) {
                findConnectedBubbles(other, connected, visited);
            }
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if (gameOver || gameWon) {
            if (gameWon) {
                level++;
                initializeGame();
            } else {
                level = 1;
                score = 0;
                initializeGame();
            }
            return;
        }
        
        if (nextBubble != null && shootingBubbles.isEmpty()) {
            // Calculate shooting direction
            double dx = e.getX() - (nextBubble.x + BUBBLE_SIZE / 2);
            double dy = e.getY() - (nextBubble.y + BUBBLE_SIZE / 2);
            double distance = Math.sqrt(dx * dx + dy * dy);
            
            if (distance > 0 && dy < 0) { // Only shoot upward
                double speed = 10;
                nextBubble.vx = (int) ((dx / distance) * speed);
                nextBubble.vy = (int) ((dy / distance) * speed);
                
                shootingBubbles.add(nextBubble);
                nextBubble = null;
            }
        }
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        mousePosition = e.getPoint();
    }
    
    // Floating score effect
    private class FloatingScore {
        private int x, y;
        private int points;
        private int timer;
        private Color color;
        
        public FloatingScore(int x, int y, int points) {
            this.x = x;
            this.y = y;
            this.points = points;
            this.timer = 0;
            this.color = points > 50 ? successColor : accentColor;
        }
        
        public void update() {
            timer++;
            y -= 2;
        }
        
        public void draw(Graphics2D g2d) {
            if (timer < 60) {
                float alpha = 1.0f - (float) timer / 60;
                g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(255 * alpha)));
                g2d.setFont(new Font("Arial", Font.BOLD, 16));
                g2d.drawString("+" + points, x, y);
            }
        }
        
        public boolean isFinished() {
            return timer >= 60;
        }
    }
    
    // Background particle effect
    private class BackgroundParticle {
        private float x, y;
        private float vx, vy;
        private Color color;
        private int size;
        private float alpha;
        
        public BackgroundParticle() {
            reset();
        }
        
        private void reset() {
            x = random.nextFloat() * WINDOW_WIDTH;
            y = random.nextFloat() * WINDOW_HEIGHT;
            vx = (random.nextFloat() - 0.5f) * 0.5f;
            vy = (random.nextFloat() - 0.5f) * 0.5f;
            size = random.nextInt(3) + 1;
            alpha = random.nextFloat() * 0.3f + 0.1f;
            
            int colorChoice = random.nextInt(3);
            switch (colorChoice) {
                case 0: color = accentColor; break;
                case 1: color = successColor; break;
                case 2: color = warningColor; break;
            }
        }
        
        public void update() {
            x += vx;
            y += vy;
            
            if (x < 0 || x > WINDOW_WIDTH || y < 0 || y > WINDOW_HEIGHT) {
                reset();
            }
        }
        
        public void draw(Graphics2D g2d) {
            g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(255 * alpha)));
            g2d.fillOval((int)x, (int)y, size, size);
        }
    }
    
    // Unused mouse events
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseDragged(MouseEvent e) {}
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Enhanced Bubble Shooter");
            BubbleShooterGame game = new BubbleShooterGame();
            
            frame.add(game);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
