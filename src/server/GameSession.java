package server;

import logic.OmokGameLogic;
import model.Message;
import model.OmokConstants;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;

public class GameSession implements Runnable {
    private final PrintWriter out1, out2;
    private final BufferedReader in1, in2;
    private final int[][] board = new int[OmokConstants.BOARD_SIZE][OmokConstants.BOARD_SIZE];
    private int currentTurn = OmokConstants.BLACK;

    public GameSession(BufferedReader in1, BufferedReader in2, PrintWriter out1, PrintWriter out2) {
        this.in1 = in1;
        this.in2 = in2;
        this.out1 = out1;
        this.out2 = out2;
    }

    @Override
    public void run() {
        try {
            out1.println(OmokConstants.BLACK);
            out2.println(OmokConstants.WHITE);
            out1.println(Message.READY);
            out2.println(Message.READY);

            while (true) {
                boolean turnOfBlack = (currentTurn == OmokConstants.BLACK);
                BufferedReader currentIn = turnOfBlack ? in1 : in2;
                PrintWriter currentOut = turnOfBlack ? out1 : out2;
                PrintWriter opponentOut = turnOfBlack ? out2 : out1;

                String msg = currentIn.readLine();
                if (msg == null) break;

                if (Message.isMove(msg)) {
                    int[] pos = Message.parseMove(msg);
                    int x = pos[0], y = pos[1], color = pos[2];

                    if (color != currentTurn) {
                        currentOut.println("❌ Invalid move: it's not your turn.");
                        continue;
                    }

                    if (board[y][x] != OmokConstants.EMPTY) {
                        currentOut.println("❌ Invalid move: cell occupied.");
                        continue;
                    }

                    board[y][x] = currentTurn;
                    currentOut.println(msg);
                    opponentOut.println(msg);

                    if (OmokGameLogic.checkWin(board, x, y, currentTurn)) {
                        currentOut.println(Message.WIN);
                        opponentOut.println(Message.LOSE);
                        break;
                    }

                    currentTurn = (currentTurn == OmokConstants.BLACK) ? OmokConstants.WHITE : OmokConstants.BLACK;
                }
            }
        } catch (IOException e) {
            System.err.println("❗ Game session error: " + e.getMessage());
        } finally {
            closeQuietly(in1);
            closeQuietly(in2);
            closeQuietly(out1);
            closeQuietly(out2);
        }
    }

    private void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) closeable.close();
        } catch (IOException e) {
            System.err.println("❗ 자원 해제 중 오류 발생: " + e.getMessage());
        }
    }

    private void closeQuietly(PrintWriter writer) {
        if (writer != null) writer.close();
    }
}
