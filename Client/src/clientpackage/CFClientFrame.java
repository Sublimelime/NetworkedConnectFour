package clientpackage;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

public class CFClientFrame extends JFrame implements MouseListener {

    private int mode = 0;
    private int turn = 0;
    private CFClientGame game;
    private BufferedImage buffer;

    public static final int ONE_PLAYER = 1;
    public static final int TWO_PLAYER = 2;

    public CFClientFrame(int mode) {

        super("Connect Four Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.mode = mode;
        setSize(440, 400);
        buffer = new BufferedImage(440, 400, BufferedImage.TYPE_4BYTE_ABGR);
        game = new CFClientGame();
        this.mode = mode;
        turn = CFClientGame.RED;

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

                if (game.getSpot(r, c) == CFClientGame.RED) {
                    g.setColor(Color.RED);
                    g.fillOval(dc, dr, 40, 40);
                } else if (game.getSpot(r, c) == CFClientGame.BLACK) {
                    g.setColor(Color.BLACK);
                    g.fillOval(dc, dr, 40, 40);
                } else {
                    g.setColor(Color.WHITE);
                    g.fillOval(dc, dr, 40, 40);
                }
            }
        }

        if (game.status() == CFClientGame.RED_WINS) {
            g.setColor(Color.BLUE);
            g.setFont(new Font("Courier New", Font.BOLD, 25));
            g.drawString("RED WINS!", 20, 200);
            g.drawString("(RIGHT CLICK TO RESTART)", 20, 250);
        } else if (game.status() == CFClientGame.BLACK_WINS) {
            g.setColor(Color.BLUE);
            g.setFont(new Font("Courier New", Font.BOLD, 25));
            g.drawString("BLACK WINS!", 20, 200);
            g.drawString("(RIGHT CLICK TO RESTART)", 20, 250);

        } else if (game.status() == CFClientGame.DRAW) {
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

        if (game.status() == CFClientGame.PLAYING) {
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
                    if (turn == CFClientGame.RED) {
                        turn = CFClientGame.BLACK;
                    } else {
                        turn = CFClientGame.RED;
                    }
                }
            } else {
                if (game.status() == CFClientGame.PLAYING && changeTurns) {
                    for (int c = 0; c < 7; c++) {
                        CFClientGame clone = (CFClientGame) game.clone();
                        clone.dropPiece(c, CFClientGame.BLACK);
                        if (clone.status() == CFClientGame.BLACK_WINS) {
                            game.dropPiece(c, CFClientGame.BLACK);
                            repaint();
                            return;
                        }
                    }

                    for (int c = 0; c < 7; c++) {
                        CFClientGame clone = (CFClientGame) game.clone();
                        clone.dropPiece(c, CFClientGame.RED);
                        if (clone.status() == CFClientGame.RED_WINS) {
                            game.dropPiece(c, CFClientGame.BLACK);
                            repaint();
                            return;
                        }
                    }
                    while (game.dropPiece((int) (Math.random() * 7), CFClientGame.BLACK) == false);
                }
            }
        } else {
            if (e.getButton() == MouseEvent.BUTTON3) {
                turn = CFClientGame.RED;
                game = new CFClientGame();
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
