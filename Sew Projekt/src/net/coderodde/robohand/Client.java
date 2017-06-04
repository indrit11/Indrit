
package net.coderodde.robohand;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This class implements a vector. The tail of another vector can be attached to
 * the head of this vector.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Apr 17, 2016)
 */
public class Client {

    /**
     * The <tt>x</tt>-coordinate of the tail of this vector.
     */
    private float locationX;

    /**
     * The <tt>y</tt>-coordinate of the tail of this vector.
     */
    private float locationY;

    /**
     * The length of this vector.
     */
    private float length;

    /**
     * The angle of this vector in radians.
     */
    private float angle;

    /**
     * The list of all the child vectors of this vector. Child vectors' tails
     * are attached to the head of this vector.
     */
    private final List<Client> childVectors = new ArrayList<>();

    /**
     * Constructs a vector with given length and angle.
     * 
     * @param length the length of a new vector.
     * @param angle  the angle in radians of a new vector.
     */
    public Client(float length, float angle) {
        this.length = length;
        this.angle = angle;
    }

    /**
     * Connects {@code vector} as a child to this vector.
     * 
     * @param vector the child vector to add.
     */
    public void addChildVector(Client vector) {
        Objects.requireNonNull(vector, "The input vector is null.");
        childVectors.add(vector);
        Point2D.Float headLocation = getHeadLocation();
        vector.setLocation(headLocation.x, headLocation.y);
    }

    /**
     * Removes a child vector if it is connected to the tail of this vector.
     * 
     * @param vector the vector to remove from the child list.
     */
    public void removeChildVector(Client vector) {
        Objects.requireNonNull(vector, "The input vector is null.");
        childVectors.remove(vector);
    }

    /**
     * Gets the location of the tail of this vector.
     * 
     * @return the tail location.
     */
    public Point2D.Float getLocation() {
        return new Point2D.Float(locationX, locationY);
    }

    /**
     * Sets the location of this vector. If this vector has child vectors, they
     * are moved as well.
     * 
     * @param locationX the <tt>x</tt>-coordinate of the new location.
     * @param locationY the <tt>y</tt>-coordinate of the new location.
     */
    public void setLocation(float locationX, float locationY) {
        this.locationX = locationX;
        this.locationY = locationY;
        Point2D.Float point = getHeadLocation();

        for (Client
                childVector : childVectors) {
            childVector.setLocation(point.x, point.y);
        }
    }

    /**
     * Returns the length of this vector.
     * 
     * @return the length of this vector. 
     */
    public float getLength() {
        return length;
    }

    /**
     * Sets the new length of this vector. If this vector has child vectors, 
     * they are moved as well.
     * 
     * @param length the new length.
     */
    public void setLength(float length) {
        this.length = length;
        Point2D.Float point = getHeadLocation();

        for (Client childVector : childVectors) {
            childVector.setLocation(point.x, point.y);
        }
    }

    /**
     * Gets the angle of this vector.
     * 
     * @return the angle of this vector.
     */
    public float getAngle() {
        return angle;
    }

    /**
     * Sets the angle of this vector. If this vector has child vectors, it 
     * updates their location and angles as well.
     * 
     * @param angle the angle in radians.
     */
    public void setAngle(float angle) {
        float angleDelta = angle - this.angle;
        this.angle = angle;
        Point2D.Float headLocation = getHeadLocation();

        for (Client childVector : childVectors) {
            childVector.setLocation(headLocation.x, headLocation.y);
            childVector.setAngle(childVector.getAngle() + angleDelta);
        }
    }

    /**
     * Returns the location of the head of this vector.
     * 
     * @return the location of the head of this vector.
     */
    public Point2D.Float getHeadLocation() {
        Point2D.Float point = new Point2D.Float();
        float x = locationX + length * (float) Math.cos(angle);
        float y = locationY + length * (float) Math.sin(angle);
        point.x = x;
        point.y = y;
        return point;
    }
}
