import java.io.*;
import java.net.*;

public class Client2 {
    private static final String SERVER_IP = "localhost"; // 서버 IP 주소
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try {
        	boolean first = true;
            Socket socket = new Socket(SERVER_IP, PORT);
            System.out.println("서버에 연결되었습니다.");

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            
            int[][] boardMap = (int[][]) in.readObject();
            
            first = false;
            // 게임 진행 로직
            while (true) {
                // 클라이언트의 수 입력 로직
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("돌을 놓을 x 좌표 입력 (1부터 15까지): ");
                int x = Integer.parseInt(reader.readLine()) - 1;
                System.out.print("돌을 놓을 y 좌표 입력 (1부터 15까지): ");
                int y = Integer.parseInt(reader.readLine()) - 1;

                // 서버로 좌표 전송
                out.writeInt(x);
                out.flush();
                out.writeInt(y);
                out.flush();

                // 승패 여부 확인
                String result = (String) in.readObject();
                if (result.equals("win")) {
                    System.out.println("승리하셨습니다!");
                    break;
                } else if (result.equals("lose")) {
                    System.out.println("패배하셨습니다.");
                    break;
                }

                // 서버로부터 오목판 정보 수신
                boardMap = (int[][]) in.readObject();
                // 오목판 출력 또는 필요한 로직 수행
            }

            // 연결 종료
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
