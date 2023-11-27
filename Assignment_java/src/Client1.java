import java.io.*;
import java.net.*;

public class Client1 {
    private static final String SERVER_IP = "localhost"; // ���� IP �ּ�
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try {
        	boolean first = true;
            Socket socket = new Socket(SERVER_IP, PORT);
            System.out.println("������ ����Ǿ����ϴ�.");

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            
            int[][] boardMap = (int[][]) in.readObject();
            
            first = false;
            // ���� ���� ����
            while (true) {
                // Ŭ���̾�Ʈ�� �� �Է� ����
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("���� ���� x ��ǥ �Է� (1���� 15����): ");
                int x = Integer.parseInt(reader.readLine()) - 1;
                System.out.print("���� ���� y ��ǥ �Է� (1���� 15����): ");
                int y = Integer.parseInt(reader.readLine()) - 1;

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
                boardMap = (int[][]) in.readObject();
                // ������ ��� �Ǵ� �ʿ��� ���� ����
            }

            // ���� ����
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
