package client;

import model.Message;
import ui.OmokBoardPanel;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class OmokClient {
    public static void main(String[] args) {
        System.out.println("🔥 OmokClient 직접 실행됨!");
        try {
            new OmokClient("localhost");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private final OmokBoardPanel boardPanel;
    private final int myColor;

    public OmokClient(String host) throws Exception {
        System.out.println("🚀 클라이언트: 서버에 연결 시도 중...");
        socket = new Socket(host, 12345);
        System.out.println("✅ 클라이언트: 서버에 연결 성공!");
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        myColor = Integer.parseInt(in.readLine());
        boardPanel = new OmokBoardPanel(this, myColor);

        JFrame frame = new JFrame("Omok Game - " + (myColor == 1 ? "Black" : "White"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(boardPanel);
        frame.pack();
        frame.setVisible(true);

        new Thread(this::listen).start();
    }

    private void listen() {
        try {
            String msg;
            while ((msg = in.readLine()) != null) {
                if (msg.equals(Message.READY)) {
                    boardPanel.setReady();
                } else if (msg.equals(Message.WIN)) {
                    JOptionPane.showMessageDialog(null, "You Win!");
                    System.exit(0);
                } else if (msg.equals(Message.LOSE)) {
                    JOptionPane.showMessageDialog(null, "You Lose!");
                    System.exit(0);
                } else if (Message.isMove(msg)) {
                    int[] pos = Message.parseMove(msg);
                    int opponentColor = (myColor == 1) ? 2 : 1;
                    boardPanel.applyOpponentMove(pos[0], pos[1], opponentColor);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMove(int x, int y) {
        out.println(Message.move(x, y));
    }
}
