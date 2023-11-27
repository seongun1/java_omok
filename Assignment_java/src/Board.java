import java.io.Serializable;
import java.util.Arrays;

public class Board implements Serializable {
    private int boardXSize = 15;
    private int boardYSize = 15;
    public int[][] boardMap = new int[boardXSize][boardYSize];
    private Player playerOne, playerTwo;
    private Player winner;

    public Board(int boardSize) {
        initializeBoard();
    }

    public int[][] boardMapReturn() {
        return boardMap;
    }

    public void initializeBoard() {
        for (int i = 0; i < boardXSize; i++) {
            Arrays.fill(boardMap[i], 0);
        }
    }

    public boolean put(int x, int y, Player player) {
        if (!isValidPosition(x, y)) {
            System.out.println("올바르지 않은 좌표입니다. 1부터 15 사이의 좌표를 입력하세요");
            return false;
        } else if (boardMap[x][y] != 0) {
            System.out.println("놓을 수 없는 자리입니다.");
            return false;
        }
        boardMap[x][y] = player.getPlayerColor();
        return true;
    }

    public void show() {
        for (int i = 0; i < boardXSize; i++) {
            for (int j = 0; j < boardYSize; j++) {
                System.out.print(boardMap[i][j] + " ");
            }
            System.out.println();
        }
    }

    public Player getWinner() {
        return winner;
    }

    public boolean win(int x, int y, Player player) {
        int stoneColor = boardMap[x][y];

        if (checkWinDirection(x, y, stoneColor, 1, 0) ||
            checkWinDirection(x, y, stoneColor, 0, 1) ||
            checkWinDirection(x, y, stoneColor, 1, 1) ||
            checkWinDirection(x, y, stoneColor, 1, -1)) {
            winner = player;
            return true;
        }

        return false;
    }

    private boolean checkWinDirection(int x, int y, int stoneColor, int dx, int dy) {
        int count = 1;
        int currentX = x + dx;
        int currentY = y + dy;

        while (isValidPosition(currentX, currentY) && boardMap[currentX][currentY] == stoneColor) {
            count++;
            currentX += dx;
            currentY += dy;
        }

        currentX = x - dx;
        currentY = y - dy;

        while (isValidPosition(currentX, currentY) && boardMap[currentX][currentY] == stoneColor) {
            count++;
            currentX -= dx;
            currentY -= dy;
        }

        return count >= 5;
    }

    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < boardXSize && y >= 0 && y < boardYSize;
    }
}
