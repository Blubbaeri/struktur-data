package snakegame;

import objects.Apple;
import objects.Snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Point;

public class SnakeGame extends JPanel implements ActionListener {
    public static final int SCALE = 40;
    public static final int WIDTH = 30;
    public static final int HEIGHT = 20;
    public static int SPEED = 5;

    //Score & Speed
    private int currentScore = 0;
    private final int POINTS_PER_APPLE = 10;
    private final int POINTS_SPEED_INCREASE = 100;       //Batas poin waktu speed meningkat
    private final int SPEED_INCREASE_AMOUNT = 3;        //Banyak nambah speed
    private int nextSpeed = POINTS_SPEED_INCREASE;      //Batas poin dimana speed meningkat


    Apple a = new Apple(
            (int) (Math.random() * WIDTH),
            (int) (Math.random() * HEIGHT),
            WIDTH, HEIGHT
    );

    Snake s = new Snake(10, 10, 9, 10, WIDTH, HEIGHT);
    Timer t = new Timer(1000 / SPEED, this);

    Image headImg = new ImageIcon(getClass().getResource("/Resource/kgkMangap.png")).getImage();
    Image appleImg = new ImageIcon(getClass().getResource("/Resource/mieAyam.png")).getImage();
    Image bodyImg = new ImageIcon(getClass().getResource("/Resource/mangap.png")).getImage();
    Image bgImage = new ImageIcon(getClass().getResource("/Resource/bg.png")).getImage();


    boolean inGame = false;
    boolean paused = false;
    boolean gameOver = false;
    boolean showLeaderboard = false;

    JButton playButton, quitButton, resumeButton, quitPauseButton, playAgainButton,backToMenuButton, leaderboardButton;


