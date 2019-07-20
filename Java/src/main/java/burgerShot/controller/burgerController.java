/* Ryan Charles
 * CS 4000 - BurgerShot
 */

package burgerShot.controller;

import java.awt.image.BufferedImage;

import burgerShot.GUI.GamePanel;
import burgerShot.model.burger;
import burgerShot.utility.Resources.Resources;

public class burgerController {
//set up burger variables
    private static final int LEFT = -3;
    private static final int RIGHT = 3;
    private static final int UP = -3;
    private static final int DOWN = 3;
    private static final int DELAY = 200;

    private static burgerController burgerControllerIstance = null;
    private Spritesheet spriteSheet;
    private BufferedImage currentImage;
    private burgerAnimation burgerAnimation;
    private GamePanel panel;
    private burger burger;

    private int xDirection;
    private int yDirection;
    private int x;
    private int y;
    private int ammunition;


    private boolean isburgerVisible;
    private boolean wasburgerHit;
    private boolean isshot;
    private boolean Gone;

    private burgerController() {
        currentImage = Resources.getImage("/images/burgerRight0.png");
        spriteSheet = new Spritesheet();
        burgerAnimation = new burgerAnimation();
        xDirection = RIGHT;
        yDirection = DOWN;
        isburgerVisible = false;
        wasburgerHit = false;
        isshot = false;
        Gone = false;
        x = 0;
        y = 0;
    }

    public static burgerController getIstance() {
        if (burgerControllerIstance == null) {
            return burgerControllerIstance = new burgerController();
        }
        return burgerControllerIstance;
    }

    public void setPanel(GamePanel pPanel) {
        this.panel = pPanel;
    }

    public void setburger(burger pburger) {
        this.burger = pburger;
    }

    public burger getburger() {
        return burger;
    }

    private void flight() {
        if (xDirection == RIGHT && yDirection == UP) {
            spriteSheet.setFrames(2, "burgerRight");
            spriteSheet.setDelay(DELAY);
            spriteSheet.update();
            currentImage = spriteSheet.getCurrentFrame();
        } else {
            spriteSheet.setFrames(2, "burgerLeft");
            spriteSheet.setDelay(DELAY);
            spriteSheet.update();
            currentImage = spriteSheet.getCurrentFrame();
        }

        x = burger.getX();
        y = burger.getY();
        x += xDirection;
        y += yDirection;
        burger.setX(x);
        burger.setY(y);

        if (burger.getX() <= 0) {
            this.xDirection = RIGHT;
        } else if (burger.getX() + 40 >= 800) {
            this.xDirection = LEFT;
        }
        if (burger.getY() <= 0) {
            this.yDirection = DOWN;
        } else if (burger.getY() >= 390) {
            this.yDirection = UP;
        }
    }

    private void shot() {
        currentImage = Resources.getImage("/images/burgerShot.png");
    }

    private void fall() {
        spriteSheet.setFrames(3, "burgerfall");
        spriteSheet.setDelay(DELAY);
        spriteSheet.update();
        currentImage = spriteSheet.getCurrentFrame();

        y = burger.getY();
        y += 6;
        burger.setY(y);
    }

    private void flyAway() {
        spriteSheet.setFrames(3, "burgerFlyAway");
        spriteSheet.setDelay(DELAY);
        spriteSheet.update();
        currentImage = spriteSheet.getCurrentFrame();

        y = burger.getY();
        y -= 2;
        burger.setY(y);
    }

    public BufferedImage getCurrentImage() {
        return currentImage;
    }

    public boolean isburgerVisible() {
        return isburgerVisible;
    }

    public boolean isshot() {
        return isshot;
    }

    public boolean isGone() {
        return Gone;
    }

    public void theburgerIsGone(boolean pValue) {
        this.Gone = pValue;
    }

    public void theburgerWasHit(boolean pValue) {
        this.wasburgerHit = pValue;
    }

    public void decreaseAmmunition() {
        ammunition--;
    }

    public burgerAnimation getburgerAnimation() {
        return burgerAnimation;
    }

    public class burgerAnimation implements Runnable {

        private Thread thread;

        public void start() throws InterruptedException {
            reset();
            thread = new Thread(this);
            thread.start();
            thread.join();
        }

        public void stop() {
            if (thread != null && thread.isAlive()) {
                thread.interrupt();
            }
        }

        private void reset() {
            isburgerVisible = true;
            wasburgerHit = false;
            isshot = false;
            Gone = false;
            ammunition = 3;
        }

        @Override
        public void run() {
            while (!wasburgerHit && ammunition > 0) {
                flight();
                panel.setburgerCurrentImage(currentImage);
                panel.repaint();
            }
            if (ammunition == 0) {
                Gone = true;
                while (burger.getY() > -50) {
                    flyAway();
                    panel.setburgerCurrentImage(currentImage);
                    panel.repaint();
                }
            } else {
                try {
                    shot();
                    panel.setburgerCurrentImage(currentImage);
                    panel.repaint();
                    Thread.sleep(500);
                    while (burger.getY() < 420) {
                        fall();
                        panel.setburgerCurrentImage(currentImage);
                        panel.repaint();
                    }
                    isshot = true;
                } catch (InterruptedException ex) {
                    System.out.println("an error occured during burger animation thread");
                    stop();
                }
            }
            isburgerVisible = false;
            stop();
        }
    }

}
