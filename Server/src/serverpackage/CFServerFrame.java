package serverpackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class CFServerFrame extends JFrame implements MouseListener {

    private int mode = 0;
    private int turn = 0;
    private CFServerGame game;
    private BufferedImage buffer;

    public static final int ONE_PLAYER = 1;
    public static final int TWO_PLAYER = 2;

    public CFServerFrame(int mode) {

        super("Connect Four Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.mode = mode;
        setSize(440, 400);
        buffer = new BufferedImage(440, 400, BufferedImage.TYPE_4BYTE_ABGR);
        game = new CFServerGame();
        this.mode = mode;
        turn = CFServerGame.RED;

        addMouseListener(this);
        setVisible(true);
    }

    public void paint(Graphics rg) {
        // draws background to the buffer
        Graphics g = buffer.getGraphics();
        g.setColor(Color.YELLOW);
        g.fillRect(0, 0, 440, 400);

        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 7; c++) {
                int dr = r * 60 + 40;
                int dc = c * 60 + 20;

                if (game.getSpot(r, c) == CFServerGame.RED) {
                    g.setColor(Color.RED);
                    g.fillOval(dc, dr, 40, 40);
                } else if (game.getSpot(r, c) == CFServerGame.BLACK) {
                    g.setColor(Color.BLACK);
                    g.fillOval(dc, dr, 40, 40);
                } else {
                    g.setColor(Color.WHITE);
                    g.fillOval(dc, dr, 40, 40);
                }
            }
        }

        if (game.status() == CFServerGame.RED_WINS) {
            g.setColor(Color.BLUE);
            g.setFont(new Font("Courier New", Font.BOLD, 25));
            g.drawString("RED WINS!", 20, 200);
            g.drawString("(RIGHT CLICK TO RESTART)", 20, 250);
        } else if (game.status() == CFServerGame.BLACK_WINS) {
            g.setColor(Color.BLUE);
            g.setFont(new Font("Courier New", Font.BOLD, 25));
            g.drawString("BLACK WINS!", 20, 200);
            g.drawString("(RIGHT CLICK TO RESTART)", 20, 250);

        } else if (game.status() == CFServerGame.DRAW) {
            g.setColor(Color.BLUE);
            g.setFont(new Font("Courier New", Font.BOLD, 25));
            g.drawString("TIE GAME.", 20, 200);
            g.drawString("(RIGHT CLICK TO RESTART)", 20, 250);

        }

        rg.drawImage(buffer, 0, 0, null);
    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

        int x = e.getX();
        boolean changeTurns = false;

        if (game.status() == CFServerGame.PLAYING) {
            if (x >= 20 && x < 59) {
                changeTurns = game.dropPiece(0, turn);
            } else if (x >= 80 && x < 129) {
                changeTurns = game.dropPiece(1, turn);
            } else if (x >= 140 && x < 179) {
                changeTurns = game.dropPiece(2, turn);
            } else if (x >= 200 && x < 239) {
                changeTurns = game.dropPiece(3, turn);
            } else if (x >= 260 && x < 299) {
                changeTurns = game.dropPiece(4, turn);
            } else if (x >= 320 && x < 359) {
                changeTurns = game.dropPiece(5, turn);
            } else if (x >= 380 && x < 419) {
                changeTurns = game.dropPiece(6, turn);
            } else {
                return;
            }

            if (mode == TWO_PLAYER) {
                if (changeTurns == true) {
                    if (turn == CFServerGame.RED) {
                        turn = CFServerGame.BLACK;
                    } else {
                        turn = CFServerGame.RED;
                    }
                }
            } else {
                if (game.status() == CFServerGame.PLAYING && changeTurns) {
                    for (int c = 0; c < 7; c++) {
                        CFServerGame clone = (CFServerGame) game.clone();
                        clone.dropPiece(c, CFServerGame.BLACK);
                        if (clone.status() == CFServerGame.BLACK_WINS) {
                            game.dropPiece(c, CFServerGame.BLACK);
                            repaint();
                            return;
                        }
                    }

                    for (int c = 0; c < 7; c++) {
                        CFServerGame clone = (CFServerGame) game.clone();
                        clone.dropPiece(c, CFServerGame.RED);
                        if (clone.status() == CFServerGame.RED_WINS) {
                            game.dropPiece(c, CFServerGame.BLACK);
                            repaint();
                            return;
                        }
                    }
                    while (game.dropPiece((int) (Math.random() * 7), CFServerGame.BLACK) == false);
                }
            }
        } else {
            if (e.getButton() == MouseEvent.BUTTON3) {
                turn = CFServerGame.RED;
                game = new CFServerGame();
            }
        }

        repaint();
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }
}
