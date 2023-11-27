import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client1 {
    private static final String SERVER_IP = "localhost"; // ���� IP �ּ�
    private static final int PORT = 12345;

    public static void main(String[] args) {
    	Scanner scanner  = new Scanner (System.in);
        try {
        	boolean first = true;
            Socket socket = new Socket(SERVER_IP, PORT);
            System.out.println("������ ����Ǿ����ϴ�.");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            
            int[][] boardMap = (int[][]) in.readLine();
            
            first = false;
            // ���� ���� ����
            while (true) {
                // Ŭ���̾�Ʈ�� �� �Է� ����
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("���� ���� x ,y��ǥ �Է� (1���� 15����): ");
	            String input = 	scanner.next();

                // ������ ��ǥ ����
                out.write(input);
                out.flush();
               
                // ���� ���� Ȯ��
                String result = in.read();
                if (result.equals("win")) {
                    System.out.println("�¸��ϼ̽��ϴ�!");
                    break;
                } else if (result.equals("lose")) {
                    System.out.println("�й��ϼ̽��ϴ�.");
                    break;
                }

                // �����κ��� ������ ���� ����
                boardMap = (int[][]) in.read();
                // ������ ��� �Ǵ� �ʿ��� ���� ����
            }
            // ���� ����
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
