package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class OmokServer {
    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(12345)) {
            System.out.println("Waiting for two players...");

            Socket p1 = server.accept();
            System.out.println("✅ Player 1 connected.");
            PrintWriter out1 = new PrintWriter(p1.getOutputStream(), true);
            BufferedReader in1 = new BufferedReader(new InputStreamReader(p1.getInputStream()));

            Socket p2 = server.accept();
            System.out.println("✅ Player 2 connected.");
            PrintWriter out2 = new PrintWriter(p2.getOutputStream(), true);
            BufferedReader in2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));

            GameSession session = new GameSession(in1, in2, out1, out2);
            new Thread(session).start();
        } catch (IOException e) {
            System.err.println("❗ 서버 실행 중 오류 발생: " + e.getMessage());
        }
    }
}