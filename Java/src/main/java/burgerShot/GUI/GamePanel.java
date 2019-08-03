/* Ryan Charles
 * CS 4000 - BurgerShot
 */

package burgerShot.GUI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

import burgerShot.controller.ronController;
import burgerShot.controller.burgerController;
import burgerShot.controller.GameListener;
import burgerShot.model.ron;
import burgerShot.model.burger;
import burgerShot.utility.Resources.Resources;

public class GamePanel extends JPanel implements MouseMotionListener {
//initialize everything
    private static final Cursor CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(), "null");
    private static final int BULLET_NUMBER = 3;

    private BufferedImage backgroundImg;
    private BufferedImage cursorImg;
    private BufferedImage ronCurrentImage;
    private BufferedImage burgerCurrentImage;
    private BufferedImage flyAwayImage;
    private BufferedImage[] Ketchup;
    private BufferedImage gameResultImage;
    private BufferedImage pressEnterImage;

    private Rectangle cursorRectangle;

    private ronController ronController;
    private burgerController burgerController;

    private GameListener gameListener;

    private ron ron;
    private burger burger;

    private GameThread gameThread;

    private boolean isGameFinished;
    private boolean showImage;

    private int currentKetchupNumber;
    private int shotburgers;
//start the game
    public GamePanel() {
        initPanel();
        gameThread.start();
    }

    private void initPanel() {
        this.setLayout(null);
        this.setCursor(CURSOR);
        this.addMouseMotionListener(this);
//set the images 
        backgroundImg = Resources.getImage("/images/gameBackground.png");
        burgerCurrentImage = Resources.getImage("/images/burgerRight0.png");
        ronCurrentImage = Resources.getImage("/images/ronRight0.png");
        cursorImg = Resources.getImage("/images/mcSight.png");
        flyAwayImage = Resources.getImage("/images/flyaway.png");
        pressEnterImage = Resources.getImage("/images/pressEnter.png");
        Ketchup = new BufferedImage[3];
        for (int i = 0; i < BULLET_NUMBER; i++) {
            Ketchup[i] = Resources.getImage("/images/ketchup.png");
        }
        showImage = true;
        isGameFinished = false;
        cursorRectangle = new Rectangle();
        cursorRectangle.setSize(cursorImg.getWidth(null), cursorImg.getHeight(null));
        cursorRectangle.setLocation(-cursorImg.getWidth(null), -cursorImg.getHeight(null));
        gameThread = new GameThread();

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point hitPoint = e.getPoint();
                if (burger != null) {
                    burgerController.decreaseKetchup();
                    currentKetchupNumber--;
                    hitPoint.x -= burger.getX();
                    hitPoint.y -= burger.getY();
                    if (contains(burgerController.getCurrentImage(), hitPoint.x, hitPoint.y)) {
                        burgerController.theburgerWasHit(true);
                        shotburgers++;
                    }
                }
            }
        });
    }

    public boolean contains(BufferedImage image, int x, int y) {
        if (x < 0 || y < 0 || x >= image.getWidth() || y >= image.getHeight()) {
            return false;
        }
        Color pixel = new Color(image.getRGB(x, y), true);
        return pixel.getAlpha() > 128;
    }

    public void setronCurrentImage(BufferedImage pImage) {
        this.ronCurrentImage = pImage;
    }

    public void setburgerCurrentImage(BufferedImage pImage) {
        this.burgerCurrentImage = pImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;

        g2D.drawImage(backgroundImg, 0, 0, this);
        g2D.drawImage(cursorImg, this.cursorRectangle.x, this.cursorRectangle.y, this);

        if (!ronController.isIntroAnimationFinished()) {
            g2D.drawImage(ronCurrentImage, ron.getX(), ron.getY(), this);
        }

        for (int i = 0; i < currentKetchupNumber; i++) {
            g2D.drawImage(Ketchup[i], i * 30, 520, this);
        }

        if (burgerController.isburgerVisible()) {
            g2D.drawImage(burgerCurrentImage, burger.getX(), burger.getY(), this);
        }

        if (burgerController.isGone()) {
            g2D.drawImage(flyAwayImage, 300, 85, this);
        }

        if (isGameFinished) {
            g2D.drawImage(gameResultImage, 300, 350, this);
            if (showImage && pressEnterImage != null) {
                g2D.drawImage(pressEnterImage, 300, 385, this);
            }
        }

    }

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

    @Override
    public void mouseMoved(MouseEvent e) {
        cursorRectangle.x = e.getPoint().x - cursorRectangle.width / 2;
        cursorRectangle.y = e.getPoint().y - cursorRectangle.height / 2;
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // 
    }

    public void addListener(GameListener pListener) {
        gameListener = pListener;
    }

    public void notifyGameStatus() {
        if (isGameFinished) {
            gameListener.gameIsFinished();
        }
    }

    public class GameThread implements Runnable {

        static final int burger_NUMBER = 10;
        private Thread thread;
        private int i;

        public GameThread() {
            ron = new ron();
            ronController = ronController.getIstance();
            burgerController = burgerController.getIstance();
            ronController.setron(ron);
            ronController.setPanel(GamePanel.this);
            burgerController.setPanel(GamePanel.this);
        }

        public void start() {
            reset();
            thread = new Thread(this);
            thread.start();
        }

        public void stop() {
            burgerController.theburgerIsGone(false);
            currentKetchupNumber = 0;
            imageBlinker();
            notifyGameStatus();
            if (thread != null && thread.isAlive()) {
                thread.interrupt();
            }
        }

        private void reset() {
            isGameFinished = false;
            burgerController.theburgerIsGone(false);
            isGameFinished = false;
            shotburgers = 0;
            i = 0;
        }

        @Override
        public void run() {
            while (!isGameFinished) {
                try {
                    ronController.getIntroAnimation().start();
                    while (i < burger_NUMBER) {
                        currentKetchupNumber = 3;
                        burger = new burger();
                        burgerController.setburger(burger);
                        burgerController.getburgerAnimation().start();
                        ronController.getAnimation().start(burgerController.isshot());
                        i++;
                    }
                    //set the variable for burgers to win
                    if (shotburgers >= 6) {
                        System.out.println("YOU WIN");
                        gameResultImage = Resources.getImage("/images/youWin.png");
                        repaint();
                    } else {
                        System.out.println("YOU LOSE");
                        gameResultImage = Resources.getImage("/images/gameover.png");
                        repaint();
                    }
                    isGameFinished = true;
                } catch (InterruptedException ex) {
                    System.out.println("an error occured during game thread execution");
                }
            }
            stop();
        }
    }
}
