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

            ObjectOutputStream outToOne = new ObjectOutputStream(clientOne.getOutputStream());
            ObjectInputStream inFromOne = new ObjectInputStream(clientOne.getInputStream());

            ObjectOutputStream outToTwo = new ObjectOutputStream(clientTwo.getOutputStream());
            ObjectInputStream inFromTwo = new ObjectInputStream(clientTwo.getInputStream());

            // ���� ���� ����
            Board board = new Board(BOARD_SIZE);

            // �� Ŭ���̾�Ʈ���� ���� ũ�� ����
            outToOne.writeInt(BOARD_SIZE);
            outToOne.flush();
            outToTwo.writeInt(BOARD_SIZE);
            outToTwo.flush();

            // ���� ����
            Player playerOne = new Player("Player1", "Player1�Դϴ�.");
            Player playerTwo = new Player("Player2", "Player2�Դϴ�.");
            Scanner scan = new Scanner (System.in);
            int x = scan.nextInt();
            int y = scan.nextInt();
            while (true) {
                // �÷��̾�1�� ����
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

                // �÷��̾�2�� ����
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

            // ���� ����
            serverSocket.close();
            clientOne.close();
            clientTwo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
