package logic;

public class OmokGameLogic {
    public static boolean checkWin(int[][] board, int x, int y, int color) {
        return count(board, x, y, 1, 0, color) + count(board, x, y, -1, 0, color) >= 4 ||
                count(board, x, y, 0, 1, color) + count(board, x, y, 0, -1, color) >= 4 ||
                count(board, x, y, 1, 1, color) + count(board, x, y, -1, -1, color) >= 4 ||
                count(board, x, y, -1, 1, color) + count(board, x, y, 1, -1, color) >= 4;
    }

    private static int count(int[][] board, int x, int y, int dx, int dy, int color) {
        int count = 0;
        for (int i = 1; i <= 4; i++) {
            int nx = x + dx * i;
            int ny = y + dy * i;
            if (nx < 0 || ny < 0 || nx >= 15 || ny >= 15) break;
            if (board[ny][nx] != color) break;
            count++;
        }
        return count;
    }
}