package net.coderodde.robohand;


import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * This class implements the GUI for dealing with a robohand.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Apr 17, 2016)
 */
public class RobohandApp extends JFrame {

    /**
     * The width of this frame in pixels.
     */
    private static final int FRAME_WIDTH  = 800;

    /**
     * The height of this frame in pixels.
     */
    private static final int FRAME_HEIGHT = 600;

    private final JPanel panel = new JPanel();
    private Canvas myCanvas;

    public RobohandApp() {
        super("Robohand 1.6");

        constructCanvas();

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(panel, BorderLayout.CENTER);

        panel.setLayout(new BorderLayout());
        panel.add(myCanvas, BorderLayout.CENTER);
        myCanvas.setBackground(Color.BLACK);

        setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setResizable(false);
        centerOut();
        setVisible(true);
    }

    private void constructCanvas() {
        List<Client> vectorList = new ArrayList<>();
        float length = 30.0f;
        float angle  = 0.0f;

        Client[] vectors = new Client[] {
            new Client(length, angle),

            new Client(length, angle),
            new Client(length, angle),

            new Client(length, angle),
            new Client(length, angle),
            new Client(length, angle),
            new Client(length, angle),

            new Client(length, angle),
        };

        vectors[0].addChildVector(vectors[1]);
        vectors[0].addChildVector(vectors[2]);

        vectors[1].addChildVector(vectors[3]);
        vectors[1].addChildVector(vectors[4]);
        vectors[2].addChildVector(vectors[5]);
        vectors[2].addChildVector(vectors[6]);

        vectors[6].addChildVector(vectors[7]);

        vectorList.addAll(Arrays.asList(vectors));
        myCanvas = new Server(vectorList);
    }

    private void centerOut() {
        final int currentWidth  = getWidth();
        final int currentHeight = getHeight();
        final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width - currentWidth) / 2,
                    (screen.height - currentHeight) / 2);
    }

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(() -> {
            RobohandApp app = new RobohandApp();
        });
    }
}
