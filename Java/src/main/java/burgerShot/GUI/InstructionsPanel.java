/* Ryan Charles
 * CS 4000 - BurgerShot
 */

package burgerShot.GUI;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.Timer;

import burgerShot.utility.Resources.Resources;

public class InstructionsPanel extends JPanel {
//initialize variables
    private static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);

    private BufferedImage backgroundImg;
    private BufferedImage pressEnterImage;
    private boolean showImage;

    public InstructionsPanel() {
        init();
    }
//initialize instructions
    private void init() {
        this.setLayout(null);
        this.setCursor(HAND_CURSOR);
        backgroundImg = Resources.getImage("/images/instructionsBackgroundwithText.png");
        pressEnterImage = Resources.getImage("/images/pressEnter.png");
        showImage = true;
        imageBlinker();
    }
//set up blinker for enter to continue
    private void imageBlinker() {
        ActionListener listener = (ActionEvent e) -> {
            if (pressEnterImage != null) {
                showImage = !showImage;
                repaint();
            }
        };
        Timer timer = new Timer(600, listener);
        timer.start();
    }
//place instruction image and enter image
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(backgroundImg, 0, 0, getParent());
        if (showImage && pressEnterImage != null) {
            g2D.drawImage(pressEnterImage, 260, 500, this);
        }
    }
}