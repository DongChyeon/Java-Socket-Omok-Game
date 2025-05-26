package model;

public class Message {
    public static final String READY = "READY";
    public static final String WIN = "WIN";
    public static final String LOSE = "LOSE";

    public static String move(int x, int y) {
        return "MOVE:" + x + "," + y;
    }

    public static boolean isMove(String msg) {
        return msg.startsWith("MOVE:");
    }

    public static int[] parseMove(String msg) {
        String[] parts = msg.substring(5).split(",");
        return new int[]{
            Integer.parseInt(parts[0]),
            Integer.parseInt(parts[1])
        };
    }
}
