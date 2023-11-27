import java.io.Serializable;
import java.util.Arrays;

public class Board implements Serializable{
    private int boardXSize = 15;
    private int boardYSize = 15;
    private int[][] boardMap = new int[boardXSize][boardYSize];
    private Player playerOne, playerTwo;
    private Player Winner;
    

    public Board() {
        for (int i = 0; i < boardXSize; i++) {
            Arrays.fill(boardMap[i], 0);
        }
    }
    
    public int[][] boardMapReturn() {
    	return boardMap;
    }
    
    public void get(int[][] inputBoard) { // 입력한 값을 업데이트
    	 int rows = inputBoard.length;
         int cols = inputBoard[0].length;
         for (int i = 0; i < rows; i++) {
             for (int j = 0; j < cols; j++) {
                 boardMap[i][j] = inputBoard[i][j];
             }
         }
    }
    
    public boolean put(int x, int y, Player player) {
    	if (x<0 || x>=boardXSize || y<0 || y>=boardYSize) {
    		System.out.println("올바르지 않은 좌표입니다.1부터 15 사이의 좌표를 입력하세요");
        	return false;
    	}
    	else if (boardMap[x][y] != 0) {
    		System.out.println("놓을 수 없는 자리입니다.");
            return false;
    	}
    	else if (!Character.isDigit(x) || !Character.isDigit(y)) {
    		System.out.println("숫자가 아닙니다. 숫자를 입력해주세요");
    		return false;
    	}
        boardMap[x][y] = player.getPlayerColor();
        return true;
    }

    public void show() { // boardMap을 출력
        for (int i = 0; i < boardXSize; i++) {
            for (int j = 0; j < boardYSize; j++) {
                System.out.print(boardMap[i][j] + " ");
            }
            System.out.println();
        }
    }

    public Player getWinner() {
        return Winner;
    }

    public boolean win(int x, int y, Player player) {
        int stoneColor = boardMap[x][y];

        if (checkWinDirection(x, y, stoneColor, 1, 0) || // 가로 방향
            checkWinDirection(x, y, stoneColor, 0, 1) || // 세로 방향
            checkWinDirection(x, y, stoneColor, 1, 1) || // 우상향 대각선 방향
            checkWinDirection(x, y, stoneColor, 1, -1)) { // 우하향 대각선 방향
            Winner = player;
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

    public static void main(String[] args) {
    }
}
