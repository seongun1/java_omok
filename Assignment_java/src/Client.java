
import java.io.*;
import java.net.*;
import java.util.*;
public class Client {

   private static int black =1;
   private static int white=2;
      
      public static void main(String[] args) {

         Player playerTwo= new Player("Player2","Player2�Դϴ�.");
         Board board = new Board();
         
         playerTwo.setPlayerColor(white);
         Scanner scanner= new Scanner(System.in);
         int x,y;
         boolean first = true;
         try {
            Socket clientSocket = new Socket("localhost", 12345); // ������ IP �ּҿ� ��Ʈ ��ȣ�� ����

               ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
               ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
         
            while(true) {
             
             if (!first) {
             board.get((int[][])in.readObject());
             }       
             first = false;
             board.show();
               System.out.println("-----------------------------------");
               System.out.println(playerTwo.getPlayerName()+"�� �� �����Դϴ�.");
               System.out.print("�� ��ġ�� �Է��ϼ���: ");
               x=scanner.nextInt()-1;
               y=scanner.nextInt()-1;
               while(!board.put(x,y,playerTwo)) 
               {
                  System.out.println("�� ��ġ�� �ٽ� �����ϼ���: ");
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
            System.out.println("�¸��ڴ�"+ board.getWinner().getPlayerName()+"�Դϴ�.");
            scanner.close();
            clientSocket.close();
        }
         catch (IOException | ClassNotFoundException e) {
               e.printStackTrace();
           }
     }

}