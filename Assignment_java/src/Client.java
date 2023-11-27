
import java.io.*;
import java.net.*;
import java.util.*;
public class Client {

   private static int black =1;
   private static int white=2;
      
      public static void main(String[] args) {

         Player playerTwo= new Player("Player2","Player2입니다.");
         Board board = new Board();
         
         playerTwo.setPlayerColor(white);
         Scanner scanner= new Scanner(System.in);
         int x,y;
         boolean first = true;
         try {
            Socket clientSocket = new Socket("localhost", 12345); // 서버의 IP 주소와 포트 번호로 연결

               ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
               ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
         
            while(true) {
             
             if (!first) {
             board.get((int[][])in.readObject());
             }       
             first = false;
             board.show();
               System.out.println("-----------------------------------");
               System.out.println(playerTwo.getPlayerName()+"이 돌 차례입니다.");
               System.out.print("돌 위치를 입력하세요: ");
               x=scanner.nextInt()-1;
               y=scanner.nextInt()-1;
               while(!board.put(x,y,playerTwo)) 
               {
                  System.out.println("돌 위치를 다시 선택하세요: ");
                  x=scanner.nextInt()-1;
                  y=scanner.nextInt()-1;
               }
               board.show();
               System.out.println("------------------------------");
               if(board.win(x,y,playerTwo))
                  break;
               
               out.writeObject(board.boardMapReturn());
               out.flush();
         }
            System.out.println("승리자는"+ board.getWinner().getPlayerName()+"입니다.");
            scanner.close();
            clientSocket.close();
        }
         catch (IOException | ClassNotFoundException e) {
               e.printStackTrace();
           }
     }

}