package burgerShot.model;

import java.awt.Rectangle;

public class ron {

    private static final int START_X_COORDINATE = 0;
    private static final int START_Y_COORDINATE = 450;
    
    private Rectangle rectangle;
    private int speed;

    public ron() {
        rectangle = new Rectangle(70, 50);
        rectangle.setLocation(START_X_COORDINATE, START_Y_COORDINATE);
        speed = 1;
    }

    public void setSpeed(int pSpeed) {
        this.speed = pSpeed;
    }

    public int getSpeed() {
        return speed;
    }

    public void setX(int pX) {
        this.rectangle.x = pX;
    }

    public int getX() {
        return rectangle.x;
    }

    public void setY(int pY) {
        this.rectangle.y = pY;
    }
    
    public int getY() {
        return rectangle.y;
    }

}