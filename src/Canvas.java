import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Canvas extends JFrame {
    private int oldX, oldY;
    private int cursX, cursY;
    private int subCanvasScale;
    private GameOfLife subCanvas;

    public Canvas() {
        oldX = -1; oldY = -1;
        subCanvasScale = 50;
        subCanvas = new GameOfLife(15, 15);

        JPanel canvas = new JPanel() {
            public void paintComponent(Graphics g) {
                int m = subCanvas.getM(), n = subCanvas.getN();
                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < n; j++) {
                        g.setColor(subCanvas.currentState(i, j) == 1 ? Color.WHITE : Color.BLACK);
                        g.fillRect(i * subCanvasScale, j * subCanvasScale, subCanvasScale, subCanvasScale);
                        g.setColor(Color.BLACK);
                        //g.drawRect(x, y, subCanvasScale, subCanvasScale);
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

            @Override
            public void mouseMoved(MouseEvent e) {
                cursX = e.getX() / subCanvasScale;
                cursY = e.getY() / subCanvasScale;
                repaint();
            }
        });
        canvas.setPreferredSize(new Dimension(subCanvasScale * subCanvas.getM(), subCanvasScale * subCanvas.getN()));
        this.setTitle("Conway's Game of Life");
        this.setBackground(Color.black);
        this.add(canvas);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        //this.setSize(300, 300);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void setOldCursorCoord(int x, int y) {
        oldX = x; oldY = y;
    }

    public void start() {

    }
}
