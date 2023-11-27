import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server {
    private static final int PORT = 12345;
    private static final int BOARD_SIZE = 15;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("���� ����. Ŭ���̾�Ʈ�� ��ٸ��ϴ�...");

            // ù ��° Ŭ���̾�Ʈ ����
            Socket clientOne = serverSocket.accept();
            System.out.println("ù ��° Ŭ���̾�Ʈ�� ����Ǿ����ϴ�.");

            // �� ��° Ŭ���̾�Ʈ ����
            Socket clientTwo = serverSocket.accept();
            System.out.println("�� ��° Ŭ���̾�Ʈ�� ����Ǿ����ϴ�.");

            BufferedReader oneRead = new BufferedReader(new InputStreamReader(clientOne.getInputStream()));
            BufferedWriter oneWrite = new BufferedWriter(new OutputStreamWriter(clientOne.getOutputStream()));

            BufferedReader twoRead = new BufferedReader(new InputStreamReader(clientTwo.getInputStream()));
            BufferedWriter twoWrite = new BufferedWriter(new OutputStreamWriter(clientTwo.getOutputStream()));

            // ���� ���� ����
            Board board = new Board(BOARD_SIZE);

            // �� Ŭ���̾�Ʈ���� ���� ũ�� ����
            oneWrite.write(Integer.toString(BOARD_SIZE));
            oneWrite.flush();
            twoWrite.write(Integer.toString(BOARD_SIZE));
            twoWrite.flush();

            // ���� ����
            Player playerOne = new Player("Player1", "Player1�Դϴ�.");
            Player playerTwo = new Player("Player2", "Player2�Դϴ�.");
            Scanner scan = new Scanner (System.in);
            int x,y;
            while (true) {
                // �÷��̾�1�� ����
                board.show();
                String input1 = oneRead.readLine();
                if (input1.equalsIgnoreCase("give up")) {
                	System.out.println("�÷��̾� 1�� �׺��Ͽ����ϴ�... ������ �����մϴ�.");
                	break;
                }
                String [] result1 = input1.split(" ");
                x = Integer.parseInt(result1[0]);
                y = Integer.parseInt(result1[1]);

                if (board.put(x, y, playerOne) && board.win(x, y, playerOne)) {
                    oneWrite.write("win");
                    oneWrite.flush();
                    twoWrite.write("lose");
                    twoWrite.flush();
                    break;
                } else {
                	board.put(x, y, playerOne);
                	oneWrite.write(board.boardMapReturn());
                	oneWrite.flush();
                }

                // �÷��̾�2�� ����
                board.show();
                String Input2 = oneRead.readLine();
                String [] result2 = Input2.split(" ");
                x = Integer.parseInt(result2[0]);
                y = Integer.parseInt(result2[1]);
                
                if (board.put(x, y, playerTwo) && board.win(x, y, playerTwo)) {
                	twoWrite.write("win");
                	twoWrite.flush();
                	oneWrite.write("lose");
                	oneWrite.flush();
                    break;
                } else {
                	board.put(x, y, playerTwo);
                    twoWrite.write(board.boardMapReturn());
                    twoWrite.flush();
                }
            }
            // ���� ����
            serverSocket.close();
            clientOne.close();
            clientTwo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
