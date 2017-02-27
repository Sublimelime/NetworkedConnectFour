package clientpackage;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import javax.swing.JFrame;

public class CFClientFrame extends JFrame implements MouseListener, Runnable {

    private int mode = 0, turn = 0, lastMove = -1;
    private CFClientGame game;
    private BufferedImage buffer;

    public static final int ONE_PLAYER = 1;
    public static final int TWO_PLAYER = 2;
    ObjectInputStream inputStream1;
    ObjectOutputStream outputStream1;
    Socket socket;

    public CFClientFrame(int mode) {

        super("Connect Four Game - Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.mode = mode;
        setSize(440, 400);
        buffer = new BufferedImage(440, 400, BufferedImage.TYPE_4BYTE_ABGR);
        game = new CFClientGame();
        this.mode = mode;
        turn = CFClientGame.RED;

        addMouseListener(this);
        setVisible(true);

        try {
            //setup connection 1
            socket = new Socket("127.0.0.1", 8765);
            outputStream1 = new ObjectOutputStream(socket.getOutputStream());
            inputStream1 = new ObjectInputStream(socket.getInputStream());

            System.out.println("Setup streams to server.");

        } catch (IOException e) {
            System.err.println("Unable to create connection:" + e.getMessage());
        }

        //ping server
        try {
            System.out.println("Got ping from server: " + inputStream1.readObject());
            outputStream1.writeObject(new String("Polo")); //send it back polo
        } catch (IOException e) {
            System.err.println("Unable to ping server: " + e.getMessage());
        } catch (ClassNotFoundException c) {
            System.err.println("Misunderstood data from server: " + c.getMessage());
        }

        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        //flips back and forth between recieving and sending client info.

        while (true) {

            try {
                System.out.println("Waiting for message from server...");

                System.out.println(inputStream1.readObject().toString()); //print out the received message
                while (lastMove == -1) { //wait for the player to make a move
                    Thread.sleep(10);
                }
                outputStream1.writeInt(lastMove);
                lastMove = -1; //invalidate the var again

            } catch (IOException e) {
                System.err.println("Failed to send or receive message to/from server." + e.getMessage());
            } catch (ClassNotFoundException ignored) {
            } catch (InterruptedException i) {
                System.err.println("Cannot sleep thread. " + i.getMessage());
            }

            repaint();
        }

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
        if (game.status() == CFClientGame.PLAYING) {
            if (x >= 20 && x < 59) {
                game.dropPiece(0, turn);
                lastMove = 0;
            } else if (x >= 80 && x < 129) {
                game.dropPiece(1, turn);
                lastMove = 1;
            } else if (x >= 140 && x < 179) {
                game.dropPiece(2, turn);
                lastMove = 2;
            } else if (x >= 200 && x < 239) {
                game.dropPiece(3, turn);
                lastMove = 3;
            } else if (x >= 260 && x < 299) {
                game.dropPiece(4, turn);
                lastMove = 4;
            } else if (x >= 320 && x < 359) {
                game.dropPiece(5, turn);
                lastMove = 5;
            } else if (x >= 380 && x < 419) {
                game.dropPiece(6, turn);
                lastMove = 6;
            } else {
                return;
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
