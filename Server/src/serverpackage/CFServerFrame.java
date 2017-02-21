package serverpackage;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;

public class CFServerFrame extends JFrame {

    private int mode = 0, turn = 0;
    private CFServerGame game;
    private BufferedImage buffer;

    protected static final int ONE_PLAYER = 1, TWO_PLAYER = 2;

    private ServerSocket serverSocket1, serverSocket2;
    private ObjectOutputStream outputStream1, outputStream2;
    private ObjectInputStream inputStream1, inputStream2;
    private Socket socket1, socket2;

    public CFServerFrame(int mode) {

        super("Connect Four Game - Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.mode = mode;
        setSize(440, 400);
        buffer = new BufferedImage(440, 400, BufferedImage.TYPE_4BYTE_ABGR);
        game = new CFServerGame();
        this.mode = mode;
        turn = CFServerGame.RED;

        setVisible(true);

        //create the socket connection for the first client
        try {
            serverSocket1 = new ServerSocket(8765);

            //setup connection 1
            socket1 = serverSocket1.accept();
            outputStream1 = new ObjectOutputStream(socket1.getOutputStream());
            inputStream1 = new ObjectInputStream(socket1.getInputStream());

            //setup connection 2
            socket2 = serverSocket2.accept();
            outputStream2 = new ObjectOutputStream(socket2.getOutputStream());
            inputStream2 = new ObjectInputStream(socket2.getInputStream());
        } catch (IOException e) {
            System.err.println("Unable to create connection:" + e.getMessage());
        }

    }

    @Override
    public void paint(Graphics rg) {
        // draws background to the buffer
        Graphics g = buffer.getGraphics();
        g.setColor(Color.YELLOW);
        g.fillRect(0, 0, 440, 400);

        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 7; c++) {
                int dr = r * 60 + 40;
                int dc = c * 60 + 20;

                switch (game.getSpot(r, c)) {
                    case CFServerGame.RED:
                        g.setColor(Color.RED);
                        g.fillOval(dc, dr, 40, 40);
                        break;
                    case CFServerGame.BLACK:
                        g.setColor(Color.BLACK);
                        g.fillOval(dc, dr, 40, 40);
                        break;
                    default:
                        g.setColor(Color.WHITE);
                        g.fillOval(dc, dr, 40, 40);
                        break;
                }
            }
        }

        switch (game.status()) {
            case CFServerGame.RED_WINS:
                g.setColor(Color.BLUE);
                g.setFont(new Font("Courier New", Font.BOLD, 25));
                g.drawString("RED WINS!", 20, 200);
                g.drawString("(RIGHT CLICK TO RESTART)", 20, 250);
                break;
            case CFServerGame.BLACK_WINS:
                g.setColor(Color.BLUE);
                g.setFont(new Font("Courier New", Font.BOLD, 25));
                g.drawString("BLACK WINS!", 20, 200);
                g.drawString("(RIGHT CLICK TO RESTART)", 20, 250);
                break;
            case CFServerGame.DRAW:
                g.setColor(Color.BLUE);
                g.setFont(new Font("Courier New", Font.BOLD, 25));
                g.drawString("TIE GAME.", 20, 200);
                g.drawString("(RIGHT CLICK TO RESTART)", 20, 250);
                break;
            default:
                break;
        }

        rg.drawImage(buffer, 0, 0, null);
    }

    public void mouseReleased(MouseEvent e) {

        int x = e.getX();
        boolean changeTurns;

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

}
