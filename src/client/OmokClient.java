package client;

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

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private OmokBoardPanel boardPanel;
    private int myColor;

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
                if (msg.equals("READY")) {
                    boardPanel.setReady();
                } else if (msg.equals("WIN")) {
                    JOptionPane.showMessageDialog(null, "You Win!");
                    System.exit(0);
                } else if (msg.equals("LOSE")) {
                    JOptionPane.showMessageDialog(null, "You Lose!");
                    System.exit(0);
                } else {
                    String[] parts = msg.split(",");
                    int x = Integer.parseInt(parts[0]);
                    int y = Integer.parseInt(parts[1]);
                    int opponentColor = (myColor == 1) ? 2 : 1;
                    boardPanel.applyOpponentMove(x, y, opponentColor);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMove(int x, int y) {
        out.println(x + "," + y);
    }
}
