import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * The Canvas class serves (mostly) a single purpose: to let the user
 * paint and to erase some squares on the screen. Its other purpose is managing
 * the Game of Life itself.
 */
public class Canvas extends JFrame {
    private static Canvas instance = null;

    private Timer timer;
    private int oldX, oldY;
    private boolean isRunning;

    private int subCanvasScale;
    private GameOfLife subCanvas;

    /**
     * Creates a new blank Canvas.
     */
    private Canvas() {
        oldX = -1; oldY = -1;
        isRunning = false;

        double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        if (height == 1080) {
            subCanvasScale = 10;    
        } else if (height <= 768) {
            subCanvasScale = 7;
        } else if (height > 1080) {
            subCanvasScale = 15;
        }
        subCanvas = new GameOfLife(120, 75);

        timer = new Timer(500, e -> updateCanvas());
        
        this.setBackground(Color.black);
        this.setResizable(false);
        this.setJMenuBar(generateMenuBar());
        this.setTitle("Conway's Game of Life");
        this.add(generateCanvas());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        //this.setSize(300, 300);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * Returns the Canvas instance. New instances will never
     * be created.
     * @return the Canvas instance.
     */
    public static Canvas getInstance() {
        if (instance == null) {
            instance = new Canvas();
        }
        return instance;
    }

    /**
     * Sets previous cursor coordinates.
     * @param x
     * @param y
     */
    private void setOldCursorCoord(int x, int y) {
        oldX = x; oldY = y;
    }

    /**
     * Starts the Game of Life game
     */
    private void startGame() {
        timer.start();
        isRunning = true;
        super.setTitle("Conway's Game of Life (Running)");
    }

    /**
     * Stops the Game of Life
     */
    private void stopGame() {
        timer.stop();
        isRunning = false;
        super.setTitle("Conway's Game of Life");
    }

    /**
     * Resets the Game of Life. This method will clear both
     * the main canvas and the subcanvas (the game itself).
     */
    private void resetGame() {
        if (isRunning) {
            return;
        }
        subCanvas.reset();
        repaint();
    }

    /**
     * Updates the Game of Life and the Canvas.
     */
    private void updateCanvas() {
        if (isRunning) {
            subCanvas.tick();
            repaint();
        }
        if (subCanvas.updated() <= 0) {
            isRunning = false;
            super.setTitle("Conway's Game of Life (Halt)");
        }
    }

    /**
     * Asks the user for a new update rate for the Game of Life.
     */
    private void askForNewRate() {
        String val = JOptionPane.showInputDialog("Type in the desired update rate (in milliseconds)");
        if (val == null || val.isEmpty() || val.matches("[a-zA-Z ]+")) {
            JOptionPane.showMessageDialog(this, "Invalid input");
            return;
        }
        int rate = Integer.valueOf(val);
        timer.setDelay(rate);
    }

    /**
     * Generates this frame's menu bar.
     * @return the generated menu bar.
     */
    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        JMenuItem item = new JMenuItem("Exit");
        item.addActionListener(e -> System.exit(0));
        menu.add(item);
        menuBar.add(menu);

        menu = new JMenu("Game");
        menu.setMnemonic(KeyEvent.VK_G);
        item = new JMenuItem("Start");
        item.addActionListener(e -> startGame());
        item.setMnemonic(KeyEvent.VK_1);
        menu.add(item);

        item = new JMenuItem("Stop");
        item.addActionListener(e -> stopGame());
        item.setMnemonic(KeyEvent.VK_2);
        menu.add(item);

        item = new JMenuItem("Reset");
        item.addActionListener(e -> resetGame());
        item.setMnemonic(KeyEvent.VK_3);
        menu.add(item);
        menuBar.add(menu);

        menu = new JMenu("Options");
        menu.setMnemonic(KeyEvent.VK_V);
        item = new JMenuItem("Change Update Rate");
        item.addActionListener(e -> askForNewRate());
        item.setMnemonic(KeyEvent.VK_1);
        menu.add(item);
        menuBar.add(menu);

        return menuBar;
    }

    /**
     * Generates this frame's Canvas.
     * @return the generated Canvas.
     */
    private JPanel generateCanvas() {
        JPanel canvas = new JPanel() {
            public void paintComponent(Graphics g) {
                int m = subCanvas.getM(), n = subCanvas.getN();
                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < n; j++) {
                        g.setColor(subCanvas.currentState(i, j) == 1 ? Color.WHITE : Color.BLACK);
                        g.fillRect(i * subCanvasScale, j * subCanvasScale, subCanvasScale, subCanvasScale);
                        g.setColor(Color.BLACK);
                        g.draw3DRect(i * subCanvasScale, j * subCanvasScale, subCanvasScale, subCanvasScale, false);
                        g.setColor(Color.BLUE);
                    }
                }
            }
        };
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX() / subCanvasScale;
                int y = e.getY() / subCanvasScale;
                subCanvas.toggle(x, y);
                setOldCursorCoord(x, y);
                repaint();
            }  
        });
        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e){
                int x = e.getX() / subCanvasScale;
                int y = e.getY() / subCanvasScale;
                if (x == oldX && y == oldY) {
                    return;
                }
                subCanvas.toggle(x, y);
                setOldCursorCoord(x, y);
                repaint();
            }
        });
        canvas.setPreferredSize(new Dimension(subCanvasScale * subCanvas.getM(), subCanvasScale * subCanvas.getN()));
        return canvas;
    }
}
