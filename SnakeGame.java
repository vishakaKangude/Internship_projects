import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SnakeGame extends JPanel implements ActionListener {
    private final int WIDTH = 600;
    private final int HEIGHT = 400;
    private final int SIZE = 20;
    private final int DELAY = 100;

    private ArrayList<Point> snake;
    private Point food;
    private char direction;
    private boolean running;
    private Timer timer;

    public SnakeGame() {
        snake = new ArrayList<>();
        snake.add(new Point(5, 5));
        direction = 'R';
        running = true;
        generateFood();

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        if (direction != 'D') direction = 'U';
                        break;
                    case KeyEvent.VK_DOWN:
                        if (direction != 'U') direction = 'D';
                        break;
                    case KeyEvent.VK_LEFT:
                        if (direction != 'R') direction = 'L';
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (direction != 'L') direction = 'R';
                        break;
                }
            }
        });

        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void generateFood() {
        Random random = new Random();
        int x = random.nextInt(WIDTH / SIZE);
        int y = random.nextInt(HEIGHT / SIZE);
        food = new Point(x, y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);
        g.fillRect(food.x * SIZE, food.y * SIZE, SIZE, SIZE);

        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * SIZE, p.y * SIZE, SIZE, SIZE);
        }

        if (!running) {
            showGameOver(g);
        }
    }

    private void showGameOver(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString("Game Over", WIDTH / 2 - 30, HEIGHT / 2);
        g.drawString("Press R to Restart", WIDTH / 2 - 70, HEIGHT / 2 + 20);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkCollision();
            checkFood();
        }
        repaint();
    }

    private void move() {
        Point head = snake.get(0);
        Point newHead = new Point(head);

        switch (direction) {
            case 'U':
                newHead.translate(0, -1);
                break;
            case 'D':
                newHead.translate(0, 1);
                break;
            case 'L':
                newHead.translate(-1, 0);
                break;
            case 'R':
                newHead.translate(1, 0);
                break;
        }

        snake.add(0, newHead);
        snake.remove(snake.size() - 1);
    }

    private void checkCollision() {
        Point head = snake.get(0);

        // Check wall collision
        if (head.x < 0 || head.x >= WIDTH / SIZE || head.y < 0 || head.y >= HEIGHT / SIZE) {
            running = false;
        }

        // Check self-collision
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                running = false;
                break;
            }
        }
    }

    private void checkFood() {
        Point head = snake.get(0);
        if (head.equals(food)) {
            snake.add(0, food);
            generateFood();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        game.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!game.running && e.getKeyCode() == KeyEvent.VK_R) {
                    game.snake.clear();
                    game.snake.add(new Point(5, 5));
                    game.direction = 'R';
                    game.running = true;
                    game.generateFood();
                    game.timer.start();
                }
            }
        });
    }
}
