import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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


public class Canvas extends JFrame {
    private int oldX, oldY;
    private int cursX, cursY;
    private int subCanvasScale;
    private boolean running;

    private GameOfLife subCanvas;
    private Timer timer;

    public Canvas() {
        oldX = -1; oldY = -1;
        running = false;

        String val = null;
        while (val == null) {
            val = JOptionPane.showInputDialog("Type in the desired MxN size of the grid, separated by commas. (Ex.: 15, 15)");
            if (val == null || val.isEmpty() || val.matches("[a-zA-Z]+")) {
                JOptionPane.showMessageDialog(this, "Invalid input");
                val = null;
            }
        }

        String[] aux = val.trim().split(", *");
        String m = aux[0].trim(), n = aux[1].trim();

        val = null;
        while (val == null) {
            val = JOptionPane.showInputDialog("Type in the desired scale of the grid. (Ex.: 15) \n This will define the number of pixels per cell.");
            if (val == null || val.isEmpty() || val.matches("[a-zA-Z ]+")) {
                JOptionPane.showMessageDialog(this, "Invalid input");
                val = null;
            }
        }

        subCanvasScale = Integer.valueOf(val.trim());
        subCanvas = new GameOfLife(Integer.valueOf(m), Integer.valueOf(n));

        timer = new Timer(500, e -> {
            if (running) {
                subCanvas.tick();
                repaint();
            }
            if (subCanvas.updated() <= 0) {
                running = false;
                super.setTitle("Conway's Game of Life (Halt)");
            }
        });
        
        this.setBackground(Color.black);
        this.setJMenuBar(generateMenuBar());
        this.setTitle("Conway's Game of Life");
        this.add(generateCanvas());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        //this.setSize(300, 300);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void setOldCursorCoord(int x, int y) {
        oldX = x; oldY = y;
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem item = new JMenuItem("Start");
        item.addActionListener(e -> {
            timer.start();
            running = true;
            super.setTitle("Conway's Game of Life (Running)");
        });
        menu.add(item);

        item = new JMenuItem("Stop");
        item.addActionListener(e -> {
            timer.stop();
            running = false;
            super.setTitle("Conway's Game of Life");
        });
        menu.add(item);

        item = new JMenuItem("Reset");
        item.addActionListener(e -> {
            if (running) {
                return;
            }
            subCanvas.reset();
            repaint();
        });
        menu.add(item);

        menu.addSeparator();
        item = new JMenuItem("Exit");
        item.addActionListener(e -> System.exit(0));
        menu.add(item);
        menuBar.add(menu);

        menu = new JMenu("Options");
        item = new JMenuItem("Update Rate");
        item.addActionListener(e -> {
            String val = JOptionPane.showInputDialog("Type in the desired update rate (in milliseconds)");
            if (val == null || val.isEmpty() || val.matches("[a-zA-Z ]+")) {
                JOptionPane.showMessageDialog(this, "Invalid input");
                return;
            }
            int rate = Integer.valueOf(val);
            timer.setDelay(rate);
        });
        menu.add(item);

        menuBar.add(menu);
        return menuBar;
    }

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
                //g.setColor(Color.BLUE);
                //g.fillRect(cursX * subCanvasScale, cursY * subCanvasScale, subCanvasScale, subCanvasScale);
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
                setOldCursorCoord(x, y);
                subCanvas.toggle(x, y);
                repaint();
            }

            // @Override
            // public void mouseMoved(MouseEvent e) {
            //     cursX = e.getX() / subCanvasScale;
            //     cursY = e.getY() / subCanvasScale;
            //     repaint();
            // }
        });
        canvas.setPreferredSize(new Dimension(subCanvasScale * subCanvas.getM(), subCanvasScale * subCanvas.getN()));
        return canvas;
    }
}