    public SnakeGame() {
        setLayout(null);
        setDoubleBuffered(true);
        setFocusable(true);
        requestFocusInWindow();

        playButton = new JButton("Play");
        quitButton = new JButton("Quit");
        resumeButton = new JButton("Resume");
        quitPauseButton = new JButton("Quit");
        playAgainButton = new JButton("Play Again");
        backToMenuButton = new JButton("Back to Menu");
        leaderboardButton = new JButton("Leaderboard");

        add(playButton);
        add(quitButton);
        add(resumeButton);
        add(quitPauseButton);
        add(playAgainButton);
        add(backToMenuButton);
        add(leaderboardButton);

        playButton.setSize(120, 40);
        quitButton.setSize(120, 40);
        resumeButton.setSize(120, 40);
        quitPauseButton.setSize(120, 40);
        playAgainButton.setSize(140, 40);
        backToMenuButton.setSize(140, 40);
        leaderboardButton.setSize(120, 40);

        resumeButton.setVisible(false);
        quitPauseButton.setVisible(false);
        playAgainButton.setVisible(false);
        backToMenuButton.setVisible(false);

        playButton.addActionListener(e -> {
            startGame();
            requestFocusInWindow();
        });

        backToMenuButton.addActionListener(e -> {
            // Balik ke menu awal
            playButton.setVisible(true);
            quitButton.setVisible(true);
            leaderboardButton.setVisible(true);
            playAgainButton.setVisible(false);
            backToMenuButton.setVisible(false);
            gameOver = false;
            showLeaderboard = false;

            // Reset snake dan apple
            s = new Snake(10, 10, 9, 10, WIDTH, HEIGHT);
            a = new Apple((int)(Math.random() * WIDTH), (int)(Math.random() * HEIGHT), WIDTH, HEIGHT);

            repaint();
        });

        quitButton.addActionListener(e -> System.exit(0));
        resumeButton.addActionListener(e -> resumeGame());
        quitPauseButton.addActionListener(e -> System.exit(0));
        playAgainButton.addActionListener(e -> {
            resetGame();
            requestFocusInWindow();
        });

        leaderboardButton.addActionListener(e -> {
            showLeaderboard = true;
            inGame = false;
            paused = false;
            gameOver = false;

            playButton.setVisible(false);
            quitButton.setVisible(false);
            leaderboardButton.setVisible(false);

            backToMenuButton.setVisible(false);

            repaint();
        });

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE && inGame) {
                    if (!paused) pauseGame();
                    else resumeGame();
                }
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                repositionButtons();
            }
        });


    }

    //Update speed
    private void updateGameSpeed() {
        t.stop();
        t = new Timer(1000 / SPEED, this);
        if (inGame && !paused) {
            t.start();
        }
    }

    private void resetGame() {
        s = new Snake(10, 10, 9, 10, WIDTH, HEIGHT);
        a = new Apple((int)(Math.random() * WIDTH), (int)(Math.random() * HEIGHT), WIDTH, HEIGHT);
        inGame = true;
        paused = false;
        gameOver = false;

        currentScore = 0;
        SPEED = 5;
        nextSpeed = POINTS_SPEED_INCREASE;

        updateGameSpeed();

        //t.start();
        playAgainButton.setVisible(false);
        backToMenuButton.setVisible(false);
        repaint();
    }



    private void repositionButtons() {
        int centerX = getWidth() / 2 - 60;
        int centerY = getHeight() / 2 - 20;
        playButton.setLocation(centerX, centerY - 30);
        leaderboardButton.setLocation(centerX, centerY + 30);
        quitButton.setLocation(centerX, centerY + 90);
        resumeButton.setLocation(centerX, centerY - 30);
        quitPauseButton.setLocation(centerX, centerY + 30);
        playAgainButton.setLocation(centerX, centerY);
        backToMenuButton.setLocation(centerX, centerY + 50);
    }

    private void startGame() {
        inGame = true;
        playButton.setVisible(false);
        quitButton.setVisible(false);
        leaderboardButton.setVisible(false);
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new Keyboard());
        t.start();
        repaint();
    }

    private void pauseGame() {
        paused = true;
        t.stop();
        resumeButton.setVisible(true);
        quitPauseButton.setVisible(true);
        repaint();
    }

    private void resumeGame() {
        paused = false;
        t.start();
        resumeButton.setVisible(false);
        quitPauseButton.setVisible(false);
        requestFocusInWindow();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //Hitung offset untuk center dulu
        int offsetX = (getWidth() - WIDTH * SCALE) / 2;
        int offsetY = (getHeight() - HEIGHT * SCALE) / 2;

// ========== Background Image ==========
        int imgWidth = bgImage.getWidth(this);
        int imgHeight = bgImage.getHeight(this);
        double imgRatio = (double) imgWidth / imgHeight;
        double panelRatio = (double) getWidth() / getHeight();

        int drawWidth, drawHeight;

// Fit image ke panel, tetap proporsional
        if (panelRatio < imgRatio) {
            drawHeight = getHeight();
            drawWidth = (int) (imgRatio * drawHeight);
        } else {
            drawWidth = getWidth();
            drawHeight = (int) (drawWidth / imgRatio);
        }


// Gambar ditaruh di tengah
        int drawX = (getWidth() - drawWidth) / 2;
        int drawY = (getHeight() - drawHeight) / 2;

        g.drawImage(bgImage, drawX, drawY, drawWidth, drawHeight, this);
// ========== End Background ==========

        if (!inGame) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Consolas", Font.BOLD, 30));
            g.drawString("Snake Game", getWidth() / 2 - 100, getHeight() / 2 - 100);

            return;
        }

        // Grid
        g.setColor(new Color(60, 60, 60));
        for (int x = 0; x <= WIDTH * SCALE; x += SCALE)
            g.drawLine(offsetX + x, offsetY, offsetX + x, offsetY + HEIGHT * SCALE);
        for (int y = 0; y <= HEIGHT * SCALE; y += SCALE)
            g.drawLine(offsetX, offsetY + y, offsetX + WIDTH * SCALE, offsetY + y);

        // Snake
        int i = 0;
        for (Point p : s.body) {
            Image img = (i == s.getLength() - 1) ? headImg : bodyImg;
            g.drawImage(img, offsetX + p.x * SCALE, offsetY + p.y * SCALE, SCALE, SCALE, this);
            i++;
        }

        // Apple
        g.drawImage(appleImg, offsetX + a.posX * SCALE, offsetY + a.posY * SCALE, SCALE, SCALE, this);

        // Score (bisa digeser ke pojok atas dalam area grid kalau mau)
        g.setColor(Color.WHITE);
        g.setFont(new Font("Consolas", Font.BOLD, 20));
        g.drawString("Score: " + currentScore, offsetX + 10, offsetY + 25);

        // Paused
        if (paused) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.WHITE);
            g.setFont(new Font("Consolas", Font.BOLD, 40));
            g.drawString("Paused", getWidth() / 2 - 80, getHeight() / 2 - 100);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame && !paused) {
            Point nextHead = s.getNextHead(); // ⬅️ Kamu harus bikin method ini di class Snake
            boolean eat = nextHead.x == a.posX && nextHead.y == a.posY;

            // ⛔ Game over kalau nabrak tembok
            if (nextHead.x < 0 || nextHead.x >= WIDTH || nextHead.y < 0 || nextHead.y >= HEIGHT) {
                inGame = false;

                //int finalScore = currentScore;   //Buat leaderboard

                t.stop();
                playAgainButton.setVisible(true);
                backToMenuButton.setVisible(true);
                repaint();
                return;
            }

            s.move(eat);

            if (eat) {
                a.setRandomPosition();
                currentScore += POINTS_PER_APPLE;

                if (currentScore >= nextSpeed) {
                    SPEED += SPEED_INCREASE_AMOUNT; // Tingkatkan kecepatan
                    nextSpeed += POINTS_SPEED_INCREASE; // Atur target skor berikutnya
                    updateGameSpeed(); // Perbarui timer dengan kecepatan baru
                    System.out.println("Speed increased to: " + SPEED + ", Next speed increase at score: " + nextSpeed); // Debugging
                }
            }
            repaint();
        }
    }


    private class Keyboard extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (paused) return;
            if (key == KeyEvent.VK_RIGHT && s.direction != 2) s.direction = 0;
            if (key == KeyEvent.VK_DOWN && s.direction != 3) s.direction = 1;
            if (key == KeyEvent.VK_LEFT && s.direction != 0) s.direction = 2;
            if (key == KeyEvent.VK_UP && s.direction != 1) s.direction = 3;
        }
    }

    public static void main(String[] args) {
        new IntroPlayer("Resource/Intro.mp4", () -> {
            // Setelah video selesai, baru tampilkan game
            JFrame frame = new JFrame();
            SnakeGame panel = new SnakeGame();
            frame.setUndecorated(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(panel);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            gd.setFullScreenWindow(frame);

            panel.requestFocusInWindow();
        });
    }

}
