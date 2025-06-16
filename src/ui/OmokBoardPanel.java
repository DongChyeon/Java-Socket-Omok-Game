package ui;

import client.OmokClient;
import model.OmokConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OmokBoardPanel extends JPanel {
    private final int[][] board = new int[OmokConstants.BOARD_SIZE][OmokConstants.BOARD_SIZE];
    private boolean myTurn = false;
    private final int myColor;
    private final int padding = OmokConstants.CELL_SIZE;
    private final JLabel statusLabel;

    public OmokBoardPanel(OmokClient client, int myColor) {
        setLayout(new BorderLayout());

        this.myColor = myColor;

        // 상단 상태 표시
        statusLabel = new JLabel("상대 플레이어를 기다리는 중입니다...");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        statusPanel.add(statusLabel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.NORTH);

        JPanel boardPanel = getBoardPanel();
        add(boardPanel, BorderLayout.CENTER);

        if (myColor == OmokConstants.BLACK) myTurn = true;

        boardPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (!myTurn) {
                    showStatusMessage(myColor == OmokConstants.BLACK ? "백돌의 차례입니다!" : "흑돌의 차례입니다!");
                    return;
                }

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
                if (distance > 30) return;  // ← 반경 20px 이내에서만 착수 허용

                if (board[y][x] != 0) return;

                client.sendMove(x, y, myColor);
                myTurn = false;
            }
        });
    }

    public void setReady() {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText("당신은 " + (myColor == OmokConstants.BLACK ? "흑(●)" : "백(○)") + " 입니다.");
            if (myColor == OmokConstants.BLACK) {
                myTurn = true;
            }
        });
    }

    private void showStatusMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            String originalText = statusLabel.getText();
            statusLabel.setText(message);
            statusLabel.repaint();

            Timer timer = new Timer(1000, e -> {
                statusLabel.setText(originalText);
                statusLabel.repaint();
            });
            timer.setRepeats(false);
            timer.start();
        });
    }

    private JPanel getBoardPanel() {
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
        return boardPanel;
    }

    public void applyOpponentMove(int x, int y, int color) {
        board[y][x] = color;
        repaint();
        myTurn = (color != myColor);
    }
}