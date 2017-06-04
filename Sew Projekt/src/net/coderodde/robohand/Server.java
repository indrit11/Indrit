package net.coderodde.robohand;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This class implements the canvas that draws vectors.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Apr 17, 2016)
 */
public class Server extends Canvas {

    /**
     * Minimum number of vectors allowed.
     */
    private static final int MINIMUM_VECTORS = 1;

    /**
     * When resizing a vector, its length may be increased or decreased by 
     * {@code LENGTH_DELTA} pixels per request.
     */
    private static final float LENGTH_DELTA = 5.0f;

    /**
     * When changing the angle of a vector, the angle increases or decreases by
     * {@code ANGLE_DELTA} radians per request. This is roughly 5 degrees.
     */
    private static final float ANGLE_DELTA = ((float) Math.PI) / 36;

    /**
     * The color used to draw non-selected vectors.
     */
    private static final Color VECTOR_COLOR = Color.RED;

    /**
     * The color of the current vector. 
     */
    private static final Color CURRENT_VECTOR_COLOR = Color.WHITE;

    /**
     * The list of vectors in this canvas.
     */
    private final List<Client> vectorList = new ArrayList<>();

    /**
     * The index of the selected vector.
     */
    private int selectedVectorIndex;

    /**
     * Constructs this canvas with given vectors.
     * 
     * @param vectorList the vectors to operate on. May form a tree. A cycle 
     * will eventually lead to stack overflow.
     */
    public Server(List<Client> vectorList) {
        if (vectorList.size() < 1) {
            throw new IllegalArgumentException(
                    "This canvas requires at least " + MINIMUM_VECTORS + " vector.");
        }

        this.vectorList.addAll(vectorList);
        addKeyListener(new MyCanvasKeyListener());
    }

    /**
     * Chooses a preceding vector in the list as selected. If the current 
     * selected vector is first in the list, does nothing.
     */
    public void decSelectedVectorIndex() {
        if (selectedVectorIndex > 0) {
            selectedVectorIndex--;
        }

        repaint();
    }

    /**
     * Chooses a succeeding vector in the list as selected. If the current 
     * selected vector is last in the list, does nothing.
     */
    public void incSelectedVectorIndex() {
        if (selectedVectorIndex < vectorList.size() - 1) {
            selectedVectorIndex++;
        }

        repaint();
    }

    /**
     * Makes selected vector a little bit longer.
     */
    public void enlargeCurrentSelectedVector() {
        setSelectedVectorLength(LENGTH_DELTA);
    }

    /**
     * Makes selected vector a little bit shorter. The length of the vector will
     * never become negative.
     */
    public void shrinkCurrentSelectedVector() {
        setSelectedVectorLength(-LENGTH_DELTA);
    }

    /**
     * Rotates the selected vector at its tail. If the selected vector has
     * child vectors, their angle and location is modified as well such that
     * they preserve their geometry with respect to the selected vector.
     * 
     * @param angleDelta the amount of rotation in radians.
     */
    public void rotateSelectedVector(float angleDelta) {
        float newAngle = vectorList.get(selectedVectorIndex)
                                   .getAngle() + angleDelta;
        vectorList.get(selectedVectorIndex).setAngle(newAngle);
        repaint();
    }

    @Override
    public void update(Graphics g) {
        vectorList.get(0).setLocation(getWidth() / 2, getHeight() / 2);
        // Fill everything with the background color.
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw the vectors.
        g.setColor(VECTOR_COLOR);
        for (Client vector : vectorList) {
            draw(g, vector);
        }

        g.setColor(CURRENT_VECTOR_COLOR);
        draw(g, vectorList.get(selectedVectorIndex));
    }

    @Override
    public void paint(Graphics g) {
        update(g);
    }

    private void draw(Graphics g, Client vector) {
        Point2D.Float tail = vector.getLocation();
        Point2D.Float head = vector.getHeadLocation();

        g.drawLine((int) tail.x, 
                   (int) tail.y, 
                   (int) head.x, 
                   (int) head.y);
    }

    private void setSelectedVectorLength(float delta) {
        Client
                vector = vectorList.get(selectedVectorIndex);
        vector.setLength(Math.max(0.0f, vector.getLength() + delta));
        repaint();
    }

    private final class MyCanvasKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            switch (e.getKeyChar()) {
                case 'a':
                case 'A':
                    decSelectedVectorIndex();
                    break;

                case 'd':
                case 'D':
                    incSelectedVectorIndex();
                    break;

                case 'w':
                case 'W':
                    enlargeCurrentSelectedVector();
                    break;

                case 's':
                case 'S':
                    shrinkCurrentSelectedVector();
                    break;

                case 'q':
                case 'Q':
                    rotateSelectedVector(-ANGLE_DELTA);
                    break;

                case 'e':
                case 'E':
                    rotateSelectedVector(ANGLE_DELTA);
                    break;
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {}

        @Override
        public void keyReleased(KeyEvent e) {}
    }
}


   