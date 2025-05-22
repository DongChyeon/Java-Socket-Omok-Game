package ui;

import client.OmokClient;
import logic.OmokGameLogic;
import model.OmokConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OmokBoardPanel extends JPanel {
    private final int[][] board = new int[OmokConstants.BOARD_SIZE][OmokConstants.BOARD_SIZE];
    private boolean myTurn = false;
    private JLabel statusLabel;
    private final int padding = OmokConstants.CELL_SIZE;

    public OmokBoardPanel(OmokClient client, int myColor) {
        setLayout(new BorderLayout());

        // 상단 상태 표시
        statusLabel = new JLabel("당신은 " + (myColor == OmokConstants.BLACK ? "흑(●)" : "백(○)") + " 입니다.");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(statusLabel, BorderLayout.NORTH);

        // 판 패널
        JPanel boardPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int cell = OmokConstants.CELL_SIZE;

                // 바둑판 격자 그리기
                for (int i = 0; i < OmokConstants.BOARD_SIZE; i++) {
                    g.drawLine(padding, padding + i * cell, padding + (OmokConstants.BOARD_SIZE - 1) * cell, padding + i * cell);
                    g.drawLine(padding + i * cell, padding, padding + i * cell, padding + (OmokConstants.BOARD_SIZE - 1) * cell);
                }

                // 돌 그리기
                for (int y = 0; y < OmokConstants.BOARD_SIZE; y++) {
                    for (int x = 0; x < OmokConstants.BOARD_SIZE; x++) {
                        if (board[y][x] != 0) {
                            int centerX = padding + x * cell;
                            int centerY = padding + y * cell;
                            g.setColor(board[y][x] == OmokConstants.BLACK ? Color.BLACK : Color.WHITE);
                            g.fillOval(centerX - 16, centerY - 16, 32, 32);
                            g.setColor(Color.BLACK);
                            g.drawOval(centerX - 16, centerY - 16, 32, 32); // 테두리
                        }
                    }
                }
            }
        };

        boardPanel.setPreferredSize(new Dimension(
                padding * 2 + (OmokConstants.BOARD_SIZE - 1) * OmokConstants.CELL_SIZE,
                padding * 2 + (OmokConstants.BOARD_SIZE - 1) * OmokConstants.CELL_SIZE));
        boardPanel.setBackground(new Color(245, 222, 179));
        add(boardPanel, BorderLayout.CENTER);

        if (myColor == OmokConstants.BLACK) myTurn = true;

        boardPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (!myTurn) return;

                int mouseX = e.getX();
                int mouseY = e.getY();
                int cell = OmokConstants.CELL_SIZE;

                // 가장 가까운 교차점 찾기
                int x = Math.round((float)(mouseX - padding) / cell);
                int y = Math.round((float)(mouseY - padding) / cell);

                // 보드 범위 체크
                if (x < 0 || x >= OmokConstants.BOARD_SIZE || y < 0 || y >= OmokConstants.BOARD_SIZE) return;

                // 클릭 위치가 해당 교차점 반경 내에 있는지 확인
                int centerX = padding + x * cell;
                int centerY = padding + y * cell;
                int dx = mouseX - centerX;
                int dy = mouseY - centerY;
                double distance = Math.sqrt(dx * dx + dy * dy);
                if (distance > 20) return;  // ← 반경 20px 이내에서만 착수 허용

                if (board[y][x] != 0) return;

                board[y][x] = myColor;
                boardPanel.repaint();
                myTurn = false;
                client.sendMove(x, y);

                if (OmokGameLogic.checkWin(board, x, y, myColor)) {
                    JOptionPane.showMessageDialog(null, "You Win!");
                    System.exit(0);
                }
            }
        });
    }

    public void applyOpponentMove(int x, int y, int color) {
        board[y][x] = color;
        repaint();
        myTurn = true;

        if (OmokGameLogic.checkWin(board, x, y, color)) {
            JOptionPane.showMessageDialog(null, "You Lose!");
            System.exit(0);
        }
    }
}