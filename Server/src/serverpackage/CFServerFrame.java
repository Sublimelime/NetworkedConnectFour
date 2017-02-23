package serverpackage;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;

public class CFServerFrame extends JFrame implements MouseListener {

    private int mode = 0, turn = 0;
    private CFServerGame game;
    private BufferedImage buffer;

    protected static final int ONE_PLAYER = 1, TWO_PLAYER = 2;

    private ServerSocket serverSocket1;
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
            System.out.println("Waiting for client 1.");
            socket1 = serverSocket1.accept();
            outputStream1 = new ObjectOutputStream(socket1.getOutputStream());
            inputStream1 = new ObjectInputStream(socket1.getInputStream());

            System.out.println("Setup streams for client 1.");

            //setup connection 2
            System.out.println("Waiting for client 2.");
            socket2 = serverSocket1.accept();
            outputStream2 = new ObjectOutputStream(socket2.getOutputStream());
            inputStream2 = new ObjectInputStream(socket2.getInputStream());

            System.out.println("Setup streams for client 2.");
        } catch (IOException e) {
            System.err.println("Unable to create connection:" + e.getMessage());
        }

        //ping both clients
        try {
            outputStream1.writeObject(new String("Marco"));
            System.out.println("Got ping back from client 1: " + inputStream1.readObject());
        } catch (IOException e) {
            System.err.println("Unable to ping client 1: " + e.getMessage());
        } catch (ClassNotFoundException c) {
            System.out.println("Misunderstood data from Client 1: " + c.getMessage());
        }

        try {
            outputStream2.writeObject(new String("Marco"));
            System.out.println("Got ping back from client 2: " + inputStream2.readObject());
        } catch (IOException e) {
            System.err.println("Unable to ping client 2: " + e.getMessage());
        } catch (ClassNotFoundException c) {
            System.out.println("Misunderstood data from Client 2: " + c.getMessage());
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
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
        if (e.getButton() == MouseEvent.BUTTON3) {
            turn = CFServerGame.RED;
            game = new CFServerGame();
        }
        repaint();
    }
}
