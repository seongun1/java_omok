import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client1 {
    private static final String SERVER_IP = "127.0.0.1"; // ���� IP �ּ�
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_IP, PORT);
            System.out.println("������ ����Ǿ����ϴ�.");

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            int boardSize = Integer.parseInt(in.readObject().toString());

            Scanner scanner = new Scanner(System.in);

            while (true) {
                // Ŭ���̾�Ʈ�� �� �Է� ����
                System.out.print("���� ���� x, y ��ǥ �Է� (��: 1 2 - 1���� " + boardSize + "����): ");
                String input = scanner.nextLine();
                String[] inputArr = input.split(" ");
                int x = Integer.parseInt(inputArr[0]) - 1;
                int y = Integer.parseInt(inputArr[1]) - 1;

                // ������ ��ǥ ����
                out.writeInt(x);
                out.flush();
                out.writeInt(y);
                out.flush();

                // ���� ���� Ȯ��
                String result = (String) in.readObject();
                if (result.equals("win")) {
                    System.out.println("�¸��ϼ̽��ϴ�!");
                    break;
                } else if (result.equals("lose")) {
                    System.out.println("�й��ϼ̽��ϴ�.");
                    break;
                }

                // �����κ��� ������ ���� ����
                int[][] boardMap = (int[][]) in.readObject();
                // ������ ��� �Ǵ� �ʿ��� ���� ����
            }

            // ���� ����
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
