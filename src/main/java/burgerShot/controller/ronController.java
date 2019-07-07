package burgerShot.controller;

import java.awt.image.BufferedImage;

import burgerShot.GUI.GamePanel;
import burgerShot.model.ron;
import burgerShot.utility.Resources.Resources;

public class ronController {

    private static final int DELAY = 250;
    private static ronController ronControllerIstance = null;

    private ronIntroAnimation ronIntroAnimation;
    private ronAnimation ronAnimation;
    
    private Spritesheet spriteSheet;
    
    private BufferedImage currentImage;
    
    private GamePanel panel;
    
    private ron ron;
    
    private boolean isAnimationFinished;
    
    private int x;
    private int y;

    private ronController() {
        currentImage = Resources.getImage("/images/ronRight0.png");
        ronIntroAnimation = new ronIntroAnimation();
        ronAnimation = new ronAnimation();
        spriteSheet = new Spritesheet();
        isAnimationFinished = false;
        x = 0;
        y = 0;
    }

    public static ronController getIstance() {
        if (ronControllerIstance == null) {
            return ronControllerIstance = new ronController();
        }
        return ronControllerIstance;
    }

    public void setPanel(GamePanel pPanel) {
        this.panel = pPanel;
    }

    public ronIntroAnimation getIntroAnimation() {
        return ronIntroAnimation;
    }

    public ronAnimation getAnimation() {
        return ronAnimation;
    }

    public void setron(ron pron) {
        this.ron = pron;
    }

    public ron getron() {
        return ron;
    }

    private void moveRigth() {
        spriteSheet.setFrames(3, "ronRight");
        spriteSheet.setDelay(DELAY);
        spriteSheet.update();
        currentImage = spriteSheet.getCurrentFrame();
        x = ron.getX();
        x += ron.getSpeed();
        ron.setX(x);
    }

    private void crouch() {
        spriteSheet.setFrames(2, "ronCrouch");
        spriteSheet.setDelay(DELAY);
        spriteSheet.update();
        currentImage = spriteSheet.getCurrentFrame();
    }

    private void moveUp() {
        currentImage = Resources.getImage("/images/ronHappy.png");
        y = ron.getY();
        y -= ron.getSpeed();
        ron.setY(y);
    }

    private void laughAndGoUp() {
        spriteSheet.setFrames(2, "ronLaugh");
        spriteSheet.update();
        currentImage = spriteSheet.getCurrentFrame();
        y = ron.getY();
        y -= ron.getSpeed();
        ron.setY(y);
    }

    private void laugh() {
        spriteSheet.setFrames(2, "ronLaugh");
        spriteSheet.update();
        currentImage = spriteSheet.getCurrentFrame();
    }

    private void laughAndGoDown() {
        spriteSheet.setFrames(2, "ronLaugh");
        spriteSheet.update();
        currentImage = spriteSheet.getCurrentFrame();
        y = ron.getY();
        y += ron.getSpeed();
        ron.setY(y);
    }

    private void moveDown() {
        currentImage = Resources.getImage("/images/ronHappy.png");
        y = ron.getY();
        y += ron.getSpeed();
        ron.setY(y);
    }

    private void ronInAlert() {
        currentImage = Resources.getImage("/images/ronAttention.png");
    }

    private void jump() {
        currentImage = Resources.getImage("/images/ronJump.png");
        x = ron.getX();
        y = ron.getY();
        x += ron.getSpeed();
        y -= ron.getSpeed();
        ron.setX(x);
        ron.setY(y);
    }

    private void land() {
        currentImage = Resources.getImage("/images/ronLanding.png");
        x = ron.getX();
        y = ron.getY();
        x += ron.getSpeed();
        y += ron.getSpeed();
        ron.setX(x);
        ron.setY(y);
    }

    public BufferedImage getCurrentImage() {
        return currentImage;
    }

    public boolean isIntroAnimationFinished() {
        return isAnimationFinished;
    }

    public class ronIntroAnimation implements Runnable {

        private Thread thread;
        private int i;

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
            isAnimationFinished = false;
            i = 0;
            ron.setX(0);
            ron.setY(450);
        }

        @Override
        public void run() {
            while (!isAnimationFinished) {
                while (ron.getX() < 250) {
                    moveRigth();
                    panel.setronCurrentImage(currentImage);
                    panel.repaint();
                }
                if (ron.getX() == 250) {
                    while (i < 180) {
                        crouch();
                        panel.setronCurrentImage(currentImage);
                        panel.repaint();
                        i++;
                    }
                    ronInAlert();
                    panel.setronCurrentImage(currentImage);
                    panel.repaint();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        System.out.println("an error occured during ron intro animation thread");
                        stop();
                    }
                    while (ron.getY() > 320) {
                        jump();
                        panel.setronCurrentImage(currentImage);
                        panel.repaint();
                    }
                    while (ron.getY() < 390) {
                        land();
                        panel.setronCurrentImage(currentImage);
                        panel.repaint();
                    }
                }
                isAnimationFinished = true;
                stop();
            }
        }
    }

    public class ronAnimation implements Runnable {

        private Thread thread;
        private boolean theDuckIsDead;
        private int i;

        public void start(boolean pValue) throws InterruptedException {
            reset();
            theDuckIsDead = pValue;
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
            ron.setX(350);
            ron.setY(380);
            i = 0;
            isAnimationFinished = false;
        }

        @Override
        public void run() {
            while (!isAnimationFinished) {
                if (theDuckIsDead) {
                    while (ron.getY() > 350) {
                        moveUp();
                        panel.setronCurrentImage(currentImage);
                        panel.repaint();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        System.out.println("an error occured during ron animation thread");
                        stop();
                    }
                    while (ron.getY() < 360) {
                        moveDown();
                        panel.setronCurrentImage(currentImage);
                        panel.repaint();
                    }
                } else {
                    while (ron.getY() > 370) {
                        laughAndGoUp();
                        panel.setronCurrentImage(currentImage);
                        panel.repaint();
                    }
                    while (i < 200) {
                        laugh();
                        panel.setronCurrentImage(currentImage);
                        panel.repaint();
                        i++;
                    }
                    while (ron.getY() < 390) {
                        laughAndGoDown();
                        panel.setronCurrentImage(currentImage);
                        panel.repaint();
                    }
                }
                isAnimationFinished = true;
                stop();
            }
        }
    }
}
