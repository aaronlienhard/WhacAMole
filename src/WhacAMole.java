import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class WhacAMole {
    int boardWidth = 600;
    int boardHeight = 650; // 50px for the text panel on top

    JFrame frame = new JFrame("Whac A Mole");
    JLabel scoreLabel = new JLabel();
    JLabel highscoreLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel resetPanel = new JPanel();

    JButton[] board = new JButton[9];
    ImageIcon moleIcon;
    ImageIcon enemyIcon;

    JButton currMoleTile;
    JButton currEnemyTile;
    JButton resetButton = new JButton("Reset Game");

    Random random = new Random();
    Timer setMoleTimer;
    Timer setEnemyTimer;

    int score = 0;
    int highScore = 0;

    WhacAMole() {
        // frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        scoreLabel.setFont(new Font("Times New Roman", Font.PLAIN, 50));
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        scoreLabel.setText("Score: 0");
        scoreLabel.setOpaque(true);

        highscoreLabel.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        highscoreLabel.setHorizontalAlignment(JLabel.CENTER);
        highscoreLabel.setText("High Score: 0");
        highscoreLabel.setOpaque(true);

        textPanel.setLayout(new GridLayout(0, 1));
        textPanel.add(scoreLabel);
        textPanel.add(highscoreLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        resetButton.setPreferredSize(new Dimension(60, 60));

        resetPanel.setLayout(new BorderLayout());
        resetPanel.add(resetButton);
        frame.add(resetPanel, BorderLayout.SOUTH);

        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                highScore = score;
                score = 0;
                scoreLabel.setText("Score: " + Integer.toString(score));

                for (int i = 0; i < 9; i++) {
                    board[i].setEnabled(true);
                }

                setMoleTimer.start();
                setEnemyTimer.start();
                resetButton.setEnabled(false);
            }
        });

        resetButton.setEnabled(false);

        boardPanel.setLayout(new GridLayout(3, 3));
        // boardPanel.setBackground(Color.black);
        frame.add(boardPanel);

        // enemyIcon = new ImageIcon(getClass().getResource("./piranha.png"));
        Image enemyImg = new ImageIcon(getClass().getResource("./blackFrost.png")).getImage();
        enemyIcon = new ImageIcon(enemyImg.getScaledInstance(150, 140, java.awt.Image.SCALE_SMOOTH));

        Image moleImg = new ImageIcon(getClass().getResource("./jackFrost.png")).getImage();
        moleIcon = new ImageIcon(moleImg.getScaledInstance(175, 135, java.awt.Image.SCALE_SMOOTH));
        
        for (int i = 0; i < 9; i++) {
            JButton tile = new JButton();
            board[i] = tile;
            boardPanel.add(tile);
            tile.setFocusable(false); // hides the square around the image when clicked

            tile.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JButton tile = (JButton) e.getSource();
                    if (tile == currMoleTile) {
                        score += 10;
                        scoreLabel.setText("Score: " + Integer.toString(score));
                    }
                    else if (tile == currEnemyTile) {
                        scoreLabel.setText("Game Over: " + Integer.toString(score));

                        if (highScore <= score) {
                            highscoreLabel.setText("High Score: " + Integer.toString(score));
                        }

                        resetButton.setEnabled(true);

                        // Stops the movement of the mole and enemy
                        setMoleTimer.stop();
                        setEnemyTimer.stop();

                        // Iterates through each button of the array and disables it
                        for (int i = 0; i < 9; i++) {
                            board[i].setEnabled(false);
                        }
                    }
                }
            });
        }

        setMoleTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Remove mole from current tile
                if (currMoleTile != null) {
                    currMoleTile.setIcon(null);
                    currMoleTile = null;
                }

                // Randomly select another tile
                int num = random.nextInt(9); // Gives random number up to 9 (0-8)
                JButton tile = board[num]; // Select random tile from array

                // If tile is occupied by enemy, skip tile for this turn
                if (currEnemyTile == tile) return;

                // Set tile to mole
                currMoleTile = tile;
                currMoleTile.setIcon(moleIcon);
            }
        });

        setEnemyTimer = new Timer(1500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Remove enemy from current tile
                if (currEnemyTile != null) {
                    currEnemyTile.setIcon(null);
                    currEnemyTile = null;
                }

                // Randomly select another tile
                int num = random.nextInt(9); // Gives random number up to 9 (0-8)
                JButton tile = board[num]; // Select random tile from array

                // If tile is occupied by mole, skip tile for this turn
                if (currMoleTile == tile) return;

                // Set tile to enemy
                currEnemyTile = tile;
                currEnemyTile.setIcon(enemyIcon);
            }
        });

        setMoleTimer.start();
        setEnemyTimer.start();
        frame.setVisible(true); // by setting at very end, everything loads before the window is visible
    }
}