import java.io.*;
import java.net.*;

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

            BufferedReader oneRead = new BufferedReader(new InputStreamReader(clientOne.getInputStream()));
            BufferedWriter oneWrite = new BufferedWriter(new OutputStreamWriter(clientOne.getOutputStream()));

            BufferedReader twoRead = new BufferedReader(new InputStreamReader(clientTwo.getInputStream()));
            BufferedWriter twoWrite = new BufferedWriter(new OutputStreamWriter(clientTwo.getOutputStream()));

            // 오목 보드 생성
            Board board = new Board(BOARD_SIZE);

            // 직렬화하여 보드 전달
            ObjectOutputStream outToOne = new ObjectOutputStream(clientOne.getOutputStream());
            outToOne.writeObject(board);
            outToOne.flush();

            ObjectOutputStream outToTwo = new ObjectOutputStream(clientTwo.getOutputStream());
            outToTwo.writeObject(board);
            outToTwo.flush();

            // 게임 시작
            Player playerOne = new Player("Player1", "Player1입니다.");
            Player playerTwo = new Player("Player2", "Player2입니다.");

            while (true) {
                // 플레이어1의 차례
                board.show();
                String input1 = oneRead.readLine();
                if (input1.equalsIgnoreCase("give up")) {
                    System.out.println("플레이어 1이 항복하였습니다... 대전을 종료합니다.");
                    break;
                }
                String[] result1 = input1.split(" ");
                int x = Integer.parseInt(result1[0]);
                int y = Integer.parseInt(result1[1]);

                if (board.put(x, y, playerOne) && board.win(x, y, playerOne)) {
                    oneWrite.write("win");
                    oneWrite.newLine();
                    oneWrite.flush();
                    twoWrite.write("lose");
                    twoWrite.newLine();
                    twoWrite.flush();
                    break;
                } else {
                    board.put(x, y, playerOne);
                    outToOne.writeObject(board);
                    outToOne.flush();
                }

                // 플레이어2의 차례
                board.show();
                String input2 = twoRead.readLine();
                if (input2.equalsIgnoreCase("give up")) {
                    System.out.println("플레이어 2가 항복하였습니다... 대전을 종료합니다.");
                    break;
                }
                String[] result2 = input2.split(" ");
                x = Integer.parseInt(result2[0]);
                y = Integer.parseInt(result2[1]);

                if (board.put(x, y, playerTwo) && board.win(x, y, playerTwo)) {
                    twoWrite.write("win");
                    twoWrite.newLine();
                    twoWrite.flush();
                    oneWrite.write("lose");
                    oneWrite.newLine();
                    oneWrite.flush();
                    break;
                } else {
                    board.put(x, y, playerTwo);
                    outToTwo.writeObject(board);
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
