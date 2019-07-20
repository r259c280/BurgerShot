/* Ryan Charles
 * CS 4000 - BurgerShot
 */

package burgerShot.GUI;

import java.awt.image.BufferedImage;
import javax.swing.*;
import burgerShot.utility.Resources.Resources;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class MenuPanel extends JPanel {
//initialize main menu items
    private static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);
    private BufferedImage backgroundImg;

    public MenuPanel() {
        init();
    }
//set main menu background
    private void init() {
        this.setLayout(null);
        backgroundImg = Resources.getImage("/images/menuPanelBackground.png");
        this.setCursor(HAND_CURSOR);
    }
  //place main menu image 
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(backgroundImg, 0, 0, getParent());
    }

}