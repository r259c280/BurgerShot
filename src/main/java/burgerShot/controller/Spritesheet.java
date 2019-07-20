package burgerShot.controller;

import java.awt.image.BufferedImage;

import burgerShot.utility.Resources.Resources;

public class Spritesheet {

    private BufferedImage[] frames;
    private int currentFramePosition;
    private long startTime;
    private long delay;

    public Spritesheet() {
    }


    public void setFrames(int pSize, String pName) {
        frames = new BufferedImage[pSize];
        if (currentFramePosition >= frames.length) {
            currentFramePosition = 0;
        }
        for (int i = 0; i < frames.length; i++) {
            frames[i] = Resources.getImage("/images/" + pName + i + ".png");
        }
    }

    public void setDelay(long pDelay) {
        this.delay = pDelay;
    }

    public void update() {
        if (this.delay == -1) {
            return;
        }
        long elapsed = (System.nanoTime() - this.startTime) / 1000000;
        if (elapsed > this.delay) {
            this.currentFramePosition++;
            this.startTime = System.nanoTime();
        }
        if (this.currentFramePosition == this.frames.length) {
            this.currentFramePosition = 0;
        }
    }

    public BufferedImage getCurrentFrame() {
        return frames[currentFramePosition];
    }

}
