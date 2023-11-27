import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server {
    private static final int PORT = 12345;
    private static final int BOARD_SIZE = 15;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("서버 시작. 클라이언트를 기다립니다...");

            // 첫 번째 클라이언트 연결
            Socket clientOne = serverSocket.accept();
            System.out.println("첫 번째 클라이언트가 연결되었습니다.");

            // 두 번째 클라이언트 연결
            Socket clientTwo = serverSocket.accept();
            System.out.println("두 번째 클라이언트가 연결되었습니다.");

            ObjectOutputStream outToOne = new ObjectOutputStream(clientOne.getOutputStream());
            ObjectInputStream inFromOne = new ObjectInputStream(clientOne.getInputStream());

            ObjectOutputStream outToTwo = new ObjectOutputStream(clientTwo.getOutputStream());
            ObjectInputStream inFromTwo = new ObjectInputStream(clientTwo.getInputStream());

            // 오목 보드 생성
            Board board = new Board(BOARD_SIZE);

            // 두 클라이언트에게 보드 크기 전달
            outToOne.writeInt(BOARD_SIZE);
            outToOne.flush();
            outToTwo.writeInt(BOARD_SIZE);
            outToTwo.flush();

            // 게임 시작
            Player playerOne = new Player("Player1", "Player1입니다.");
            Player playerTwo = new Player("Player2", "Player2입니다.");
            Scanner scan = new Scanner (System.in);
            int x = scan.nextInt();
            int y = scan.nextInt();
            while (true) {
                // 플레이어1의 차례
                board.show();
                x = inFromOne.readInt() - 1;
                y = inFromOne.readInt() - 1;

                if (board.put(x, y, playerOne) && board.win(x, y, playerOne)) {
                    outToOne.writeObject("win");
                    outToOne.flush();
                    outToTwo.writeObject("lose");
                    outToTwo.flush();
                    break;
                } else {
                    outToOne.writeObject(board.boardMapReturn());
                    outToOne.flush();
                }

                // 플레이어2의 차례
                board.show();
                x = inFromTwo.readInt() - 1;
                y = inFromTwo.readInt() - 1;

                if (board.put(x, y, playerTwo) && board.win(x, y, playerTwo)) {
                    outToTwo.writeObject("win");
                    outToTwo.flush();
                    outToOne.writeObject("lose");
                    outToOne.flush();
                    break;
                } else {
                    outToTwo.writeObject(board.boardMapReturn());
                    outToTwo.flush();
                }
            }

            // 연결 종료
            serverSocket.close();
            clientOne.close();
            clientTwo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
