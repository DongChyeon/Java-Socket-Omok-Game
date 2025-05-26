package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class OmokServer {
    public static void main(String[] args) {
        ServerSocket server = null;
        Socket p1 = null, p2 = null;
        BufferedReader in1 = null, in2 = null;
        PrintWriter out1 = null, out2 = null;

        try {
            server = new ServerSocket(12345);
            System.out.println("Waiting for two players...");

            p1 = server.accept();
            System.out.println("✅ Player 1 connected.");
            out1 = new PrintWriter(p1.getOutputStream(), true);
            in1 = new BufferedReader(new InputStreamReader(p1.getInputStream()));
            out1.println("1"); // 흑

            p2 = server.accept();
            System.out.println("✅ Player 2 connected.");
            out2 = new PrintWriter(p2.getOutputStream(), true);
            in2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));
            out2.println("2"); // 백

            out1.println("READY");
            out2.println("READY");

            BufferedReader finalIn1 = in1;
            PrintWriter finalOut2 = out2;
            Thread t1 = new Thread(() -> relay(finalIn1, finalOut2));
            BufferedReader finalIn2 = in2;
            PrintWriter finalOut1 = out1;
            Thread t2 = new Thread(() -> relay(finalIn2, finalOut1));
            t1.start();
            t2.start();

            t1.join();
            t2.join();
        } catch (IOException | InterruptedException e) {
            System.err.println("❗ 서버 실행 중 오류 발생: " + e.getMessage());
        } finally {
            try {
                if (in1 != null) in1.close();
                if (in2 != null) in2.close();
                if (out1 != null) out1.close();
                if (out2 != null) out2.close();
                if (p1 != null) p1.close();
                if (p2 != null) p2.close();
                if (server != null) server.close();
                System.out.println("🔒 Server resources closed.");
            } catch (IOException e) {
                System.err.println("❗ 자원 해제 중 오류 발생: " + e.getMessage());
            }
        }
    }

    private static void relay(BufferedReader in, PrintWriter out) {
        try {
            String msg;
            while ((msg = in.readLine()) != null) {
                out.println(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}