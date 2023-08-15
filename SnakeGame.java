import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
public class SnakeGame {
    public static void main(String[] args) {
        new GameFrame();
    }
}

class GamePanel extends JPanel implements ActionListener {

        static final int SCREEN_WIDTH = 800;
        static final int SCREEN_HEIGHT = 600;
        static final int UNIT_SIZE = 25;
        static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
        static final int DELAY = 75;

        final int x[] = new int[GAME_UNITS];
        final int y[] = new int[GAME_UNITS];
        int bodyParts = 6;
        int appleEaten;
        int appleX;
        int appleY;
        int walls;
        final int wallX[] = new int[GAME_UNITS];
        final int wallY[] = new int[GAME_UNITS];
        char direction = 'R';
        boolean running = false;
        Timer timer;
        Random random;

        GamePanel() {
            random = new Random();
            this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
            this.setBackground(Color.BLACK);

            this.setFocusable(true);
            this.addKeyListener(new MyKeyAdapter());
            startGame();
        }

        public void startGame() {
            newApple();
            addWalls();
            running = true;
            timer = new Timer(DELAY, this);
            timer.start();
            this.requestFocusInWindow();
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            draw(g);
        }

        public void draw(Graphics g) {
            if (running) {
                for (int i = 0; i < walls; i++) {
                    g.setColor(Color.YELLOW);
                    g.fillRect(wallX[i], wallY[i], UNIT_SIZE, UNIT_SIZE);
                }

                g.setColor(Color.RED);
                g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

                for (int i = 0; i < bodyParts; i++) {
                    if (i == 0) {
                        g.setColor(Color.green);
                        g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    } else {
                        g.setColor(Color.green);
                        g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    }
                }

                g.setColor(Color.red);
                g.setFont(new Font("Times New Roman", Font.BOLD, 40));
                FontMetrics metrics = getFontMetrics(g.getFont());
                g.drawString("SCORE: " + appleEaten, (SCREEN_WIDTH - metrics.stringWidth("SCORE " + appleEaten)) / 2, g.getFont().getSize());

            } else {
                gameOver(g);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Times New Roman", Font.BOLD, 30));
                FontMetrics metrics = getFontMetrics(g.getFont());
                //  g.drawString(" 'Enter' for FREE lifeline", (SCREEN_WIDTH - metrics.stringWidth("Press Enter to Restart")) / 2, SCREEN_HEIGHT / 2 + 50);
            }
        }

        public void newApple() {
            boolean appleOnWall;
            do {
                appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
                appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
                appleOnWall = false;
                for (int i = 0; i < walls; i++) {
                    if (wallX[i] == appleX && wallY[i] == appleY) {
                        appleOnWall = true;
                        break;
                    }
                }
            } while (appleOnWall);
        }

        public void addWalls() {
            walls = random.nextInt(10) + 1; // Number of walls (1 to 10)
            for (int i = 0; i < walls; i++) {
                wallX[i] = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
                wallY[i] = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
            }
        }

        public void move() {
            for (int i = bodyParts; i > 0; i--) {
                x[i] = x[i - 1];
                y[i] = y[i - 1];
            }

            switch (direction) {
                case 'U':
                    y[0] = y[0] - UNIT_SIZE;
                    break;
                case 'D':
                    y[0] = y[0] + UNIT_SIZE;
                    break;
                case 'L':
                    x[0] = x[0] - UNIT_SIZE;
                    break;
                case 'R':
                    x[0] = x[0] + UNIT_SIZE;
                    break;
            }
        }

        public void checkApple() {
            if ((x[0] == appleX) && (y[0] == appleY)) {
                bodyParts++;
                appleEaten++;
                newApple();
            }
        }

        public void checkCollisions() {
            // checks if head touches collides with body
            for (int i = bodyParts; i > 0; i--) {
                if ((x[0] == x[i]) && (y[0] == y[i])) {
                    running = false;
                }
            }

            // checks if head touches collides with left Border
            if (x[0] < 0) {
                running = false;
            }

            // checks if head touches collides with right border
            if (x[0] > SCREEN_WIDTH) {
                running = false;
            }

            // checks if head touches collides with top Border
            if (y[0] < 0) {
                running = false;
            }

            // checks if head touches collides with bottom Border
            if (y[0] > SCREEN_HEIGHT) {
                running = false;
            }

            // Check if snake touches any of the walls
            for (int i = 0; i < walls; i++) {
                if (x[0] == wallX[i] && y[0] == wallY[i]) {
                    running = false;
                }
            }

            if (!running) {
                timer.stop();
            }
        }

        public void gameOver(Graphics g) {
            g.setColor(Color.GREEN);
            g.setFont(new Font("Times New Roman", Font.BOLD, 40));
            FontMetrics metrics1 = getFontMetrics(g.getFont());
            g.drawString("SCORE : " + appleEaten, (SCREEN_WIDTH - metrics1.stringWidth("SCORE" + appleEaten)) / 2, g.getFont().getSize());

            //Game Over Text
            g.setColor(Color.CYAN);
            g.setFont(new Font("Times New Roman", Font.BOLD, 50));
            FontMetrics metrics2 = getFontMetrics(g.getFont());
            g.drawString("BETTER LUCK NEXT TIME", (SCREEN_WIDTH - metrics2.stringWidth("BETTER LUCK NEXT TIME")) / 2, SCREEN_HEIGHT / 2);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (running) {
                move();
                checkApple();
                checkCollisions();
            }
            repaint();
        }
        // for inputs from key board
        public class MyKeyAdapter extends KeyAdapter {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        if (direction != 'R') {
                            direction = 'L';
                        }
                        break;

                    case KeyEvent.VK_RIGHT:
                        if (direction != 'L') {
                            direction = 'R';
                        }
                        break;

                    case KeyEvent.VK_UP:
                        if (direction != 'D') {
                            direction = 'U';
                        }
                        break;

                    case KeyEvent.VK_DOWN:
                        if (direction != 'U') {
                            direction = 'D';
                        }
                        break;

                }
            }
        }
    }



class GameFrame extends JFrame {
    GameFrame() {
        this.add(new GamePanel());
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
